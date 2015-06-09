package vn.me.ui;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.Effects;
import vn.me.ui.common.ResourceManager;

public class Font {

    public static final int LEFT = BaseCanvas.TOPLEFT;
    public static final int RIGHT = BaseCanvas.TOPRIGHT;
    public static final int CENTER = BaseCanvas.TOPCENTER;
    public static String[] emotions = new String[]{":D", ":P", ":)", ":@", "(c)", "/--", "(w)",
        "(b)", ":(", "(d)", "(s)", "8|", "(y)", "(n)", ":*", "U-", "(l)", ":S", "(?)", ":zZ",
        "(B)", "(h)", "(u)", "@^", "@-"};
    
    public static int emoSize;
    public static String[] symbols;
    public static Image imgSymbols = null;
    public static final int symbolSize = 15;
    //public static Image[] emotionsImages; //  Se nang cap cho file dong sau
    //public static byte emoframeIndex = 0;
    /**
     * Bat co nay se ve emotion
     */
    public static boolean isEmotion = true;
    public static Image emotionsImage;
    public static final int STYLE_PLAIN = javax.microedition.lcdui.Font.STYLE_PLAIN;
    public static final int FACE_SYSTEM = javax.microedition.lcdui.Font.FACE_SYSTEM;
    public static final int SIZE_MEDIUM = javax.microedition.lcdui.Font.SIZE_MEDIUM;
    public static final int FACE_MONOSPACE = javax.microedition.lcdui.Font.FACE_MONOSPACE;
    public static final int FACE_PROPORTIONAL = javax.microedition.lcdui.Font.FACE_PROPORTIONAL;
    public static final int STYLE_BOLD = javax.microedition.lcdui.Font.STYLE_BOLD;
    public static final int STYLE_ITALIC = javax.microedition.lcdui.Font.STYLE_ITALIC;
    public static final int STYLE_UNDERLINED = javax.microedition.lcdui.Font.STYLE_UNDERLINED;
    public static final int SIZE_SMALL = javax.microedition.lcdui.Font.SIZE_SMALL;
    public static final int SIZE_LARGE = javax.microedition.lcdui.Font.SIZE_LARGE;
    private Image imgFont;
    public String charsets;
    public byte[] charWidths;
    private int[] cutOffsets;
    public int charHeight;
    public int charSpace;
    /**
     * true - Vertical Font, false - Horizontal Font.
     */
    private boolean type = true;
    /**
     * Dung cho browser khi can thiet dung nhieu font khac nhau de tiet kiem bo
     * nho. Neu bang null la BitmapFont.
     */
    public javax.microedition.lcdui.Font nativeFont;

    public static Font getFont(int fface, int fstyle, int fsize) {
        return (new Font(fface, fstyle, fsize));
    }

    public Font(int face, int style, int size) {
        nativeFont = javax.microedition.lcdui.Font.getFont(face, style, size);
    }

    public Font(Font orgFont, int toColor) {
        this.charsets = orgFont.charsets;
//        this.cutOffsets = orgFont.cutOffsets;
        this.charWidths = orgFont.charWidths;
        this.charHeight = orgFont.charHeight;
        this.charSpace = orgFont.charSpace;
        this.imgFont = Effects.changeColor(orgFont.imgFont, toColor);
    }
    
    public Font(Font orgFont, int fromcolor, int toColor) {
        this.charsets = orgFont.charsets;
//        this.cutOffsets = orgFont.cutOffsets;
        this.charWidths = orgFont.charWidths;
        this.charHeight = orgFont.charHeight;
        this.charSpace = orgFont.charSpace;
        this.imgFont = Effects.changeColor(orgFont.imgFont, fromcolor, toColor);
    }
    
    public Font(String charset, byte[] charWidth, int charHeight, Image bitmap, int charSpace) {
        this.charsets = charset;
        this.charWidths = charWidth;
        this.charHeight = charHeight;
        this.charSpace = charSpace;
        imgFont = bitmap;
    }

    public Font(String path) {
        try {
            InputStream is = ResourceManager.getInputStreamFromFile(path);
            DataInputStream dis = new DataInputStream(is);
            //true-dung; false - nam.
            this.type = dis.readBoolean();
            charsets = dis.readUTF();
            int l = dis.readInt();
            charWidths = new byte[l];
            for (int i = 0; i < l; i++) {
                charWidths[i] = dis.readByte();
            }
            charHeight = dis.readInt();
            charSpace = 1;

            Image img = Image.createImage(dis);
            imgFont = img;
        } catch (IOException ex) {
//            ex.printStackTrace();
        }
    }

    /**
     *
     * @param g
     * @param st
     * @param x
     * @param y
     * @param align Font align
     */
    public void drawString(Graphics g, String st, int x, int y, int align) {

        int pos;
        int x1 = x;
        int len = st.length();
        if ((align & Graphics.RIGHT) > 0) {
            x1 = x - getWidth(st);
        } else if ((align & Graphics.HCENTER) > 0) { // align center
            x1 = x - (getWidth(st) >> 1);
        }
        if ((align & Graphics.VCENTER) > 0) {
            // Trong trường hợp native font khong ho tro nen can xoa no.
            align = align ^ Graphics.VCENTER; 
        }
        if (isEmotion && emotionsImage != null) {
            for (int i = 0; i < emotions.length; i++) {
                String emotion = emotions[i];
                int indexEmotion = st.indexOf(emotion);
                if (indexEmotion > -1) {
                    String before = st.substring(0, indexEmotion);
                    String after = st.substring(indexEmotion + emotion.length(), st.length());
                    drawString(g, before, x1, y, BaseCanvas.TOPLEFT);
                    int beforeWidth = getWidth(before);
//                        g.drawRegion(emotionsImages[i], emoframeIndex * emoSize, 0, emoSize, emoSize, 0, x + beforeWidth, y, BaseCanvas.TOPLEFT);
                    g.drawRegion(emotionsImage, i * emoSize, 0, emoSize, emoSize, 0, x1 + beforeWidth, y + (getHeight() >> 1), BaseCanvas.CENTERLEFT);
                    drawString(g, after, emoSize + x1 + beforeWidth, y, BaseCanvas.TOPLEFT);
                    return;
                }
            }
        }
        if (imgSymbols != null && symbols != null) {
            for (int i = 0; i < symbols.length; i++) {
                String emotion = symbols[i];
                int indexEmotion = st.indexOf(emotion);
                if (indexEmotion > -1) {
                    String before = st.substring(0, indexEmotion);
                    String after = st.substring(indexEmotion + emotion.length(), st.length());
                    drawString(g, before, x1, y, BaseCanvas.TOPLEFT);
                    int beforeWidth = getWidth(before);
//                        g.drawRegion(emotionsImages[i], emoframeIndex * emoSize, 0, emoSize, emoSize, 0, x + beforeWidth, y, BaseCanvas.TOPLEFT);
                    g.drawRegion(imgSymbols, i * symbolSize, 0, symbolSize, symbolSize, 0, x1 + beforeWidth, y + (getHeight() >> 1), BaseCanvas.CENTERLEFT);
//                    g.drawRegion(emotionsImage, i * emoSize, 0, emoSize, emoSize, 0, x1 + beforeWidth, y + (getHeight() >> 1), BaseCanvas.CENTERLEFT);
                    drawString(g, after, symbolSize + x1 + beforeWidth, y, BaseCanvas.TOPLEFT);
                    return;
                }
            }
        }
        if (nativeFont == null) {
            for (int i = 0; i < len; i++) {
                pos = charsets.indexOf(st.charAt(i));
                if (pos == -1) {
                    pos = 0;
                }

                if (pos > -1) {
                    if (type) {
                        try {
                        g.drawRegion(imgFont, 0, pos * charHeight, charWidths[pos], charHeight, 0,
                                x1, y, BaseCanvas.TOPLEFT);
                        } catch (Exception e) {
                            e.printStackTrace();
//                            System.out.println(st);
                        }
                    } else {
                        g.drawRegion(imgFont, cutOffsets[pos], 0, charWidths[pos], charHeight, 0,
                                x1, y, BaseCanvas.TOPLEFT);
                    }
                }
                x1 += charWidths[pos] + (i < len - 1 ? charSpace : 0);
            }
        } else {
            g.setFont(nativeFont);
            g.drawString(st, x, y, align);
        }
    }

    public void drawString(Graphics g, String st, int x, int y, int align, int color) {
        if (nativeFont == null) {
            // Font.changeColor
            drawString(g, st, x, y, align);
        } else {
            g.setColor(color);
            drawString(g, st, x, y, align);
        }
    }

    public int getWidth(String st) {
        if (isEmotion && emotionsImage != null && emotions != null) {
            //boolean found = false;
            int returnValue = 0;
            for (int i = 0; i < emotions.length; i++) {
                String emotion = emotions[i];
                int indexEmotion = st.indexOf(emotion);
                if (indexEmotion > -1) {
                    String before = st.substring(0, indexEmotion);
                    String after = st.substring(indexEmotion + emotion.length(), st.length());
                    returnValue += getWidth(before);
                    returnValue += emoSize;
                    return returnValue + getWidth(after);
                }
            }
        }
        
        if (symbols!= null && imgSymbols != null) {
            //boolean found = false;
            int returnValue = 0;
            for (int i = 0; i < symbols.length; i++) {
                String emotion = symbols[i];
                int indexEmotion = st.indexOf(emotion);
                if (indexEmotion > -1) {
                    String before = st.substring(0, indexEmotion);
                    String after = st.substring(indexEmotion + emotion.length(), st.length());
                    returnValue += getWidth(before);
                    returnValue += symbolSize;
                    return returnValue + getWidth(after);
                }
            }
        }
        
        if (nativeFont == null) {
            int pos;
            int len = 0;
            int size = st.length();
            for (int i = 0; i < size; i++) {
                pos = charsets.indexOf(st.charAt(i));
                if (pos == -1) {
                    pos = 0;
                }
                len += charWidths[pos] + (i < size - 1 ? charSpace : 0);
            }
            return len;
        } else {
            return nativeFont.stringWidth(st);
        }

    }

    public String[] wrap(String text, int width) {
        Vector list = new Vector();
        int position = 0;
        int length = text.length();
        int start = 0;
        while (position < length) {
            int i = position;
            int lastBreak = -1;
            for (; i < length; i++) {
                int subW = getWidth(text.substring(position, i + 1));
                if (subW > width) {
                    if (lastBreak == -1) {
                        lastBreak = i;
                    }
                    break;
//                } else if (subW == width) {
//                    if (lastBreak == -1) {
//                        lastBreak = i + 1;
//                    }
//                    break;
                } else if (text.charAt(i) == ' ') {
                    lastBreak = i;
                } else if (text.charAt(i) == '\n') {
                    lastBreak = i;
                    break;
//                } else {

                }
            }

            if (i == length || lastBreak <= position) {
                position = i;
            } else {
                position = lastBreak;
            }
            list.addElement(text.substring(start, position).trim());

            if (position >= 0 && position < length && text.charAt(position) == '\n') {
                position++;
            }
            start = position;
        }
        String[] strs = new String[list.size()];
        list.copyInto(strs);
        return strs;
    }

    public int getHeight() {
        if (nativeFont == null) {
            return charHeight;
        } else {
            return nativeFont.getHeight();
        }
    }

    public static Font getDefaultFont() {
        return ResourceManager.defaultFont;
    }

    public int charWidth(char c) {
        if (nativeFont == null) {
            return charWidths[charsets.indexOf(c)];
        } else {
            return nativeFont.charWidth(c);
        }
    }

    public int stringWidth(String string) {
        return getWidth(string);
    }

    public int getFace() {
        return nativeFont.getFace();
    }

    public int getStyle() {
        return nativeFont.getStyle();
    }

    public int getSize() {
        return nativeFont.getSize();
    }
}
