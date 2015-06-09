package vn.me.network;

/**
 * Interface dùng để bắt một thông điệp từ server.
 * @author PCGPS
 */
public interface IMessageListener {

    /**
     * Bắt tất cả các message đến tại đây.
     * Trừ các message như ConnectionFail, Disconnected, và ConnectOK được gọi
     * callBack thông qua các hàm riêng biệt.
     * @param message
     */
    public void onMessage(Message message);
    /**
     * Được gọi trong trường hợp không thể connect tới server.
     */
    public void onConnectionFail();
    /**
     * Được gọi khi đã connect được và bị mất kết nối.
     * Nghía là đã tạo được kết nối và bây giờ bị mất kết nối.
     * Trong các trường hợp như : mạng bị đứt, tắt server....
     */
    public void onDisconnected();
    /**
     * Đã kết nối được với server.
     * Trong protocol của iWin sau khi kết nối xong cần gửi thông tin client lên
     * server để kiểm tra tính hợp lệ của client với cấu trức như sau :
     * - CommandId
     * - Byte : Client type
     * - Byte : Provider :
     * - UTF8 : Application Version
     * - UTF8 : Các thông tin khác về platform của điện thoại ( cách nhau bằng ; )
     * - Integer : Chiều ngang của màn hình thiết bị
     * - Integer : Chiều dọc của màn hình thiết bị
     * 
     */
    public void onConnectOK();
}
