/*
 * Copyright (c) 2010, 2011, Giải Pháp Số JSC and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Please contact Mobile Entertainment Corp., 8th Floor Ha Phan Building 456 Phan Xich Long street, Phu Nhuan Dst.
, Ho Chi Minh, Vietnam
 * Or email : tamdtm@mecorp.vn
 * 
 */
package vn.me.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

/**
 * Đối tượng dùng để kết nối tới server iWin.
 * Kiểu kết nối : Socket.
 * iWin Protocol version : 0x06 : Trao đổi key mã hóa, luôn mã hóa dữ liệu.
 * iWin Protocol version : 0x07 : Thêm 1 byte cho biết có mã hóa hay không.
 * iWin Protocol version : 0x08 : Thay đổi cách tính length.
 * @author Tâm Đinh
 */
public class MobileClient {

    /**
     * Version của Socket protocol.
     */
    protected final byte PROTOCOL_VERSION = 0x09;
    /**
     * 
     * Đối tượng xử lý message gửi từ server tới.
     */
    public IMessageListener messageHandler;
    public DataOutputStream dos;
    public DataInputStream dis;
    private SocketConnection sc;

    /** Cho biet hien tai da duoc ket noi hay chua*/
    public boolean isConnected;
    /**
     * Đối tượng chuyên dùng để gửi dữ liệu trên 1 thread riêng biệt
     */
    private MsgSender sender;
    /**
     * Đối tượng chuyên để đọc dữ liệu, trên 1 thread riêng biệt
     */
    private MsgReader reader;
    /**
     * Dùng đếm số byte đọc / ghi cho connection hiện tại.
     * Khi đứt kết nối sẽ được reset lại thành 0.
     */
    public int sendByteCount, recvByteCount;
    /**
     * Đối tượng mã hóa dữ liệu theo thuật toán TEA
     */
    public TEA tea;
    /**
     * Địa chỉ máy server đang được connect. Khi không connect biến này là null.
     */
    public String currentIp;
    /**
     * Số port của server đang connect. Khi chưa connect sẽ bằng -1.
     */
    public int currentPort;
    //#if DefaultConfiguration
    public static long getAvatarTime;
    public static long t1, t2;
    public static long connectTime = -1;
    public static long loginTime = -1;
    //#endif

    /**
     * Cho biết có đang kết nối tới server hay không.
     * @return : Có đang kết nối socket tới server hay không.
     */
    
    public boolean isSYNC = true;
    
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Đặt đối tượng dùng để xử lý thông điệp từ server trả về.
     * Cần được set trước khi tạo kết nối tới server.
     * @param messageHandler
     */
    public void setHandler(IMessageListener messageHandler) {
        this.messageHandler = messageHandler;
    }
    
    public void setReader(MsgReader r) {
        this.reader = r;
    }
    
    public void setSender(MsgSender s) {
        this.sender = s;
    }

    public boolean isIdle() {
        return this.reader.isIdle();
    }
    
    /**
     * Tạo mội connection mới tới server.
     * Sẽ thực hiện trên một thread riêng biệt.
     * Sẽ đóng connection hiện tại trước khi tạo kết nối.
     * @param host : Địa chỉ máy chủ
     * @param port : Cổng máy chủ cần kết nối.
     */
    public void connect(final String host, final int port) {
//        if (currentIp == null || !currentIp.equals(host) || currentPort != port) {
        new Thread(new Runnable() {

            public void run() {
//                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                close();
                try {
                    doConnect(host, port);
                    if (messageHandler != null) {
                        messageHandler.onConnectOK();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    
                    if (messageHandler != null) {
                        close();
                        messageHandler.onConnectionFail();
                    }
                }
            }
        }).start();
//        initNetwork.start();
//        }
    }

    /**
     * Khóa dùng để mã hóa dữ liệu
     * @param key
     */
    protected void setKey(long key) {
        tea = new TEA(key);
    }

    // must invoke setSender and setReader before call this
    private void doConnect(String ip, int port) throws IOException {

        //#if BlackBerry
//#         sc = (SocketConnection) Connector.open("socket://" + ip + ":" + port + ";deviceside=true;interface=wifi");
        //#else
        sc = (SocketConnection) Connector.open("socket://" + ip + ":" + port);
        //#endif

        dos = sc.openDataOutputStream();
        dis = sc.openDataInputStream();
        
        long key = System.currentTimeMillis();
        sender.writeKey(key);
        setKey(key);
        isConnected = true;
        new Thread(sender).start();
        new Thread(reader).start();
        currentIp = ip;
        currentPort = port;
    }

    /**
     * Gửi một socket message lên server.
     * @param message :
     */
    public void sendMessage(Message message) {
//        message.cleanup();
        sender.addMessage(message);
    }

    public void processMessages() {
        reader.processMessage();
    }
    /**
     * Đóng kết nối lại.
     * Cần được gọi trước khi thoát khỏi ứng dụng.
     * Sẽ được gọi tự động khi gọi hàm connect(address, ip ).
     */
    public void close() {
        try {
            currentIp = null;
            currentPort = -1;
            isConnected = false;
            if (sender != null) {
                sender.stop();
            }

            if (dos != null) {
                dos.close();
                dos = null;
            }
            if (dis != null) {
                dis.close();
                dis = null;
            }
            if (sc != null) {

                sc.close();
                sc = null;
            }
//            sender = null;
//            reader = null;
            sendByteCount = 0;
            recvByteCount = 0;
        } catch (Exception e) {
        }
    }
}
