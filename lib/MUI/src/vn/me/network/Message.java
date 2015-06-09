package vn.me.network;

import java.io.*;

/**
 * Mô hình một {@link Message} để tro đổi thông tin với server iWin. Cấu trúc
 * của một {@link Message} bao gồm : - int id : Là một mã duy nhất cho toàn hệ
 * thống. - byte[] datas : là dữ liệu kèm theo của thông điệp. Dữ liệu kèm theo
 * có thể null. <p> Cách sử dụng :
 * <pre>
 * Gửi command không có data:
 *      MobileClient session;
 *      ...
 *      Message m = new Message(commandId);
 *      session.sendMessage(m);
 *      m.cleanup();
 * </pre> Gửi command có đata:
 * <pre>
 *      MobileClient session;
 *      ....
 *      Message m = new Message(commandId);
 *      m.writer().writeByte(aByte);
 *      m.writer().writeInt(anInteger);
 *      m.writer().writeUTF(aText);
 *      session.sendMessage(m);
 *      m.cleanup();
 * </pre> Đọc dữ liệu từ một Message:
 * <pre>
 * Ví dụ :
 *     public void onMessage(Message msg) {
 *           switch (msg.id) {
 *               case COMMANDSServer2Client.SET_CLIENT_INFO:
 *                   byte isOK = msg.reader().readByte();
 *                   if (isOK == 1) {
 *                       doSomethingOK();
 *                   } else {
 *                       doSomethingFail;
 *                   }
 *                   break;
 *               default :
 *                   break;
 *            }
 *     }
 * </pre>
 *
 * Các Message nào của hệ thống sẽ được xử lý trước khi chuyển cho từng game/ứng
 * dụng.
 *
 * @author Tâm Đinh
 */
public final class Message {

    /**
     * id của message. Mỗi message đều phải có một id riêng biệt, không trùng
     * trong toàn hệ thống. Giá trị 1 byte từ : 0->255
     */
    public int id;
    private ByteArrayOutputStream baos;// = null;
    private DataOutputStream dos;// = null;
//    private ByteArrayInputStream bais = null;
    private DataInputStream dis;// = null;
    public boolean isEncrypted = true;
    public static boolean isiWin = false;

    /**
     * Khởi tạo một message mới để gửi lên server. Dữ liệu kèm theo = null.
     *
     * @param command : id của command.
     */
    public Message(int command) {
        this(command, true);
    }

    /**
     * Khởi tạo một message mới để gửi lên server. Dữ liệu kèm theo = null.
     *
     * @param command : id của command.
     * @param isEncrypted : Co ma hoa message hay khong.
     */
    public Message(int command, boolean isEncrypted) {
        this.id = command;
        this.isEncrypted = isEncrypted;
    }

    /**
     * Khởi tạo một Message với data sẵn có bao gồm command trong đó.
     *
     * @param data : data[0] là command id còn lại là dữ liệu của command đó.
     */
    public Message(byte[] data) throws IOException {
        byte[] msgData = new byte[data.length - 1];
        System.arraycopy(data, 1, msgData, 0, msgData.length);
        id = data[0];
        dis = new DataInputStream(new ByteArrayInputStream(msgData));
    }

    /**
     * Lấy mảng byte để gửi ( bao gồm cả command ở đầu )
     *
     * @return : Dữ liệu gồm commandid ở vị trí đầu tiên, tiếp theo là dữ liệu
     * kiểu byte nếu có.
     */
    public byte[] getBuffer() {
        byte[] data;//= getData();
        if (baos == null) {
            return new byte[]{(byte) id};
        } else {
            data = baos.toByteArray();
        }
        if (isiWin) {
            byte[] buffer = new byte[data.length + 3];
            buffer[0] = (byte) 40;
            buffer[1] = (byte) ((id >>> 8) & 0xff);
            buffer[2] = (byte) ((id >>> 0) & 0xff);
            System.arraycopy(data, 0, buffer, 3, data.length);
            return buffer;
        } else {
            byte[] buffer = new byte[data.length + 1];
            buffer[0] = (byte) id;
            System.arraycopy(data, 0, buffer, 1, data.length);
            return buffer;
        }
        // }
    }

    /**
     * Lấy luồng dữ liệu để đọc message ra.
     *
     * @return : Luồng dữ liệu dùng để đọc ra khi nhận thông điệp
     */
    public DataInputStream reader() {
        return dis;
    }

    /**
     * Lấy luồng dữ liệu để ghi vào buffer của đối tượng Message.
     *
     * @return : Luồng dữ liệu để truyền đi.
     */
    public DataOutputStream writer() {
        if (baos == null) {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
        }
        return dos;
    }

    public void putByte(int value) {
        try {
            writer().writeByte(value);
        } catch (IOException ex) {
        }
    }

    public void putString(String text) {
        try {
            writer().writeUTF(text);
        } catch (IOException ex) {
        }
    }

    public void putInt(int value) {
        try {
            writer().writeInt(value);
        } catch (IOException ex) {
        }
    }

    public void putBoolean(boolean value) {
        try {
            writer().writeBoolean(value);
        } catch (IOException ex) {
        }
    }

    public void putShort(int value) {
        try {
            writer().writeShort(value);
        } catch (IOException ex) {
        }
    }

    /**
     * Cần được gọi mỗi khi tạo xong một message và gửi đi.
     */
    public void cleanup() {
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
        } catch (IOException e) {
        }
    }
}
