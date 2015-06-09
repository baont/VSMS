package vn.me.ui;

import java.util.Vector;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.Effects;
import vn.me.ui.common.LAF;
import vn.me.ui.common.ResourceManager;
import vn.me.ui.common.T;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.model.Command;

public class Menu extends WidgetGroup implements IActionListener {

    //#if BigScreen || Android
//#     public int menuItemW = 120;
//#     public int menuItemH = 120;
    //#endif
    /**
     * Focus Widget hien tai de lay lai focus khi an Menu.
     */
    public Widget focusWid;
    /**
     * Font cho menu items.
     */
    public Font normalFont = ResourceManager.boldFont,
            focusFont = ResourceManager.boldFont;
    /**
     * Chieu cao cua 1 menu item.
     */
    public int menuItemHeight = Math.max(normalFont.getHeight(), focusFont.getHeight()) + 2 * LAF.LOT_PADDING;
    /**
     * Cac commands cua menu.
     */
    private Vector commands;
    /**
     * Vi tri menu item ma minh dang chon.
     */
    private int focusedIndex = 0;
    /**
     * Bien nay se duoc set khi chon 1 command trong menu va show ra 1 menu
     * khac.
     */
    public boolean isShowNextMenu = false;

    public Menu() {
//        isMenu = true;
        padding = 1;//LAF.LOT_PADDING;
        border = 3;
        cmdRight = new Command(0, T.gL(0), this);
        cmdCenter = new Command(2, T.gL(7), this);
//        cmdLeft = new Command(1, "", null);
        isLoop = true;
        isScrollableY = true;
    }

    //#if BigScreen || Android
//#     /**
//#      *
//#      * @param commands
//#      * @param posX 0 show menu ben trai <br>
//#      *            = 1 la show ben phai<br>
//#      *            = 2 la show ben o giua<br>
//#      *            = 3 show tu phai qua trai
//#      */
//#     public void showNewMenu(Vector commands, int posX) {
//#         
//#         if (commands.isEmpty()) {
//#             return;
//#         }
//# 
//#         border = 3;
//#         int size = commands.size();
//#         for (int i = 0; i < size; i++) {
//#             Command c = (Command) commands.elementAt(i);
//#             Button b = new Button();
//#             b.foregroundColor = 0xFFFFFF;
//#             b.cmdCenter = c;
//#             b.setImage(c.icon);
//#             b.text = c.caption;
//#             b.align = Font.CENTER;
//#             b.textPosition = Graphics.BOTTOM;
//#             b.setMetrics(0, 0, menuItemW, menuItemH);
//# //            b.border = 2;
//# //            b.setMetrics(0, 0, 80, 80);
//#             addWidget(b);
//#         }
//#         w = ((menuItemW+20) << 1) + 40;
//#         h = BaseCanvas.h-LAF.LOT_ITEM_HEIGHT-2;
//#         
//#         columns = 0;
//#         setViewMode(WidgetGroup.VIEW_MODE_GRID);
//#         switch(posX) {
//#             case 0:
//#                 x = -w;
//#                 destX = 1;
//#                 break;
//#             case 1:
//#                 destX = BaseCanvas.w - w - 1;
//#                 x = BaseCanvas.w;
//#                 y = LAF.LOT_ITEM_HEIGHT;
//#                 destY = LAF.LOT_ITEM_HEIGHT;
//#                 break;
//#             case 2:
//#                 destX = BaseCanvas.w/2 - w/2 - 1;
//#                 x = BaseCanvas.w/2 - w/2 - 1;
//#                 destY = 0;
//#                 y = BaseCanvas.h;
//#                 break;
//#         }
//#     }
//# 
//#     public boolean pointerDragged(int x, int y) {
//#         if (x < getAbsoluteX() || x > getAbsoluteX() + w
//#                 || y < getAbsoluteY() || y > getAbsoluteY() + h) {
//#             return false;
//#         }
//#         return super.pointerDragged(x, y);
//#     }
    //#else
    public void showNewMenu(Vector commands, int pos) {
        this.focusedIndex = 0;
        isPressed = false;
        if (this.commands != null && this.commands != commands) {
            isShowNextMenu = true;
        }
        this.commands = commands;
        //so menu item
        int nItems = commands.size();
        //menu item height.
        menuItemHeight = Math.max(normalFont.getHeight(), focusFont.getHeight()) + 2 * LAF.LOT_PADDING;
        preferredSize.height = nItems * menuItemHeight;
        h = preferredSize.height + ((border + padding) << 1) > BaseCanvas.h3d4 ? BaseCanvas.h3d4 : preferredSize.height + ((border + padding) << 1);

        int captionW;
        for (int i = nItems; --i >= 0;) {
            captionW = normalFont.getWidth(((Command) commands.elementAt(i)).caption);
            preferredSize.width = preferredSize.width > captionW ? preferredSize.width : captionW;
        }
        if (preferredSize.width < BaseCanvas.hw) {
            preferredSize.width = BaseCanvas.hw;
        }
        w = preferredSize.width + 2 * (padding + border);
        destY = BaseCanvas.h - LAF.LOT_CMDBAR_HEIGHT - h - 1;
        y = BaseCanvas.h - LAF.LOT_CMDBAR_HEIGHT;

        //#if BigScreen || Android
//#         destX = (BaseCanvas.w - w) >> 1;
//#         x = BaseCanvas.w;
//#         destY = y = (BaseCanvas.h - h - LAF.LOT_TITLE_HEIGHT) >> 1;
        //#else
        if (pos == 0) {
            destX = x = 1;
        } else if (pos == 1) {
            destX = x = BaseCanvas.w - w;
        } else {
            destX = x = BaseCanvas.hw - (w >> 1);
        }
        //#endif
    }
    //#endif

//#if !Android && !BigScreen
    public void paint() {
        BaseCanvas.g.translate(-scrollX, -scrollY);
        if (commands != null && !commands.isEmpty()) {
            int size = commands.size();
            for (int i = 0; i < size; i++) {
                if (focusedIndex == i) {
                    Effects.drawLinearGradient(BaseCanvas.g, isPressed ? LAF.CLR_PRESSED_ITEM_LIGHTER : LAF.CLR_BACKGROUND_LIGHTER,
                            isPressed ? LAF.CLR_PRESSED_ITEM_DARKER : LAF.CLR_BACKGROUND_DARKER,
                            0, i * menuItemHeight, w - ((padding + border) << 1) - 1, menuItemHeight - 1, false);
                }
                normalFont.drawString(BaseCanvas.g, ((Command) commands.elementAt(i)).caption,
                        LAF.LOT_PADDING, i * menuItemHeight + LAF.LOT_PADDING, Font.LEFT);
                if (focusedIndex == i) {
                    BaseCanvas.g.setColor(LAF.CLR_BORDER_FOCUSED);
                    BaseCanvas.g.drawRect(0, i * menuItemHeight, w - ((padding + border) << 1) - 1, menuItemHeight - 1);
                }
            }
        }
        BaseCanvas.g.translate(scrollX, scrollY);
    }

    public boolean pointerPressed(int xPointer, int yPointer) {
        int t = getfocusedIndexOf(xPointer, yPointer);
        focusedIndex = t >= 0 ? t : focusedIndex;
        isPressed = t >= 0 ? true : false;
        return super.pointerPressed(xPointer, yPointer);
    }

    public boolean pointerDragged(int x, int y) {
        if (super.pointerDragged(x, y)) {
            isPressed = false;
            return true;
        }
        return false;
    }

    public boolean pointerReleased(int xPointer, int yPointer) {
        int i = getfocusedIndexOf(xPointer, yPointer);
        if (focusedIndex == i) {
            focusedIndex = i;
            if (isPressed && !isDragActivated) {
                doSelectedCommand();
                return true;
            }
            isPressed = false;
        }
        return super.pointerReleased(xPointer, yPointer);
    }

    /**
     * *
     * Lay vi tri cua command minh touch len.
     *
     * @param xPointer
     * @param yPointer
     * @return
     */
    private int getfocusedIndexOf(int xPointer, int yPointer) {
        if (commands == null || commands.isEmpty()) {
            return -1;
        }
        int size = commands.size();
        for (int i = 0; i < size; i++) {
            int xPixel = xPointer - x - padding + scrollX;
            int yPixel = yPointer - y - padding + scrollY;
            if (xPixel > 0 && xPixel < w
                    && yPixel > i * menuItemHeight
                    && yPixel < i * menuItemHeight + menuItemHeight) {
                return i;
            }
        }
        return -1;
    }

    public boolean checkKeys(int type, int keyCode) {
        if (keyCode == BaseCanvas.KEY_LEFT || keyCode == BaseCanvas.KEY_RIGHT) {
            return true;
        }
        if (keyCode == BaseCanvas.KEY_FIRE) {
            if (type == 1) {
                doSelectedCommand();
                isPressed = false;
            } else {
                isPressed = true;
            }
            return true;
        }
        boolean isChangedFocus = false;
        if (keyCode == BaseCanvas.KEY_DOWN && type == 0) {
            if (commands != null && !commands.isEmpty()) {
                if (focusedIndex < commands.size() - 1) {
                    focusedIndex++;
                } else {
                    focusedIndex = 0;
                }
                isChangedFocus = true;
            }
        } else if (keyCode == BaseCanvas.KEY_UP && type == 0) {
            if (commands != null && !commands.isEmpty()) {
                if (focusedIndex > 0) {
                    focusedIndex--;
                } else {
                    focusedIndex = commands.size() - 1;
                }
                isChangedFocus = true;
            }
        }
        if (isChangedFocus) {
            scrollTo(0, focusedIndex * menuItemHeight, w - ((border + padding) << 1), menuItemHeight);
        }
        return isChangedFocus;
    }

    public void setFocusedIndex(int v) {
        focusedIndex = v;
        scrollTo(0, focusedIndex * menuItemHeight, w - ((border + padding) << 1), menuItemHeight);
    }

    //#endif
    protected void paintBorder() {
        super.paintBorder(); // De ve scroll bar.
        LAF.paintMenuBorder(this);
    }

    public void paintBackground() {
//        BaseCanvas.g.setColor(0xffffff);
//        BaseCanvas.g.fillRect(3, 3, w - 6, h - 6);
        LAF.paintMenuBG(this);
    }

    public void hide() {
        ((Screen) BaseCanvas.getCurrentScreen()).hideMenu();
    }

    private void doSelectedCommand() {
        if (commands != null && focusedIndex >= 0 && focusedIndex < commands.size()) {
            if (!isShowNextMenu) {
                ((Screen) BaseCanvas.getCurrentScreen()).hideMenu();
            } else {
                isShowNextMenu = false;
            }
            ((Command) commands.elementAt(focusedIndex)).actionPerformed(new Command[]{((Command) commands.elementAt(focusedIndex))});
        }
    }

    public void actionPerformed(Object o) {
        Command srcCmd = (Command) ((Object[]) o)[0];
        switch (srcCmd.id) {
            case 0://Dong menu
                ((Screen) BaseCanvas.getCurrentScreen()).hideMenu();
                break;
            case 2:
                doSelectedCommand();
                break;
        }
    }
}
