package vn.me.ui;

import vn.me.core.BaseCanvas;
import vn.me.ui.model.Command;

/**
 * A WidgetGroup is a special Widget that can contain other widgets (called
 * children.) The widget group is the base class for layouts and widgets
 * containers.
 *
 * @author TamDinh
 */
public class WidgetGroup extends Widget {

    //// Declare constants
    public static final int VIEW_MODE_FREE = 0;
    public static final int VIEW_MODE_LIST = 1;
    public static final int VIEW_MODE_GRID = 2;
    protected int viewMode;
    /**
     * Thong so cho Layout, bao control tren 1 dong. Thong thuong columns = 1,
     * Neu khac 1 la dang Grid.
     */
    public int columns = 1;
    /**
     * Control duoc hien thi. Trong chế độ virtual thì children chỉ là mảng các
     * dữ liệu. IListModel
     */
//    public Vector cShildren;
    public Widget[] children;
    /**
     * Cho biet khi chuyen focus co lap lai vong vong trong no hay khong. Nghia
     * la khi di chuyen den doi tuong cuoi thi neu di chuyen tiep se qua control
     * dau tien
     */
    public boolean isLoop = false;
    /**
     * Khoang cach giua cac control trong WidgetGroup.
     */
    public int spacing = 1;
    /**
     * Widget mặc định được focus trong đám children.
     */
    public Widget defaultFocusWidget;
    /**
     * true : Fit chieu rong, chieu cao bang noi dung
     */
    public boolean isAutoFit = false;

    /**
     * Virtual Mode là dùng cho trường hợp WidgetGroup không có control thật sự.
     * children chỉ implement từ IListModel. Không phải thật sự là một Widget.
     */
//    public boolean isVirtualMode = false;
    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public WidgetGroup(int x, int y, int w, int h) {
        this(x, y, w, h, 0);
    }

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public WidgetGroup(int x, int y, int w, int h, int layout) {
        super(x, y, w, h);
        viewMode = layout;
//        isVirtualMode = isVirtual;
        initialize();
    }

    public WidgetGroup() {
        this(0, 0, 1, 1, 0);
    }

    /**
     *
     * @param layout : VIEW_MODE_FREE = 0; VIEW_MODE_LIST = 1; VIEW_MODE_GRID =
     * 2;
     */
    public WidgetGroup(int layout) {
        this(0, 0, 1, 1, layout);
    }

    private void initialize() {
        this.children = new Widget[0];
    }

    /**
     * Thêm control vào widgetgroup.
     *
     * @param widget widget muốn thêm.
     * @param isLayout true-layout lại các control trong widgetgroup.
     * false-ngược lại.
     */
    public void addWidget(Widget widget, boolean isLayout) {
        Widget[] tmpChildren = new Widget[children.length + 1];
        System.arraycopy(children, 0, tmpChildren, 0, children.length);
        tmpChildren[tmpChildren.length - 1] = widget;
//        this.children.addElement(widget);
        children = tmpChildren;
        widget.parent = this;
        if (defaultFocusWidget == null && widget.isFocusable) {
            defaultFocusWidget = widget;
        }
        if (isLayout) {
            doLayout();
        }
    }

    /**
     * Thêm control vào widget group nhưng không thực hiện layout lại các
     * control bên trong.
     *
     * @param wid control thêm vào widget group.
     */
    public void addWidget(Widget wid) {
//        if (!children.contains(wid)) { // Rem lai vi trong tab chat can cai nay
        addWidget(wid, false);
//        }
    }

    /**
     * Remove một control ra khỏi widget group.
     *
     * @param widget control muốn remove.
     */
    public void removeWidget(Widget widget) {
        if (widget == null || children.length == 0) {
            return;
        }
        if (widget == defaultFocusWidget) {
            defaultFocusWidget = null;
        }
//        this.children.removeElement(widget);
        Widget[] tmpChildren = new Widget[children.length - 1];
        boolean isFound = false;
        for (int i = 0; i < tmpChildren.length; i++) {
            if (children[i] == widget) {
                isFound = true;
            }
            tmpChildren[i] = children[isFound ? i + 1 : i];
        }
//        System.arraycopy(children, 0, tmpChildren, 0, children.length - 1);
//        children = tmpChildren;
        if (isFound || widget == children[children.length - 1]) {
            children = tmpChildren;
        }
        doLayout();
    }

    /**
     * Remove tất cả các control bên trong widget group.
     */
    public void removeAll() {
        defaultFocusWidget = null;
//        for(int i = children.length; --i >= 0; ) {
//            this.children[i].onLostFocused();
//        }
        this.children = new Widget[0];
    }

//    public void onLostFocused() {
////        super.onLostFocused();
//        for(int i = children.length; --i >= 0; ) {
//            this.children[i].onLostFocused();
//        }
//    }
    /**
     * Tim widget khac de focus khi remove/hide 1 widget .
     */
    private void findOtherFocus() {
        int index = getFocusedIndex();
        if (isLoop) {
            findNextFocus(true, index, 1);
        } else {
            isLoop = true;
            findNextFocus(true, index, 1);
            isLoop = false;
        }
    }

    /**
     * Ẩn một control bên trong widget group.
     *
     * @param widget control cần làm ẩn.
     */
    public void hideWidget(Widget widget) {
        if (isVisible) {
            if (widget.isFocused && children.length > 1) {
                findOtherFocus();
            } else {
                widget.setFocused(false);
            }
            widget.isVisible = false;
        }
    }

    /**
     * Lấy một control trong widget group tại vị trí index.
     *
     * @param index vị trí của control cần lấy.
     * @return control muốn lấy theo index.
     */
    public Widget getWidgetAt(int index) {
        if (children.length == 0 || index < 0 || index >= children.length) {
            return null;
        }
        return children[index];
    }

    /**
     * Thiết lập chế độ layout.
     *
     * @param mode Su dung constant VIEW_MODE_LIST hoac VIEW_MODE_GRID
     */
    public void setViewMode(int mode) {
        this.viewMode = mode;
        doLayout();
    }

    /**
     * Sắp xếp lại layout các control bên trong theo viewMode.
     */
    public void doLayout() {
        switch (viewMode) {
            case VIEW_MODE_GRID:
                doGridLayout();
                break;
            case VIEW_MODE_LIST:
                doListLayout();
                break;
                
        }
    }

    private void doGridLayout() {
        // Cho cac item co chieu rong nhu nhau:        
        if (children.length == 0) {
            return;
        }
        int spacingW = 0;

        int maxW = 0;//getWidgetAt(0).w;
        int maxH = 0;//getWidgetAt(0).h;
        int numberOfWidget = children.length;
        for (int i = numberOfWidget; --i >= 0;) {
            Widget wd = getWidgetAt(i);
            maxW = Math.max(maxW, wd.w);
            maxH = Math.max(maxH, wd.h);
        }
        if (columns > 0) {
            if (isAutoFit && w > 0) {
                maxW = (w - ((padding + border) << 1)) / columns;
                preferredSize.width = columns * maxW + (columns + 1) * spacing;
                w = preferredSize.width + 2 * padding;
            }
            // Tự động tính chiều rộng theo columns cho trước.
            preferredSize.width = columns * maxW + (columns + 1) * spacing;
        } else {
            // Chiều rộng đã được xác định, tính columns.
            int viewW = w - 2 * padding;
            columns = Math.max(viewW / maxW, 1);
            spacingW = (viewW - maxW * columns) / (columns + 1);
        }
        int rows = numberOfWidget / columns;
        if (numberOfWidget % columns != 0) {
            rows++;
        }

        preferredSize.height = rows * maxH + (rows - 1) * spacing;

        for (int i = 0; i < numberOfWidget; i++) {
            int xP = i % columns; // vi tri x trong grid
            int yP = i / columns; // vi tri y trong grid
            int wx = xP * maxW;
            int wy = yP * maxH;
            Widget item = getWidgetAt(i);
//            item.setMetrics(wx + (xP + 1) * spacing, wy , maxW, maxH);
            item.setMetrics(wx + (xP + 1) * spacingW, wy + yP * spacing, maxW, maxH);
        }

        if (isAutoFit) {
            w = preferredSize.width + 2 * padding;
            h = preferredSize.height + 2 * padding;
        }
    }

//    private void autoSizeCols() {
//        if (isAutoFit) {
//            int numOfcomponents = children.length;
//            int maxWidth = 0;
//            for (int iter = 0; iter < numOfcomponents; iter++) {
//                Widget cmp = children[iter];
//                maxWidth = Math.max(cmp.preferredSize.width, maxWidth);
//            }
//            columns = Math.max(w / maxWidth, 1);
//        }
//    }
    protected void doListLayout() {
        // Cho cac item co chieu rong nhu nhau:
        preferredSize.height = 0;
        if (children.length == 0) {
            return;
        }
        columns = 1;
        int amount = children.length;
        int maxW = 0;
        if (isAutoFit) {
            for (int i = 0; i < amount; i++) {
                if (maxW < children[i].w) {
                    maxW = children[i].w;
                }
            }
            w = preferredSize.width = maxW + ((padding + border) << 1);
        } else if (w > 0) {
            maxW = w - ((padding + border) << 1);
        }
        int wy = 0;
        int wx = 0;
        for (int i = 0; i < amount; i++) {
            Widget wd = getWidgetAt(i);
            wd.setMetrics(wx, wy, maxW, wd.h);
            if (wd instanceof WidgetGroup) {
                ((WidgetGroup) wd).doLayout();
            }
            wy = wd.y + wd.h + spacing;
        }
//        preferredSize.width = w;
        preferredSize.height = wy - spacing;
        if (isAutoFit) {
            if (parent != null && parent instanceof WidgetGroup && ((WidgetGroup) parent).isAutoFit) {
                parent.h -= h;
            }
//            w = preferredSize.width ;//+ (padding << 1);
            h = preferredSize.height + ((padding + border) << 1);
            if (parent != null && parent instanceof WidgetGroup && ((WidgetGroup) parent).isAutoFit) {
                parent.h += h;
            }
        } else if (h == 0) {
            h = preferredSize.height + ((padding + border) << 1);
        }

    }

    /**
     * Đếm số control có bên trong widget group.
     *
     * @return Số control có trong widget group.
     */
    public int count() {
        if (children == null) {
            return 0;
        }
        return children.length;
    }

    /**
     * Tra ve Widget dang duoc focus.
     *
     * @param isAtom : true : Lay phan tu nho nhat, false : Lay o 1 level thoi
     * @return : null neu khong co widget nao duoc set focus. else tra ve widget
     * đang được focus.
     */
    public Widget getFocusedWidget(boolean isAtom) {
        if (children == null) {
            return this;
        }
        int count = children.length;
        for (int i = count; --i >= 0;) {
            Widget widget = children[i];
            if (widget.isFocused) { // == defaultFocusWidget
                if (isAtom && widget instanceof WidgetGroup) {
                    return ((WidgetGroup) widget).getFocusedWidget(true);
                } else {
                    return widget;
                }
            }
        }
        return isFocusable ? this : null;
    }

    /**
     * Tra ve so thu tu cua Widget duoc focus Khong de quy de tim ben trong
     *
     * @return : -1 Neu khong co widget nao duoc focus.
     */
    public int getFocusedIndex() {
        if (children != null) {
            for (int i = children.length; --i >= 0;) {
                Widget widget = children[i];
                if (widget.isFocused) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void update() {
        super.update();
        if (children != null) {
            for (int i = children.length; --i >= 0;) {
                if (children[i].isVisible) {
                    children[i].update();
                }
            }
        }
    }

    /**
     * Vẽ widget group và các control bên trong nó.
     */
    public void paint() {
        BaseCanvas.g.translate(-scrollX, -scrollY);
        if (children != null) {
//            int count = children.length;
            Widget focusWid = getFocusedWidget(false);
            for (int i = 0; i < children.length; i++) {
                if (children[i].isVisible && focusWid != children[i] && !(children[i] instanceof Menu)) {
                    children[i].paintComponent();
                }
            }
            if (focusWid != this && focusWid != null && !(focusWid instanceof Menu)) {
                focusWid.paintComponent();
            }
        }
        BaseCanvas.g.translate(scrollX, scrollY);
    }
    /*
     * dacthang
     */

    /**
     * Tìm control tiếp theo trong widget group de set focus.
     *
     * @param isForward true-tìm lên
     * @param startIndex vị trí bắt đầu.
     * @param step Bước tìm.
     * @return true-Tìm được control để set focus.
     */
    public boolean findNextFocus(boolean isForward, int startIndex, int step) {

        if (children == null || children.length == 0) {
            return false;
        }
        if (!hasFocusableChildren()) {
            return false;
        }
        int index = startIndex + (isForward ? step : -step);
        if (index < 0) {
            if (isLoop) {
                index = children.length - 1;
            } else {
                index = startIndex;
            }
        } else if (index >= children.length) {
            if (isLoop) {
//                if(children[index].isFocusable)
                index = 0;
            } else {
                index = startIndex;
            }
        }
        if (startIndex == index || (startIndex > 0 && children[startIndex] == children[index])) { // Truong hop add cung 1 control len 1 tab thi khong focus qua lai giua cac control nay
            return false;
        }
        Widget widget = children[index];
        if (widget.isVisible && widget.isFocusable) {
            if (widget instanceof WidgetGroup) {
                Widget focW = ((WidgetGroup) widget).findDefaultfocusableWidget();
                focW.requestFocus();
            } else {
                widget.requestFocus();
            }
            return true;
        } else {
            return findNextFocus(isForward, index, step);
        }
    }

    /**
     * Hàm kiểm tra xem có control nào có thể focus được hay ko? Phải kiểm tra
     * mảng children != null trước khi sử dụng hàm này.
     *
     * @return true : có control focus được. false : tất cả các control k thể
     * focus được.
     */
    private boolean hasFocusableChildren() {
        for (int i = 0; i < children.length; i++) {
            if (children[i].isFocusable) {
                return true;
            }
        }
        return false;
    }

    public Widget findDefaultfocusableWidget() {
        if (defaultFocusWidget == null || !defaultFocusWidget.isVisible || !defaultFocusWidget.isFocusable) {
            return this;
        } else {
            if (defaultFocusWidget instanceof WidgetGroup) {
                return ((WidgetGroup) defaultFocusWidget).findDefaultfocusableWidget();
            } else {
                return defaultFocusWidget;
            }
        }
    }

    /**
     *
     * @param type
     * @param keyCode
     * @return
     */
    public boolean checkKeys(int type, int keyCode) {
        boolean isChangeFocused = false;
        boolean isHandle = false;
        Widget focusedWidget = this.getFocusedWidget(false);
        if (focusedWidget != this && focusedWidget != null) {
            isHandle = focusedWidget.checkKeys(type, keyCode);
            if (isHandle) {
                return true;
            }
        }
        if (type == 0 && keyCode == BaseCanvas.KEY_LEFT && viewMode != VIEW_MODE_LIST) {
            isChangeFocused = findNextFocus(false, getFocusedIndex(), 1);
        } else if (type == 0 && keyCode == BaseCanvas.KEY_RIGHT && viewMode != VIEW_MODE_LIST) {
            isChangeFocused = findNextFocus(true, getFocusedIndex(), 1);
        } else if (type == 0 && keyCode == BaseCanvas.KEY_DOWN) {
            isChangeFocused = findNextFocus(true, getFocusedIndex(), columns);
        } else if (type == 0 && keyCode == BaseCanvas.KEY_UP) {
            isChangeFocused = findNextFocus(false, getFocusedIndex(), columns);
        }
        return isChangeFocused;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public boolean pointerPressed(int x, int y) {
        Widget widget = getFocusableWidgetAt(x, y);
        if (widget == this) {
            return super.pointerPressed(x, y);
        } else if (widget != null) {
            return widget.pointerPressed(x, y);
        }
        return false;
    }

    /**
     * Lay widget có thể focus tai vi tri tuong doi so voi WidgetGroup nay. Dung
     * cho touch.
     *
     * @param x : toa do tuong doi trong WidgetGroup
     * @param y : toan do tuong doi trong WidgetGroup
     * @return
     */
    public Widget getFocusableWidgetAt(int x, int y) {
        int count = children.length;
//        boolean overlaps = getLayout().isOverlapSupported();
//        Widget component = null;


        for (int i = count - 1; i
                >= 0; i--) {
            Widget cmp = getWidgetAt(i);


            if (cmp.isVisible && cmp.contains(x, y)) {
                if (cmp instanceof WidgetGroup) {
                    return ((WidgetGroup) cmp).getFocusableWidgetAt(x, y);
                }

//                return cmp;
//                component = cmp;
                if (cmp.isFocusable) {
                    return cmp;


                }
//                if (cmp instanceof WidgetGroup) {
//                    return ((WidgetGroup) cmp).getFocusableWidgetAt(x, y);
////                    cmp = getFocusableWidgetAt(x, y);
//
//
//                } //                if (component.isFocusable) {
                //                    return component;
                //                }
            }
        }
//        if (component != null) {
//            return component;
//        }
        if (isFocusable && contains(x, y)) {
            return this;


        }
        return null;


    }

    /**
     * @inheritDoc
     */
    public boolean isScrollableY() {
        return isScrollableY;// && preferredSize.height > h;
    }

    /**
     * @inheritDoc
     */
    public boolean isScrollableX() {
        return isScrollableX;// && preferredSize.width > w;
    }

    /**
     * Ẩn tất cả các control bên trong widget group.
     */
    public void hideAll() {
        int count = this.count();
        for (int i = count;
                --i >= 0;) {
            Widget wg = getWidgetAt(i);
            wg.isVisible = false;
            wg.isFocused = false;
        }
        defaultFocusWidget = null;
    }

    /**
     * Gan command center cho tat ca con cua no
     *
     * @param cmd
     */
    public void setChildrenCommand(Command cmd) {
        int count = count();
        if (children != null) {
            for (int i = count;
                    --i >= 0;) {
                getWidgetAt(i).cmdCenter = cmd;
            }
        }
    }

    public void scrollComponentToVisible(Widget c) {
        scrollTo(c.x, c.y, c.w, c.h);
    }

    public void setMetrics(int x, int y, int w, int h) {
        super.setMetrics(x, y, w, h);
        doLayout();
    }

    public int getMaxContentWidth() {
        return w - (padding + border << 1);
    }

    public boolean containWidget(Widget childWidget) {
        for (int i = children.length - 1; i >= 0; i--) {
            if (children[i] == childWidget) {
                return true;
            }
        }
        return false;
    } 
    
    public int[] getMinSize() {
        return new int[] { w, h };
    }
}
