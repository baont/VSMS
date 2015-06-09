/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.me.ui.common;

/**
 *
 * @author winner
 *
 * Quy định: Các key có giá trị từ 0 => 100 là của dưới MUI sử dụng. Các key từ
 * 100 trở lên được dành cho các game.
 */
public class T {

    public final static byte VIETNAM = 0;
    public final static byte ENGLISH = 1;
    /**
     * *
     * Thuoc tinh nay dung de xac dinh dang su dung ngon ngu nao. Mac dinh la
     * tieng viet.
     */
    public static byte type = VIETNAM;

    public static String gL(int key) {
        return (type == VIETNAM ? gL_Vi(key) : gL_En(key));
    }

    /**
     * Hàm lay cac text tieng anh duoc su dung trong MUI.
     *
     * @param key
     * @return
     */
    protected static String gL_En(int key) {
        switch (key) {
            case 0:
                return "Cancel";
            case 1:
                return "Chat";
            case 2:
                return "Close";
            case 3:
                return "Clear";
            case 4:
                return "No";
            case 5:
                return "Please enter";
            case 6:
                return "OK";
            case 7:
                return "Select";
            case 8:
                return "Exit";
            case 9:
                return "Sent";
            case 10:
                return "Message";
            case 11:
                return "Can't connect to server, please select another server or update the servers list.";
            case 12:
                return "Warning";
            case 13:
                return "Not sent";
            case 14:
                return "Goal";
            case 15:
                return "Please enter phone.";
            case 16:
                return "Menu";
            case 17:
                return "Emotion";
            case 18:
                return "Back";
            case 19:
                return "Call";
            case 20:
                return "Link";
            case 21:
                return "Download";
            case 22:
                return "Please wait...";
        }
        return "";
    }

    /**
     * Ham lay cac text tieng viet dược su dung boi cac UI trong MUI.
     */
    protected static String gL_Vi(int key) {
        switch (key) {
            case 0:
                return "Thôi";
            case 1:
                return "Chat";
            case 2:
                return "Đóng";
            case 3:
                return "Xoá";
            case 4:
                return "Không";
            case 5:
                return "Chưa nhập";
            case 6:
                return "OK";
            case 7:
                return "Chọn";
            case 8:
                return "Thoát";
            case 9:
                return "Gửi";
            case 10:
                return "Tin nhắn";
            case 11:
                return "Không thể kết nối tới máy chủ, vui lòng chọn máy chủ khác hoặc cập nhật danh sách máy chủ.";
            case 12:
                return "Cảnh báo";
            case 13:
                return "Không thể gửi";
            case 14:
                return "Về";
            case 15:
                return "Chưa nhập số điện thoại!";
            case 16:
                return "Menu";
            case 17:
                return "Cảm xúc";
            case 18:
                return "Quay lai";
            case 19:
                return "Gọi";
            case 20:
                return "Đến";
            case 21:
                return "Tải về";
            case 22:
                return "Xin chờ...";

        }
        return "";
    }
}
