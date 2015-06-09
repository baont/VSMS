/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.me.network;

import java.util.Vector;

/**
 *
 * @author baont
 */
public class MsgReader implements Runnable{
    protected MobileClient session;
    final public Vector inputMessages = new Vector(10);
    private long idleTime = 0;

    public MsgReader(MobileClient session) {
        this.session = session;
    }

    protected boolean isIdle() {
        if(idleTime > 0) {
            long t = System.currentTimeMillis() - idleTime;
            if(t > 120000) {
                return true;
            }
        }
        
        return false;
    }
    
    public void run() {
        Message message;
        try {
            while (session.isConnected) {
                message = readMessage();
                if (message != null) {
                    if (session.isSYNC) {
                        synchronized (inputMessages) {
                            inputMessages.addElement(message);
                        }
                    } else {
                        session.messageHandler.onMessage(message);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
        }
        if (session.isConnected) {
            if (session.messageHandler != null) {
                session.messageHandler.onDisconnected();
            }
            session.close();
        }
    }

    public void processMessage() {
        synchronized (inputMessages) {
            int messageNum = inputMessages.size();
            for (int i = 0; i < messageNum; i++) {
                session.messageHandler.onMessage((Message) inputMessages.elementAt(i));
            }
            inputMessages.removeAllElements();
        }
    }

    /**
     * đọc message theo dạng length trước.
     * @return
     * @throws Exception
     */
    private Message readMessage() throws Exception {
        int length = 0;
        int hi = session.dis.readInt(); // Phai doc 1 byte truoc de xac dinh co bi disconnect hay khong
        if (hi == -1) {

            return null;
        }
        length = hi - 1;
        byte isEncrypted = (byte) session.dis.read(); // from 0x07
//            byte isEncrypted = 1;
        byte data[] = new byte[length];
        int len = 0;
        int byteRead = 0;
        while (len != -1 && byteRead < length) {
            len = session.dis.read(data, byteRead, length - byteRead);
            if (len > 0) {
                byteRead += len;
            }
        }
        if (length == 0) {
            return null;
        }
        Message msg;
        if (isEncrypted == 1) {
            msg = new Message(session.tea.decrypt(data));
        } else {
            msg = new Message(data);
        }

        if(session.dis.available() <= 0) {
            idleTime = System.currentTimeMillis();
        } else {
            idleTime = 0;
        }
        
        session.recvByteCount += (4 + length); // 2 byte + độ dài.        
//        System.out.println("Nhan message " + msg.id);
        return msg;
    }
}
