package vn.me.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.LAF;
import vn.me.ui.common.ResourceManager;
import vn.me.ui.geom.Dimension;
import vn.me.ui.interfaces.IWidgetPainter;
import vn.me.ui.model.FrameImage;

/**
 * Hien thi text va cho phep scroll neu nhu khong du cho de nhin thay text do.
 *
 * @author Tam Dinh
 */
public class Label extends Widget {

    /**
     * Sets the Label text
     *
     * @param text the string that the label presents.
     */
    public String text;
    /**
     * Icon ben canh text.
     */
//    public Image icon;
    /**
     * icon cua lable/button.
     */
    public FrameImage image;
    /**
     * frame index hien thoi cua tam hinh.
     */
    public int frameIndex = 0;
    /**
     * Cho phep aninmation. default = 0; 0 - Animation hay khong la theo bien
     * isAnimatable (true - animation, false - otherwise). 1 - khi focused ve 1
     * hinh, khi khong focus ve hinh khac theo indexs trong 1 frame image. 2 -
     * animation when it is focused (ve nhun nhun). 3 - animation when it is
     * focused according frame image. 4 - khi focused ve 1 hinh, khi khong focus
     * ve hinh khac theo index trong 1 frame image. và khi focus thi nhuc nhich.
     */
    public byte animationType = 0;
    /**
     * true - animation, false - otherwise. this argument depend on animationType
     */
    public boolean isAnimatable = false;
    /**
     * Hieu ung nhun nhay.
     */
//    public int shakeEffect = 0;
//    public boolean  = false;
    /**
     * Toc do animation.
     */
//    private int animationSpeed = 100;
    /**
     * Khung hinh chu nhat dung de ve g.drawRegion
     */
//    public Rectangle rectImgClip, rectImgClipSelected;
    /**
     * Sets the Alignment of the Label to one of: LEFT, RIGHT, CENTER
     *
     * @param align alignment value
     * @see Font.LEFT
     * @see Font.LEFT
     * @see Font.LEFT
     *
     */
    public int align = Font.LEFT;
    /**
     * Sets the position of the text relative to the icon if exists
     *
     * @param textPosition alignment value (LEFT, RIGHT, BOTTOM or TOP)
     * @see #LEFT
     * @see #RIGHT
     * @see #BOTTOM
     * @see #TOP
     */
    public int textPosition = Graphics.RIGHT;
    /**
     * Set the gap in pixels between the icon and text
     *
     * @param gap the gap in pixels
     */
    private int gap = 2;
    /**
     * Nam trong khoang [0 - (-shiftTextLimit )]
     */
    private int shiftText = 0;
    private int shiftTextLimit = 0;
    private boolean isTickerRunning = false;
    public boolean isTickerEnabled = true;
    /**
     * Font cua Label.
     */
    public Font normalfont = ResourceManager.defaultFont, 
            selectedfont = ResourceManager.boldFont;
    /**
     * Có nhấp nháy không? Dùng cho Tab chat.
     */
    public boolean isBlink = false;
    /**
     * 0 : Neu text dai se scroll qua de co the thay ro. 1 : Scroll dang banner,
     * scroll qua 1 lan va lan ca Label. Dung co server info.
     */
    public byte scrollType = 0;
    public IWidgetPainter painter;
//    private Timer timer = new Timer();
//    private TimerTask stopTickerTask;
//    private TimerTask startTickerTask;
    /**
     * Dung de ve mau chu cho bigscreen
     * 
     */
    //#if Android || BigScreen
//#     public  int foregroundColor;
    //#endif
    public Label() {
        super();
    }
    
    /**
     * Khoi tao label chi co text.
     */
    public Label(String text) {
        this(text, LAF.mode == LAF.IWIN ? ResourceManager.defaultFont : ResourceManager.boldFont);
    }

    /**
     * Khời tạo 1 đối tượng Label với text ban đầu.
     *
     * @param text
     */
    public Label(String text, Font font) {
        this(text, font, font);
    }

    public Label(String text, Font font, Font selectedFont) {
        super();
        this.normalfont = font;
        this.selectedfont = selectedFont == null ? font : selectedFont;
        padding = LAF.LOT_PADDING;
        setText(text);
        isFocusable = false;
    }

    /**
     * Khoi tao lable co text va icon 1 frame.
     *
     * @param text
     * @param img
     * @param font
     */
    public Label(String text, Image img, Font font) {
        this(text, img, font, font);
    }

    public Label(String text, Image img, Font font, Font selectedFont) {
        this(text, img, 1, false, font, selectedFont);
    }

    /**
     * Khời tạo 1 đối tượng Label với text ban đầu.
     *
     * @param text
     */
    public Label(String text, Image img, int nFrame, boolean isH, Font font) {
        this(text, img, nFrame, isH, font, font);
    }

    public Label(String text, Image img, int nFrame, boolean isH, Font font, Font selectedFont) {
        this(text, font, selectedFont);
        if (img != null) {
            image = new FrameImage(img, nFrame, isH);
        }
    }

    public Label(String text, FrameImage img, Font font, Font selectedFont) {
        this(text, font, selectedFont);
        image = img;
//        this.icon = img;
//        if (icon != null) {
//            rectImgClip = new Rectangle(0, 0, icon.getWidth(), icon.getHeight());
//            rectImgClipSelected = rectImgClip;
//        }
    }

    public final void doLayout() {
        preferredSize.height = Math.max(normalfont.getHeight(), selectedfont.getHeight());// + 2 * (padding + border);

        if (text != null) {
            preferredSize.width = Math.max(normalfont.getWidth(text), selectedfont.getWidth(text));// + 2 * (padding + border);
        }
//        if (icon != null) {
//            preferredSize.width += icon.getWidth();// + 2 * (padding + border);
//            preferredSize.height = Math.max(icon.getHeight(), preferredSize.height);
//        }
        if (image != null) {
            preferredSize.width += image.frameWidth + gap;//icon.getWidth();// + 2 * (padding + border);
            preferredSize.height = Math.max(image.frameHeight, preferredSize.height);//Math.max(icon.getHeight(), preferredSize.height);
        }
        h = preferredSize.height + 2 * (padding + border);
        w = preferredSize.width + 2 * (padding + border);
    }

    /**
     * Sets the Label text
     *
     * @param text the string that the label presents.
     */
    public final void setText(String text) {
        this.text = text;
        doLayout();
    }

    /**
     * Sets the Label text
     *
     * @param text the string that the label presents.
     */
    public void setFont(Font normalFont, Font selectedFont) {
        this.normalfont = normalFont;
        this.selectedfont = selectedFont == null ? normalFont : selectedFont;
        doLayout();
    }

    /**
     * Set an image with one frame. If img is null then do nothing.
     */
    public void setImage(Image img) {
        if (img == null) {
            return;
        }
        setImage(img, new Dimension(img.getWidth(), img.getHeight()), false);
    }

    /**
     * Set icon cho control.
     *
     * @param image Một tấm hình có nhiu frame theo chieu dung.
     * @param size kích thước của frame hình.
     */
    public void setImage(Image image, Dimension size) {
        if (image == null) {
            return;
        }
        setImage(image, size, false);
//        this.image = new FrameImage(image, normalRect.width, normalRect.height);
//        frameIndex = 0;
//        preferredSize = new Dimension(normalRect.width, normalRect.height, false);
    }

    /**
     * Khong duoc truyen hinh null vao.
     *
     * @param image
     * @param size
     * @param isH
     */
    public void setImage(Image image, Dimension size, boolean isH) {
        if (image == null) {
            return;
        }
        this.image = new FrameImage(image, size.width, size.height, isH);
        frameIndex = 0;
        preferredSize = new Dimension(size.width, size.height);
    }

    /**
     * Set a frame image object.
     *
     * @param frameImage
     */
    public void setFrameImage(FrameImage frameImage) {
        this.image = frameImage;
        frameIndex = 0;
        preferredSize = new Dimension(frameImage.frameWidth, frameImage.frameHeight);
    }

    /**
     * @inheritDoc
     */
    public void paint() {

        if (painter != null) {
            painter.paintContent(0, 0, w - (padding + border << 1), h - (padding + border << 1), BaseCanvas.g);
            return;
        }
        
        if (image == null && (text == null || text.length() == 0)) {
            (isFocused ? selectedfont : normalfont).drawString(BaseCanvas.g, "", align == Font.CENTER ? (w >> 1) - padding : shiftText,
                    0, align);
            return;
        }
        int cx = BaseCanvas.g.getClipX();
        int cy = BaseCanvas.g.getClipY();
        int cw = BaseCanvas.g.getClipWidth();
        int ch = BaseCanvas.g.getClipHeight();
        if (image != null) {
            if (text == null || ("").equals(text)) {
                //ve icon khi text = null.
                int yy = (h >> 1) - padding - border + ((isAnimatable && (animationType == 2 || animationType == 4) && BaseCanvas.gameTicks % 12 == 0) ? 2 : 0);
                image.drawFrame(BaseCanvas.g, frameIndex, (w >> 1) - padding - border, yy, Sprite.TRANS_NONE, BaseCanvas.CENTER);
                return;
            }
            if (textPosition == Graphics.BOTTOM) {
                int fontHeight = Math.max(normalfont.getHeight(), selectedfont.getHeight());
                int yy = ((h - ((padding + border) << 1) - fontHeight) >> 1) + ((isAnimatable && (animationType == 2 || animationType == 4) && BaseCanvas.gameTicks % 12 == 0) ? 2 : 0);
                image.drawFrame(BaseCanvas.g, frameIndex, (w >> 1) - padding - border, yy, Sprite.TRANS_NONE, BaseCanvas.CENTER);
            } else {
                //ve icon.
                int xx = (textPosition == Graphics.LEFT) ? w - ((padding + border) << 1) : 1;
                //int yy = ((isAnimatable && (animationType == 2 || animationType == 4) && BaseCanvas.gameTicks % 12 == 0) ? 2 : 0);
                int yy = ((isAnimatable && (animationType == 2 || animationType == 4) && BaseCanvas.gameTicks % 12 == 0) ? 2 : 0);
                image.drawFrame(BaseCanvas.g, frameIndex, xx,
                        yy, Sprite.TRANS_NONE,
                        textPosition == Graphics.RIGHT ? BaseCanvas.TOPLEFT : BaseCanvas.TOPRIGHT);
                
                if (textPosition == Graphics.RIGHT) {
                    //                BaseCanvas.g.clipRect(rectImgClip.getWidth(), 0, w - rectImgClip.getWidth() - (padding << 1) - gap, h);
                    BaseCanvas.g.clipRect(image.frameWidth, 0, w - image.frameWidth - (padding << 1) - gap, h);
                } else if (textPosition == Graphics.LEFT) {
                    //                BaseCanvas.g.clipRect(0, 0, w - rectImgClip.getWidth() - (padding << 1) - gap, h);
                    BaseCanvas.g.clipRect(0, 0, w - image.frameWidth - (padding << 1) - gap, h);
                }
            }
        }
        // Draw text
        /**
         * Neu cau hinh android hoac bigscreen thi phai set lai graphics color de 
         * dung navigate font
         */
        //#if Android || BigScreen
//#         BaseCanvas.g.setColor(foregroundColor);
        //#endif
        if (align == Font.RIGHT) {
            if (image == null) {
                (isFocused ? selectedfont : normalfont).drawString(BaseCanvas.g, text,
                        w - (padding << 1), (h >> 1) - (isFocusable ? (selectedfont.getHeight() >> 1) : (normalfont.getHeight() >> 1)) - LAF.LOT_PADDING, align);
//                        (w >> 1) - ((isFocused ? selectedfont : normalfont)).getWidth(text) - LAF.LOT_PADDING/*w - (padding << 1)*/, 
//                        (h >> 1) - (isFocusable ? (selectedfont.getHeight() >> 1) : (normalfont.getHeight() >> 1) ) - LAF.LOT_PADDING, align);
            } else {
                (isFocused ? selectedfont : normalfont).drawString(BaseCanvas.g, text,
                        (textPosition == Graphics.RIGHT) ? w - (padding << 1) : w - (padding << 1) - image.frameWidth + gap,
                        (h >> 1) - (isFocusable ? (selectedfont.getHeight() >> 1) : (normalfont.getHeight() >> 1)) - LAF.LOT_PADDING, align);
            }
        } else {
            int xx = (align == Font.CENTER) ? (w >> 1) - padding - border : (textPosition == Graphics.RIGHT
                    ? shiftText + (image == null ? 0 : image.frameWidth + gap) : (w >> 1) - ((isFocused ? selectedfont : normalfont)).getWidth(text) - LAF.LOT_PADDING);//shiftText);
            int fontHeight = Math.max(normalfont.getHeight(), selectedfont.getHeight());
            int yy = (textPosition == Graphics.BOTTOM) ? (h - ((border + padding) << 1) - fontHeight) : (h >> 1) - ((isFocused ? selectedfont : normalfont).getHeight() >> 1) - LAF.LOT_PADDING;
            //int yy = (h >> 1) - ((isFocused ? selectedfont : normalfont).getHeight() >> 1 ) - LAF.LOT_PADDING;
            (isFocused ? selectedfont : normalfont).drawString(BaseCanvas.g, text, xx, yy, align);
        }
        BaseCanvas.g.setClip(cx, cy, cw, ch);
    }

    public void paintBackground() {
        if (painter != null) {
            painter.paintBackGround(0, 0, w, h, BaseCanvas.g);
            return;
        }
        //#if DefaultConfiguration
//        BaseCanvas.g.setColor(0xff0000);
//        BaseCanvas.g.fillRect(0, 0, w, h);
        //#endif
        if (isBlink && BaseCanvas.gameTicks % 10 > 3) {
            BaseCanvas.g.setColor(0xee0000);
            BaseCanvas.g.fillRect(0, 0, w, h);
        } else if (scrollType == 1) {
            if (LAF.mode == LAF.IWIN) {
                BaseCanvas.g.setColor(LAF.CLR_ERROR_LIGHTER);
            } else {
                BaseCanvas.g.setColor(0x0c3e88);
            }
            BaseCanvas.g.fillRect(0, 0, w, h);
        }
    }

    /**
     * Returns true if a ticker should be started since there is no room to show
     * the text in the label.
     *
     * @return true if a ticker should start running
     */
    protected boolean isShouldTickerStart() {
        int txtW = 0;
        if (text != null) {
            txtW = selectedfont.getWidth(text);
        }
        
        if (scrollType == 0) {
//            shiftTextLimit = txtW - (w - (padding << 1) - (icon == null || align == Graphics.HCENTER ? 0
//                    : gap + icon.getWidth()));
            shiftTextLimit = txtW - (w - (padding << 1) - (image == null || align == Graphics.HCENTER ? 0
                    : gap + image.frameWidth));
        } else {
            shiftTextLimit = txtW;
        }
//        System.out.println("width: "+w);
//        System.out.println("length: "+shiftTextLimit);
        return shiftTextLimit > 0 && w > 0;
    }
    /**
     * This method will start the text ticker
     *
     * @param delay the delay in millisecods between animation intervals
     * @param rightToLeft if true move the text to the left
     */
    long timeBeforStartTicker;
    long delayToStartTicker = -1;
    long timeBeforeStopTicker;
    long delayToStopTicker = -1;

    public void startTicker(long delay) {

        if (isShouldTickerStart()) {
            timeBeforStartTicker = System.currentTimeMillis();
            delayToStartTicker = delay;
//            TimerTask startTickerTask = new TimerTask() {
//
//                public void run() {
//                    isTickerRunning = true;
//                }
//            };
//            timer.schedule(startTickerTask, delay);
        }
    }

    /**
     * Stops the text ticker
     */
    public void stopTicker(int delay) {
        isTickerRunning = false;

        timeBeforeStopTicker = System.currentTimeMillis();
        delayToStopTicker = delay;

//        TimerTask stopTickerTask = new TimerTask() {
//
//            public void run() {
//                if (scrollType == 1) {
//                    destY = -h;
//                } else {
//                    shiftText = 0;//(0);
//                }
//            }
//        };
//
//        timer.schedule(stopTickerTask, delay);
    }

    /**
     * @inheritDoc
     */
    public void update() {
        super.update();
        long currentTime = System.currentTimeMillis();
        if (delayToStartTicker != -1) {
            if (currentTime - timeBeforStartTicker >= delayToStartTicker) {
                delayToStartTicker = -1;
                isTickerRunning = true;
            }
        }

        if (delayToStopTicker != -1) {
            if (currentTime - timeBeforeStopTicker >= delayToStopTicker) {
                delayToStopTicker = -1;
                if (scrollType == 1) {
                    destY = -h;
                } else {
                    shiftText = 0;//(0);
                }
            }
        }

        if (isTickerRunning && y == destY && x == destX) {
            //if (rightToLeft) {
            shiftText -= 2;
            if (shiftText < -shiftTextLimit) {
                //shiftText = w;
                stopTicker(1000);
                //rightToLeft = !rightToLeft;
            }
//            } else {
//                shiftText += 1;
//                if (shiftText >= 0) {
//                    rightToLeft = !rightToLeft;
//                }
//            }
        }
        if (y <= -h) { // Viet tam cho banner. Can viet lai cho chuan hon
            Screen.lblServerInfo = null;
        }
        if (BaseCanvas.gameTicks % 3 == 0 && image != null && isAnimatable && animationType != 1 && animationType != 4) { // and isAnimatable
            frameIndex++;
            if (frameIndex >= image.nFrame) {
                frameIndex = 0;
            }
        }
    }

    /**
     * Set speed to animate.
     */
//    public void setAnimationSpeed(int speed) {
//        animationSpeed = speed;
//    }
    public void onFocused() {
        super.onFocused();
        if (isTickerEnabled) {
            startTicker(1000);
        }
    }

    public void onLostFocused() {
        super.onLostFocused();
        if (isTickerEnabled && isTickerRunning) {
            stopTicker(0);
        }
    }
}
