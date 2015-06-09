package vn.me.ui;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.LAF;

/**
 * Lớp dùng để hiển thị text nhiều dòng.
 * Support scroll.
 * Chỉ xem , không chỉnh sửa
 * @author TamDinh
 */
public class TextView extends Widget {

    /** Luu mot mang cac doan text se duoc ve theo thu tu*/
    protected String[] infos;
    /**
     * Hiện tại chỉ support font cho nguyên 1 dòng thôi.
     */
    protected Font font;
    public int scrollYLimit;
//    /** Co ve border hay khong */
//    private boolean isBorder;
    /**
     * Canh le cho text ben trong.
     * Graphics.LEFT | Graphics.RIGHT | Graphics.HCENTER ...
     * @see Graphics
     * @see BaseCanvas
     */
    public int textAlign = BaseCanvas.TOPLEFT;
    /** Scoll speed Y*/
    public int space = 3;
    /**
     * Dùng cho chức năng chat.
     */
    public Vector chatEntries;
    /**
     * Dùng cho chat
     */
//    private Font normalFontGreen;

    public TextView() {
        super();
        infos = new String[0];
        padding = LAF.LOT_PADDING;
        isScrollableY = true;
    }

    /**
     * Set text cho TextView
     * @param text : Chuoi dua vao se duoc wrap lai cho vua kich thuoc cua
     * textview.
     */
    public void setText(String text, Font f) {
        if (text == null) {
            text = "";
        }
        font = f;
        infos = f.wrap(text, w - (padding << 1));

//        fonts = new Font[infos.length];
//        for (int i = infos.length; --i >= 0;) {
//            fonts[i] = f;
//        }
        preferredSize.height = infos.length * (f.getHeight() + space) - space;
        preferredSize.width = w - padding * 2;
        scrollYLimit = preferredSize.height - h;
    }

    /**
     * Dùng để add thêm text vào. Áp dụng cho chat.
     * @param text
     * @param f
     */
    public void addText(String text, Font f) {
        if (text == null) {
            return;
        }
        String[] newTexts = f.wrap(text, w - (padding * 2));
        String[] resultTexts = new String[infos.length + newTexts.length];
        System.arraycopy(infos, 0, resultTexts, 0, infos.length);
        System.arraycopy(newTexts, 0, resultTexts, infos.length, newTexts.length);
//        Font[] newFont = new Font[resultTexts.length];
//        System.arraycopy(fonts, 0, newFont, 0, fonts.length);
//        for (int i = newTexts.length; --i >= 0;) {
//            newFont[i + fonts.length] = f;
//        }
        infos = resultTexts;
        preferredSize.height = infos.length * f.getHeight();
        preferredSize.width = w - padding * 2;
        scrollYLimit = preferredSize.height - h;
        scrollToEnd();
    }

    /**
     * @inheritDoc
     */
    public void paint() {
//        BaseCanvas.g.translate(-scrollX, -scrollY);
        if (chatEntries == null) {
            if (infos != null) {
                int yInfo = 0;
                for (int i = 0; i < infos.length; i++) {
                    int xx = (textAlign == BaseCanvas.TOPCENTER) ? (w >> 1)
                            : (textAlign == BaseCanvas.TOPRIGHT) ? w - padding : 0; 
                    if (yInfo - scrollY + font.getHeight() >= 0) {
                        font.drawString(BaseCanvas.g, infos[i], xx - scrollX, yInfo - scrollY, textAlign);
                    } 
                    yInfo += font.getHeight() + space;
                    
                    if (yInfo - scrollY > h) {
                        break;
                    }
                }
            }
        }
//        BaseCanvas.g.translate(scrollX, scrollY);
    }

    /**
     * @inheritDoc
     * @param type
     * @param keyCode
     * @return 
     */
    public boolean checkKeys(int type, int keyCode) {
        if (keyCode == BaseCanvas.KEY_DOWN && preferredSize.height > h - 2 * padding && destScrollY < scrollYLimit) {
            scrollTo(x + padding, destScrollY + h - 2 * padding, w - 2 * padding, font.getHeight());
        } else if (keyCode == BaseCanvas.KEY_UP && preferredSize.height > h - 2 * padding && destScrollY > 0) {
            scrollTo(x + padding, destScrollY - font.getHeight(), w - 2 * padding, font.getHeight());
        } else {
            return false;
        }
        return true;
    }
}
