package vn.me.ui;

import javax.microedition.lcdui.Image;
import vn.me.ui.common.LAF;
import vn.me.ui.common.ResourceManager;
import vn.me.ui.common.T;
import vn.me.ui.geom.Rectangle;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.model.Command;

/**
 *
 * @author TamDinh
 */
public class Button extends Label implements IActionListener {

    public static final byte TYPE_NORMAL = 0;
    /**
     * 0 : Nut binh thuong. 1 : Check Box. 2 : RadioButton. 3 : Image Button. (
     * hinh duoc lay tu imageId ) 4 : button for touch screen. (Khong ve nen
     * phia duoi. chi ve text) - balloon buttons. 5 : button cho menu trong man
     * hinh touch sreen. 6 : button cho dialog trong man hinh touch screen.
     */
    public int index = 0;
    /**
     * Danh cho checkBox/Radio Box
     */
    public boolean isSelected = false;
    /**
     * Hinh duoc lay tu server.
     */
    public int imageId;
    /**
     * Màu nền của button. Mặc định 0x00ffffff.
     */
    public int backgroundcolor = 0x00ffffff;
    /**
     * Màu nền của button khi focus. Mặc định 0x00ffffff.
     */
    public int focusBackgroundColor = 0x00ffffff;
    /**
     * Màu border của button. Mặc định 0x00ffffff.
     */
    public int borderColor = 0x00ffffff;
    private int frameIndexOnFocused = -1;
    private int frameIndexOnLostFocused = -1;
    /**
     * Khởi tạo button loại thường. type = 0;
     */
    public Button() {
        this(0);
    }

    /**
     * Khởi tạo button theo loại type
     *
     * @param type 0-Button bình thường, 1-check box, 2-radio button, 3-image
     * button.
     */
    public Button(int type) {
        this("");
        this.index = type;
        if (type == 1 || type == 2) {
            cmdCenter = new Command(0, T.gL(7), this);
        }
    }

    /**
     * Khởi tạo Button với Image có sẵn
     *
     * @param img : {@link Image}
     *
     */
    public Button(Image img) {
        this(0);
//        padding = LAF.LOT_PADDING;

        setImage(img);
    }

    /**
     * Khởi tạo Button
     *
     * @param text : {@link String}
     * @param defaultFont : {@link Font}
     *
     */
    public Button(String text, Font font) {
        super(text, font);
//        padding = LAF.LOT_PADDING;
//        w = preferredSize.width = w+ 2 * padding;
//        h = preferredSize.height = normalfont.getHeight() + 2 * padding;
        isFocusable = true;
    }

    /**
     * Khởi tạo Button
     *
     * @param text : {@link String}
     *
     */
    public Button(String text) {
        super(text);
//        padding = LAF.LOT_PADDING;
//        w = preferredSize.width = w+ 2 * padding;
//        h = preferredSize.height = normalfont.getHeight() + 2 * padding;
        isFocusable = true;
    }

    /**
     * Khởi tạo Button với Image có sẳn được vẽ trên 1 vùng {@link  Rectangle}
     *
     * @param img : {@link Image}
     * @param rect : {@link Rectangle}
     */
    public Button(Image img, Rectangle rect) {
        this(img);
//	rectImgClip = rect;
    }

    /**
     * Khởi tạo Button với {@link Command} Center và {@link Font}
     *
     * @param selectCommand : {@link Command}
     * @param defaultFont : {@link Font}
     *
     */
    public Button(Command selectCommand, Font font) {
        this(selectCommand == null ? "" : selectCommand.caption, font);
        cmdCenter = selectCommand;
    }

    /**
     * Khởi tạo Button với {@link Command} Center
     *
     * @param selectCommand : {@link Command}
     *
     */
    public Button(Command selectCommand) {
        this(selectCommand, ResourceManager.boldFont);
    }

    /**
     * Khởi tạo Button với {@link Command} Center và ImageId (ImageId là ID
     * hình)
     *
     * @param selectCommand : {@link Command}
     * @param imageId : {@link int}
     *
     */
    public Button(int imageId, Command selectCommand) {
        this(selectCommand);
        this.imageId = imageId;
        this.index = 3;
    }

    /**
     * Khởi tạo Button với caption, Icon, cmdCenter, nomal defaultFont, selected
     * defaultFont.
     *
     * @param caption : {@link String}
     * @param img : {@link Image}
     * @param selectCommand : {@link Command}
     * @param defaultFont : {@link Font}
     * @param selectedFont : {@link Font}
     *
     */
    public Button(String caption, Image img, Command selectCommand, Font font, Font selectedFont) {
        super(caption, img, font, selectedFont);
        this.cmdCenter = selectCommand;
        isFocusable = true;
    }

    /**
     * Set {@link Command} cho button
     *
     * @param selectCommand : {@link Command}
     *
     */
    public void setCommand(Command selectCommand) {
        text = selectCommand == null ? "" : selectCommand.caption;
        cmdCenter = selectCommand;
    }

    public void paintBackground() {
        LAF.paintButtonBackground(this);
    }

    public void paint() {
        super.paint();
        LAF.paintButtonContent(this);
    }
    
    /**
     * Vẽ Viền Button
     */
    protected void paintBorder() {
        LAF.paintButtonBorder(this);
    }
    /**
     * set image
     *
     * @param f : frame index as on focus.
     * @param l : frame index as on lost focus.
     */
    public void setFrameImageIndex(int f, int l) {
        frameIndexOnFocused = f;
        frameIndexOnLostFocused = l;
    }

    public void setIndexFrameWhenLostFocused(int index) {
        setFrameImageIndex(-1, index);
    }

    /**
     * Xữ lý sự kiện Focused
     */
    public void onFocused() {
        super.onFocused();
//        if (type == 1 && icon != null) {
//            this.rectImgClip = new Rectangle(0, rectImgClip.size.height, this.rectImgClip.size);
//            this.rectImgClipSelected = new Rectangle(0, rectImgClip.size.height, this.rectImgClip.size);
//        }
        if (animationType == 1 && frameIndexOnFocused > -1) {
            frameIndex = frameIndexOnFocused;
        }

        if (animationType == 4 && frameIndexOnFocused > -1) {
            frameIndex = frameIndexOnFocused;
            isAnimatable = true;
        }
        //check box
        if ( (index == 1 || index == 2) && image != null) {
            frameIndex = 1;
            //set border này dành cho iwin, khi check box dc focus se ve border
            border = 1;
        }
        if (animationType == 2 || animationType == 3) {
            isAnimatable = true;
        }
    }

    /**
     * Xữ lý sự kiện Mất Focused
     */
    public void onLostFocused() {
        super.onLostFocused();
        //isPressed = false;
//        if (type == 1 && icon != null) {
//            this.rectImgClip = new Rectangle(0, 0, this.rectImgClip.size);
//            this.rectImgClipSelected = new Rectangle(0, 0, this.rectImgClip.size);
//        }
        if ( (index == 1 || index == 2) && image != null) {
//            this.rectImgClip = new Rectangle(0, 0, this.rectImgClip.size);
//            this.rectImgClipSelected = new Rectangle(0, 0, this.rectImgClip.size);
            frameIndex = 0;
            //set border này dành cho iwin, khi check box dc focus se ve border
            border = 0;
        }
        if (animationType == 1 && frameIndexOnLostFocused > -1) {
            frameIndex = frameIndexOnLostFocused;
        }
        if (animationType == 4 && frameIndexOnLostFocused > -1) {
            frameIndex = frameIndexOnLostFocused;
            isAnimatable = false;
        }
        if (animationType == 2 || animationType == 3) {
            isAnimatable = false;
            frameIndex = frameIndexOnLostFocused == -1 ? 0 : frameIndexOnLostFocused;
        }
    }
    /**
     * Xữ lý action
     */
    public void actionPerformed(Object o) {
        if (index == 1) {
            this.isSelected = !this.isSelected;
        }
        if (index == 2) {
            if (!isSelected) {
                ((ButtonGroup) parent).setSelected(this);
            }
        }
    }
}
