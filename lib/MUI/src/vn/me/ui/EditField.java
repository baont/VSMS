package vn.me.ui;

import javax.microedition.lcdui.*;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.LAF;
import vn.me.ui.common.ResourceManager;
import vn.me.ui.common.T;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.model.Command;

public class EditField extends Widget implements IActionListener {

    public static int typeXpeed = 0;
    private static final int[] MAX_TIME_TO_CONFIRM_KEY = {18, 14, 11, 9, 6, 4, 2};
    private static int CARET_HEIGHT = 0;
    private static final int CARET_WIDTH = 1;
    private static final int CARET_SHOWING_TIME = 5;
    //private static final int TEXT_GAP_X = 4;
    private static final int MAX_SHOW_CARET_COUNTER = 10;
    public static final int INPUT_TYPE_ANY = 0;
    public static final int INPUT_TYPE_NUMERIC = 1;
    public static final int INPUT_TYPE_PASSWORD = 2;
    public static final int INPUT_ALPHA_NUMBER_ONLY = 3;
    private static String[] print = {" 0", ".,@?!_1\"/$-():*+<=>;%&~#%^&*{}[];\'/1", "abc2áàảãạâấầẩẫậăắằẳẵặ2", "def3đéèẻẽẹêếềểễệ3", "ghi4íìỉĩị4", "jkl5", "mno6óòỏõọôốồổỗộơớờởỡợ6", "pqrs7", "tuv8úùủũụưứừửữự8", "wxyz9ýỳỷỹỵ9", "*", "#"};
    private static String[] printA = {"0", "1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", "0", "0"};
    protected String text = "";
    protected String passwordText = "";
    protected String paintedText = "";
    private int caretPos = 0;
    private int counter = 0;
    public int maxTextLenght = 500;
    private int offsetX = 0;
    private int lastKey = BaseCanvas.KEY_NONE;
    private int keyInActiveState = 0;
    private int indexOfActiveChar = 0;
    private int showCaretCounter = MAX_SHOW_CARET_COUNTER;
    public int inputType = INPUT_TYPE_ANY;
    public static boolean isQwerty;
    public boolean isPaintInputType = true;
    public int typingModeAreaWidth;
    private boolean isReadOnly = false;
    /**
     * Nhãn đăt trước, nếu = null thi ko ve
     */
    private String caption;
    public int lableWidth = 0;
    /**
     * 0 : "abc" 1 : "Abc" 2 : "ABC" 3 : "123" 4 : "aăâ" ( Tiếng Việt ) 5 :
     * "Aăâ" 6 : "AĂÂ"
     */
    public int mode = 0;
    public static final String modeNotify[] = {"abc", "Abc", "ABC", "123"};
    public static int changeModeKey = '#';
    public static int symbolKey = '*';
    public Font normalFont = ResourceManager.bigFont;
    /**
     * Font bình thường trong trường hợp không chọn , focus. Trường hợp có focus
     * có thể là 1 defaultFont khác.
     */
//    public Font font = ResourceManager.defaultFont;
    /**
     * Font khi text field được focus.
     */
    public Font focusFont = ResourceManager.bigFont;
    /**
     * Font được vẽ vào ô kiểu gõ.
     */
    public Font modeFont = ResourceManager.bigFont;
    /**
     * Màu sắc của EditText Field.
     */
    public int focusBackgroundColor;
    public int focusBorderColor = LAF.CLR_BORDER_FOCUSED;
    public int textColor = 0;
    /**
     * Nút xóa bên phải của EditField, Nút symbol dùng hiển thị emotion hoặc tùy
     * ý.
     */
    public Command cmdDelete;
    public static Command cmdSymbol;
    /**
     * Danh cho EditTextField co label.
     */
    public int x1, y1, w1, h1;
    /**
     * Default empty text
     */
    public String defaultEmptyText = "";//L.gL(303);
    public IActionListener onTextChanged;

    //#if !BigScreen
    /**
     *
     * @param gameMidlet
     * @param actOK = if (cmd.getLabel().equals(L.gL(337))) {
     * setText(tb.getString()); } UIController.showGameCanvas();
     *
     */
    public void showNativeTextBox() {
        final TextBox tb = new TextBox("", "", 500, TextField.ANY);
        tb.addCommand(new javax.microedition.lcdui.Command(T.gL(6), javax.microedition.lcdui.Command.OK, 0));
        tb.addCommand(new javax.microedition.lcdui.Command(T.gL(0), javax.microedition.lcdui.Command.BACK, 0));
        tb.setCommandListener(
                new CommandListener() {

                    public void commandAction(javax.microedition.lcdui.Command cmd, Displayable d) {
                        if (cmd.getLabel().equals(T.gL(6))) {
                            setText(tb.getString());
                        }
                        BaseCanvas.instance.showGameCanvas();
                    }
                });
        if (inputType == INPUT_TYPE_PASSWORD) {
            tb.setConstraints(TextField.PASSWORD);
        } else if (inputType == INPUT_TYPE_NUMERIC) {
            tb.setConstraints(TextField.NUMERIC);
        } else {
            tb.setConstraints(TextField.ANY);
        }
        tb.setString(getText());
        tb.setMaxSize(maxTextLenght);
        Display.getDisplay(BaseCanvas.instance.gameMidlet).setCurrent(tb);
    }
    //#endif

    public static void setVendorTypeMode(int mode) {
//        print[0] = " 0";
        switch (mode) { // Motorola
            case 0:
                break;
            case 1:
                print[10] = " *";
                break;
//                print[11] = "#";
//            changeModeKey = '#';
//            case 0:
//                print[0] = " 0";
//                print[10] = "*";
//                print[11] = "#";
//            changeModeKey = '#';
            default:// LG, Samsung, SonyErricson
//                print[0] = " 0";
//                print[10] = "*";
                print[11] = " #";
                changeModeKey = '*';
                symbolKey = '#';
        }
    }

    private void init() {
        CARET_HEIGHT = normalFont.getHeight() - 1;
        cmdRight = cmdDelete = new Command(0, T.gL(3), this);
        typingModeAreaWidth = modeFont.getWidth("ABC") + 5;
        border = 0;
        setOffset();
    }

    public EditField() {
        this(0, 0, 0, 0, "");
    }

    public EditField(int x, int y, int w, int h) {
        this(x, y, w, h, null);
    }

    public EditField(int x, int y, int w, int h, String label) {
        this(x, y, w, h, label, ResourceManager.biFontBlack, ResourceManager.biFontBlack, ResourceManager.bigFont);
    }

    public EditField(int x, int y, int w, int h, String label, Font normalFont, Font focusFont, Font modeFont) {
        super(x, y, w, h);
        this.normalFont = normalFont;
        this.focusFont = focusFont;
        this.modeFont = modeFont;
        lableWidth = (label == null ? 0 : normalFont.getWidth(label) + 2 * LAF.LOT_PADDING);
        caption = label;
        isReadOnly = false;
        text = "";
        init();
    }

    public EditField(String text, int maxLen, int inputType) {
        this(0, 0, ResourceManager.biFontBlack.getWidth(text), ResourceManager.biFontBlack.getHeight() + 2 * LAF.LOT_PADDING);
        isReadOnly = false;
        this.text = text;
        this.maxTextLenght = maxLen;
        this.inputType = inputType;
        init();
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        cmdRight = isReadOnly ? null : cmdDelete;
    }

    public void setMetrics(int x, int y, int w, int h) {
        super.setMetrics(x, y, w, h);
        if (lableWidth == 0) {
            return;
        }

        String lb = caption;
        lableWidth = normalFont.getWidth(lb) + padding;
        while (lableWidth > (this.w * 2) / 3) {
            lb = lb.substring(0, lb.length() - 1);
            lableWidth = normalFont.getWidth(lb) + padding;
//            System.out.println("lb-: " + lb);
        }
        caption = lb;
        setOffset();
    }

    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Set input type, khi là kieu nhap so thi k ve cai notify dang sau.
     */
    public void setInputType(int inputType) {
        this.inputType = inputType;
        switch (inputType) {

            case INPUT_TYPE_NUMERIC:
                mode = 3;
                break;
            case INPUT_TYPE_ANY:
            case INPUT_TYPE_PASSWORD:
            case INPUT_ALPHA_NUMBER_ONLY:
                mode = 0;
                break;
        }
//        if (inputType == INPUT_TYPE_NUMERIC) {
//            mode = 3;
        typingModeAreaWidth = 0;
//        } else {
        if (isPaintInputType) {
            typingModeAreaWidth = modeFont.getWidth("ABC") + 5;
        }
//        mode //        }
    }

    public String getPaintedText() {
        return paintedText;
    }

    public int getCaretPos() {
        return caretPos;
    }

    public void setLabel(String label) {
//        this(x, y, w, h);
        //lableWidth = 
        setLabelWidth(ResourceManager.boldFont.getWidth(label) + (padding << 1));
        caption = label;
    }

    public void clear() {
        if (caretPos > 0 && text.length() > 0) {
            text = text.substring(0, caretPos - 1) + text.substring(caretPos, text.length());
            caretPos--;
            setOffset();
            setPasswordTest();
        }
    }

    public void setOffset() {
        if (text.length() == 0) {
            paintedText = defaultEmptyText;
//        } else if (inputType == INPUT_TYPE_PASSWORD) {
            paintedText = passwordText;
        } else {
            paintedText = text;
        }
        x1 = lableWidth;
        y1 = 0;
        w1 = w - 1 - lableWidth;
        h1 = h - 1;
        if (offsetX < 0 && normalFont.getWidth(paintedText) + offsetX < w1 - padding - 13 - typingModeAreaWidth) {
            offsetX = w1 - 10 - typingModeAreaWidth - normalFont.getWidth(paintedText);
        }
        if (offsetX + normalFont.getWidth(paintedText.substring(0, caretPos)) <= 0) {
            offsetX = -normalFont.getWidth(paintedText.substring(0, caretPos));
            offsetX = offsetX + 40;
        } else if (offsetX + normalFont.getWidth(paintedText.substring(0, caretPos)) >= w1 - 12 - typingModeAreaWidth) {
            offsetX = w1 - 10 - typingModeAreaWidth - normalFont.getWidth(paintedText.substring(0, caretPos)) - (padding << 1);
        }
        if (offsetX > 0) {
            offsetX = 0;
        }
        if (onTextChanged != null) {
            onTextChanged.actionPerformed(new Object[]{new Command(-6, null, null), this});
        }
    }

    private boolean keyPressedAny(int keyCode) {

//        if (keyCode == 'ư' || keyCode == 0xFD) {
//            keyCode = 'w';
//        } else if (keyCode == 'Ư' || keyCode == 0xDD) {
//            keyCode = 'W'; // 0xĐ
//        }

        String[] printTemp;
        if (/*
                 * inputType == INPUT_TYPE_PASSWORD ||
                 */inputType == INPUT_ALPHA_NUMBER_ONLY) {
            printTemp = printA;
        } else {
            printTemp = print;
        }
        if (keyCode == lastKey) {
            indexOfActiveChar = (indexOfActiveChar + 1) % printTemp[keyCode - '0'].length();
            char ch = printTemp[keyCode - '0'].charAt(indexOfActiveChar);
            if (mode == 0) {
                ch = Character.toLowerCase(ch);
            } else if (mode == 1) {
                ch = Character.toUpperCase(ch);
            } else if (mode == 2) {
                ch = Character.toUpperCase(ch);
            } else {
                ch = printTemp[keyCode - '0'].charAt(printTemp[keyCode - '0'].length() - 1);
            }
            String ttext = text.substring(0, caretPos > 0 ? caretPos - 1 : 0) + ch;
            if (caretPos < text.length()) {
                ttext = ttext + text.substring(caretPos, text.length());
            }
            text = ttext;
            keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
            setPasswordTest();
        } else {
            if (text.length() < maxTextLenght) {
                if (mode == 1 && lastKey != BaseCanvas.KEY_NONE) {
                    mode = 0;
                }
                indexOfActiveChar = 0;
                char ch = printTemp[keyCode - '0'].charAt(indexOfActiveChar);
                if (mode == 0) {
                    ch = Character.toLowerCase(ch);
                } else if (mode == 1) {
                    ch = Character.toUpperCase(ch);
                } else if (mode == 2) {
                    ch = Character.toUpperCase(ch);
                } else {
                    ch = printTemp[keyCode - '0'].charAt(printTemp[keyCode - '0'].length() - 1);
                }
                String ttext = text.substring(0, caretPos) + ch;
                if (caretPos < text.length()) {
                    ttext = ttext + text.substring(caretPos, text.length());
                }
                text = ttext;
                keyInActiveState = MAX_TIME_TO_CONFIRM_KEY[typeXpeed];
                caretPos++;
                setPasswordTest();
                setOffset();
            }
        }
        lastKey = keyCode;
        return true;
    }

    private boolean keyPressedAscii(int keyCode) {
        if (/*
                 * inputType == INPUT_TYPE_PASSWORD ||
                 */inputType == INPUT_ALPHA_NUMBER_ONLY) {
            if ((keyCode < '0' || keyCode > '9') && (keyCode < 'A' || keyCode > 'Z') && (keyCode < 'a' || keyCode > 'z')) {
                return false;
            }
        } else if (inputType == INPUT_TYPE_NUMERIC) {
            if ((keyCode < '0' || keyCode > '9')) {
                return false;
            }
        }
//        else if (inputType == INPUT_TYPE_PASSWORD) {
//            if (keyCode < 32 || keyCode > 126) {
//                return false;
//            }
//        }
        if (text.length() < maxTextLenght) {
            String ttext = text.substring(0, caretPos) + (char) keyCode;
            if (caretPos < text.length()) {
                ttext = ttext + text.substring(caretPos, text.length());
            }
            text = ttext;
            caretPos++;
            setPasswordTest();
            setOffset();
        }
        return true;
    }

    protected void paintBorder() {
        if (LAF.mode == LAF.IWIN) {
            LAF.paintEditFieldBorder(this);
        } else {
            if (isFocused) {
                BaseCanvas.g.setColor(0x19418c);
            } else {
                BaseCanvas.g.setColor(0x2e4b59);
            }
            BaseCanvas.g.drawRect(x1, y1, w1, h1);

//            if (isFocused) {
//                BaseCanvas.g.setColor(LAF.CLR_BORDER_FOCUSED);
//                BaseCanvas.g.drawRect(x1 + 1, y1 + 1, w1 - 2, h1 - 2);
//            }
        }
    }

    public void paintBackground() {
        if (LAF.mode == LAF.MGO) {
            if (isFocused || border > 0) {
                BaseCanvas.g.setColor(0xffffff);
                BaseCanvas.g.fillRect(x1 + 1, 1, w1 - 1, h - 2);
            }
            if (!isQwerty) {
                if (inputType != INPUT_TYPE_NUMERIC) {
                    BaseCanvas.g.setColor(0x88a4bb);
                    BaseCanvas.g.fillRect(w - typingModeAreaWidth - 3, y1 + 3, typingModeAreaWidth, h1 - 5);
                    modeFont.drawString(BaseCanvas.g, modeNotify[mode], w - 3, (h1 - modeFont.getHeight()) >> 1, Font.RIGHT);
                }
            }
            return;
        }
        /*************************/

        if (isFocused) {
            
             //#if Android || BigScreen            
//#             BaseCanvas.g.setColor(LAF.CLR_EDITFIELD_BACKGROUND);
            //#else
            BaseCanvas.g.setColor(LAF.CLR_EDITFIELD_BG);
            //#endif
           
//            BaseCanvas.g.fillRect(x1, 0, w1, h);
           
            if (lableWidth == 0) {
                BaseCanvas.g.fillRoundRect(0, 0, w - 1, h - 1, LAF.LOT_ARC_SIZE, LAF.LOT_ARC_SIZE);
//                BaseCanvas.g.fillRoundRect(0+border/2, 0+border/2, w - border, h - border, LAF.LOT_ARC_SIZE, LAF.LOT_ARC_SIZE);
            } else {
                BaseCanvas.g.fillRoundRect(0 + lableWidth, 0, w - 1 - lableWidth, h - 1, LAF.LOT_ARC_SIZE, LAF.LOT_ARC_SIZE);
//                BaseCanvas.g.fillRoundRect(0 + lableWidth+ border/2, 0+border/2, w - border - lableWidth, h - border, LAF.LOT_ARC_SIZE, LAF.LOT_ARC_SIZE);
            }
            
        }
        if (!EditField.isQwerty && isPaintInputType) {
            //#if iwin_lite
//# 	    BaseCanvas.g.setColor(LAF.CLR_POP_BGR_LIGHTER);          
            //#else
            BaseCanvas.g.setColor(LAF.CLR_EDITFIELD_EDIT_TYPE_BG);//0x88a4bb
            //#endif
            BaseCanvas.g.fillRect(w - typingModeAreaWidth - 3, y1 + 3, typingModeAreaWidth, h1 - 4);
            BaseCanvas.g.fillRect(w - 3, y1 + 4, 1, h1 - 6);
            modeFont.drawString(BaseCanvas.g, EditField.modeNotify[mode], w - 4, (h1 - modeFont.getHeight()) >> 1, Font.RIGHT);
        }
    }
    private int index = 0;
    public int xCaption = 0;

    public void paint() {
//        BaseCanvas.g.setColor(0);
//        BaseCanvas.g.fillRect(0, 0, 100, 1);
//        BaseCanvas.g.drawRect(0, 0, w - 2, h - 2);
        
        if (((textColor >> 24) & 0xff) != 0) {
            BaseCanvas.g.setColor(textColor);
        }
        if (text.length() == 0) {
            paintedText = defaultEmptyText;
        } else if (inputType == INPUT_TYPE_PASSWORD) {
            paintedText = passwordText;
        } else {
            paintedText = text;
        }
        // ve chuổi nhập       
        //BaseCanvas.g.clipRect(x1 + 3, y1, w1 - typingModeAreaWidth - 6, h1);
        if (lableWidth == 0) {
            //BaseCanvas.g.clipRect(0, y1, w1 - typingModeAreaWidth - 6, h1);
            //#if Android || BigScreen
//#             normalFont.drawString(BaseCanvas.g, paintedText, LAF.LOT_PADDING + offsetX, (h - normalFont.getHeight() - padding - border) >> 1, Font.LEFT,LAF.CLR_EDITFIELD_FOREGROUND);
            //#else
            normalFont.drawString(BaseCanvas.g, paintedText, LAF.LOT_PADDING + offsetX, (h - normalFont.getHeight() - padding - border) >> 1, Font.LEFT);
            //#endif
        } else {
            BaseCanvas.g.clipRect(0, y1, x1 + w1 - typingModeAreaWidth - 6, h1);
//            if(isFocused){
//                ResourceManager.boldFont.drawString(BaseCanvas.g, caption.substring(index), 0, (h - boldFont.getHeight()) >> 1, Font.LEFT);                
//            }else{                
//                ResourceManager.boldFont.drawString(BaseCanvas.g, caption, 0, (h - boldFont.getHeight()) >> 1, Font.LEFT);
//            }
            if (isFocused) {
                ResourceManager.boldFont.drawString(BaseCanvas.g, caption, -xCaption, (h - normalFont.getHeight()) >> 1, Font.LEFT);
            } else {
                ResourceManager.boldFont.drawString(BaseCanvas.g, caption, 0, (h - normalFont.getHeight()) >> 1, Font.LEFT);
            }
            BaseCanvas.g.setClip(x1 + 3, y1, w1 - typingModeAreaWidth - 6, h1);
            normalFont.drawString(BaseCanvas.g, paintedText, LAF.LOT_PADDING + offsetX + lableWidth, (h1 - normalFont.getHeight()) >> 1, Font.LEFT);
        }

        //<editor-fold defaultstate="collapsed" desc="Ve cay caret.">
        if (isFocused && !isReadOnly) {

            if (keyInActiveState == 0 && (showCaretCounter > 0 || (counter / CARET_SHOWING_TIME) % 2 == 0)) {
                //color trong mGo
//                BaseCanvas.g.setColor(0x000000);
                //color trong iwin
                
                //#if Android || BigScreen
//#                 BaseCanvas.g.setColor(LAF.CLR_EDITFIELD_FOREGROUND);
                //#else
                BaseCanvas.g.setColor(LAF.CLR_EDITFIELD_CARRET_COLROR);
                //#endif

                if (lableWidth == 0) {
                    BaseCanvas.g.fillRect(LAF.LOT_PADDING + offsetX
                            + normalFont.getWidth(paintedText.substring(0, caretPos)) + 1,
                            ((h - CARET_HEIGHT) >> 1) + 1, CARET_WIDTH, CARET_HEIGHT);
                } else {
                    BaseCanvas.g.fillRect(LAF.LOT_PADDING + offsetX + lableWidth + normalFont.getWidth(paintedText.substring(0, caretPos)) + 1,
                            ((h - CARET_HEIGHT) >> 1) + 1,
                            CARET_WIDTH,
                            CARET_HEIGHT);
                }

            }
        }
//</editor-fold>
//        //#if DefaultConfiguration
//        System.out.println("TextField (x, y) : ("+x+" ,"+y+")");
//        //#endif
    }

    private void setPasswordTest() {
        if (inputType == INPUT_TYPE_PASSWORD) {
            passwordText = "";
            for (int i = 0; i < text.length(); i++) {
                passwordText = passwordText + "*";
            }
            if (keyInActiveState > 0 && caretPos > 0) {
                passwordText = passwordText.substring(0, caretPos - 1) + text.charAt(caretPos - 1) + passwordText.substring(caretPos, passwordText.length());
            }
        }
    }
    long currentTime = 0;

    public void update() {
        super.update();

        counter++;
        if (keyInActiveState > 0) {
            keyInActiveState--;
            if (keyInActiveState == 0) {
                indexOfActiveChar = 0;
                if (mode == 1 && lastKey != changeModeKey) {
                    mode = 0;
                }
//                lastKey = -BaseCanvas.KEY_NONE;
                lastKey = BaseCanvas.KEY_NONE;
                setPasswordTest();
            }
        }
        if (showCaretCounter > 0) {
            showCaretCounter--;
        }
        if (System.currentTimeMillis() - currentTime > 100) {
            if (caption != null && lableWidth < normalFont.getWidth(caption)) {
                xCaption = ++xCaption >= normalFont.getWidth(caption) ? -lableWidth : xCaption;
//                index = ++index >= caption.length() ? 0 : index;
//                currentTime = System.currentTimeMillis();
            }

        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null) {
            return;
        }
        lastKey = BaseCanvas.KEY_NONE;
        keyInActiveState = 0;
        indexOfActiveChar = 0;
        this.text = text;
        this.paintedText = text;
        setPasswordTest();
        caretPos = text.length();
        setOffset();
    }

    public void insertText(String text) {
        this.text = this.text.substring(0, caretPos) + text + this.text.substring(caretPos);
        setPasswordTest();
        caretPos += text.length();
        setOffset();
    }

    public boolean pointerReleased(int x, int y) {
        if (isReadOnly) {
            return false;
        }
        //#if BigScreen
//#          if(isPressed) {
//#              if (BaseCanvas.w > BaseCanvas.h) {
//#                  isQwerty = true;
//#                  ((Screen) BaseCanvas.getCurrentScreen()).showVirtualKeyboard(this , true);
//#              } else {
//#                  ((Screen) BaseCanvas.getCurrentScreen()).showVirtualKeyboard(this , true);
//#              }
//#              isPressed = false;
//#              return true;
//#          }
        //#else
        if (isPressed) {
            isPressed = false;
            showNativeTextBox();
            return true;
        }
        //#endif
        return false;
    }

    public boolean checkKeys(int type, int keyCode) {

        // Chi xu ly key pressed.
        if (type == 1) {
            return false;
        }
        //#if TestMUI
//# //@         System.out.println("EditField CheckKey: "+keyCode);
        //#endif
        if (isReadOnly) {
            return false;
        }

        // Phim clear ( tru BlackBerry phim -8 la nut fire ).
//        //#if BlackBerry
////# //@         if (keyCode == 8 || keyCode == -204) {
////# //@             clear();
////# //@             return true;
////# //@         }
//        //#else
//        if (keyCode == 8 || keyCode == -204 || keyCode == -8) {
//            clear();
//            return true;
//        }
//        //#endif

        if (keyCode == BaseCanvas.KEY_CLEAR) {
            clear();
            return true;
        }


        // Phim chu
        if (keyCode >= 'A' && keyCode <= 'z') {
            isQwerty = true;
            typingModeAreaWidth = 0;
        }
        if (isQwerty) {
            // Nokia E71 character '_' by 2x '-'
            if (keyCode == '-') {
                if (keyCode == lastKey
                        && keyInActiveState < MAX_TIME_TO_CONFIRM_KEY[typeXpeed]) {
                    if (inputType != INPUT_TYPE_NUMERIC) {
                        text = text.substring(0, caretPos == 0 ? 0 : caretPos - 1) + '_';
                        this.paintedText = text;
                        setPasswordTest();
                        setOffset();
                        lastKey = BaseCanvas.KEY_NONE;
                        return true;
                    }
                }
                lastKey = '-';
            }

            if (isVisible && Font.isEmotion && (keyCode == symbolKey) && (inputType == INPUT_TYPE_ANY)) {
                if (cmdSymbol != null) {
                    cmdSymbol.actionPerformed(new Object[]{cmdSymbol, this});
                    return true;
                }
            }
            if (inputType == INPUT_TYPE_NUMERIC) {
                if (keyCode >= '0' && keyCode <= '9') {
                    return keyPressedAscii(keyCode);
                } else {
                    return false;
                }
            }
            if (keyCode >= 32) {
                return keyPressedAscii(keyCode);
            }
        }

        if (keyCode == changeModeKey) {
            if (inputType != INPUT_TYPE_NUMERIC) {
                mode = ++mode % 4;
//                if (mode > 3) {
//                    mode = 0;
//                }
            }
            keyInActiveState = 1;
            lastKey = keyCode;
            return true;
        } else if (isVisible && Font.isEmotion && keyCode == symbolKey && inputType == INPUT_TYPE_ANY) {
//            ((BaseCanvas.getCurrentScreen()
            if (cmdSymbol != null) {
                cmdSymbol.actionPerformed(new Object[]{cmdSymbol, this});
                return true;
            }
        }

        if (keyCode == '*') {
            keyCode = '9' + 1;
        }
        if (keyCode == '#') {
            keyCode = '9' + 2;
        }

        if (keyCode >= '0' && keyCode <= '9' + 2) {
            if (inputType == INPUT_TYPE_ANY || inputType == INPUT_TYPE_PASSWORD || inputType == INPUT_ALPHA_NUMBER_ONLY) {
                keyPressedAny(keyCode);
            } else if (inputType == INPUT_TYPE_NUMERIC) {
                keyPressedAscii(keyCode);
                keyInActiveState = 1;
            }
            return true;
        } else {
            indexOfActiveChar = 0;
            lastKey = BaseCanvas.KEY_NONE;
            if (keyCode == BaseCanvas.KEY_LEFT) {
                if (caretPos > 0) {
                    caretPos--;
                    setOffset();
                    showCaretCounter = MAX_SHOW_CARET_COUNTER;
                    return true;
                }
            } else if (keyCode == BaseCanvas.KEY_RIGHT) {
                if (caretPos < text.length()) {
                    caretPos++;
                    setOffset();
                    showCaretCounter = MAX_SHOW_CARET_COUNTER;
                    return true;
                }
            } else {
                lastKey = keyCode;
            }
        }
        return false; // Not handled
    }

    public void actionPerformed(Object o) {
        Command srcCmd = (Command) ((Object[]) o)[0];
        if (srcCmd == cmdDelete) {
            clear();
        }
    }

    public void setLabelWidth(int value) {// dacthang
        if (value > w - 60) {
            value = w - 60;
        }
        lableWidth = value;
        setOffset();
    }
    
    //********************************Cai nay them vao cho phu hop voi wap.
    /**
     * Name cua cai variable.
     */
    public String name;

    public void paintComponent() {
        super.paintComponent(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
