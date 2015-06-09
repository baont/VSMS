package vn.me.ui.model;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/*
 * Quan ly cac frame anh duoc xep theo chieu doc
 */
public class FrameImage {

    public int frameWidth;
    public int frameHeight;
    public int nFrame;
    private Image imgFrame;
    private int pos[];
    private int total;
    
    /**true - cac frame xep theo chieu ngang; false - nguoc lai.*/
    private boolean isHorizontal = false;

    /**
     * Khởi tạo đối tượng FrameImage dung.
     * @param img : Hình chứa các frame dạng đứng.
     * @param fwidth : chiều rộng mỗi frame.
     * @param fheight : chiều cao mỗi frame.
     */
    public FrameImage(Image img, int fwidth, int fheight) {
        this(img, fwidth, fheight, false);
//        imgFrame = img;
//        frameWidth = fwidth;
//        frameHeight = fheight;
////        totalHeight = img.getHeight();
////        nFrame = totalHeight / fheight;
//        pos = new int[nFrame];
//        for (int i = 0; i < nFrame; i++) {
//            pos[i] = i * fheight;
//        }
    }
    
    /**
     * Khoi tao mot FrameImage
     * img: Hinh chua cac frame theo chieu doc
     * fWight: chieu rong moi frame
     * fHeight : chieu dai moi frame
     * isH: true - frame hinh xep theo chieu ngang; false - nguoc lai.
     */
    public FrameImage(Image img, int fwidth, int fheight, boolean isH){
        imgFrame = img;
        frameWidth = fwidth;
        frameHeight = fheight;
        total = isH ? img.getWidth() : img.getHeight();
        nFrame = total / (isH ? fwidth : fheight);
        pos  = new int[nFrame];
        for(int i = 0; i < nFrame ; i++){
            pos[i] = i * (isH ? fwidth : fheight);
        }
        isHorizontal = isH;
    }
    
    /**
     * Khởi tạo một FrameImage theo chieu dung.
     * @param img : Hình chứa các frame theo chiều dọc
     * @param numberOfFrame : Số lượng frame.
     */
    public FrameImage(Image img, int numberOfFrame) {
        this(img, img.getWidth(), img.getHeight() / numberOfFrame);
// Không dùng đoạn code này vì sẽ bị làm tròn.
// 
//        imgFrame = img;
//        totalHeight = img.getHeight();
//        nFrame = totalHeight;
//        frameWidth = img.getWidth();
//        frameHeight = totalHeight / numberOfFrame;
//        pos = new int[nFrame];
//        for (int i = 0; i < nFrame; i++) {
//            pos[i] = i * frameWidth;
//        } 
    }
    /**
     * Ve frame image theo chieu isHorizontal
     * true - ngang
     * false - dung.
     */
    public FrameImage(Image img, int noFrame, boolean isHorizontital){
        this(img, img.getWidth() / noFrame, img.getHeight(), isHorizontital);
    }
    
    /**
     * Vẽ một frame hình.
     * @param index : Thứ tự của frame
     * @param x : Vị trí x cần vẽ
     * @param y : Vị trí y cần vẽ
     * @param trans : có quay hay không
     * @param g : Graphics cần vẽ.
     */
    public void drawFrame(int index, int x, int y, int trans, Graphics g) {
        drawFrame(g, index, x, y, trans, 20);
    }

    public void drawFrame(Graphics g, int index, int x, int y, int trans, int anchor) {
        if (index >= 0 && index < nFrame && !isHorizontal && imgFrame != null) {
            g.drawRegion(imgFrame, 0, pos[index], frameWidth, frameHeight, trans, x, y, anchor);
        } else if(index >= 0 && index < nFrame && imgFrame != null && isHorizontal) {
            g.drawRegion(imgFrame, pos[index], 0, frameWidth, frameHeight, trans, x, y, anchor);
        }
    }

    public void clear() {
        imgFrame = null;
    }

    /**
     * Lay frame hinh o vi tri id.
     * @param id
     * @return
     */
    public Image getImage(int id) {
        int xx = isHorizontal ? id * frameWidth : 0;
        int yy = isHorizontal ? 0 : id * frameHeight;
        return Image.createImage(imgFrame, xx, yy, frameWidth, frameHeight, 0);
//        return Image.createImage(imgFrame, 0, id * frameHeight, frameWidth, frameHeight, 0);
    }
    
    /**Lay tam hinh chua tat ca cac frame.*/
    public Image getImage() {
        return imgFrame;
    }
    
//    /**
//     * Lay Image theo chieu ngang
//     * @param id
//     * @return 
//     */
//    public Image getImageW(int id){
//        return Image.createImage(imgFrame, id * frameWidth, 0, frameWidth, frameHeight, 0);
//    }
    
}
