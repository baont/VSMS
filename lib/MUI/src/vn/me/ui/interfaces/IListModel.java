package vn.me.ui.interfaces;

import javax.microedition.lcdui.Image;

/**
 *
 * @author TamDinh
 */
public interface IListModel {

    /**
     * Icon hien thi ben trai. La icon chinh
     * @return
     */
    public Image getIcon();
    /** Icon thu 2 dung de ve ben phai. Neu == null se khong ve*/
    public Image getSubIcon();
    /** Tên hiển thị dùng nhận dạng item trong list. Được vẽ nét đậm của default painter.*/
    public String getName();
    /**
     * Thông tin của item cần hiển thị. Được vẽ nét mảnh của DefaultPainter.
     * @return
     */
    public String getDescription();
    /**
     * Lấy thông tin phụ, sẽ được vẽ bên trái của DefaultPainter
     * @return
     */
    public String getSubDescription();
    
    //
    public String getContent();

}
