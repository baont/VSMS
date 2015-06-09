/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.me.network;

import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author baont
 */
public class MsgSender implements Runnable {
    protected MobileClient session;
    protected final Vector sendingMessage = new Vector();

    public MsgSender(MobileClient session) {
        this.session = session;
    }

    public void addMessage(Message message) {
        synchronized (sendingMessage) {
            sendingMessage.addElement(message);
            sendingMessage.notifyAll();
        }
    }

    public void run() {
        try {
            while (session.isConnected) {
                synchronized (sendingMessage) {
                    while (sendingMessage.size() > 0) {
                        // Kiểm tra xem vẫn còn connect hay không
                        // Cho truong hop khi cat ket noi ma van dang co message.
                        if (session.isConnected) {
                            Message m = (Message) sendingMessage.elementAt(0);
                            sendingMessage.removeElementAt(0);
                            doSendMessage(m);
                        }
                    }

                    try {
                        sendingMessage.wait();
                    } catch (InterruptedException ex) {
                    }
                }

            }
        } catch (Exception e) {
        }
    }

    public void doSendMessage(Message m) throws IOException {
        //#if DefaultConfiguration
//        System.out.println("Gui message " + m.id + " " + (m.isEncrypted ? "Ma hoa" : "khong ma hoa"));
        //#endif
        byte[] data;
        data = m.getBuffer();
        if (data != null) {
            if (m.isEncrypted) {
                data = session.tea.encrypt(data);
            }
            session.dos.writeInt(data.length + 1); //0x09

            session.dos.writeByte(m.isEncrypted ? 1 : 0);  //from 0x09
            session.dos.write(data);
            session.sendByteCount += data.length;
        } else {
            session.dos.writeInt(0);
        }
        session.sendByteCount += 4; // 4 byte độ dài
        session.dos.flush();
    }
    
    public void stop() {
        synchronized (sendingMessage) {
            sendingMessage.removeAllElements();
            sendingMessage.notifyAll();
        }
    }

    protected void writeKey(long key) throws IOException{
        byte[] buf = new byte[]{session.PROTOCOL_VERSION, (byte) (key >>> 56),
            (byte) (key >>> 48), (byte) (key >>> 40), (byte) (key >>> 32),
            (byte) (key >>> 24), (byte) (key >>> 16), (byte) (key >>> 8), (byte) key
        };
        session.dos.write(buf);
        session.dos.flush();
    }
}
