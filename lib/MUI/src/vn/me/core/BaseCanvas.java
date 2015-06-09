package vn.me.core;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import vn.me.network.MobileClient;
import vn.me.ui.Screen;

//#if BlackBerry
//# //@ import net.rim.device.api.system.Application;
//# //@ import net.rim.device.api.system.KeyListener;
//# //@ import net.rim.device.api.ui.Keypad;
//#endif
/**
 * Lop co so cho viec hien thi len man hinh. Bao gom ca GameThread. Chi co 1
 * Canvas cho tat ca cac man hinh.
 *
 * @author Tam Dinh
 */
//#if BlackBerry
//# //@ public class BaseCanvas extends Canvas implements Runnable, KeyListener {
//#else
public class BaseCanvas extends Canvas implements Runnable {
//#endif

    //--------------------------------------------------------------------------    
    public static final int TOPLEFT = Graphics.TOP | Graphics.LEFT;
    public static final int RIGHTBOTTOM = Graphics.RIGHT | Graphics.BOTTOM;
    public static final int TOPRIGHT = Graphics.RIGHT | Graphics.TOP;
    public static final int TOPCENTER = Graphics.TOP | Graphics.HCENTER;
    public static final int BOTTOMCENTER = Graphics.BOTTOM | Graphics.HCENTER;
    public static final int CENTER = Graphics.HCENTER | Graphics.VCENTER;
    public static final int CENTERLEFT = Graphics.LEFT | Graphics.VCENTER;
    public static final int LEFTBOTTOM = Graphics.LEFT | Graphics.BOTTOM;
    // -------------------------------------------------------------------------
    public static final int KEY_UP = -1;
    public static final int KEY_DOWN = -2;
    public static final int KEY_LEFT = -3;
    public static final int KEY_RIGHT = -4;
    public static final int KEY_FIRE = -5;
    public static final int KEY_SOFT_LEFT = -6;
    public static final int KEY_SOFT_RIGHT = -7;
    public static final int KEY_CLEAR = -8;
    public static final int KEY_NONE = -1982;
    //--------------------------------------------------------------------------
    public static final int DEFAULT_KEY_REPEAT_INITIAL_INTERVAL_TIME = 800;
    public static final int DEFAULT_KEY_REPEAT_NEXT_INTERVAL_TIME = 10;
    //--------------------------------------------------------------------------
    /**
     * Được phép thay đổi, mặc định = DEFAULT_KEY_REPEAT_INITIAL_INTERVAL_TIME =
     * 800
     */
    public static int keyRepeatInitialIntervalTime = 800;
    /**
     * Được phép thay đổi, mặc định = DEFAULT_KEY_REPEAT_NEXT_INTERVAL_TIME = 10
     */
    public static int keyRepeatNextIntervalTime = 10;
    private long nextKeyRepeatTimeEvent;
    /**
     * Key repeat hiện tại.
     */
    public int keyRepeatCode = KEY_NONE;
    /**
     * true: Ca phim 2,4,6,8 se la cac phim trai, phai, tren, xuong. false: tra
     * ve dung keycode.
     */
    public static boolean isGamePadMode = false;
    /**
     * Hàng đợi để xử lý các input event như key và pointer Dữ liệu gồm :
     * KeyEvent { type, keycode } PointerEvent { type, pointerX, pointerY } type
     * : 0 : keyPressed; 1 : keyReleased; 2 : Pointer Pressed 3 : Pointer
     * Released 4 : Pointer Dragged
     */
    public static final int MAX_INPUT = 10;
    public int[][] inputEvents = new int[MAX_INPUT][3];
    public int inputEventsNum = 0;
    /**
     * Dung cho game thread. Xác định trạng thái gameloop đang là chạy hay dừng.
     */
    public static boolean isRunning, isPause;
    /**
     * Dem thoi gian dung de dong bo du lieu
     */
    public static int gameTicks;
    /**
     * Width Height Half-W, Half-H, w/3, h/3, 2*w/3, 2*h/2,
     */
    public static int w, h, hw, hh, wd3, hd3, hd4, w2d3, h2d3, w3d4, h3d4, wd6, hd6;
    /**
     * Man hinh hien tai. Chi co 1 man hinh la duoc set hien tai thoi
     */
    public static Screen currentScreen;
    /**
     * Xac dinh platform cua dien thoai dang su dung -1 : Khong biet 0 : Nokia 1
     * : Motorola 2 : SonyErricson 3 : Samsung 4 : Blackberry 5 : Siemen
     */
    public static int platform = 0;
    /**
     * Midlet của toàn ứng dụng. Chỉ nên 1 cái.
     */
    public MIDlet gameMidlet;
    /**
     * Chỉ có 1 instance duy nhất.
     */
    public static BaseCanvas instance;
    public int vibrateTime;
    /**
     * Graphics dùng chung toàn Canvas.
     */
    public static Graphics g;
    public ILiveObject liveObject;

    public static BaseCanvas createBaseCanvas(MIDlet midlet) {
        if (instance == null) {
            instance = new BaseCanvas(midlet);
        }
        return instance;
    }
    /**
     * Session chính được đồng bộ cùng GameLoop
     */
    public MobileClient session;
    public int dragActivationCounter = 0;
    public int dragAutoActivationThreshold = 7;
    public int dragActivationX = 0;
    public int dragActivationY = 0;
    
    public int pointerPressedX;
    public int pointerPressedY;
    

    /**
     * Graphics đễ vẽ offline chống giật
     */
//    private Image offlineGraphics;
    /**
     * Hàm khởi tạo của BaseCanvas để set - Xác định kích thước màn hình full
     * screen. - Xác định loại platform xem biến platform. (
     * EditField.setVendorTypeMode(platform) cần được gọi sau khi xác định được
     * platform )
     */
    private BaseCanvas(MIDlet midlet) {

        setFullScreenMode(true);
//        System.gc();
        //
        w = this.getWidth();
        h = this.getHeight();
        layout();
        platform = initPlatform();
        //#if BlackBerry
//# //@         Application.getApplication().addKeyListener(this);
        //#endif
//        if (isDoubleBuffered()) {
//            offlineGraphics = Image.createImage(w, h);
//        }
        gameMidlet = midlet;
    }

    private int initPlatform() {
        //#if BlackBerry
//# //@         return 4;
        //#else
        // detecting Motorola
        if (getKeyCode(FIRE) == -20) {
            return 1;
        }
        // detecting SonyErricson
        final String currentPlatform = System.getProperty("microedition.platform");
        if (currentPlatform.indexOf("SonyEricsson") != -1) {
            return 2;
        }
        // detecting SAMSUNG
        try {
            Class.forName("com.samsung.util.Vibration");
            return 3;
        } catch (Exception ex) {
        }
        // // detecting Siement
        try {
            Class.forName("com.siemens.mp.io.File");
            return 5;
        } catch (Exception ex) {
        }
        // Detecting BlackBerry
//        try {
//            Class.forName("net.rim.device.api.system.Application");
//            return 4;
//        } catch (Exception ex) {
//        }
        return 0;
        //#endif
    }

    protected void sizeChanged(int w, int h) {
        super.sizeChanged(w, h);
        BaseCanvas.w = w;
        BaseCanvas.h = h;
        layout();
    }

    public final void layout() {
        hw = w >> 1;
        hh = h >> 1;
        wd3 = w / 3;
        hd3 = h / 3;
        w2d3 = 2 * w / 3;
        h2d3 = 2 * h / 3;
        w3d4 = 3 * w / 4;
        h3d4 = 3 * h / 4;
        wd6 = w / 6;
        hd6 = h / 6;
        hd4 = w >> 2;
        if (currentScreen != null) {
            currentScreen.sizeChanged();
        }
    }

    public static void setCurrentScreen(Screen newScreen) {
        if (currentScreen == newScreen) {
            return;
        }
        if (currentScreen != null) {
            currentScreen.onClosed();
            currentScreen = null;
        }
        if (newScreen != null) {
            currentScreen = newScreen;
            currentScreen.onShowed();
        } else {
            currentScreen = null;
        }
    }

    /**
     *
     * @param aScreen
     * @return Xem thêm {@link BaseCanvas#isCurrentScreenId(java.lang.String) }
     */
    public static boolean isCurrentScreen(Screen aScreen) {
        return (currentScreen != null && currentScreen == aScreen);
    }

    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    public void start() {
        new Thread(this).start();
    }

    // MAIN GAME LOOP THREAD.
    public void run() {
        isRunning = true;
        while (isRunning) {
            if (!isPause) {
                try {
                    long t1 = System.currentTimeMillis();
                    gameTicks++;

                    if (vibrateTime > 0) {
                        vibrateTime--;
                        if (vibrateTime == 0) {
                            Display.getDisplay(BaseCanvas.instance.gameMidlet).vibrate(0); // Tắt rung.
                        }
                    }
                    checkInputEvents();
                    updateScreen();
                    
                    if (liveObject != null) {
                        liveObject.update(t1);
                    }
                    
                    repaint();
                    serviceRepaints();
                    
                    if (session != null && session.isConnected() && session.isSYNC) {
                        session.processMessages();
                    }
                    // Synchronize time
                    long sleepTime = 50 - (System.currentTimeMillis() - t1); // try to get 25pfs
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //#if DefaultConfiguration
//@                    e.printStackTrace();
                    //#endif
                }
            }
        }
    }

    private void checkInputEvents() {
        // Kiem tra phim game
        if (inputEventsNum != 0) {
            synchronized (this) {
                for (int i = 0; i < inputEventsNum; ++i) {
                    int[] ev = inputEvents[i];
                    switch (ev[0]) {
                        // KEY
                        case 0: // Key Pressed
                        case 1: // Key Released
                            currentScreen.checkKeys(ev[0], ev[1]);
                            break;
                        case 2: // Pointer pressed
                            currentScreen.pointerPressed(ev[1], ev[2]);
                            break;
                        case 3: // Ponter dragged
                            currentScreen.pointerDragged(ev[1], ev[2]);
                            break;
                        case 4: // ponter release
                            currentScreen.pointerReleased(ev[1], ev[2]);
                            break;
                    }
                }
                inputEventsNum = 0;
            }
        }
        // check key repeat events
        long t = System.currentTimeMillis();
        if (keyRepeatCode != KEY_NONE && nextKeyRepeatTimeEvent <= t) {
//            if (!currentScreen.keyPressed(keyRepeatCode)) {
            synchronized (this) {
                if (inputEventsNum < MAX_INPUT) {
                    inputEvents[inputEventsNum][0] = 0;
                    inputEvents[inputEventsNum][1] = keyRepeatCode;
                    inputEventsNum++;
                }
            }
//            }
            nextKeyRepeatTimeEvent = t + keyRepeatNextIntervalTime;
        }
    }

    private void updateScreen() {
        if (currentScreen != null) {
            currentScreen.update();
            currentScreen.updateDialogPopupOverlay();
            for (int i = 0; i < currentScreen.backScreens.size(); i++) {
                Screen sc = (Screen) currentScreen.backScreens.elementAt(i);
                if (sc.needBackgroundUpdate) {
                    sc.update();
                }
            }
        }
    }

    public void keyPressed(int keyCode) {

        //#if DefaultConfiguration
//@        System.out.print("\nkeyPressed = " + keyCode);
        //#endif
        int code = mapKey(keyCode);
        code = getKeyPadCode(code);
        //#if DefaultConfiguration
//@        System.out.print("\nkeyPressed Map ->" + code);
        //#endif
        if (code != KEY_SOFT_LEFT && code != KEY_SOFT_RIGHT) {
            keyRepeatCode = code;
            nextKeyRepeatTimeEvent = System.currentTimeMillis() + keyRepeatInitialIntervalTime;
        }
        if (currentScreen.keyPressed(code)) {
            return;
        }

        synchronized (this) {
            // Cac nut thuc hien lenh phai release moi tac dung
//            if (code != KEY_FIRE && code != KEY_SOFT_LEFT && code != KEY_SOFT_RIGHT) {
            if (inputEventsNum < MAX_INPUT) {
                inputEvents[inputEventsNum][0] = 0;
                inputEvents[inputEventsNum][1] = code;
                inputEventsNum++;
            }
//            }
        }
//        }


    }

    private int getKeyPadCode(int code) {
        if (isGamePadMode) {
            switch (code) {
                case 'e'://tren
                    return KEY_UP;
                case 'x':
                    return KEY_DOWN;
                case 's':
                    return KEY_LEFT;
                case 'f':
                    return KEY_RIGHT;
                case 'd':
                    return KEY_FIRE;
            }
        }
        return code;
    }

    /**
     * Catch key-up input and clear from Input Array
     */
    public void keyReleased(int keyCode) {
//        #if BlackBerry
//@//        System.out.print("\nkeyReleased= " + keyCode);
//        #endif
//        InfoPopup popup = new InfoPopup();
//        popup.setInfo(String.valueOf(keyCode));
//        popup.show(10, -1);
        keyRepeatCode = KEY_NONE;
        int code = mapKey(keyCode);
        code = getKeyPadCode(code);
        if (currentScreen.keyReleased(code)) {
            return;
        }
        synchronized (this) {
            // Cac nut thuc hien lenh phai release moi tac dung
//            if (code == KEY_FIRE || code == KEY_SOFT_LEFT || code == KEY_SOFT_RIGHT) {
            if (inputEventsNum < MAX_INPUT) {
                inputEvents[inputEventsNum][0] = 1;
                inputEvents[inputEventsNum][1] = code;
                inputEventsNum++;
            }
//            }
        }

//        #if BlackBerry
//@//        System.out.print("\nkeyReleased Map to ->" + code);
//        #endif
    }

    /**
     * Chi dung cho phim dieu khien ( keycode < 0 ) Return BaseCanvas KeyCode.
     * @param keyCode : keyCode cua tung loai dien thoai
     * @return : key code chung cua BaseCanvas
     */
    public int mapKey(int keyCode) {

        if (platform == 1) {
            switch (keyCode) {
                case -6:
                    return KEY_DOWN;
                case -5:
                    return KEY_RIGHT;
                case -2:
                    return KEY_LEFT;
            }
        } else if (platform == 5) {
            /*
             * +Menu left: -1 +Menu right: -4 +Phim call: -11 +Phim On/off: -12
             * +Phim ok (o giua up,down,left,right): -26 +Phim Up: -59 +Phim
             * Down: -60 +Phim Left: -61 +Phim Right: -62
             *
             */
            switch (keyCode) {
                case -1:
                    return BaseCanvas.KEY_SOFT_LEFT;
                case -61:
                    return BaseCanvas.KEY_LEFT;
                case -4:
                    return BaseCanvas.KEY_SOFT_RIGHT;
                case -62:
                    return BaseCanvas.KEY_RIGHT;
                case -26:
                    return BaseCanvas.KEY_FIRE;
                case -59:
                    return BaseCanvas.KEY_UP;
                case -60:
                    return BaseCanvas.KEY_DOWN;
            }
        }
        switch (keyCode) {
//            case 42:
//                keyHold[10] = true;
//                keyPressed[10] = true;
//                return; // Key [*]
//            case 35:
//                keyHold[11] = true;
//                keyPressed[11] = true;
//                return; // Key [#]
            case -6:
            case -21:
            case 4098:
                return BaseCanvas.KEY_SOFT_LEFT;
            case -7:
            case -22:
                return BaseCanvas.KEY_SOFT_RIGHT; // Soft2
            case -5:
            case 10:
            case -20:
                //#if BlackBerry
//# //@             case -8:
                //#endif
                return BaseCanvas.KEY_FIRE; // [i]
            case -1:
            //#if BlackBerry
//# //@             case 1:
            //#endif
            case -38:
                return BaseCanvas.KEY_UP; // UP
            case -2:
            case -39:
                //#if BlackBerry
//# //@             case 6:
                //#endif
                return BaseCanvas.KEY_DOWN; // DOWN
            case -3:
                //#if BlackBerry
//# //@             case 2:
                //#endif
                return BaseCanvas.KEY_LEFT; // LEFT
            case -4:
                //#if BlackBerry
//# //@             case 5:
                //#endif
                return BaseCanvas.KEY_RIGHT; // RIGHT                
            case 8:
            case -204:
            //#if !BlackBerry
            case -8:
                //#endif
                return BaseCanvas.KEY_CLEAR;
            default:
                return keyCode;
        }
    }

    protected void keyRepeated(int keyCode) {
        // Đã xử lý trong framework. Không xử lý ở đây.
    }

    /**
     * This method can be overriden by subclasses to indicate whether a drag
     * event has started or whether the device is just sending out "noise". This
     * method is invoked by pointer dragged to determine whether to propogate
     * the actual pointer drag event to Screen.
     *
     * @param x the position of the current drag event
     * @param y the position of the current drag event
     * @return true if the drag should propogate into Screen
     */
    protected boolean hasDragStarted(final int x, final int y) {

        if (dragActivationCounter == 0) {
            dragActivationX = x;
            dragActivationY = y;
            dragActivationCounter++;
            return false;
        }
        //send the drag events to the form only after latency of 7 drag events,
        //most touch devices are too sensitive and send too many drag events.
        //7 is just a latency const number that is pretty good for most devices
        //this may be tuned for specific devices.
        dragActivationCounter++;
        if (dragActivationCounter > dragAutoActivationThreshold) {
            return true;
        }
        // have we passed the motion threshold on the X axis?
        if (3 * w / 100 <= Math.abs(dragActivationX - x)) {
            dragActivationCounter = dragAutoActivationThreshold + 1;
            return true;
        }

        // have we passed the motion threshold on the Y axis?
        if (3 * h / 100 <= Math.abs(dragActivationY - y)) {
            dragActivationCounter = dragAutoActivationThreshold + 1;
            return true;
        }

        return false;
    }

    protected void pointerDragged(int x, int y) {

        synchronized (this) {
            if (hasDragStarted(x, y)
                    && inputEventsNum < MAX_INPUT) {
                inputEvents[inputEventsNum][0] = 3;
                inputEvents[inputEventsNum][1] = x;
                inputEvents[inputEventsNum][2] = y;
                inputEventsNum++;
            }
        }

    }

    protected void pointerPressed(int x, int y) {
        pointerPressedX = x;
        pointerPressedY = y;
        synchronized (this) {
            if (inputEventsNum < MAX_INPUT) {
                inputEvents[inputEventsNum][0] = 2;
                inputEvents[inputEventsNum][1] = x;
                inputEvents[inputEventsNum][2] = y;
                inputEventsNum++;
            }
        }
    }

    protected void pointerReleased(int x, int y) {
        // this is a special case designed to detect a "flick" event on some Samsung devices
        // that send a pointerPressed/Released with widely differing X/Y values but don't send
        // the pointerDrag events in between
        if (dragActivationCounter == 0 && x != pointerPressedX && y != pointerPressedY) {
            hasDragStarted(pointerPressedX, pointerPressedY);
            if (hasDragStarted(x, y)) {
                pointerDragged(pointerPressedX, pointerPressedY);
                pointerDragged(x, y);
            }
        }
        dragActivationCounter = 0;
        synchronized (this) {
            if (inputEventsNum < MAX_INPUT) {
                inputEvents[inputEventsNum][0] = 4;
                inputEvents[inputEventsNum][1] = x;
                inputEvents[inputEventsNum][2] = y;
                inputEventsNum++;
            }
        }
    }

    protected void paint(Graphics g) {
//        if ( offScreenImage != null ){
//        Graphics offG = offScreenImage.getGraphics();                        
//        BaseCanvas.g = offG;
//        } else 

        /*
         * code cu bi loi (n72) if (BaseCanvas.g == null) { BaseCanvas.g = g; }
         */
        if (BaseCanvas.g != g) {
            BaseCanvas.g = g;
        }
        if (currentScreen != null) {
            currentScreen.paint();
        } else {
            g.setColor(0);
            g.fillRect(0, 0, w, h);
        }

    }

    public void showGameCanvas() {
        Display.getDisplay(gameMidlet).setCurrent(this);
        setFullScreenMode(true);
    }
    //#if BlackBerry
//# //@ 
//# //@     public boolean keyChar(char key, int status, int time) {
//# //@ //        getCurrentScreen().addFlyText("keyChar = "+key, 100, 0, 5);
//# //@         //keyPressed(key);
//# //@         return false;
//# //@     }
//# //@ 
//# //@     public boolean keyDown(int keycode, int time) {
//# //@         int key = Keypad.key(keycode);
//# //@         System.out.print("\nkeyDown= " + keycode + "->" + key);
//# //@ //        getCurrentScreen().addFlyText("keyDown = "+key, 50, 0, 5);
//# //@         switch (key) {
//# //@             case Keypad.KEY_MENU:
//# //@                 keyPressed(-6);
//# //@                 return true;
//# //@             case Keypad.KEY_ESCAPE:
//# //@                 keyPressed(-7);
//# //@                 return true;
//# //@             case Keypad.KEY_CONVENIENCE_1:
//# //@                 keyPressed(-4);
//# //@                 return true;
//# //@             case Keypad.KEY_CONVENIENCE_2:
//# //@                 keyPressed(-3);
//# //@                 return true;
//# //@             case Keypad.KEY_NEXT:
//# //@                 keyPressed(-5);
//# //@                 return true;
//# //@         }
//# //@         return false;
//# //@     }
//# //@ 
//# //@     public boolean keyUp(int keycode, int time) {
//# //@         int key = Keypad.key(keycode);
    //#if BlackBerry
//# //@         System.out.print("\nkeyUp= " + keycode + "->" + key);
    //#endif
//# //@         switch (key) {
//# //@             case Keypad.KEY_MENU:
//# //@                 keyReleased(-6);
//# //@                 return true;
//# //@             case Keypad.KEY_ESCAPE:
//# //@                 keyReleased(-7);
//# //@                 return true;
//# //@             case Keypad.KEY_CONVENIENCE_1:
//# //@                 keyReleased(-4);
//# //@                 return true;
//# //@             case Keypad.KEY_CONVENIENCE_2:
//# //@                 keyReleased(-3);
//# //@                 return true;
//# //@             case Keypad.KEY_NEXT:
//# //@                 keyReleased(-5);
//# //@                 return true;
//# //@         }
//# //@         return false;
//# //@     }
//# //@ 
//# //@     public boolean keyRepeat(int keycode, int time) {
//# //@         return false;
//# //@     }
//# //@ 
//# //@     public boolean keyStatus(int keycode, int time) {
//# //@         return false;
//# //@     }
//# //@ 
//# //@     protected boolean navigationClick(int status, int time) {
//# //@         keyPressed(-5);
//# //@         return true;
//# //@     }
//# //@ 
//# //@     protected boolean navigationMovement(int dx, int dy, int status, int time) {
//# //@         if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
//# //@             return false;
//# //@         }
//# //@         if (Math.abs(dx) > Math.abs(dy)) {
//# //@             if (dx > 0) {
//# //@ 
//# //@                 keyPressed(-4);
//# //@             } else {
//# //@                 keyPressed(-3);
//# //@             }
//# //@         } else {
//# //@             if (dy > 0) {
//# //@                 keyPressed(-2);
//# //@             } else {
//# //@                 keyPressed(-1);
//# //@             }
//# //@         }
//# //@         return true;
//# //@     }
//# //@ 
//# //@     protected boolean navigationUnclick(int status, int time) {
//# //@         keyReleased(-5);
//# //@         return true;
//# //@     }
//# //@ //
//# //@ //    public boolean trackwheelClick(int status, int time) {
//# //@ //        return navigationClick(status, time);
//# //@ //    }
//# //@ //
//# //@ //    public boolean trackwheelUnclick(int status, int time) {
//# //@ //        return navigationUnclick(status, time);
//# //@ //    }
//# //@ //
//# //@ //    public boolean trackwheelRoll(int amount, int status, int time) {
//# //@ ////        InfoPopup popup = new InfoPopup();
//# //@ ////        popup.setInfo(String.valueOf(amount + "-" + status));
//# //@ ////        popup.show(10,1);
//# //@ ////        return navigationMovement(amount, amount, status, time);
//# //@ //        return false;
//# //@ //    }
    //#endif
}
