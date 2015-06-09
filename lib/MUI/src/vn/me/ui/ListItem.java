package vn.me.ui;

import javax.microedition.lcdui.Image;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.LAF;
import vn.me.ui.common.ResourceManager;
import vn.me.ui.common.T;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.interfaces.IListModel;
import vn.me.ui.model.Command;

/**
 * La 1 Widget duoc dung de bo trong 1 List.
 *
 * @author TamDinh
 */
public class ListItem extends WidgetGroup implements IActionListener {

    /*
     * Size của icon nếu có.
     */
    protected int iconSize = 43;
    public IListModel model;
    /*
     * Font binh thuong
     */
    public Font normalFont = ResourceManager.boldFont;
    public Font focusFont = ResourceManager.boldFont;
    public Font descriptionFont = ResourceManager.defaultFont;
    public Font focusDescFont = ResourceManager.defaultFont;
    public Font subDescriptionFont = ResourceManager.boldFont;
    public Image imgLike;// Chỉ dùng khi có tường ( ben Nhật ký mGo )

    /*S
     * Cái chuổi nằm giửa, support nhiều dòng, hiện tại chỉ áp dụng cho Tường
     */
    protected String[] infos;

    /**
     * 0 - List bình thuong. 1 - List chọn.
     */
    private byte listMode = 0;

    public boolean isSelected = false;

    public ListItem(IListModel object, int x, int y, int w, int h) {
        super(x, y, w, h);
        model = object;

        border = 1;

        IListModel listModel = model;
        String content = listModel.getContent();
        if (content != null && !"".equals(content)) {
            infos = descriptionFont.wrap(content, w - iconSize - padding);
//	    isWrapped = true;
            if (infos != null) {
                for (int i = 0; i < infos.length; i++) {
                    h += descriptionFont.getHeight() + 2;
                }
            }
        }

        this.setMetrics(x, y, w, h);
//	doLayout();

    }

    public ListItem(String string) {
    }

    public void setMode(byte mode) {
        this.listMode = mode;
        if (mode == 1) {
            cmdCenter = new Command(0, T.gL(7), this);
        }
    }

    //trong mGo dung paintBackground de ve nen.
    public void paintBackground() {
        LAF.paintListItemBG(this);
//        LAF.paintButtonBackground(this);
    }

    public void paint() {
        //<editor-fold defaultstate="collapsed" desc="Paint cua iwin.">
        IListModel listModel = (IListModel) model;
        Image icon = listModel.getIcon();
        int yInfo = padding;
        if (icon != null) {
            BaseCanvas.g.drawImage(icon, LAF.LOT_PADDING + (iconSize >> 1), h >> 1, BaseCanvas.CENTER);
        }
        if (isFocused) {
            int cx = BaseCanvas.g.getClipX();
            int cy = BaseCanvas.g.getClipY();
            int cw = BaseCanvas.g.getClipWidth();
            int ch = BaseCanvas.g.getClipHeight();
            BaseCanvas.g.clipRect(LAF.LOT_PADDING + (icon != null ? iconSize : 0), 0, w - iconSize, h);
            focusFont.drawString(BaseCanvas.g, listModel.getName(), LAF.LOT_PADDING + (icon != null ? iconSize : 0), yInfo, Font.LEFT);
            BaseCanvas.g.setClip(cx, cy, cw, ch);
            yInfo += focusFont.getHeight();
        } else {
            normalFont.drawString(BaseCanvas.g, listModel.getName(), LAF.LOT_PADDING + (icon != null ? iconSize : 0), yInfo, Font.LEFT);
            yInfo += normalFont.getHeight();
        }

        if (infos != null) {
            if (isFocused) {
                for (int i = 0; i < infos.length; i++) {
                    focusDescFont.drawString(BaseCanvas.g, infos[i], LAF.LOT_PADDING + (icon != null ? iconSize : 0), yInfo, Font.LEFT);
                    yInfo += focusDescFont.getHeight() + 2;
                }
            } else {
                for (int i = 0; i < infos.length; i++) {
                    descriptionFont.drawString(BaseCanvas.g, infos[i], LAF.LOT_PADDING + (icon != null ? iconSize : 0), yInfo, Font.LEFT);
                    yInfo += descriptionFont.getHeight() + 2;
                }
            }
        }
        String desc = listModel.getDescription();
        if (desc != null) {
            int cx = BaseCanvas.g.getClipX();
            int cy = BaseCanvas.g.getClipY();
            int cw = BaseCanvas.g.getClipWidth();
            int ch = BaseCanvas.g.getClipHeight();
            BaseCanvas.g.clipRect(LAF.LOT_PADDING + (icon != null ? iconSize : 0), (h - focusDescFont.getHeight() - padding - 10), w - 20 - LAF.LOT_PADDING - iconSize, h);
            if (isFocused && imgLike != null) {
                focusDescFont.drawString(BaseCanvas.g, desc, LAF.LOT_PADDING + (icon != null ? iconSize : 0), (h - focusDescFont.getHeight() - padding - 10), Font.LEFT);
            } else if (!isFocused) {
                descriptionFont.drawString(BaseCanvas.g, desc, LAF.LOT_PADDING + (icon != null ? iconSize : 0), (h - focusDescFont.getHeight() - padding - 10), Font.LEFT);
            }
            if (imgLike != null) {
                BaseCanvas.g.drawRegion(imgLike, 0, 0, 20, 20, 0, LAF.LOT_PADDING + iconSize + descriptionFont.getWidth(desc) - 56, (h - focusDescFont.getHeight() - padding - 3), BaseCanvas.CENTERLEFT);
                BaseCanvas.g.drawRegion(imgLike, 20, 0, 20, 20, 0, LAF.LOT_PADDING + iconSize + descriptionFont.getWidth(desc) - 24, (h - focusDescFont.getHeight() - padding - 3), BaseCanvas.CENTERLEFT);
            }
            BaseCanvas.g.setClip(cx, cy, cw, ch);

        }
        // Ve sub be tay phai

        desc = listModel.getSubDescription();
        if (desc != null) {
            subDescriptionFont.drawString(BaseCanvas.g, desc, x + w - LAF.LOT_PADDING, 1, Font.RIGHT);
        }
        icon = listModel.getSubIcon();
        if (icon != null) {
            BaseCanvas.g.drawImage(icon, x + w - LAF.LOT_PADDING, h - LAF.LOT_PADDING, BaseCanvas.RIGHTBOTTOM);
        }

        if (isFocused || isPressed) {
            super.paint();
        }
//</editor-fold>
    }

    protected void paintBorder() {
        if (border > 0) {
            LAF.paintListItemBorder(this);
        }
    }

    public void onFocused() {
        super.onFocused();
        //<editor-fold defaultstate="collapsed" desc="ham onfocus cua iwin.">
        String desc = model.getDescription();
        if (desc == null || imgLike != null) {
            return;
        }
        Label lblTemp = new Label(model.getDescription(), focusDescFont);
        lblTemp.padding = 0;
        Image icon = model.getSubIcon();
        int extraH = 0;
        if (LAF.mode == LAF.MGO) {
            extraH = LAF.LOT_PADDING << 1;
        }
        if (infos == null) {
            lblTemp.setMetrics(LAF.LOT_PADDING + (model.getIcon() != null ? LAF.LOT_AVATAR_SIZE : 0), (h - focusDescFont.getHeight() - padding - 10), w - (LAF.LOT_PADDING << 1) - LAF.LOT_AVATAR_SIZE - (icon == null ? 0 : icon.getWidth()), /*lblTemp.h*/ focusDescFont.getHeight() + extraH);
        } else {
            lblTemp.setMetrics(LAF.LOT_PADDING + (model.getIcon() != null ? LAF.LOT_AVATAR_SIZE : 0),
                    (h - focusDescFont.getHeight() - padding - 10),
                    w - (LAF.LOT_PADDING << 1) - LAF.LOT_AVATAR_SIZE - (icon == null ? 0 : icon.getWidth()),
                    /*lblTemp.h*/ focusDescFont.getHeight() + extraH);
        }
        lblTemp.isFocusable = false;
        removeAll();
        addWidget(lblTemp, false);
        lblTemp.startTicker(1000);
//</editor-fold>
    }

    public void onLostFocused() {
        super.onLostFocused();
        removeAll();
    }

    public void actionPerformed(Object o) {
        if (listMode == 1) {
            this.isSelected = !this.isSelected;
        }
    }
}
