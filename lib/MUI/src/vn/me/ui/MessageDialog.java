package vn.me.ui;

import vn.me.core.BaseCanvas;
import vn.me.ui.common.ResourceManager;
import vn.me.ui.common.LAF;
import vn.me.ui.model.Command;

/**
 * La dialog chi co 1 nut OK va chuoi thong tin. Có 2 loại : 0 : Dialog thông
 * báo bình thường. 1 : Dialog dạng error sẽ có màu đỏ. 2 : Dialog waiting sẽ có
 * 1 icon bên trái.
 *
 * @author Tam Dinh
 *
 */
public class MessageDialog extends Dialog {

    /**
     * 0 : Message thong bao binh thuong 1 : Error Message. 2 : Wait Dialog
     */
    public int type = 0;
    /**
     * Dữ liệu để vẽ text.
     */
    protected String[] info;
    /**
     * Vị trí bắt đầu để vẽ text.
     */
    private int yTextStart;
    private int waitW, waitH;
    private byte imgWaitFrameIndex = 0;

    /**
     * Đối tượng hiển thị một hộp hội thoại.
     *
     * @param info : thông tin cần hiển thị.
     * @param left : Command Left
     * @param center : Command Center
     * @param right : Command right
     * @param type : Loại dialog
     * @see #type
     *
     */
    public MessageDialog(String info, Command left, Command center, Command right, int type) {
        padding = LAF.LOT_PADDING;
        this.cmdLeft = left == null ? cmdNull : left;
        this.cmdCenter = center == null ? cmdNull : center;
        this.cmdRight = right == null ? cmdNull : right;
        this.type = type;
        if (type == 2) {
            waitW = ResourceManager.waitCircle.frameWidth >> 2;
            waitH = ResourceManager.waitCircle.frameHeight;
        } else if (type == 1) {
            isModal = true;
        }
        w = BaseCanvas.w - LAF.LOT_PADDING * 2;// - (type == 2 ? waitW + padding : 0);
        //#if BigScreen
//#         w = w > 240 ? 240 : w;
//# //        foregroundColor = 0xFFFFFF;
        //#endif
        preferredSize.width = w - 2 * padding;
        this.info = ResourceManager.bigFont.wrap(info, w - (type == 2 ? waitW + padding + border : 2 * (padding + border)));
        h = ResourceManager.bigFont.getHeight() * this.info.length + (padding + border) * 2;
        if (h < 60) {
            h = 60;
        }
//        int bottom = LAF.LOT_ITEM_HEIGHT;
        int bottom = BaseCanvas.h / 2 - h / 2;

        setMetrics(LAF.LOT_PADDING, BaseCanvas.h - bottom - h - padding, w, h);
        yTextStart = (h >> 1) - (this.info.length * ResourceManager.bigFont.getHeight()) / 2 - padding - border;

        //#if BigScreen || Android
//#         try {
//#             spacing = 10;
//#             int btnH = ResourceManager.imgButtonBg.getHeight();
//#             int btnW = w - ((border + padding) << 1);
//#             Button btnRight = null, btnLeft = null;
//#             int count = 0;
//#             if (right != null && !right.caption.equals("")) {
//#                 count++;
//#                 btnRight = new Button(right.caption);
//#                 btnRight.align = Font.CENTER;
//#             }
//#             Command cmd;
//#             if (center == null || center.caption.equals("")) {
//#                 cmd = left;
//#             } else {
//#                 cmd = center;
//#             }
//#             if (cmd != null && !cmd.caption.equals("")) {
//#                 count++;
//#                 btnLeft = new Button(cmd.caption);
//#                 btnLeft.align = Font.CENTER;
//#             }
//#             if (count > 0) {
//#                 h += btnH;
//#                 if (count == 2) {
//#                     btnW = (w - ((border + padding) << 1) - spacing) >> 1;
//#                 }
//#             }
//# 
//#             if (btnRight != null) {
//#                 btnRight.setMetrics(count == 1 ? 0 : w - ((border + padding) << 1) - btnW,
//#                         h - ((border + padding) << 1) - btnH, btnW, btnH);
//#                 btnRight.cmdCenter = cmdRight;
//#                 btnRight.type = 6;
//#                 addWidget(btnRight);
//#             }
//#             if (btnLeft != null) {
//#                 btnLeft.setMetrics(0,
//#                         h - ((border + padding) << 1) - btnH, btnW, btnH);
//#                 btnLeft.cmdCenter = cmd;
//#                 btnLeft.type = 6;
//#                 addWidget(btnLeft);
//#             }
//#             x = destX = (BaseCanvas.w - w) >> 1;
//#             y = destY = (BaseCanvas.h - h) >> 1;
//#         } catch (Exception e) { 
//# //            e.printStackTrace(); 
//#         }
        //#endif
    }

    public void paintBackground() {
        if (type == 1) {
//            Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_ERROR_LIGHTER, LAF.CLR_ERROR_DARKER,
//                    3, 3, w - 5, h - 5, false, LAF.LOT_ARC_SIZE);
//            BaseCanvas.g.set, y, waitW, h);
            BaseCanvas.g.setColor(LAF.CLR_ERROR_LIGHTER);
            BaseCanvas.g.fillRect(2, 2, w - 4, h - 4);//, 12, 12);
        } else {
            super.paintBackground();
        }
    }

    public void paint() {
        super.paint();
        if (type == 2) {
            //#if Android || BigScreen
//#             ResourceManager.waitCircle.drawFrame(BaseCanvas.g, imgWaitFrameIndex, 
//#                     padding, (h  - waitH - ((border + padding) << 1) - 
//#                     (ResourceManager.imgButtonBg.getHeight())) >> 1, 0, 0);
            //#else
            ResourceManager.waitCircle.drawFrame(BaseCanvas.g, imgWaitFrameIndex, padding, (h >> 1) - (waitH >> 1) - padding, 0, 0);
            //#endif
        }
       
        for (int i = 0, yInfo = yTextStart; i < info.length; i++, yInfo += ResourceManager.bigFont.getHeight()) {
            ResourceManager.bigFont.drawString(BaseCanvas.g, info[i],
                    ((w + (type == 2 ? waitW + padding : 0)) >> 1) - (padding << 1), yInfo, Font.CENTER);
        }
    }

    public void update() {
        super.update();
        if (type == 2 && BaseCanvas.gameTicks % 2 == 0) {
            imgWaitFrameIndex++;
            if (imgWaitFrameIndex == ResourceManager.waitCircle.nFrame) {
                imgWaitFrameIndex = 0;
            }
        }
    }
}
