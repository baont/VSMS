package vn.me.ui;

import java.util.Stack;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.*;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.model.Command;
//#if BigScreen || Android
//# import vn.me.ui.geom.Rectangle;
//# import javax.microedition.lcdui.game.Sprite;
//#endif

/**
 * Mot Screen chiu trach nhiem ve len GameCanvas va quan ly cac widget con cua
 * minh. Quan ly viec update objects, keys va pointer event. Cac widget chi duoc
 * quan ly khi duoc add vao children.
 *
 * @author Tam Dinh
 */
public class Screen implements IActionListener {

    /**
     * indicate that this screen needs update in the background
     */
    public boolean needBackgroundUpdate = false;
    
    /**
     * Lưu màn hình trước đó. Có 2 loại : - ID Screen : Gọi hiện màn hình tương
     * ứng. - Screen Object : chuyển qua màn hình này.
     */
    public Stack backScreens = new Stack();
    /**
     * ID của screen.
     */
    public String screenId;
    /**
     * Commands cua Man hinh. Neu tren man hinh co widget khac hoac dialog, hoac
     * Menu thi cac command cua cac control do se thay the cac command cua man
     * hinh. Co the set Null neu khong co.
     */
    public Command cmdLeft, cmdCenter, cmdRight;
    /**
     * Vector chau tat ca cac hot keys cua man hinh. Hot key dung de bat su kien
     * khi nhan vao 1 phim nao do. Bien datas trong command se cho biet phim nao
     * duoc nhan se goi den command nay.
     */
//    public Hashtable hotKeys;
    /**
     * Cac nut tuong ung voi 3 command tren MenuBarCommand Dung de quan ly viec
     * touch len command bar. Có thể kế thừa từ các màn hình khác để thay đổi
     * defaultFont chữ.
     */
    //#if Android || BigScreen
//#     protected Button btnLeft, btnCenter, btnRight;
    //#else
    protected Widget btnLeft, btnCenter, btnRight;
    //#endif
    /**
     * Hình để vẽ thanh toolbar và background.
     */
    public static Image imgSoft;
    /**
     * Quan ly editfeild chat
     */
    public EditField chatEditField;
    /**
     * Menu cua man hinh neu co.
     */
    public Menu currentMenu;
    /**
     * Danh sach cac component de Screen quan ly focus va pointer. Se duoc
     * chuyen vao lop WidgetGroup trong tuong lai
     */
    protected WidgetGroup container;
    /**
     * Co showMenu command bar hay khong
     */
    public boolean transparentCommandBar = false;
    public Font commandBarFont;
    /**
     * Widget dang duoc drag.
     */
    public Widget draggedWidget;
    /**
     * Dung cho truong hop khong co container. Cho biet man hinh dang drag hay
     * khong
     */
//    public boolean isDragActivated = false;
//    /**
//     * Dung de han che do nhay khi drag
//     */
//    public static int initialPressX, initialPressY;
    /**
     * Dialog hien tai
     */
    public static Dialog currentDialog;
    /**
     * Các dialog được hiển thị
     */
    public static Vector dialogs = new Vector();
    /**
     * lable hiển thị các thông tin từ server trả về.
     */
    public static Label lblServerInfo;
    /**
     * Xac dinh xem cai label banner no show nhu the nao.
     */
//    public boolean isShowOnCrossScreen = true;
    /**
     * Các hiệu ứng hoạt hình.
     */
    public static Vector overlays = new Vector();
//    private Vector effectsAftCmdBar = new Vector();   
    /**
     * Hinh nen de ve background.
     */
    //#if Android || BigScreen
//#     public Image imgBg;
    //#else
    public static Image imgBg;
    //#endif

    /**
     * Hàm khởi tạo không sử dụng MUI.
     */
    public Screen() {
        this(false);
    }

    /**
     *
     * @param isUseMUI true-Sử dụng MUI, hiển thị command bar.
     */
    public Screen(boolean isUseMUI) {
        if (isUseMUI) {
            container = new WidgetGroup(0, 0, BaseCanvas.w, BaseCanvas.h);
            enableCommandBar();
        }

        //#if BigScreen || Android
        //#else
        commandBarFont = ResourceManager.bigFont;
        //#endif
    }

    /**
     * Luu thong tin de chay tren thanh title 1 lan
     */
    public Screen(String title) {
        this(true);
        setTitle(title);//e(title, false);
    }
    /**
     * Cho phép các nút Left/Center/Right hien thi Cho phép command bar.
     */
    private boolean isUseCommandBar = false;

    public void updateDialogPopupOverlay() {
        for (int i = 0; i < dialogs.size(); i++) {
            Dialog d = (Dialog) dialogs.elementAt(i);
            d.update();
            if (d.isEnd) {
                dialogs.removeElement(d);
                if (dialogs.isEmpty()) {
                    currentDialog = null;
                    if (container != null) {
                        container.findDefaultfocusableWidget().requestFocus();
                    }
                } else {
                    currentDialog = (Dialog) dialogs.lastElement();
                    currentDialog.findDefaultfocusableWidget().requestFocus();
                }
            }
        }

        if (lblServerInfo != null) {
            lblServerInfo.update();
        }

        int i = 0;
        while (i < overlays.size()) {
            Overlay e = (Overlay) overlays.elementAt(i);

            if (e != null && e.isInEffect) {
                e.update(System.currentTimeMillis());
            }

            if (e.isInEffect) {
                i++;
            } else {
                overlays.removeElement(e);
                e = null;
            }
        }
    }

    protected void enableCommandBar() {
        isUseCommandBar = true;
        //#if !Android && !BigScreen
        createSoftBarImage();
        btnLeft = new Widget();
        btnLeft.setMetrics(0, BaseCanvas.h - LAF.LOT_ITEM_HEIGHT, BaseCanvas.w / 3, LAF.LOT_ITEM_HEIGHT);

        btnCenter = new Widget();
        btnCenter.setMetrics(BaseCanvas.w / 3, BaseCanvas.h - LAF.LOT_ITEM_HEIGHT, BaseCanvas.w / 3, LAF.LOT_ITEM_HEIGHT);

        btnRight = new Widget();
        btnRight.setMetrics(BaseCanvas.w * 2 / 3, BaseCanvas.h - LAF.LOT_ITEM_HEIGHT, BaseCanvas.w / 3, LAF.LOT_ITEM_HEIGHT);

        btnLeft.cmdCenter = new Command(0, "", this);
        btnCenter.cmdCenter = new Command(1, "", this);
        btnRight.cmdCenter = new Command(2, "", this);
        btnLeft.isFocusable = false;
        btnCenter.isFocusable = false;
        btnRight.isFocusable = false;
//        container.addWidget(btnLeft);
//        container.addWidget(btnCenter);
//        container.addWidget(btnRight);
        //#else
//#         createSoftBarImage();
//#         btnLeft = new Button();
//#         btnLeft.setMetrics(0, 0, BaseCanvas.w / 2, LAF.LOT_TITLE_HEIGHT);
//# 
//#         btnRight = new Button();
//#         btnRight.setMetrics(BaseCanvas.hw, 0, BaseCanvas.w / 2, LAF.LOT_TITLE_HEIGHT);
//# 
//# //        btnLeft.cmdCenter = new Command(0, "", this);
//# //        btnRight.cmdCenter = new Command(2, "", this);
//# //        btnLeft.isFocusable = false;
//# //        btnRight.isFocusable = false;
//# //        container.addWidget(btnLeft);
//# //        container.addWidget(btnRight);
        //#endif
    }

    /**
     * Ham nay duoc goi khi kich thuoc man hinh thay doi. Cac lop ke thua phai
     * override ham nay de layou lai control.
     */
    public void sizeChanged() {
        if (container != null) {
            container.w = BaseCanvas.w;
            container.h = BaseCanvas.h;
            if (isUseCommandBar) {
                btnLeft.w = btnCenter.w = btnRight.w = BaseCanvas.wd3;
                btnLeft.h = btnCenter.h = btnRight.h = BaseCanvas.wd3;
                btnCenter.x = BaseCanvas.wd3;
                btnRight.x = BaseCanvas.w2d3;
                btnLeft.y = btnCenter.y = btnRight.y = BaseCanvas.h - LAF.LOT_ITEM_HEIGHT;
            }
        }
        imgSoft = null;
        createSoftBarImage();
        //#if BigScreen || Android
//#         if (txtKeyBoard == null) {
//#             return;
//#         }
//#         if (BaseCanvas.w > BaseCanvas.h) {
//#             EditField.isQwerty = true;
//#             ((Screen) BaseCanvas.getCurrentScreen()).showVirtualKeyboard(txtKeyBoard, true);
//#         } else {
//#             EditField.isQwerty = false;
//#             ((Screen) BaseCanvas.getCurrentScreen()).showVirtualKeyboard(txtKeyBoard, false);
//#         }
        //#endif
    }

    /**
     * Show man hinh cua minh.
     *
     * @param effect : 0 : No effect 1 : slide from right -1 : slide from left
     */
    public void switchToMe(int effect) {
        switchToMe(effect, false);
    }

    /**
     * Show man hinh cua minh.
     *
     * @param effect : 0 : No effect 1 : slide from right -1 : slide from left
     */
    public void switchToMe(int effect, boolean pushPreviousScreen) {
        if (BaseCanvas.getCurrentScreen() != this) {
            // Trong cac truong hop refresh du lieu thi khong can thao tac gi
            switch (effect) {
//            case 0:
//                // No effect , Do nothing
//                break;
                case 1:
                    //#if !iwin_lite
                    startSlideFromRight();
                    //#endif
                    break;
                case -1:
                    //#if !iwin_lite
                    startSlideFromLeft();
                    //#endif
                    break;
                default:
                    // No effect, Do nothing
                    break;
            }
            if (pushPreviousScreen) {
                if (backScreens.empty() || (backScreens.peek() != ((Screen) BaseCanvas.getCurrentScreen()))
                        || ((Screen) backScreens.peek()).screenId.equals(((Screen) BaseCanvas.getCurrentScreen()).screenId)) {
                    if (((Screen) BaseCanvas.getCurrentScreen()).currentMenu != null) {
                        //reset lai man hinh truoc do, hide menu neu con co menu.
                        ((Screen) BaseCanvas.getCurrentScreen()).hideMenu();
                    }
                    backScreens.push(BaseCanvas.getCurrentScreen());
//                    backScreens.push(screenId);
                }
            }
        }
        //#if !iwin_lite
        Effects.clearCache();
        //#endif
        BaseCanvas.setCurrentScreen(this);
        if (!dialogs.isEmpty()) {
            for (int i = dialogs.size(); --i >= 0;) {
                if (!((Dialog) dialogs.elementAt(i)).isModal) {
                    hideDialog((Dialog) Screen.dialogs.elementAt(i));
                    ((Dialog) Screen.dialogs.elementAt(i)).isEnd = true;
                }
            }
        }
    }

//    /**
//     * 
//     * @return 
//     */
    public void close(Screen nextScreen) {
        if (nextScreen == null) {
            if (!backScreens.empty()) {
                ((Screen) backScreens.pop()).switchToMe(-1);
            }
        } else {
            nextScreen.switchToMe(-1);
        }
    }

    public void createSoftBarImage() {
        if (imgSoft == null) {
            imgSoft = Image.createImage(BaseCanvas.w, LAF.LOT_CMDBAR_HEIGHT);
            Graphics g = imgSoft.getGraphics();
            //#if !iwin_lite
            Effects.isRadialGradientCache = false; // Khong can cache o day.
            Effects.drawRectRadialGradient(g, LAF.CLR_BACKGROUND_LIGHTER, LAF.CLR_BACKGROUND_DARKER,
                    0, 0, BaseCanvas.w, LAF.LOT_CMDBAR_HEIGHT, 0, -10, BaseCanvas.w, 10 + LAF.LOT_CMDBAR_HEIGHT);
            Effects.isRadialGradientCache = true;
            //#else
//# 	    g.setColor(LAF.CLR_MENU_BGR);
//# 	    g.fillRect(0, 0, BaseCanvas.w, LAF.LOT_ITEM_HEIGHT);
            //#endif
            g.setColor(0);
            g.drawLine(0, 0, BaseCanvas.w, 0);
            g.setColor(LAF.CLR_BORDER);
            g.drawLine(0, 1, BaseCanvas.w, 1);
        }
    }

    /**
     * Goi cac ham ve sau: 1. paintBackground : Ve hinh nen. 2. paintChildren :
     * Ve noi dung ben trong. 3. paintTitle : Ve title neu co. 4.
     * paintCommandBar : Ve Commandbar phia duoi neu co Tat ca cac ham deu co
     * the override de ve tuy y tuy man hinh.
     *
     * @param g
     */
    public void paint() {
        BaseCanvas.g.translate(-BaseCanvas.g.getTranslateX(), -BaseCanvas.g.getTranslateY());
        BaseCanvas.g.setClip(0, 0, BaseCanvas.w, BaseCanvas.h);
        paintBackground();
        paintChildren();
        paintTitle();
        //#if BigScreen || Android
//# 
//# //        if (isUseCommandBar && !isUsevirtualKeyboard) {
//# //            paintCommandBar();
//# //        }
        //#else
        if (isUseCommandBar) {
            paintCommandBar();
        }
        //#endif


        for (int i = 0; i < overlays.size(); i++) {
            Overlay e = (Overlay) overlays.elementAt(i);
            if (e != null && e.isInEffect) {
                if (e.overCommandBar) {
                    BaseCanvas.g.setClip(0, 0, BaseCanvas.w, BaseCanvas.h);
                } else {
                    BaseCanvas.g.setClip(0, 0, BaseCanvas.w, BaseCanvas.h - LAF.LOT_ITEM_HEIGHT);
                }
                e.paint();
            }
        }
        BaseCanvas.g.setClip(0, 0, BaseCanvas.w, BaseCanvas.h);
        //#if BigScreen || Android
//#         if (isUsevirtualKeyboard) {
//#             paintVirtualKeyboard();
//#         }
        //#endif
    }

    /**
     * Ve noi dung ben trong cua mot screen. Neu co title thi noi dung se duoc
     * clip trong vung gioi han giua title va commandbar
     *
     * @param g
     */
    public void paintChildren() {
        if (container != null) {
            container.paintComponent();
        }

        if (!dialogs.isEmpty()) {
            int size = dialogs.size();
            for (int i = 0; i < size; i++) {
                if (i < dialogs.size()) {
                    ((Dialog) dialogs.elementAt(i)).paintComponent();
                }
            }
        }
        if (currentMenu != null) {
            currentMenu.paintComponent();
        }
    }

    /**
     * Ve noi dung cua CommandBar Tuy theo doi tuong tren man hinh la gi ma ve
     * tuong ung.
     *
     * @param g
     */
    public void paintCommandBar() {
        LAF.paintScreenCommandBar(this);
    }

    public void startSlideFromLeft() {
        if (container == null) {
            return;
        }
        container.x = -BaseCanvas.w;
    }

    public void startSlideFromRight() {
        container.x = BaseCanvas.w;
    }

    public void update() {
        if (container != null) {
            container.update();
        }
    }

    /**
     * @see BaseCanvas
     */
    public boolean checkKeys(int type, int keyCode) {
//        //Xu li hot key dau tien, sau do moi xu li cac su kien khac.
//        if (handleHotKeys(keyCode)) {
//            return true;
//        } else 
        if (lblServerInfo != null && lblServerInfo.cmdCenter != null) {
            if (keyCode == ((Integer) lblServerInfo.cmdCenter.datas).intValue()) {
                if (type == 1) {
                    lblServerInfo.cmdCenter.actionPerformed(new Command[]{lblServerInfo.cmdCenter});
                }
                return true;
            }
        }
        if (type == 1 && keyCode == BaseCanvas.KEY_FIRE && currentMenu == null) {
            commandCenterActionPerform(getCurrentRoot());
            return true;
        } else if (keyCode == BaseCanvas.KEY_SOFT_LEFT) {
            if (type == 0) {
                commandLeftActionPerform(getCurrentRoot());
            }
            return true;
        } else if (keyCode == BaseCanvas.KEY_SOFT_RIGHT) {
            if (type == 0) {
                commandRightActionPerform(getCurrentRoot());
            }
            return true;
        } else {
            WidgetGroup root = getCurrentRoot();
            boolean reValue = false;
            if (root != null) {
                return root.checkKeys(type, keyCode);
            }
//            //#if BigScreen || Android
//            if (isUsevirtualKeyboard && keyCode == BaseCanvas.KEY_DOWN && type == 0) {
//                Widget w = root.getFocusedWidget(true);
//                if (w instanceof EditField) {
//                    txtKeyBoard = (EditField) w;
//                } else {
//                    //neu k phai thi tat luon che do virtual keyboard.
//                    isUsevirtualKeyboard = false;
//                    BaseCanvas.instance.keyReleased(BaseCanvas.KEY_DOWN);
//                }
//            }
//            //#endif
            return reValue;
        }
    }

    public void paintBackground() {
//        BaseCanvas.g.setColor(LAF.CLR_BACKGROUND_DARKER);
//        BaseCanvas.g.fillRect(0, 0, BaseCanvas.w, BaseCanvas.h);
        BaseCanvas.g.setColor(0xF1F1F1);
        BaseCanvas.g.fillRect(0, 0, BaseCanvas.w, BaseCanvas.h);
        
        if (imgBg == null) {
//            //#if !iwin_lite
//            Effects.drawRectRadialGradient(BaseCanvas.g, LAF.CLR_BACKGROUND_LIGHTER,
//                    LAF.CLR_BACKGROUND_DARKER,
//                    0, BaseCanvas.hh, BaseCanvas.w, BaseCanvas.hh,
//                    -BaseCanvas.wd3, 0,
//                    BaseCanvas.w + BaseCanvas.w2d3, BaseCanvas.h);
//            Effects.drawLinearGradient(
//                    g, LAF.CLR_BACKGROUND_LIGHTER,
//                    LAF.CLR_BACKGROUND_DARKER,
//                    0, BaseCanvas.controlButtonH, BaseCanvas.w, BaseCanvas.controlButtonH,
//                    -BaseCanvas.wd3, 0,
//                    BaseCanvas.w + BaseCanvas.w2d3, BaseCanvas.h);
//            Effects.drawLinearGradient(
//                    g, LAF.CLR_BACKGROUND_LIGHTER,
//                    LAF.CLR_BACKGROUND_DARKER,
//                    0, 0, BaseCanvas.w, BaseCanvas.h,true);
        } else {
            //#if Android || BigScreen
//#             /**
//#              * 
//#              */
//#             BaseCanvas.g.drawImage(imgBg, 0,0 , BaseCanvas.TOPLEFT);
            //#else
            BaseCanvas.g.drawImage(imgBg, BaseCanvas.w, BaseCanvas.h - LAF.LOT_ITEM_HEIGHT, BaseCanvas.RIGHTBOTTOM);
            //#endif
        }
    }
    public String title;

    public void paintTitle() {
        if (title != null) {
            LAF.paintScreenTitle(title);
            //#if Android || BigScreen
//#             if (btnLeft.image != null) {
//#                 BaseCanvas.g.drawImage(btnLeft.image.getImage(), LAF.LOT_PADDING, LAF.LOT_TITLE_HEIGHT >> 1, Graphics.VCENTER | Graphics.LEFT);
//#                 if (btnLeft.isPressed()) {
//#                     BaseCanvas.g.drawImage(ResourceManager.imgFocus, BaseCanvas.instance.pointerPressedX, BaseCanvas.instance.pointerPressedY, Graphics.HCENTER | Graphics.VCENTER);
//#                 }
//#             }
//#             if (btnRight.image != null) {
//#                 BaseCanvas.g.drawImage(btnRight.image.getImage(), BaseCanvas.w - LAF.LOT_PADDING, LAF.LOT_TITLE_HEIGHT >> 1, Graphics.VCENTER | Graphics.RIGHT);
//#                 if (btnRight.isPressed()) {
//#                     BaseCanvas.g.drawImage(ResourceManager.imgFocus, BaseCanvas.instance.pointerPressedX, BaseCanvas.instance.pointerPressedY, Graphics.HCENTER | Graphics.VCENTER);
//#                 }
//#             }
            //#endif
        }
        if (lblServerInfo != null) {
            lblServerInfo.paintComponent();
        }
    }

    public void setTitle(String text) {
        this.title = text;
        //#if DefaultConfiguration
//        System.out.println("-------------------------------------Title: " + text);
        //#endif
    }

    public void pointerPressed(int x, int y) {
        //#if BigScreen || Android
//#         if (isUsevirtualKeyboard) {
//#             if (manageVirtualPointer(x, y, 0)) {
//#                 return;
//#             }
//#         }
        //#endif
//        initialPressX = x;
//        initialPressY = y;
        if (btnLeft != null && btnLeft.contains(x, y)) {
            btnLeft.isPressed = true;
        } else if (btnCenter != null && btnCenter.contains(x, y)) {
            btnCenter.isPressed = true;
        } else if (btnRight != null && btnRight.contains(x, y)) {
            btnRight.isPressed = true;

            //#if Android || BigScreen
//#             btnRight.pointerPressed(x, y);
            //#endif
        } else {
            WidgetGroup root = getCurrentRoot();
            if (root != null) {
                root.pointerPressed(x, y);
                if (currentMenu != null && !currentMenu.contains(x, y)) {
                    currentMenu.hide();
                }
            }
        }
    }

    public void pointerDragged(int x, int y) {
        //#if BigScreen || Android
//#         if (isUsevirtualKeyboard) {
//#             if (manageVirtualPointer(x, y, 2)) {
//#                 return;
//#             }
//#         }
//#         if (btnLeft != null && !btnLeft.contains(x, y)) {
//#             btnLeft.isPressed = false;
//#         }
//#         if (btnRight != null && !btnRight.contains(x, y)) {
//#             btnRight.isPressed = false;
//#         }
        //#endif
        if (draggedWidget == null) {
            Widget wg = null;
            if (currentDialog != null) {
                wg = currentDialog.getFocusedWidget(true);
            } else if (currentMenu != null) {
                wg = currentMenu.getFocusedWidget(true);
            } else if (container != null) {
                wg = container.getFocusedWidget(true);
            }
            if (wg != null) {
                wg.pointerDragged(x, y);
            }
        } else 
        {
            draggedWidget.pointerDragged(x, y);
        }
    }

    public void pointerReleased(int x, int y) {
        //#if BigScreen || Android
//#         if (isUsevirtualKeyboard) {
//#             if (manageVirtualPointer(x, y, 1)) {
//#                 return;
//#             }
//#         }
        //#endif
        if (btnLeft != null && btnLeft.isPressed && btnLeft.contains(x, y)) {
            btnLeft.isPressed = false;
            //#if Android || BigScreen
//#             if (btnLeft.cmdCenter != null) {
//#                 btnLeft.cmdCenter.actionPerformed(new Command[]{btnLeft.cmdCenter});
//#             }
            //#else
            commandLeftActionPerform(getCurrentRoot());
            //#endif
        } else if (btnCenter != null && btnCenter.isPressed && btnCenter.contains(x, y)) {
            commandCenterActionPerform(getCurrentRoot());
        } else if (btnRight != null && btnRight.isPressed && btnRight.contains(x, y)) {
            btnRight.isPressed = false;
            //#if Android || BigScreen
//#             if (btnRight.cmdCenter != null) {
//#                 btnRight.cmdCenter.actionPerformed(new Command[]{btnRight.cmdCenter});
//#             }
            //#else
            commandRightActionPerform(getCurrentRoot());
            //#endif
        } else if (draggedWidget != null) {
            draggedWidget.pointerReleased(x, y);
            draggedWidget = null;
        } else {
            WidgetGroup root = getCurrentRoot();
            Widget wid = root.getFocusedWidget(true);
            //#if BigScreen || Android
//#             if (currentMenu != null) {
//#                 hideMenu();
//#             }
            //#endif
            wid.pointerReleased(x, y);
        }
//        isDragActivated = false;
    }
    //#if BigScreen || Android
//#     /**
//#      * True - Xử lí và vẽ virtual keyboard tren tat ca cac control khac.
//#      */
//#     public boolean isUsevirtualKeyboard = false;
//#     /**
//#      * Hằng số để xác định chuyển mode trong virtual keyboard.
//#      */
//#     public static final int KEY_CHANGE_MODE = 2002;
//#     /**
//#      * Bàn phím ao có 2 loai là bàn phím thuong va qwerty. true - ban phim kieu
//#      * qwerty
//#      */
//#     public boolean isQwerty = false;
//#     /**
//#      * Lay theo editfield va cac mode rieng. Mode cua ban phim ao. Cac mode
//#      * rieng: -----------4 : chu hoa. -----------5 : emotion.
//#      */
//#     protected byte keyBoardMode, lastMode;
//#     /**
//#      * Text cho cac keys cua loai thuong.
//#      */
//#     public static String[] normalKeys = new String[]{"1", "2 abc", "3 def", "C",
//#         "4 ghi", "5 jkl", "6 mno", "<",
//#         "7 pqrs", "8 tuv", "9 wxyz", ">",
//#         "*.", "0", "#-+", "OK"};
//#     /**
//#      * *
//#      * Text cho cac keys cua kieu qwerty, mode = abc or ABC.
//#      */
//#     public static String[] qwertyABC = new String[]{"q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
//#         "a", "s", "d", "f", "g", "h", "j", "k", "l",
//#         "ABC", "z", "x", "c", "v", "b", "n", "m", "C",};
//#     /**
//#      * *
//#      * key codes cho cac keys cua kieu qwerty, mode = abc or ABC.
//#      */
//#     public static int[] qwertyABCCodes = new int[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
//#         'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
//#         KEY_CHANGE_MODE, 'z', 'x', 'c', 'v', 'b', 'n', 'm', BaseCanvas.KEY_CLEAR};
//#     /**
//#      * *
//#      * text cho cac keys cua kieu qwerty, mode = 123 and symbol.
//#      */
//#     public static String[] qwertyNumbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", " 0",
//#         "!", "@", "#", "$", "%", "&", "*", "?", "/",
//#         "abc", ";", "^", "(", ")", "-", "+", ":", "C"};
//#     /**
//#      * *
//#      * key codes cho cac keys cua kieu qwerty, mode = 123 and symbol.
//#      */
//#     public static int[] qwertyNumberCodes = new int[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
//#         '!', '@', '#', '$', '%', '&', '*', '?', '/',
//#         KEY_CHANGE_MODE, '_', '^', '(', ')', '-', '+', ':', BaseCanvas.KEY_CLEAR};
//#     /**
//#      * Text cho cac emotion. Cái này chỉ dành cho kieu type any cua edit field.
//#      */
//#     public static String[] qwertyEmotions = new String[]{":D", ":P", ":)", ":@", "(c)", "/--", "(w)", "(b)", ":(", "(d)",
//#         "(s)", "8|", "(y)", "(n)", ":*", "U-", "(l)", ":S", "(?)",
//#         "", ":zZ", "(B)", "(h)", "(u)", "@^", "@-", "", "C"};
//#     /**
//#      * text các nut control cua ban phim qwerty.
//#      */
//#     public static String[] qwertyControlKeys = new String[]{"Clo", ",", "space", ".", "<", ">", "OK"};
//#     /**
//#      * keycode các nut control cua ban phim qwerty.
//#      */
//#     public static int[] qwertyControlKeycode = new int[]{2000, ',', ' ', '.', BaseCanvas.KEY_LEFT, BaseCanvas.KEY_RIGHT, BaseCanvas.KEY_FIRE};
//#     /**
//#      * Text cua cac keys hien thoi.
//#      */
//#     public String[] currentKeys;
//#     /**
//#      * Key code cua cac keys hien thoi.
//#      */
//#     public int[] keyCodes;
//#     /**
//#      * Xac dinh vi tri dang duoc touch.
//#      */
//#     public int keyPressedIndex = -1;
//#     /**
//#      * Toa do x chuan cho hang nut tren cung. Dùng để so sánh và vẽ các nút phía
//#      * dưới nó.
//#      */
//#     public int[] xKeys;
//#     /**
//#      * Font cua keys.
//#      */
//#     public Font keyFont;
//#     /**
//#      * Chieu cao chuan cua tat ca cac key.
//#      */
//#     protected int keyHeight = 30, keyWidth;
//#     /**
//#      * Cac thuoc tinh de ve ban phim ao.
//#      */
//#     protected int keyBoardX, keyBoardY, keyboardH;
//#     /**
//#      * Duoc dung de xac dinh vi tri cua nut symbol.
//#      */
//#     protected Rectangle btnEmotion;
//#     /**
//#      * Khoang cach giua cac keys.
//#      */
//#     protected int spacing = 2;
//#     /**
//#      * EditField hiện thời đang được bàn phím ảo xử lí.
//#      */
//#     protected EditField txtKeyBoard;
//# 
//#     /**
//#      * Hàm này được gọi khi touch vào editfield. show ban phim ao de nhap text.
//#      *
//#      * @param owner
//#      * @param isQwerty
//#      */
//#     public void showVirtualKeyboard(EditField owner, boolean isQwerty) {
//#         //su dung keyboard.
//#         isUsevirtualKeyboard = true;
//#         //gan editfield owner.
//#         txtKeyBoard = owner;
//#         this.isQwerty = isQwerty;
//#         //set khoang cach giua cac keys.
//#         spacing = 2;
//#         keyFont = ResourceManager.keyboardFont;
//#         if (isQwerty) {
//#             //<editor-fold defaultstate="collapsed" desc="Setup for qwerty virtual keyboard">
//#             this.keyBoardMode = (byte) owner.inputType;
//#             //set mode.
//#             setVirtualKeyboardMode(keyBoardMode);
//#             //chieu cao cua 1 key.
//#             keyHeight = 50;//ResourceManager.imgButtonBg.getHeight();//30;
//#             // tong chieu cao cua keyboard
//#             keyboardH = (keyHeight << 2) + (5 * spacing);
//#             keyBoardY = BaseCanvas.h - keyboardH;
//#             keyWidth = (BaseCanvas.w - (spacing * 11)) / 10;
//#             btnEmotion = new Rectangle(BaseCanvas.w - keyWidth - 3, keyBoardY - 28, keyWidth, 26);
//#             xKeys = new int[10];
//#             xKeys[0] = (BaseCanvas.w - (spacing * 10 + (keyWidth * 10))) >> 1;
//#             for (int i = 1; i < 10; i++) {
//#                 xKeys[i] = xKeys[i - 1] + keyWidth + spacing;
//#             }
//# //</editor-fold>
//#         } else {
//#             //<editor-fold defaultstate="collapsed" desc="Setup for normal virtualkeyboard.">
//#             this.currentKeys = normalKeys;
//#             this.keyCodes = new int[]{BaseCanvas.KEY_NUM1, BaseCanvas.KEY_NUM2, BaseCanvas.KEY_NUM3, BaseCanvas.KEY_CLEAR,
//#                 BaseCanvas.KEY_NUM4, BaseCanvas.KEY_NUM5, BaseCanvas.KEY_NUM6, BaseCanvas.KEY_LEFT,
//#                 BaseCanvas.KEY_NUM7, BaseCanvas.KEY_NUM8, BaseCanvas.KEY_NUM9, BaseCanvas.KEY_RIGHT,
//#                 BaseCanvas.KEY_STAR, BaseCanvas.KEY_NUM0, BaseCanvas.KEY_POUND, BaseCanvas.KEY_FIRE};
//# 
//#             //chieu cao cua 1 key.
//#             keyHeight = 30;
//#             // tong chieu cao cua keyboard
//#             keyboardH = (keyHeight << 2) + (5 << 1);
//#             keyBoardY = BaseCanvas.h - keyboardH;
//# 
//#             keyWidth = (BaseCanvas.w - (spacing * 5)) / 4;
//#             xKeys = new int[4];
//#             xKeys[0] = (BaseCanvas.w - (spacing * 3 + (keyWidth << 2))) >> 1;
//#             for (int i = 1; i < 4; i++) {
//#                 xKeys[i] = xKeys[i - 1] + keyWidth + spacing;
//#             }
//# //</editor-fold>
//#         }
//#     }
//# 
//#     /**
//#      * Thay doi mode cua ban phim ao theo kieu qwerty.
//#      */
//#     public void changeVirtualKeyboardMode() {
//#         if (keyBoardMode == EditField.INPUT_TYPE_ANY
//#                 || keyBoardMode == EditField.INPUT_ALPHA_NUMBER_ONLY
//#                 || keyBoardMode == EditField.INPUT_TYPE_PASSWORD) {
//#             //Dang la mode chu thuong, se chuyen qua mode chu hoa.
//#             setVirtualKeyboardMode((byte) 4);
//#         } else if (keyBoardMode == 4) {
//#             //Dang la mode chu hoa, se chuyen qua mode so.
//#             setVirtualKeyboardMode((byte) EditField.INPUT_TYPE_NUMERIC);
//#         } else if (txtKeyBoard.inputType != EditField.INPUT_TYPE_NUMERIC
//#                 && keyBoardMode == EditField.INPUT_TYPE_NUMERIC) {
//#             setVirtualKeyboardMode((byte) EditField.INPUT_TYPE_ANY);
//#         }
//#     }
//# 
//#     /**
//#      * Set mode cho ban phim ao.
//#      *
//#      * @param mode theo editfield. Và cac mode dinh nghia rieng cho ban phim ao.
//#      */
//#     public void setVirtualKeyboardMode(byte mode) {
//#         keyPressedIndex = -1;
//#         keyBoardMode = mode;
//#         switch (keyBoardMode) {
//#             case EditField.INPUT_ALPHA_NUMBER_ONLY:
//#             case EditField.INPUT_TYPE_ANY:
//#             case EditField.INPUT_TYPE_PASSWORD:
//#                 currentKeys = qwertyABC;
//#                 keyCodes = qwertyABCCodes;
//#                 currentKeys[19] = "ABC";
//#                 break;
//#             case EditField.INPUT_TYPE_NUMERIC:
//#                 currentKeys = qwertyNumbers;
//#                 keyCodes = qwertyNumberCodes;
//#                 currentKeys[19] = "abc";
//#                 break;
//#             case 4://mode danh cho cac ki tu hoa.
//#                 currentKeys = qwertyABC;
//#                 keyCodes = qwertyABCCodes;
//#                 currentKeys[19] = "123";
//#                 break;
//#             case 5://emotion
//#                 //cai nay k dung keycode, minh se gan chuoi truc tiep vao chuoi cua editfield.
//#                 currentKeys = qwertyEmotions;
//#                 break;
//#         }
//#     }
//# 
//#     /**
//#      * Dieu khien tat ca cac su kien touch cua ban phim ao.
//#      *
//#      * @param x
//#      * @param y
//#      * @param type 0 - pressed. 2 - drag. 1 - released.
//#      * @return
//#      */
//#     protected boolean manageVirtualPointer(int x, int y, int type) {
//#         //Xu ly khi nhan vao nut emotion.
//#         //<editor-fold defaultstate="collapsed" desc="touch on emotion button.">
//#         if (txtKeyBoard.inputType == EditField.INPUT_TYPE_ANY
//#                 && isTouchOnMe(x, y, btnEmotion.x, btnEmotion.y, btnEmotion.getWidth(), btnEmotion.getHeight())) {
//#             if (type == 1) {
//#                 keyPressedIndex = -1;
//#                 if (keyBoardMode != 5) {
//#                     lastMode = keyBoardMode;
//#                     setVirtualKeyboardMode((byte) 5);
//#                 } else {
//#                     setVirtualKeyboardMode((byte) lastMode);
//#                 }
//#                 return true;
//#             }
//#             keyPressedIndex = -2;
//#             return true;
//#         }
//# //</editor-fold>
//#         //Xu li touch ra phia ngoai. tat che do ban phim ao.
//#         if (y < keyBoardY - 30) {
//#             isUsevirtualKeyboard = false;
//#             keyPressedIndex = -1;
//#         }
//#         //<editor-fold defaultstate="collapsed" desc="Touch trên bàn phím, xử lí phím được touch.">
//#         int code = getKeyCode(x, y);
//#         if (code == BaseCanvas.KEY_NONE) {
//#             keyPressedIndex = -1;
//#             return true;
//#         } else if (code == 2000) {
//#             if (type == 1) {
//#                 isUsevirtualKeyboard = false;
//#                 keyPressedIndex = -1;
//#                 return true;
//#             }
//#         } else if (code == KEY_CHANGE_MODE) {
//#             if (type == 1) {
//#                 changeVirtualKeyboardMode();
//#             }
//#             return true;
//#         } else if (code == BaseCanvas.KEY_FIRE) {
//#             txtKeyBoard.requestFocus();
//#             if (type == 1) {
//# //                BaseCanvas.instance.keyReleased(code);
//#                 checkKeys(type, code);
//#                 isUsevirtualKeyboard = (type == 1) ? false : true;
//#                 keyPressedIndex = type == 1 ? -1 : keyPressedIndex;
//# //                return true;
//#             }
//#             return true;
//#         } else {
//#             if (type != 2) {
//#                 if (type == 0) {
//#                     if (keyBoardMode != 5) {
//#                         BaseCanvas.instance.keyPressed(code);
//#                     } else {//emotion
//#                         if (currentKeys[keyPressedIndex].endsWith("C")) {
//#                             BaseCanvas.instance.keyPressed(BaseCanvas.KEY_CLEAR);
//#                             return true;
//#                         }
//#                         txtKeyBoard.setText(txtKeyBoard.getText() + currentKeys[keyPressedIndex]);
//#                     }
//#                 } else {
//#                     keyPressedIndex = -1;
//#                     if (keyBoardMode != 5) {
//#                         BaseCanvas.instance.keyReleased(code);
//#                     } else {//emotion
//#                         BaseCanvas.instance.keyReleased(BaseCanvas.KEY_CLEAR);
//#                         keyPressedIndex = -1;
//#                         return true;
//#                     }
//#                 }
//#                 return true;
//#             }
//#             keyPressedIndex = (type == 1) ? -1 : keyPressedIndex;
//#             return true;
//#         }
//# //</editor-fold>
//#         keyPressedIndex = (type == 1) ? -1 : keyPressedIndex;
//#         return false;
//#     }
//# 
//#     public static boolean isTouchOnMe(int xp, int yp, int x, int y, int w, int h) {
//#         return (xp > x && xp < x + w && yp > y && yp < y + h);
//#     }
//# 
//#     protected int getKeyCode(int x, int y) {
//#         int length = currentKeys.length;
//#         //duoc dung khi la dang qwerty.
//#         int currentLegnth = length;
//#         //dong cua keys.
//#         int row = 0;
//#         //count duoc dung de dem cac phim nam cung hang.
//#         int count = 0;
//#         //so column lon nhat 1 dong co dc.
//#         int maxCol = isQwerty ? 10 : 4;
//#         for (int i = 0; i < length; i++) {
//#             int xx = xKeys[count] + (isQwerty ? (maxCol == 9 ? (keyWidth >> 1) : (maxCol == 8 ? keyWidth : 0)) : 0);
//#             int yy = keyBoardY + 2 + (row * keyHeight + row * 2);
//#             int more = 0;
//#             if (isQwerty && row == 2 && ((i == length - currentLegnth) || (i == length - 1))) {
//#                 xx = i != length - 1 ? xKeys[count] : xx;
//#                 more = keyWidth >> 1;
//#             } else {
//#                 if ((txtKeyBoard.inputType == EditField.INPUT_TYPE_NUMERIC
//#                         || txtKeyBoard.inputType == EditField.INPUT_ALPHA_NUMBER_ONLY
//#                         || txtKeyBoard.inputType == EditField.INPUT_TYPE_PASSWORD)
//#                         && keyBoardMode == EditField.INPUT_TYPE_NUMERIC && i >= 10) {
//# //                    break;
//#                 }
//#             }
//# //            paintKey(xx, yy, keyWidth + more, keyHeight, i == keyPressedIndex ? 0x96ff96 : 0xdfe1e2, currentKeys, i);
//#             if (isTouchOnMe(x, y, xx, yy, keyWidth + more, keyHeight)) {
//#                 keyPressedIndex = i;
//#                 return (keyBoardMode == 4 && more == 0) ? keyCodes[i] - 32 : keyCodes[i];
//#             }
//#             count++;
//#             if (count >= maxCol) {
//#                 count = 0;
//#                 row++;
//#                 if (isQwerty) {
//#                     currentLegnth -= maxCol;
//#                     maxCol = currentLegnth > 10 ? (maxCol == 10 ? 9 : 10) : currentLegnth;
//#                 }
//#             }
//#         }
//# 
//#         if (isQwerty) {
//#             //neu la ban phim qwerty thi ve them cac button chuc nang o duoi.
//#             length = qwertyControlKeys.length;
//#             int xx = xKeys[0];
//#             int more = 0;
//#             for (int i = 0; i < length; i++) {
//#                 xx = i * (keyWidth) + i * spacing + spacing + more - (more == 0 ? 0 : keyWidth);
//#                 if (i == (length >> 1) - 1) {
//#                     more = BaseCanvas.w - (xKeys[0] << 1) - (((i) * (keyWidth + spacing) << 1)) - ((keyWidth + spacing) << 1);
//#                     if (isTouchOnMe(x, y, xx, keyBoardY + 2 + (row * keyHeight + row * 2), more, keyHeight)) {
//#                         keyPressedIndex = i + currentKeys.length;
//#                         return qwertyControlKeycode[i];
//#                     }
//# //                    paintKey(xx, keyBoardY + 2 + (row * keyHeight + row * 2), more, keyHeight, 0xdfe1e2, qwertyControlKeys, i);
//#                 } else {
//#                     if (isTouchOnMe(x, y, xx, keyBoardY + 2 + (row * keyHeight + row * 2), keyWidth, keyHeight)) {
//#                         keyPressedIndex = i + currentKeys.length;
//#                         return qwertyControlKeycode[i];
//#                     }
//# //                    paintKey(xx, keyBoardY + 2 + (row * keyHeight + row * 2), keyWidth, keyHeight, 0xdfe1e2, qwertyControlKeys, i);
//#                 }
//#             }
//#         }
//#         keyPressedIndex = -1;
//#         return BaseCanvas.KEY_NONE;
//#     }
//#     private long lastTime = System.currentTimeMillis();
//#     private boolean isPaintCaret = false;
//# 
//#     public void paintVirtualKeyboard() {
//#         //paint background
//#         paintTranparentBackground(0, keyBoardY - 30, BaseCanvas.w, keyboardH + 30);
//# //        BaseCanvas.g.setColor(0x8f98a3);
//# //        BaseCanvas.g.fillRect(0, keyBoardY - 30, BaseCanvas.w, keyboardH + 30);
//# 
//#         int length = currentKeys.length;
//#         //duoc dung khi la dang qwerty.
//#         int currentLegnth = length;
//#         //dong cua keys.
//#         int row = 0;
//#         //count duoc dung de dem cac phim nam cung hang.
//#         int count = 0;
//#         //so column lon nhat 1 dong co dc.
//#         int maxCol = isQwerty ? 10 : 4;
//#         for (int i = 0; i < length; i++) {
//#             int xx = xKeys[count] + (isQwerty ? (maxCol == 9 ? (keyWidth >> 1) : (maxCol == 8 ? keyWidth : 0)) : 0);
//#             int yy = keyBoardY + 2 + (row * keyHeight + row * 2);
//#             int more = 0;
//#             int keyBGC = 0xCBD1E6;
//#             if (isQwerty && row == 2 && ((i == length - currentLegnth) || (i == length - 1))) {
//#                 xx = i != length - 1 ? xKeys[count] : xx;
//#                 more = keyWidth >> 1;
//#             } else {
//#                 keyBGC = ((txtKeyBoard.inputType == EditField.INPUT_TYPE_NUMERIC
//#                         || txtKeyBoard.inputType == EditField.INPUT_ALPHA_NUMBER_ONLY
//#                         || txtKeyBoard.inputType == EditField.INPUT_TYPE_PASSWORD)
//#                         && keyBoardMode == EditField.INPUT_TYPE_NUMERIC && i >= 10)
//#                         ? 0xdfe1e2 : 0xCBD1E6;
//#             }
//#             paintKey(xx, yy, keyWidth + more, keyHeight, i == keyPressedIndex ? 0xFF7F2A : keyBGC/*
//#                      * 0xdfe1e2 0xCBD1E6
//#                      */, currentKeys, i, keyBoardMode == 4 ? true : false);
//# //            BaseCanvas.g.setColor(i == keyPressedIndex ? 0x96ff96 : 0xdfe1e2);
//# //            BaseCanvas.g.fillRoundRect(xx, yy, keyWidth + more, keyHeight, 6, 6);
//# //            BaseCanvas.g.setColor(0x637f97);
//# //            BaseCanvas.g.drawRoundRect(xx, yy, keyWidth + more, keyHeight, 6, 6);
//# //            Effects.drawRectRadialGradient(BaseCanvas.g, 0xf8f8f9, 0xdfe1e2,
//# //                    xKeys[count], keyBoardY + 2 + (row * 1 + row * keyHeight), keyWidth, keyHeight,
//# //                    xKeys[count], keyBoardY + 2 + (row * 1 + row * keyHeight) - (keyHeight >> 1), keyWidth, keyHeight + (keyHeight >> 1));
//#             count++;
//#             if (count >= maxCol) {
//#                 count = 0;
//#                 row++;
//#                 if (isQwerty) {
//#                     currentLegnth -= maxCol;
//#                     maxCol = currentLegnth > 10 ? (maxCol == 10 ? 9 : 10) : currentLegnth;
//#                 }
//#             }
//# 
//# //            //draw text.
//# //            keyFont.drawString(BaseCanvas.g, currentKeys[i], xx + ((keyWidth + more) >> 1), (yy + ((keyHeight - keyFont.getHeight()) >> 1)), Font.CENTER);
//#         }
//# 
//#         if (isQwerty) {
//#             //neu la ban phim qwerty thi ve them cac button chuc nang o duoi.
//#             length = qwertyControlKeys.length;
//#             int xx = xKeys[0];
//#             int more = 0;
//#             for (int i = 0; i < length; i++) {
//#                 xx = i * (keyWidth) + i * spacing + spacing + more - (more == 0 ? 0 : keyWidth);
//#                 if (i == (length >> 1) - 1) {
//#                     more = BaseCanvas.w - (xKeys[0] << 1) - (((i) * (keyWidth + spacing) << 1)) - ((keyWidth + spacing) << 1);
//#                     paintKey(xx, keyBoardY + 2 + (row * keyHeight + row * 2), more, keyHeight, (i == (keyPressedIndex - currentKeys.length)) ? 0x4398ED : 0xdfe1e2, qwertyControlKeys, i, false);
//#                 } else {
//#                     paintKey(xx, keyBoardY + 2 + (row * keyHeight + row * 2), keyWidth, keyHeight, (i == (keyPressedIndex - currentKeys.length)) ? 0x4398ED : 0xdfe1e2, qwertyControlKeys, i, false);
//#                 }
//#             }
//#         }
//#         paintTextField();
//#     }
//#     int xText = 4;
//# 
//#     private void paintTextField() {
//#         int yy = keyBoardY - 28;
//#         int xx = spacing;
//#         int ww = BaseCanvas.w - 4 - ((txtKeyBoard.inputType == EditField.INPUT_TYPE_ANY) ? (btnEmotion.getWidth() + spacing) : 0);
//#         
//#         /**
//#          * @param hh<br>
//#          * la chieu cao cua text field         * 
//#          */
//#         int hh = ResourceManager.boldFont.getHeight()+5;
//#         //fill background with white color.
//#         BaseCanvas.g.setColor(0xffffff);
//#         
//#         BaseCanvas.g.fillRoundRect(xx, yy, ww, hh, 6, 6);
//#         //paint border for text field.
//#         BaseCanvas.g.setColor(0x637f97);//0x96ff96
//#         BaseCanvas.g.drawRoundRect(xx, yy, ww, hh, 6, 6);
//#         BaseCanvas.g.setColor(0x11b3ff);//(0x96ff96);
//#         BaseCanvas.g.drawRoundRect(xx + 1, yy + 1, ww - 2,hh- 2, 6, 6);
//#         //ve button symbol.
//#         String[] t = new String[]{"Sym"};
//#         if (txtKeyBoard.inputType == EditField.INPUT_TYPE_ANY) {
//#             btnEmotion.x = BaseCanvas.w - 2 - spacing - btnEmotion.getWidth();
//#             btnEmotion.y = yy;
//#             paintKey(btnEmotion.x, btnEmotion.y, btnEmotion.getWidth(), btnEmotion.getHeight(),
//#                     keyPressedIndex == -2 ? 0xFF7F2A : 0xdfe1e2, t, 0, false);
//#         }
//#         //paint cho de viet text.
//#         int clipWidth = BaseCanvas.g.getClipWidth();
//#         int clipHeight = BaseCanvas.g.getClipHeight();
//#         int clipX = BaseCanvas.g.getClipX();
//#         int clipY = BaseCanvas.g.getClipY();
//#         
//#         xx += 2;
//#         yy += 2;
//#         ww -= 4;
//#         
//#         BaseCanvas.g.setClip(xx, yy, ww, 26 - 4);
//#         //paint text user nhap.
//#         Font f = ResourceManager.boldFont;//new Font(ResourceManager.defaultFont, 0xff000000);
//#         final int a = f.getWidth(txtKeyBoard.getPaintedText().substring(0, txtKeyBoard.getCaretPos()));        
//#         if (xText + a >= xx + ww) {
//# //            if(txtKeyBoard.getPaintedText().length() == txtKeyBoard.getCaretPos()) {
//#             //o cuoi chuoi.
//#             xText = xx - (a - ww) - 1;
//# //            }
//#         } else if (xText + a <= xx) {
//#             if (txtKeyBoard.getCaretPos() > 0) {
//#                 xText += f.getWidth(""+ txtKeyBoard.getPaintedText().charAt(txtKeyBoard.getCaretPos() - 1)) + 4;
//#             }
//#         }
//#         
//#         f.drawString(BaseCanvas.g, txtKeyBoard.getPaintedText(), xText, yy , Font.LEFT,0x000000);
//#         BaseCanvas.g.setColor(0x000000);
//#         
//#         if (System.currentTimeMillis() - lastTime > 900) {
//#             lastTime = System.currentTimeMillis();
//#             isPaintCaret = !isPaintCaret;
//#         }
//#         if (isPaintCaret) {
//#             BaseCanvas.g.drawLine( xText + a, yy + 2, xText + a, yy + 20);
//#         }
//#         BaseCanvas.g.setClip(clipX, clipY, clipWidth, clipHeight);
//#         
//#     }
//# 
//#     private void paintKey(int x, int y, int w, int h, int color, String[] text, int index, boolean isUpperCase) {
//# //        if(text == qwertyControlKeys){
//# //            if (keyPressedIndex == index+currentKeys.length) {            
//# //                BaseCanvas.g.drawRegion(Effects.createButtonBG(ResourceManager.imgButtonBg, w, h), 0, 0, w, h, Sprite.TRANS_MIRROR_ROT180, x, y, 0);
//# //            } else {
//# //                BaseCanvas.g.drawImage(Effects.createButtonBG(ResourceManager.imgButtonBg, w, h), x, y, 0);
//# //            }
//# //        }else{
//#         if (keyPressedIndex == index + (text == qwertyControlKeys?currentKeys.length:0)) {            
//# //            BaseCanvas.g.drawRegion(Effects.createButtonBG(ResourceManager.imgButtonBg, w, h), 0, 0, w, h, Sprite.TRANS_MIRROR_ROT180, x, y, 0);
//#             Effects.drawLinearGradient(BaseCanvas.g,LAF.CLR_BUTTON_START ,LAF.CLR_BUTTON_END , x, y, w, h, false,LAF.LOT_ARC_SIZE);
//#         } else {
//# //            BaseCanvas.g.drawImage(Effects.createButtonBG(ResourceManager.imgButtonBg, w, h), x, y, 0);
//#             Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_BUTTON_END, LAF.CLR_BUTTON_START, x, y, w, h, false,LAF.LOT_ARC_SIZE);
//#         }
//#         
//#                     
//# //        }
//#         
//#         
//#         //draw text.
//#         if (text[index].equals("Clo")) {
//#             BaseCanvas.g.drawRegion(ResourceManager.imgKeys, 0, 0, 10, 10,
//#                     0, x + (w >> 1), y + (h >> 1), Graphics.HCENTER | Graphics.VCENTER);
//#         } else if (text[index].equals("Sym")) {
//#             keyFont.drawString(BaseCanvas.g,
//#                     ":D",
//#                     x + (w >> 1), (y + ((keyHeight - keyFont.getHeight()) >> 1)), Font.CENTER,0xFFFFFF);
//#         } else if (text[index].equals("<") || text[index].equals(">")) {
//#             BaseCanvas.g.drawRegion(ResourceManager.imgKeys, 10, 0, 10, 10,
//#                     text[index].equals("<") ? 0 : Sprite.TRANS_MIRROR, x + (w >> 1), y + (h >> 1), Graphics.HCENTER | Graphics.VCENTER);
//#         } else if (text[index].equals("C")) {
//#             BaseCanvas.g.drawRegion(ResourceManager.imgKeys, 10, 0, 10, 10,
//#                     0, x + (w >> 1) + 1, y + (h >> 1) + 1, Graphics.RIGHT | Graphics.VCENTER);
//#             BaseCanvas.g.setColor(0xff7500);
//#             BaseCanvas.g.fillRect(x + (w >> 1) - 2, y + (h >> 1) - (ResourceManager.imgKeys.getHeight() >> 1),
//#                     ResourceManager.imgKeys.getHeight(), ResourceManager.imgKeys.getHeight());
//#             BaseCanvas.g.setColor(0x000000);
//#             BaseCanvas.g.drawRect(x + (w >> 1) - 2, y + (h >> 1) - (ResourceManager.imgKeys.getHeight() >> 1),
//#                     ResourceManager.imgKeys.getHeight(), ResourceManager.imgKeys.getHeight());
//#         } else {
//#             keyFont.drawString(BaseCanvas.g,
//#                     isUpperCase ? text[index].toUpperCase() : text[index],
//#                     x + (w >> 1), (y + ((keyHeight - keyFont.getHeight()) >> 1)), Font.CENTER,0xFFFFFF);
//#         }
//#     }
//#     public static Image virtualKeyboardbg;
//# 
//#     private void paintTranparentBackground(int x, int y, int w, int h) {
//#         if (virtualKeyboardbg == null || virtualKeyboardbg.getWidth() != w || virtualKeyboardbg.getHeight() != h) {
//#             virtualKeyboardbg = Effects.scale(ResourceManager.imgTranparent, w, h);
//#         }
//#         if (virtualKeyboardbg != null) {
//#             BaseCanvas.g.drawImage(virtualKeyboardbg, x, y, 0);
//#         }
//# //        int currentLegnth = w;
//# //        int unitH = ResourceManager.imgTranparent.getHeight();
//# //        int count = h / unitH + 1;
//# //        int i = 0;
//# //        while (i < count) {
//# //            if (currentLegnth <= 200) {
//# //                BaseCanvas.g.drawRegion(ResourceManager.imgTranparent, 12, 0, w, unitH, 0, x, y + i * unitH, Graphics.TOP | Graphics.LEFT);
//# //            } else {
//# //                int xx = x;
//# //                currentLegnth = currentLegnth > 200 ? 200 : currentLegnth;
//# //                while (xx < w) {
//# //                    BaseCanvas.g.drawRegion(ResourceManager.imgTranparent, 12, 0, currentLegnth, unitH,
//# //                            0, xx, y + i * unitH, Graphics.TOP | Graphics.LEFT);
//# //                    xx += currentLegnth;
//# //                    currentLegnth = (w - xx);
//# //                    currentLegnth = currentLegnth > 200 ? 200 : currentLegnth;
//# //                }
//# //                currentLegnth = w;
//# //            }
//# //            i++;
//# //        }
//#     }
    //#endif

    protected void commandLeftActionPerform(WidgetGroup root) {
        if (root != null) {
            Widget wid = root.getFocusedWidget(true);
            if (wid != null) {
                wid.isPressed = false;
                Command left = wid.getLeftCommand();
                if (left != null) {
                    left.actionPerformed(new Object[]{left, wid});
                    return;
                }
            } else if (root.cmdLeft != null) {
                root.cmdLeft.actionPerformed(
                        new Object[]{root.cmdLeft, root});
                return;
            }
        }
        if (currentDialog == null && currentMenu == null && cmdLeft != null) {
            cmdLeft.actionPerformed(new Object[]{cmdLeft, btnLeft});
        }
    }

    protected void commandCenterActionPerform(WidgetGroup root) {

        if (root != null) {
            Widget wid = root.getFocusedWidget(true);
            if (wid != null) {
                wid.isPressed = false;
                Command center = wid.getCenterCommand();
                if (center != null) {
                    if (currentMenu != null) {
                        System.out.println(this.toString() + "Hide menu khi focus");
                        hideMenu();
                    }
                    center.actionPerformed(new Object[]{center, wid});
                    return;
                }
            } else if (root.cmdCenter != null) {
                if (currentMenu != null) {
                    hideMenu();
                }
                root.cmdCenter.actionPerformed(
                        new Object[]{root.cmdCenter, root});

                return;
            }
        }
        if (currentDialog == null && cmdCenter != null) {
            cmdCenter.actionPerformed(new Object[]{cmdCenter, btnCenter});
        }

    }

    protected void commandRightActionPerform(WidgetGroup root) {

        if (root != null) {
            Widget wid = root.getFocusedWidget(true);
            if (wid != null) {
                wid.isPressed = false;
                Command right = wid.getRightCommand();
                if (right != null) {
                    right.actionPerformed(new Object[]{right, wid});
                    return;
                }

            } else if (root.cmdRight != null) {
                root.cmdRight.actionPerformed(
                        new Object[]{root.cmdRight, root});
                return;
            }
        }

        if (currentDialog == null && cmdRight != null) {
            cmdRight.actionPerformed(new Object[]{cmdRight, btnRight});
        }
    }

    /**
     * Hien thi Menu.
     *
     * @param menu : Danh sach Command
     * @param pos : 0: LEFT, 1 : RIGHT, 2 : CENTER
     * @param initialFocusIndex : =-1 mac dinh, >= 0 : focus vao item index duoc
     * chi dinh
     *
     */
    public void showMenu(Vector menu, int pos) {
        if (menu == null || menu.isEmpty()) {
            return;
        }
        if (currentMenu == null) {
            currentMenu = new Menu();
            currentMenu.focusWid = container.getFocusedWidget(true);
//             currentMenu.show(menu, pos, transparentCommandBar);//cai nay chay on dinh ne.            
            currentMenu.showNewMenu(menu, pos);
//            currentMenu.isVisible = true;
            container.addWidget(currentMenu);
            currentMenu.requestFocus();
        } else {
            currentMenu.showNewMenu(menu, pos);
            currentMenu.isShowNextMenu = true;
        }
    }

    public void hideMenu() {
        container.removeWidget(currentMenu);
        if (currentMenu != null && currentMenu.focusWid != null && currentMenu.focusWid.isVisible
                && currentDialog == null) {
            currentMenu.focusWid.requestFocus();
        }
        currentMenu = null;
    }

    /**
     * Makes sure the component is visible in the scroll if this container is
     * scrollable
     *
     * @param c the componant to be visible
     */
    public void scrollComponentToVisible(Widget c) {
//        if ()
        Widget parent = c.parent;
        while (parent != null) {
            if (parent.isScrollable()) {
                if (parent instanceof WidgetGroup) {
                    ((WidgetGroup) parent).scrollComponentToVisible(c);
                }
                scrollComponentToVisible(parent);
                return;
            }
            c = parent;
            parent = parent.parent;
        }
    }

    protected void onFocusChanged(Widget lostFocusWidget, Widget focusedWidget) {
        setDefaultFocus(focusedWidget);
        if (lostFocusWidget != null) {
            lostFocusWidget.onLostFocused();
        }
        if (focusedWidget != null) {
            scrollComponentToVisible(focusedWidget);
            focusedWidget.onFocused();
        }
    }

    private void setDefaultFocus(Widget focusedWidget) {
        Widget parent = focusedWidget.parent;
        while (parent != null) {
            if (parent instanceof WidgetGroup) {
                ((WidgetGroup) parent).defaultFocusWidget = focusedWidget;
            }
            parent = parent.parent;
        }
    }

    /**
     * Request focus for a screen child widget
     *
     * @param wid the Screen child widget
     */
    public void requestFocus(Widget wid) {
        if (wid == null) {
            return;
        }
        WidgetGroup wg = currentDialog == null ? container : currentDialog;
        if (wg != null) {
            Widget oldFocusedWid = wg.getFocusedWidget(true);
            if (oldFocusedWid != wg) {
                oldFocusedWid.setFocusWithParents(false);
            }
            if (wid != null) {
                wid.setFocusWithParents(true);
                onFocusChanged(oldFocusedWid, wid);
            }
        }
    }

    public void showDialog(Dialog newDialog, boolean useAppearAniamtion) {
        newDialog.isEnd = false;
        newDialog.useAppearAnimation = useAppearAniamtion;
        if (useAppearAniamtion) {
            newDialog.x = -newDialog.w;
            newDialog.destX = (BaseCanvas.w - newDialog.w) >> 1;
        }
        
        newDialog.focusedWid = container.getFocusedWidget(true);
        if (newDialog.focusedWid != container) {
            newDialog.focusedWid.setFocusWithParents(false);
        }
        dialogs.addElement(newDialog);
        currentDialog = newDialog;
        if (newDialog.defaultFocusWidget != null) {
            newDialog.defaultFocusWidget.requestFocus();
        } else if (newDialog.children.length > 0) {
            newDialog.children[0].requestFocus();
        } else {
            newDialog.requestFocus();
        }
    }

    public void showDialog(Dialog newDialog) {
        showDialog(newDialog, false);
    }

    public void hideDialog() {
        hideDialog(currentDialog);
    }

    public void hideDialog(Dialog dialog) {
        if (dialog == null || dialogs.isEmpty()) {
            return;
        }

        dialog.onClose();
    }

    public void hideAllDialog() {
        if (container == null || dialogs.isEmpty()) {
            return;
        }
        for (int i = 0; i < dialogs.size(); i++) {
            Dialog d = (Dialog) dialogs.elementAt(i);
            d.onClose();
        }
        dialogs.removeAllElements();
        currentDialog = null;
        container.findDefaultfocusableWidget().requestFocus();
    }

    public void onClosed() {
        // Các lớp kế thừa release resource tại đây.
        //#if Android || BigScreen
//#         imgBg = null;
        //#endif
    }

    public boolean keyPressed(int keyCode) {
        if (currentDialog != null) {
            Widget focusWid = currentDialog.getFocusedWidget(true);
            if (focusWid instanceof EditField) {
                return focusWid.checkKeys(0, keyCode);
            }
        } else if (container != null) {
            Widget focusWid = container.getFocusedWidget(true);
            if (focusWid instanceof EditField) {
                boolean isHandled = focusWid.checkKeys(0, keyCode);
                return isHandled;
            } else if (chatEditField != null && currentDialog == null && currentMenu == null) {
                if (keyCode > 0) {
                    return chatEditField.checkKeys(0, keyCode);
                }
            }
        }
        return false;
    }

    public boolean keyReleased(int i) {
        return false;
    }

    public void onShowed() {
        //#if DefaultConfiguration

        //#endif
//        if ( container != null && container.defaultFocusWidget != null )
//            container.defaultFocusWidget.requestFocus();
        if (currentDialog != null) {
            currentDialog.isFocused = false;
            currentDialog.requestFocus();
        }
    }

    /**
     * Add mot widget vao man hinh. Chi dung cho man hinh co su dung MUI
     * framework.
     *
     * @param component : Widget can them vao.
     * @see Widget
     */
    public void addWidget(Widget component) {
        if (container != null) {
            container.addWidget(component);
        }
    }

    //#if BigScreen || Android
//#     /**
//#      * Dùng cái này thay cho cái container.removeAll.
//#      */
//#     public void removeAllWidget() {
//#         if (container != null) {
//#             container.removeAll();
//#         }
//#     }
    //#endif
    public void removeWidget(Widget component) {
        if (container != null) {
            container.removeWidget(component);
        }
    }

    public void hideWidget(Widget component) {
        container.hideWidget(component);
    }

    public void showBanner(String info) {
        if (LAF.mode == LAF.IWIN) {
            lblServerInfo = new Label(info);
        } else {
            lblServerInfo = new Label(info, ResourceManager.boldFont);
        }
        //        {
        //
        //            public void paintBackground() {
        //                BaseCanvas.g.setColor(LAF.CLR_ERROR_LIGHTER);
        //                BaseCanvas.g.fillRect(0, 0, w, h);
        //            }
        //        }
        ;
        lblServerInfo.padding = LAF.LOT_PADDING;
        lblServerInfo.scrollType = 1;
        lblServerInfo.speed = 1;
        lblServerInfo.setMetrics(0, 0, BaseCanvas.w, LAF.LOT_TITLE_HEIGHT);
        lblServerInfo.y = -lblServerInfo.h;
        lblServerInfo.startTicker(1000);
    }

    public void addHotKeyForBanner(int keyCode, Command cmd) {
        cmd.datas = new Integer(keyCode);
        lblServerInfo.cmdCenter = cmd;
    }
//    public static Font chatEditFieldFont;

    public void enableChatEditField(boolean isEnabled) {
        if (isEnabled) {
            chatEditField = new EditField(0, BaseCanvas.h - 2 * LAF.LOT_ITEM_HEIGHT, BaseCanvas.w, LAF.LOT_ITEM_HEIGHT);
            chatEditField.cmdCenter = new Command(-2, T.gL(1), this);
            chatEditField.cmdLeft = new Command(-3, T.gL(16), this);
            chatEditField.isVisible = false;
            chatEditField.onTextChanged = this;
//            chatEditField.boldFont = ResourceManager.whiteFont;
        } else {
            removeWidget(chatEditField);
            chatEditField = null;
        }
    }

    public void onChat(String text) {
    }

    public void actionPerformed(Object o) {
        Command srcCmd = (Command) ((Object[]) o)[0];
        switch (srcCmd.id) {
            case 0:
                checkKeys(1, BaseCanvas.KEY_SOFT_LEFT);
                break;
            case 1:
                checkKeys(1, BaseCanvas.KEY_FIRE);
                break;
            case 2:
                checkKeys(1, BaseCanvas.KEY_SOFT_RIGHT);
                break;
            case -2:
                String str = chatEditField.getText().trim();
                if (str.length() > 0) {
                    onChat(str);
                }
            case -4:
                removeWidget(chatEditField);
                chatEditField.setText("");
                chatEditField.isVisible = false;
                break;
            case -3:
                Vector menus = new Vector(2);
                menus.addElement(new Command(-5, T.gL(17), this));
                menus.addElement(new Command(-4, T.gL(2), this));
                showMenu(menus, 0);
                break;
            case -5:
                new EmotionDialog(chatEditField).show(false);
                break;
            case -6:
                if (chatEditField.getText().length() == 0 && container.containWidget(chatEditField)) {
                    chatEditField.isVisible = false;
                    removeWidget(chatEditField);
                } else if (chatEditField.getText().length() > 0 && !container.containWidget(chatEditField)) {
                    chatEditField.isVisible = true;
                    addWidget(chatEditField);
                    chatEditField.requestFocus();
                    if ("*".equals(chatEditField.text)) {
                        new EmotionDialog(chatEditField).show(false);
//			EditField.cmdSymbol.actionPerformed(new Object[]{EditField.cmdSymbol, chatEditField});
                        chatEditField.clear();
                    }
                }
                break;
//            case 0:
//                checkKeys(1, BaseCanvas.KEY_SOFT_LEFT);
//                break;
//            case 1:
//                checkKeys(1, BaseCanvas.KEY_FIRE);
//                break;
//            case 2:
//                checkKeys(1, BaseCanvas.KEY_SOFT_RIGHT);
//                break;
        }
    }

    public WidgetGroup getCurrentRoot() {
        WidgetGroup root = null;
        if (currentMenu != null) {
            root = currentMenu;
        } else if (currentDialog != null) {
            root = currentDialog;
//        } else if (chatEditField != null && chatEditField.isVisible) {
//            return chatEditField;
        } else if (container != null) {
            root = container;
        }
        return root;
    }

    //<editor-fold defaultstate="collapsed" desc="Các hàm và thuộc tính layout cho bigscreen.">
    //#if BigScreen || Android
//#     /**
//#      * Set command nằm ở trên title.
//#      *
//#      * @param img
//#      * @param cmd
//#      * @param w
//#      */
//#     public void setTitleCommand(Image img, Command cmd, int w, boolean isLeft) {
//#         if (cmd == null) {
//#             disableTitleCommand(isLeft);
//#             return;
//#         }
//#         (isLeft ? btnLeft : btnRight).setImage(img);
//#         (isLeft ? btnLeft : btnRight).cmdCenter = cmd;
//#         (isLeft ? btnLeft : btnRight).w = w;
//#     }
//# 
//#     public void disableTitleCommand(boolean isLeft) {
//#         (isLeft ? btnLeft : btnRight).setImage(null);
//#         (isLeft ? btnLeft : btnRight).cmdCenter = null;
//#     }
//# 
//#     /**
//#      * Create a button with an image and metric is the same the image.
//#      */
//#     public Button createScreenButton(Image img, Command cmd, int x, int y) {
//#         return createButton(img, cmd, x, y, false);
//#     }
//# 
//#     /**
//#      * Tao ra 1 button nam tren title, co chieu cao toi da la = title height.
//#      */
//#     public Button createTitleButton(Image img, Command cmd, int x, int y) {
//#         return createButton(img, cmd, x, y, true);
//#     }
//# 
//#     /**
//#      * *
//#      *
//#      */
//#     public Button createButton(Image img, Command cmd, int x, int y, boolean isOnTitle) {
//#         Button tempB = new Button(3);
//#         tempB.padding = isOnTitle ? ((LAF.LOT_TITLE_HEIGHT - img.getHeight()) >> 1) : 0;
//#         tempB.setMetrics(x, y, img.getWidth(), isOnTitle ? LAF.LOT_TITLE_HEIGHT : img.getHeight());
//#         tempB.setImage(img);
//#         tempB.cmdCenter = cmd;
//#         return tempB;
//#     }
    //#endif
    //</editor-fold>
    /**
     *
     * @param isAtom
     * @return
     */
    public Widget getFocusedWidget(boolean isAtom) {
        return container.getFocusedWidget(isAtom);
    }

    public void removeAll() {
        container.removeAll();
    }

    public static void endAnimation(int aniId) {
        for (int i = 0; i < overlays.size(); i++) {
            Overlay e = (Overlay) overlays.elementAt(i);
            if (e.id == aniId) {
                overlays.removeElement(e);
                return;
            }
        }
    }
}
