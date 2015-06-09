package vn.me.ui;

import vn.me.core.BaseCanvas;
import vn.me.ui.common.LAF;
import vn.me.ui.geom.Dimension;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.model.Command;

/**
 * A base class for all widgets. It contains attibutes and features common to
 * all widgets, such as the position of the widget, widget anchoring, color, ID
 * string, and routines for hiding and showing the widget.
 */
public class Widget {

    public static Command cmdNull = new Command(-1, "", null);
//    /**
//     * Constrains for this object, used by the layoutManager
//     */
//    public Object constrains = null;
    /**
     * Current X coordinate of the item, in pixels. Tuong doi so voi cha
     */
    public int x;
    /**
     * Current Y coordinate of the item, in pixels. Tuong doi so voi cha cua no
     */
    public int y;
    /**
     * Current Width of this widget, in pixels.
     */
    public int w;
    /**
     * Current Height of this widget, in pixels.
     */
    public int h;
    /**
     * Kích thước của một widget
     */
    /**
     * Set to true when the widget is being focused.
     */
    public boolean isFocused = false;
    /**
     * Set to true when the widget is being drag.
     */
    public boolean isDragActivated = false;
    /**
     * Co hien thi hay khong
     */
    public boolean isVisible = true;
    /**
     * Cac command khi widget nay duoc nhan focus
     */
    public Command cmdLeft, cmdCenter, cmdRight;
    /**
     * Vị trí mong muốn đến, dung cho viec di chuyen X. khi x == destX thì không
     * di chuyển nữa
     */
    public int destX;
    /**
     * Vị trí mong muốn đến, dung cho viec di chuyen Y. khi y - destY thì không
     * di chuyển nữa
     */
    public int destY;
    /**
     * Dung chung top/left/right/bottom
     */
    public int border = 0, padding = 0;
    /**
     * Set to true when the widget is being focused.
     */
    public boolean isFocusable = true;
    /**
     * Lay cha cua no trong viec gom nhieu
     */
    public Widget parent;
    /**
     * Dùng trong drag.
     */
    private int lastScrollY, lastScrollX;
    /**
     * Vi tri scoll hien tai
     */
    public int scrollX, scrollY;
    /**
     * Vi tri can scroll toi.
     */
    public int destScrollX, destScrollY;
    private int scrollVy;
    private int scrollDy;
    private int scrollVx;
    private int scrollDx;
    public boolean isScrollableX, isScrollableY;
    public Dimension preferredSize;
    public boolean isPressed;
    /**
     * Call back khi widget nhan duoc focus.
     */
    public IActionListener onFocusAction;
    /**
     * Tốc độ di chuyển.
     */
    public int speed = 3;
    /**
     *
     */
    public int scrollBarH, scrollBarY;

//    //#if BigScreen || Android
//    public boolean  isPaintCommands = false;
//    public Font commandFont;
//    public int commandH = 30;
//    //#endif
    /**
     * Constructor for creating this widget from XML Called by the
     * <code>UI</code> class.
     */
    public Widget() {
        preferredSize = new Dimension();
    }

    /**
     * Contructor for creating widgets from Java. Its not possible to create an
     * instance of the
     * <code>Widget</code> class, but it is from its derived classes.
     *
     * @param x X-coodrinate of the widget.
     * @param y Y-coodrinate of the widget.
     * @param w : widget's w.
     * @param h : widget's h.
     */
    public Widget(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.destX = x;
        this.destY = y;
        preferredSize = new Dimension(w, h);
    }

    /**
     * Derived classes should implement this method for drawing the widget on
     * this screen.
     *
     * @param g Graphics context.
     */
    public void paint() {
    }

    /**
     * Derived classes should implement this method for drawing the widget on
     * this screen.
     *
     * @param g Graphics context.
     */
    public void paintBackground() {
    }

    /**
     * Chi dung cho framework, khong nen dung trong cac game. Trong cac game hay
     * override ham paint de ve control.
     *
     * @param g
     */
    public void paintComponent() {
//        if (w == 0) throw new IllegalStateException("width = 0");
//        if (h == 0) throw new IllegalStateException("height = 0");
        
        int clipWidth = BaseCanvas.g.getClipWidth();
        int clipHeight = BaseCanvas.g.getClipHeight();
        int clipX = BaseCanvas.g.getClipX();
        int clipY = BaseCanvas.g.getClipY();
        if ((w > 0) && (h > 0)) {
            if ((clipX + clipWidth < x) || (clipX > x + w) || (clipY + clipHeight < y) || (clipY > y + h)) {
                // All regions outside the clip rect region are ignored
                return;
            }
        }
        BaseCanvas.g.translate(x, y);
        // Paint itself
        if ((w > 0) && (h > 0)) {
            BaseCanvas.g.clipRect(0, 0, w, h);
        }
        paintBackground();

        int borderClipW = BaseCanvas.g.getClipWidth();
        int borderClipH = BaseCanvas.g.getClipHeight();
        int borderClipX = BaseCanvas.g.getClipX();
        int borderClipY = BaseCanvas.g.getClipY();
        int totalPadding = padding + border;
        
        BaseCanvas.g.clipRect(totalPadding, totalPadding, w - (totalPadding << 1), h - ((totalPadding << 1)));
        BaseCanvas.g.translate(totalPadding, totalPadding);       
        paint();
        BaseCanvas.g.translate(-totalPadding, -totalPadding);
        BaseCanvas.g.setClip(borderClipX, borderClipY, borderClipW, borderClipH);
        
        paintBorder();
        BaseCanvas.g.translate(-x, -y);
        BaseCanvas.g.setClip(clipX, clipY, clipWidth, clipHeight);
    }

//    public void paintCommands() {
//        Graphics g = BaseCanvas.g;
//        
//        Vector cmds = new Vector();
//        if (cmdLeft != null && cmdLeft != cmdNull) {
//            cmds.addElement(cmdLeft);
//        }
//        if (cmdCenter != null && cmdCenter != cmdNull) {
//            cmds.addElement(cmdCenter);
//        }
//        if (cmdRight != null && cmdRight != cmdNull) {
//            cmds.addElement(cmdRight);
//        }
//        int cmdNum = cmds.size();
//        if (cmdNum == 0) {return;}
//        if (cmdNum == 1) {
//            Command cmd = (Command) cmds.elementAt(0);
//            int xx = w >> 1;
//            int yy = h - ((border + padding) << 1) + ((commandH - commandFont.getHeight()) >> 1) - commandFont.getHeight();
//            commandFont.drawString(g, cmd.caption, xx, yy, Font.CENTER);
//            paintCommandBorder((w >> 1) - (commandFont.getWidth(cmd.caption) >> 1) - 5, yy - 3, commandFont.getWidth(cmd.caption) + 10, commandFont.getHeight() + 6);
//        } else if (cmdNum == 2) {
//            for (int i = 0; i < 2; i++) {
//                Command cmd = (Command) cmds.elementAt(i);
//                int xx = (w >> 2) + (i * (w >> 1));
//                int yy = h - ((border + padding) << 1) + ((commandH - commandFont.getHeight()) >> 1) - commandFont.getHeight();
//                commandFont.drawString(g, cmd.caption, xx, yy, Font.CENTER);
//                paintCommandBorder(xx - (commandFont.getWidth(cmd.caption) >> 1) - 5, yy - 3, commandFont.getWidth(cmd.caption) + 10, commandFont.getHeight() + 6);
//            }
//        } else {
//            for (int i = 0; i < 3; i++) {
//                Command cmd = (Command) cmds.elementAt(i);
//                commandFont.drawString(g, cmd.caption, 0, 
//                        h - ((border + padding) << 1) + ((commandH - commandFont.getHeight()) >> 1) - commandFont.getHeight(), 
//                        i == 1 ? Font.CENTER : (i == 0 ? Font.LEFT : Font.RIGHT));
//            }
//        }
//    }
//    
//    private void paintCommandBorder(int x, int y, int w, int h) {
//        Graphics g = BaseCanvas.g;
//        int ww = w > commandFont.getWidth(L.gL(64)) + 6 ? w : commandFont.getWidth(L.gL(64)) + 6;
//        g.setColor(0x000000);
//        g.drawRoundRect(x, y, w, h, 6, 6);
//        g.setColor(0xffffff);
//        g.drawRoundRect(x + 1, y + 1, w - 2, h, 6, 6);
//    }
    /**
     * Dùng cho việc scroll bên trong
     */
    public void moveCamera() {
        if (scrollY != destScrollY) {
            scrollVy = (destScrollY - scrollY) << 2;
            scrollDy += scrollVy;
            scrollY += scrollDy >> 4;
            scrollDy = scrollDy & 0xf;
            calcScrollSize();
        }
        if (scrollX != destScrollX) {
            scrollVx = (destScrollX - scrollX) << 2;
            scrollDx += scrollVx;
            scrollX += scrollDx >> 4;
            scrollDx = scrollDx & 0xf;
        }
    }
    int dy, vy, dx, vx;

    /**
     * Dung de update thong tin cho animation. Mac dinh da update cho viec di
     * chuyen.
     */
    public void update() {
        if (destY != y) {
            vy = (destY - y) << speed;
            dy += vy;
            y += dy >> 4;
            dy = dy & 0xf;
//            y += ((destY - y) >> speed);
//            if (Math.abs(destY - y) <= 1) {
//                y = destY;
//            }
        }
        if (destX != x) {
            vx = (destX - x) << speed;
            dx += vx;
            x += dx >> 4;
            dx = dx & 0xf;
//            x += ((destX - x) >> speed);
//            if (Math.abs(destX - x) <= 1) {
//                x = destX;
//            }
        }
        if (isScrollable()) {
            moveCamera();
        }
    }

    /**
     * Xu ly phim dieu khien
     *
     * @param keyCode
     * @return
     */
    public boolean checkKeys(int type, int keyCode) {
        if (keyCode == BaseCanvas.KEY_FIRE) {
            if (type == 1) {
                isPressed = false;
                if (cmdCenter != null) {
                    cmdCenter.actionPerformed(new Object[]{cmdCenter, this});
                    return true;
                }
            } else {
                isPressed = true;
            }
        } else if (type == 1 && keyCode == BaseCanvas.KEY_SOFT_LEFT) {
            if (cmdLeft != null) {
                cmdLeft.actionPerformed(new Object[]{cmdLeft, this});
                return true;
            }
        } else if (type == 1 && keyCode == BaseCanvas.KEY_SOFT_RIGHT) {
            if (cmdRight != null) {
                cmdRight.actionPerformed(new Object[]{cmdRight, this});
                return true;
            }
        }
        return false;
    }

    /**
     * Kich hoat pointerPress len widget.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean pointerPressed(int x, int y) {
//        #if BigScreen|| Android
        lastDragTicker = System.currentTimeMillis();
        lastDragY = y;
        lastDragX = x;
//        #endif
        // Do nothing now
        clearDrag();
        isPressed = true;
        if (isFocusable) {
            requestFocus();
        }
        return true;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public boolean pointerReleased(int x, int y) {

        if (isDragActivated) {
            isPressed = false;
//            int t = BaseCanvas.gameTicks - la;
//            int scroll = scrollY;
            isDragActivated = false;
            boolean shouldScrollX = chooseScrollXOrY(x, y);
            if (shouldScrollX) {
//                int s = x - Screen1.initialPressX;
//                int v = s/t;
//                scrollX += (v<<2);
//                scroll = scrollX;
                if (scrollX < 0) {
//                    startTensile(scroll, 0);
                    destScrollX = 0;
                    return true;
                }
                int viewW = w - 2 * padding;
                if (scrollX > preferredSize.width - viewW) {
//                        startTensile(scroll, getScrollDimension().getWidth() - getWidth());
                    destScrollX = preferredSize.width - viewW;
                    if (destScrollX < 0) {
                        destScrollX = 0;
                    }
                    return true;
                }

            } else {
//                #if BigScreen|| Android
                int s = (int) (y - lastDragY);
                long deltaTime = System.currentTimeMillis() - lastDragTicker + 1;
                if (deltaTime != 1) {
                    int aT = (int) ((s << 11) / (deltaTime * deltaTime)); // gia tốc trung bình tính theo s và t ( x1000 )                    
                    int vT = (int) (aT * deltaTime); // vận tốc hiện tại
                    long time = vT / ((aT << 1) + 1); // gia tốc do lực ma sát == aT/2. Tính thời gian để vT giảm = 0
                    s = (int) (((vT * time) - ((aT * time * time) >> 1)) >> 8); // (quán tính) quãng đường có thể đi tiếp được                 
//                    System.out.println("deltaTime : " + deltaTime);
//                    System.out.println("aT : " + aT);
//                    System.out.println("time : " + time);                   
                } else {
                    s = (int) ((y - lastDragY + 1) * (MIN_STOPPING_TIME_DRAG >> 1) / (Math.abs(y - lastDragY + 1)));
                }
//                #endif
//                System.out.println("s : " + s);
//                int s = y - Screen1.initialPressY;
//                int v = s/t;
//                scrollY += (v<<2);
                if (scrollY < 0) {
                    destScrollY = 0;
//                    startTensile(scroll, 0);
                    return true;
                }
//                int viewH = h - 2 * padding;
                int viewH = h - ((padding + border) << 1);
//                if (scrollY > preferredSize.height - viewH) { // source cu
////                        startTensile(scroll, getScrollDimension().getHeight() - getHeight());
//                    destScrollY = preferredSize.height - viewH;                    
//                }
                if (scrollY > preferredSize.height - viewH) {//dacthang
//                        startTensile(scroll, getScrollDimension().getHeight() - getHeight());
                    destScrollY = preferredSize.height - viewH;
                } //                #if BigScreen|| Android
                else {
                    destScrollY -= s;
                }
                if (destScrollY > preferredSize.height - viewH) {
                    destScrollY = preferredSize.height - viewH;
                } else if (destScrollY < 0) {
                    destScrollY = 0;
                }
//                #endif
                return true;

            }

//            float speed = Display.getInstance().getDragSpeed(!shouldScrollX);
//            int tensileLength = getWidth() / 2;
//            if (!isTensileDragEnabled()) {
//                tensileLength = 0;
//            }
//            if (!shouldScrollX) {
//                if (speed < 0) {
//                    draggedMotion = Motion.createFrictionMotion(scroll, -tensileLength, speed, 0.0004f);
//                } else {
//                    draggedMotion = Motion.createFrictionMotion(scroll, getScrollDimension().getHeight()
//                            - getHeight() + tensileLength, speed, 0.0004f);
//                }
//            } else {
//                if (speed < 0) {
//                    draggedMotion = Motion.createFrictionMotion(scroll, -tensileLength, speed, 0.0004f);
//                } else {
//                    draggedMotion = Motion.createFrictionMotion(scroll, getScrollDimension().getWidth()
//                            - getWidth() + tensileLength, speed, 0.0004f);
//                }
//            }
//            draggedMotion.start();
        } else {
            if (isPressed) {
                isPressed = false;
                if (cmdCenter != null) {
                    cmdCenter.actionPerformed(new Object[]{cmdCenter, this});
                    return true;
                }
            }
            isPressed = false;
        }
        return false;
    }

    public boolean isPressed() {
        return isPressed;
    }
    
//    #if BigScreen|| Android
    final long MIN_STOPPING_TIME_DRAG = 500; // khoảng thời gian tối đa di chuyển giữa 2 vị trí khi drag. Nếu lớn hơn khoảng thời gian thì tính là bắt đầu drag
    long lastDragTicker = 0; // thời điểm bắt đầu drag
    long lastDragY = 0;
    long lastDragX = 0;
//    #endif

    /**
     * Goi khi pointer Dragged tren widget
     */
    public boolean pointerDragged(int x, int y) {
        // Do nothing now
        if (isScrollable()) {
//            #if BigScreen|| Android
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDragTicker > MIN_STOPPING_TIME_DRAG) { // begin new drag
                lastDragTicker = currentTime;
                lastDragY = y;
                lastDragX = x;
            }
//            #endif
            if (!isDragActivated) {
                lastScrollY = y;
                lastScrollX = x;
                isDragActivated = true;
                ((Screen) BaseCanvas.getCurrentScreen()).draggedWidget = this;
                return true;
            }
            // we drag inversly to get a feel of grabbing a physical screen
            // and pulling it in the reverse direction of the drag
            if (isScrollableY()) {
//                int tensileLength = h >> 1;
//                if (!isSmoothScrolling() || !isTensileDragEnabled()) {
//                    tensileLength = 0;
//                }
                int scroll = scrollY + (lastScrollY - y);
//                if (scroll >= -tensileLength && scroll < preferredSize.height - h + tensileLength) {
//                    setScrollY(scroll);
                destScrollY = scrollY = scroll;
                calcScrollSize();
//                }
            }
            if (isScrollableX()) {
//                int tensileLength = w >> 1;
//                if (!isSmoothScrolling() || !isTensileDragEnabled()) {
//                    tensileLength = 0;
//                }
                int scroll = scrollX + (lastScrollX - x);
//                if (scroll >= -tensileLength && scroll < preferredSize.width - w + tensileLength) {
//                    setScrollX(scroll);
                destScrollX = scrollX = scroll;
//                }
            }
            lastScrollY = y;
            lastScrollX = x;
            return true;
        } else {
            // Neu nhu widget nay khong the scoll duoc thi tim cha no xem co scroll khong
//            isPressed = false;
//            isFocused = false;
//            if (!contains(getAbsoluteX(), getAbsoluteY())) {
//                isFocused = isPressed = false;
//            }
            if (parent != null) {
                isPressed = false;
                parent.pointerDragged(x, y);
//                if ( isParentDragged ){
//                isFocused = isPressed =false;
//                } else {
//                    isPressed = false;
//                }
//                return false;
//                if ( isParentDragged)
//                return isFocused = isPressed = !parent.pointerDragged(x, y);
            }
        }
        return false;
    }

    /**
     * Set cho control focus hay khong.
     *
     * @param focused
     */
    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public void setFocusWithParents(boolean focused) {
        setFocused(focused);
        if (parent != null && parent.isFocusable) {
            parent.setFocusWithParents(focused);
        }
    }

    /**
     * Xóa tất cả focus của wid và cha của nó nếu có.
     *
     * @param wid
     */
    public void clearFocus() {
        isFocused = false;
        if (parent != null) {
            parent.isFocused = false;
        }
    }

    /**
     * Kiem tra xem 1 diem co nam trong wid nay khong. Dung de check cho viec
     * touch. Toa do la vi tri tuong doi cua Widget nam trong WidgetGroup
     *
     * @param wid : Widget can kiem tra.
     * @param x : Vi tri pixel X tuong doi so voi WidgetGroup tren man hinh
     * @param y : Vi tri pixel Y tuong doi so voi WidgetGroup tren man hinh
     * @return : wid co chua hay khong
     */
    public static boolean containPoint(Widget wid, int x, int y) {
        return (wid.x < x && wid.y < y && wid.x + wid.w > x && wid.y + wid.h > y);
    }

    public void onFocused() {
        if (onFocusAction != null) {
            onFocusAction.actionPerformed(new Object[]{null, this});
        }
    }

    /**
     * Check thu co nam trong vi tri man hinh nay khong
     *
     * @param x : toa do man hinh
     * @param y : toa do man hinh
     * @return
     */
    public boolean contains(int x, int y) {
        int absX = getAbsoluteX() + scrollX;
        int absY = getAbsoluteY() + scrollY;
        return (x >= absX && x < absX + w && y >= absY && y < absY + h);
    }

    /**
     * Returns the absolute X location based on the component hierarchy, this
     * method calculates a location on the screen for the component rather than
     * a relative location as returned by getX()
     *
     * @return the absolute x location of the component
     * @see #getX
     */
    public int getAbsoluteX() {
        int xx = this.x - scrollX + border + padding;
        if (parent != null) {
            xx += parent.getAbsoluteX();
        }
        return xx;
    }

    /**
     * Returns the absolute Y location based on the component hierarchy, this
     * method calculates a location on the screen for the component rather than
     * a relative location as returned by getX()
     *
     * @return the absolute y location of the component
     * @see #getY
     */
    public int getAbsoluteY() {
        int yy = this.y - scrollY + border + padding;
        if (parent != null) {
            yy += parent.getAbsoluteY();
        }
        return yy;
    }

    public void onLostFocused() {
    }

    public void clearDrag() {
        isDragActivated = false;
        if (parent != null) {
            parent.clearDrag();
        }
    }

    /**
     * Indicates whether the component should/could scroll by default a
     * component is not scrollable.
     *
     * @return whether the component is scrollable
     */
    public boolean isScrollable() {
        return isScrollableX() || isScrollableY();
    }

    /**
     * Indicates whether the component should/could scroll on the X axis
     *
     * @return whether the component is scrollable on the X axis
     */
    public boolean isScrollableX() {
        return isScrollableX;
    }

    /**
     * Indicates whether the component should/could scroll on the Y axis
     *
     * @return whether the component is scrollable on the X axis
     */
    public boolean isScrollableY() {
        return isScrollableY;
    }

    public boolean chooseScrollXOrY(int x, int y) {
        boolean ix = isScrollableX();
        boolean iy = isScrollableY();
        if (ix && iy) {
            return Math.abs(BaseCanvas.instance.pointerPressedX - x) > Math.abs(BaseCanvas.instance.pointerPressedY - y);
        }
        if (ix) {
            return true;
        }
        return false;
    }

    public Widget getDragWidget() {
        if (isDragActivated) {
            return this;
        }
        if (this instanceof WidgetGroup) {
            WidgetGroup wg = (WidgetGroup) this;
            int count = wg.count();
            for (int i = count; --i >= 0;) {
                return wg.getWidgetAt(i).getDragWidget();
            }
        }
        return null;
    }

    /**
     * Trả lại Widget root tức là Widget không có parent.
     *
     * @return
     */
    protected Widget getRootWidget() {
        if (parent == null) {
            return this;
        } else {
            return parent.getRootWidget();
        }
    }

    /**
     * Lay command de thuc hien khi widget nay duoc focus. Neu command khong ton
     * tai thi lay command cua cha no.
     *
     * @return : Command thuc thi hoac null neu khong co command nao .
     */
    public Command getLeftCommand() {
        if (cmdLeft != null) {
            return cmdLeft;
        }
        if (parent != null) {
            return parent.getLeftCommand();
        }
        return null;
    }

    public Command getRightCommand() {
        if (cmdRight != null) {
            return cmdRight;
        }
        if (parent != null) {
            return parent.getRightCommand();
        }
        return null;
    }

    public Command getCenterCommand() {
        if (cmdCenter != null) {
            return cmdCenter;
        }
        if (parent != null) {
            return parent.getCenterCommand();
        }
        return null;
    }

    public void scrollTo(int sx, int sy, int sw, int sh) {
        if (isScrollableY()) {
            int viewH = h - ((padding + border) << 1);
            if (sy < destScrollY) {
                destScrollY = sy;
            } else if (sy + sh > destScrollY + viewH) {
                destScrollY = sy + sh - viewH;
                if (destScrollY >= preferredSize.height - viewH) {
                    destScrollY = preferredSize.height - viewH;
                }
            }
            if (destScrollY < 0) {
                destScrollY = 0;
            }
        }
//        System.out.println("------------------------------------------");
//        System.out.println("Des Y: " + destScrollY);
//        System.out.println("Des X: " + destScrollX);
//        System.out.println("prefer H: "+preferredSize.height);
//        System.out.println("h: "+h);
//        System.out.println("border, pading: " + border + ", "+padding);
//        System.out.println("viewh: "+ (h - ( (padding + border) << 1)));
//        System.out.println("------------------------------------------");
        if (isScrollableX()) {
            int viewW = w - ((padding + border) << 1);
            if (sx < destScrollX) {
                destScrollX = sx;
            } else if (sx + sw > destScrollX + viewW) {
                destScrollX = sx + sw - viewW;
                if (destScrollX > preferredSize.width - viewW) {
                    destScrollX = preferredSize.width - viewW;
                }
            }
            if (destScrollX < 0) {
                destScrollX = 0;
            }
        }
    }

    public void scrollToEnd() {
        int viewH = h - (padding << 1);
        destScrollY = preferredSize.height - viewH;
        if (destScrollY < 0) {
            destScrollY = 0;
        }
    }

    public void requestFocus() {
        ((Screen) BaseCanvas.getCurrentScreen()).requestFocus(this);
    }

    public void setMetrics(int x, int y, int w, int h) {
        this.w = w;
        this.h = h;
        setPosition(x, y);
    }
    
    /**
     * Set vi tri x, y cho component.
     * @param x
     * @param y 
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.destX = x;
        this.destY = y;
    }
    
    public void setSize(int w, int h) {
        this.w = w;
        this.h = h;
    }

    private void calcScrollSize() {
        if (preferredSize.height == 0) {
            return;
        }
        scrollBarY = scrollY * (h - ((border) << 1)) / preferredSize.height + border;
        scrollBarH = (h - ((border) << 1)) * (h - ((border) << 1)) / preferredSize.height;
    }

    protected void paintBorder() {
        LAF.paintScrollBar(this);
    }
    
    public void setPreferredSize(int width, int height) {
        preferredSize = new Dimension(width, height);
    }
}
