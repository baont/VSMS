package vn.me.ui.common;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Image;
import vn.me.ui.Font;
import vn.me.ui.model.FrameImage;

/**
 *
 * @author Tam Dinh
 */
public class ResourceManager {

    public static String MUI_RES_FILE = "/mui.dat";
    
    
    //#if BigScreen || Android
//# //    public static String BIG_SCREEN_RES_FILE = "/bigscreen.dat";
//#     final static int MN_BG_PNG = 0;
//#     final static int TRANS_BG_PNG = 1;
//#     final static int BUTTON_PNG = 2;
//#     public final static int CHECKBOX_PNG = 3;
//#     final static int DLG_PNG = 4;
//#     final static int EMOTIONS_PNG = 5;
//#     final static int F_PNG = 6;
//#     final static int FB_PNG = 7;
//#     final static int FOCUS_PNG = 8;
//#     final static int KEYS_PNG = 9;
//#     public final static int SLIDER_PNG = 10;
//#     public final static int SLIDERTHUMB_PNG = 11;
//#     final static int WAITCIRCLE_PNG = 12;
//#     final static int BFS_EOF = 13;
//# 
//#     public static Image imgMenuBg;
//#     public static Image imgTranparent;
//#     public static Image imgFocus, imgKeys;
//#     public static Font menuFont, keyboardFont;
//#     public static Image imgButtonBg;   
//#     
//#     public static Image slider, sliderThumb;
//#     public static FrameImage waitCircle;
//#     public static Image checkBox;
//#     public static Font boldFont, defaultFont;
//#     public static Image imgDialog;
    //#else
    public final static int CHECKBOX_PNG = 0;
    final static int DLG_PNG = 1;
    final static int EMOTIONS_PNG = 2;
    public final static int F_PNG = 3;
    final static int FB_PNG = 4;
    public final static int SLIDER_PNG = 5;
    public final static int SLIDERTHUMB_PNG = 6;
    public final static int WAITCIRCLE_PNG = 7;
    final static int BFS_EOF = 8;

    public static Image slider, sliderThumb;
    public static FrameImage waitCircle;
    public static Image checkBox;
    public static Font boldFont, defaultFont, bigFont, biFontBlack;
    public static Image imgDialog;
    //#endif

    /**
     * ---------------------------- Quan ly hinh anh -------------------------
     */
    /**
     * Load cac resource can thiet cho MUI: 1. Default Font. 2. WaitCircle. 3.
     * Dialog images 4. EditField Image. 5. Checkbox
     */
    public static void loadMUIResources() {
        Resource.setFileTable(MUI_RES_FILE);
        waitCircle = new FrameImage(Resource.createImage(WAITCIRCLE_PNG), 4);
        Font.emotionsImage = Resource.createImage(ResourceManager.EMOTIONS_PNG);
        Font.emoSize = Font.emotionsImage.getHeight();
        imgDialog = Resource.createImage(ResourceManager.DLG_PNG);
        //#if BigScreen || Android
//#         imgTranparent = Resource.createImage(TRANS_BG_PNG);
//#         imgMenuBg = Resource.createImage(MN_BG_PNG);
//#         imgFocus = Resource.createImage(FOCUS_PNG);
//#         imgKeys = Resource.createImage(KEYS_PNG);
//#         imgButtonBg = Resource.createImage(BUTTON_PNG);
//#         menuFont = boldFont;//new Font("/menufont.dat");
//#         keyboardFont = boldFont;//new Font(ResourceManager.boldFont, 0xff3b5998);
        //#endif
    }

    public static void loadDefaultFonts() {
        //#if BigScreen || Android
//# //        boldFont = new Font("/normalFont.dat");
//# ////        boldFont = new Font(" 0123456789.,:!?()+*<>/-%abcdefghijklmnopqrstuvwxyz��?�?a?????�?????��???�?????��?i?��?�?�?????o?????��?u?u?????�????dABCDEFGHIJKLMNOPQRSTUVWXYZ�$A��",//
//# ////                new byte[]{4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 9, 7, 7, 8, 8, 6, 7, 7, 7},//
//# ////                13, Resource.createImage(ResourceManager.FB_PNG), 0);
//#         boldFont = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
        //#else
        boldFont = new Font(" 0123456789.,:!?()+*<>/-%abcdefghijklmnopqrstuvwxyzáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđABCDEFGHIJKLMNOPQRSTUVWXYZĐ$ĂÁÂ=",//
                new byte[]{4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 9, 7, 7, 8, 8, 6, 7, 7, 7, 9},//
                13, Resource.createImage(ResourceManager.FB_PNG), 0);
        //#endif
        defaultFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
//         defaultFont = new Font("/font.dat");
        defaultFont = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyzáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđABCDEFGHIJKLMNOPQRSTUVWXYZĐ@Á=",//
                new byte[]{4, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 6, 4, 3, 6, 5, 6, 7, 3, 3, 10, 6, 6, 5, 6, 6, 4, 6, 6, 2, 2, 6, 2, 10, 6, 6, 6, 6, 4, 6, 3, 6, 5, 9, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 3, 2, 3, 4, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 8, 8, 8, 8, 8, 8, 5, 5, 5, 5, 5, 7, 7, 7, 8, 8, 7, 6, 8, 8, 2, 5, 8, 7, 8, 8, 8, 7, 8, 8, 7, 7, 8, 7, 9, 7, 7, 7, 8, 9, 7, 7, 9},//
                14, Resource.createImage(ResourceManager.F_PNG), 0);
        
        Image bigBl = null;
        try {
            bigBl = Image.createImage("/bl.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        biFontBlack = new Font("Ý !$%()*+-./0123456789:<>?ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzÁÂÔàáâãèéêìíòóôõùúýĂăĐđĩũơưạảấầẩẫậắằẳẵặẹẻẽếềểễệỉịọỏốồổỗộớờởỡợụủứừửữựỳỵỷỹ,=",//
                new byte[]{12, 5, 6, 10, 16, 7, 6, 8, 11, 8, 6, 6, 11, 11, 10, 10, 11, 10, 11, 11, 11, 11, 5, 11, 11, 10, 12, 12, 13, 13, 12, 11, 14, 13, 6, 10, 12, 10, 15, 13, 14, 12, 14, 13, 12, 12, 13, 12, 17, 12, 12, 12, 11, 10, 10, 11, 11, 7, 11, 10, 5, 5, 9, 5, 15, 10, 11, 10, 11, 7, 10, 7, 10, 9, 13, 9, 9, 11, 12, 12, 14, 11, 11, 11, 11, 11, 11, 11, 6, 6, 11, 11, 11, 11, 10, 10, 9, 12, 11, 13, 11, 6, 10, 13, 13, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 5, 5, 11, 11, 11, 11, 11, 11, 11, 13, 13, 13, 13, 13, 10, 10, 13, 13, 13, 13, 13, 9, 10, 10, 10, 6, 11},//
                19, bigBl, 0);
        
        Image big = null;
        try {
            big = Image.createImage("/wt.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        bigFont = new Font("Ý !$%()*+-./0123456789:<>?ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzÁÂÔàáâãèéêìíòóôõùúýĂăĐđĩũơưạảấầẩẫậắằẳẵặẹẻẽếềểễệỉịọỏốồổỗộớờởỡợụủứừửữựỳỵỷỹ,=",//
                new byte[]{12, 5, 6, 10, 16, 7, 6, 8, 11, 8, 6, 6, 11, 11, 10, 10, 11, 10, 11, 11, 11, 11, 5, 11, 11, 10, 12, 12, 13, 13, 12, 11, 14, 13, 6, 10, 12, 10, 15, 13, 14, 12, 14, 13, 12, 12, 13, 12, 17, 12, 12, 12, 11, 10, 10, 11, 11, 7, 11, 10, 5, 5, 9, 5, 15, 10, 11, 10, 11, 7, 10, 7, 10, 9, 13, 9, 9, 11, 12, 12, 14, 11, 11, 11, 11, 11, 11, 11, 6, 6, 11, 11, 11, 11, 10, 10, 9, 12, 11, 13, 11, 6, 10, 13, 13, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 5, 5, 11, 11, 11, 11, 11, 11, 11, 13, 13, 13, 13, 13, 10, 10, 13, 13, 13, 13, 13, 9, 10, 10, 10, 6, 11},//
                19, big, 0);
    }
    
    public static InputStream getInputStreamFromFile(String file) {
        return new byte[]{}.getClass().getResourceAsStream(file);
    }
}
