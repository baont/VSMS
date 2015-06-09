package vn.me.ui;

import javax.microedition.lcdui.Image;
import vn.me.ui.interfaces.IActionListener;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.ResourceManager;
import vn.me.ui.common.LAF;
import vn.me.ui.model.Command;

/**
 *
 * @author TAm Dinh
 */
public class TabView extends WidgetGroup {

    /**
     * Chieu cao moi button trong tab.
     */
//    public static final int TAB_BUTTON_H = LAF.LOT_ITEM_HEIGHT;
    /**
     * Do dai moi button trong tab.
     */
//    public static final int TAB_BUTTON_W = 70;
    public IActionListener onTabChanged;
    /**
     * Quan ly cac button co trong tab.
     */
    public ButtonGroup tabButtons;
    /**
     * Quản lý các control nội dung từng tab.
     */
    public WidgetGroup tabContainer;
    
    public int backGoundColor = LAF.CLR_MENU_BGR;

    /**
     * Dùng để paint riêng cho nút của Tab.
     */
//    private TabButtonPainter tabPainter;
    /**
     * 
     * @param x
     * @param y
     * @param w
     * @param h 
     */
    public TabView(int x, int y, int w, int h) {
        this(x, y, w, h, LAF.LOT_ITEM_HEIGHT);
    }

    /**
     * 
     * @param x
     * @param y
     * @param w
     * @param h
     * @param tabButtonHeight Độ cao của TabButton.
     */
    public TabView(int x, int y, int w, int h, int tabButtonHeight) {
        
        super(x, y, w, h);
        isScrollableX = false;
        isScrollableY = false;
        tabButtons = new ButtonGroup(0, 0, w, tabButtonHeight);
        
        tabContainer = new WidgetGroup(0, tabButtonHeight + 2, w, h - tabButtonHeight - 2);
        addWidget(tabButtons);
        addWidget(tabContainer);
        tabButtons.buttonSelectionChanged = new IActionListener() {

            public void actionPerformed(Object o) {
                tabContainer.hideAll();
                Widget wid = tabContainer.getWidgetAt(tabButtons.selectedIndex);
                if (wid != null) {
                    wid.isVisible = true;
                    if (wid.isFocusable) {
                        tabContainer.defaultFocusWidget = wid;
                    } else {
                        tabContainer.defaultFocusWidget = null;
                    }
                }
                if (onTabChanged != null) {
                    onTabChanged.actionPerformed(new Object[]{new Command(-1, null, onTabChanged), this});
                }
            }
        };
        tabButtons.isScrollableX = true;
//        isLoop = true;
    }

    /**
     * Khởi tạo TabView với w = Độ rộng màn hình, h = chiều cao cao của màn hình.
     */
    public TabView() {
        this(0, 0, BaseCanvas.w, BaseCanvas.h);
    }

    /**
     * Thêm tab vào TabView, mỗi tab sẽ tương ứng với một Widget component.
     * @param tabName Tên của tab.
     * @param component 
     */
    public void addTab(String tabName, Widget component) {
//        int fromX = 0;
//        if (tabButtons.count() > 0) {
//            TabButton lastButton = (TabButton) tabButtons.children[tabButtons.children.length - 1];
//            fromX = lastButton.x + lastButton.w;
//        }
//        TabButton b = new TabButton(tabName);
//        b.setMetrics(fromX, 0, Math.max(b.normalfont.getWidth(tabName), b.selectedfont.getWidth(tabName)) + b.padding * 2, LAF.LOT_ITEM_HEIGHT);
//        insertTab(b, component, 0);
////        if (tabButtons.selectedIndex == -1) {
////            // Neu chua chon tab nao thi chon tab 0
////            setSelectedTabIndex(0);
////        }
//        doLayout();
        addTab(tabName, component, ResourceManager.defaultFont);
    }

    /**
     * Thêm tab vào tabview.
     * @param img Hình của tab.
     * @param component Widget đi với tab này.
     */
    public void addTab(Image img, Widget component) {
        int fromX = 0;
        if (tabButtons.count() > 0) {
            TabButton lastButton = (TabButton) tabButtons.children[tabButtons.children.length - 1];
            fromX = lastButton.x + lastButton.w;
        }
        TabButton b = new TabButton(img);
//        b.setMetrics(fromX, 0, b.icon.getWidth() + b.padding * 2, tabButtons.h);//Styles.LOT_ITEM_HEIGHT);
        b.setMetrics(fromX, 0, b.image.frameWidth + b.padding * 2, tabButtons.h);//Styles.LOT_ITEM_HEIGHT);
        insertTab(b, component, 0);
//        if (tabButtons.selectedIndex == -1) {
//            // Neu chua chon tab nao thi chon tab 0
//            setSelectedTabIndex(0);
//        }
        doLayout();
    }

    public void addTab(String tabName, Widget component, Font tabFont) {
	int fromX = 0;
	if (tabButtons.count() > 0) {
	    TabButton lastButton = (TabButton) tabButtons.children[tabButtons.children.length - 1];
	    fromX = lastButton.x + lastButton.w;
	}
	TabButton b = new TabButton(tabName);
	b.normalfont = tabFont;       
	b.setMetrics(fromX, 0, Math.max(b.normalfont.getWidth(tabName), b.selectedfont.getWidth(tabName)) + b.padding * 2, LAF.LOT_ITEM_HEIGHT);
	insertTab(b, component, 0);
	doLayout();
    }
     
    public void addTab(int w, int h, Widget component) {
        int fromX = 0;
        if (tabButtons.count() > 0) {
            TabButton lastButton = (TabButton) tabButtons.children[tabButtons.children.length - 1];
            fromX = lastButton.x + lastButton.w;
        }
        TabButton b = new TabButton("");
//        b.setMetrics(fromX, 0, b.icon.getWidth() + b.padding * 2, tabButtons.h);//Styles.LOT_ITEM_HEIGHT);
        b.setMetrics(fromX, 0, w, h);//Styles.LOT_ITEM_HEIGHT);
        insertTab(b, component, 0);
//        if (tabButtons.selectedIndex == -1) {
//            // Neu chua chon tab nao thi chon tab 0
//            setSelectedTabIndex(0);
//        }
        doLayout();
    }
    
    /**
     * Returns the tab at <code>index</code>.
     *
     * @param index the index of the tab to be removed
     * @exception IndexOutOfBoundsException if index is out of range
     *            (index < 0 || index >= tab count)
     * @return the component at the given tab location
     * @see #addTab
     * @see #insertTab
     */
    /**
     * Lấy widget tại tab có chỉ số index.
     * @param index chỉ số của tab.
     * @return Widget đi theo tab.
     */
    public Widget getTabComponentAt(int index) {
        checkIndex(index);
        return tabContainer.getWidgetAt(index);
    }

    /**
     * Lấy component của tab đang được chọn.
     * @return 
     */
    public Widget getSelectedTabComponent() {
        int count = tabContainer.count();
        for (int i = count; --i >= 0;) {
            Widget wid = tabContainer.getWidgetAt(i);
            if (wid.isVisible) {
                return wid;
            }
        }
        return null;
    }

    private void checkIndex(int index) {
        if (index < 0 || index > tabContainer.count()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    /**
     * Insert 1 tab mới vào tab view
     * @param tab
     * @param component
     * @param index 
     */
    public void insertTab(final Button tabButton, Widget component, int index) {
        checkIndex(index);
        if (component == null) {
            return;
        }
        tabButtons.addWidget(tabButton);
        component.setMetrics(0, 0, tabContainer.w - tabContainer.padding * 2, tabContainer.h - tabContainer.padding * 2);
        tabContainer.addWidget(component);
        tabButtons.columns = tabButtons.count() + 1;
        tabButtons.preferredSize.width += tabButton.w;//tabButtons.count() * TAB_BUTTON_W;
        component.isVisible = false;
        tabButton.onFocusAction = new IActionListener() {

            public void actionPerformed(Object o) {
                tabButtons.setSelected(tabButton);
            }
        };
//        tabButton.painter = tabPainter;
    }

    /**
     * Set color.
     * @param backgroundColor
     * @param focusBackgroundColor 
     */
    public void setTabButtonColor(int backgroundColor, int focusBackgroundColor, int selectedColor) {

        for (int i = 0; i < tabButtons.count(); i++) {
//            tabButtons.children[i]
            TabButton t = (TabButton) tabButtons.children[i];
            t.backgroundcolor = backgroundColor;
            t.focusBackgroundColor = focusBackgroundColor;
            t.selectedColor = selectedColor;
        }
    }

    /**
     * Returns the currently selected index for this tabbedpane.
     * Returns -1 if there is no currently selected tab.
     *
     * @return the index of the selected tab
     */
    public int getSelectedIndex() {
        if (tabButtons != null) {
            return tabButtons.selectedIndex;
        }
        return -1;
    }

    /**
     * Removes the tab at <code>index</code>.
     * After the component associated with <code>index</code> is removed,
     * its visibility is reset to true to ensure it will be visible
     * if added to other containers.
     * @param index the index of the tab to be removed
     * @exception IndexOutOfBoundsException if index is out of range
     *            (index < 0 || index >= tab count)
     *
     * @see #addTab
     * @see #insertTab
     */
    public void removeTabAt(int index) {
        checkIndex(index);
        tabContainer.setFocused(false);
        Widget content = tabContainer.getWidgetAt(index);
        tabContainer.removeWidget(content);
        tabButtons.setFocused(false);
        Widget key = tabButtons.getWidgetAt(index);
        tabButtons.removeWidget(key);
        doLayout();
        if (tabButtons.selectedIndex == index && index < tabButtons.count()) {
            tabButtons.selectedIndex = -1;
            setFocusTabIndex(index);
        } else {
            setFocusTabIndex(index - 1);
        }
    }

    /**
     * Sets the selected index for this tabbedpane. The index must be a valid
     * tab index.
     * @param index the index to be selected
     * @throws IndexOutOfBoundsException if index is out of range
     * (index < 0 || index >= tab count)
     */
    public void setSelectedTabIndex(int index) {
        checkIndex(index);
//        //#if Android || BigScreen
//        tabButtons.getFocusedWidget(true).isFocused = false;
//        //#endif
        tabButtons.setSelected(index);
    }

    /**
     * Sets the selected index for this tabbedpane. The index must be a valid
     * tab index.
     * @param index the index to be selected
     * @throws IndexOutOfBoundsException if index is out of range
     * (index < 0 || index >= tab count)
     */
    public void setFocusTabIndex(int index) {
        checkIndex(index);
        tabButtons.getWidgetAt(index).requestFocus();
    }

    /**
     * Sets the selected index for this tabbedpane. The index must be a valid
     * tab index.
     * @param index the index to be selected
     * @throws IndexOutOfBoundsException if index is out of range
     * (index < 0 || index >= tab count)
     */
    public boolean setFocusTabName(String name) {
        int index = getTabIndexFromName(name);
        if (index == -1) {
            return false;
        }
        setFocusTabIndex(index);
        return true;
    }

    /**
     * Lấy tab index theo tên của tab.
     * @param name Tên của tab.
     * @return index của tab.
     */
    public int getTabIndexFromName(String name) {
        for (int i = tabButtons.count(); --i >= 0;) {
            if (name.equals(((TabButton) tabButtons.getWidgetAt(i)).text)) {
                return i;
            }
        }
        return -1;
    }
//

    protected void paintBorder() {
//        BaseCanvas.g.setColor(0xcf293e);
        BaseCanvas.g.setColor(0x6aaaf9);
//        BaseCanvas.g.setColor(0x2647a0);
        BaseCanvas.g.drawRect(0, tabButtons.h, tabContainer.w - 1, 1);

//        BaseCanvas.g.setColor(0xa60811);
//        BaseCanvas.g.setColor(0xedf5ff);
//        BaseCanvas.g.setColor(0x243356);
//        BaseCanvas.g.drawRect(0, tabButtons.h + 1, tabContainer.w - 1, 0);
    }

//    public void paintBackground() {
////        BaseCanvas.g.setColor(LAF.CLR_BACKGROUND_DARKER)S;
////        BaseCanvas.g.setColor(backGoundColor);
////        BaseCanvas.g.fillRect(0, tabContainer.y, w, tabContainer.h);
//    }

    public void onFocused() {
        super.onFocused();
        tabButtons.requestFocus();
    }

    /**
     * @inheritDoc
     */
    public void doLayout() {
        if (tabButtons.isAutoFit) {
            int bx = 0;
            for (int i = 0; i < tabButtons.children.length; i++) {
                tabButtons.children[i].destX = tabButtons.children[i].x = bx;
                bx += tabButtons.children[i].w;
            }
        } else {
            int bx = 0;
            for (int i = 0; i < tabButtons.children.length; i++) {
                tabButtons.children[i].w = (tabButtons.w - 2 * (border + padding)) / tabButtons.children.length;
                tabButtons.children[i].destX = tabButtons.children[i].x = bx;
                bx += tabButtons.children[i].w;
            }
        }
    }

//    public boolean checkKeys(int type, int keyCode) {
//        boolean isHandled = false;
//        
//        int currentIndex = getSelectedIndex();
//        if (type == 0 && keyCode == BaseCanvas.KEY_LEFT) {
//            if (currentIndex > 0) {
//                setFocusTabIndex(currentIndex - 1);
//                return true;
//            }
//        } else if (type == 0 && keyCode == BaseCanvas.KEY_RIGHT) {
//            if (currentIndex < tabButtons.count() - 1) {
//                setFocusTabIndex(currentIndex + 1);
//                return true;
//            }
//        }
//        return super.checkKeys(type, keyCode);
//    }
//    private class TabButtonPainter implements IPainter {
//
//        public void paint(Graphics g, Object model, int x, int y, int w, int h, boolean isFocused) {
//            Button tabButton = (Button) model;
//            if (tabButton.isBlink && BaseCanvas.gameTicks % 10 > 3) {
//                g.setColor(0xee0000);
//                g.fillRect(0, 0, w, h);
//            } else if (tabButton.isFocused) {
//                Effects.drawLinearGradient(g, LAF.CLR_TITLE_DARKER, LAF.CLR_TITLE_LIGHTER, 0, 0, w - 1, h, false);
//                g.setColor(LAF.CLR_BORDER_FOCUSED);
//                g.drawLine(0, 0, w - 1, 0);
//                g.drawLine(0, 0, 0, h - 1);
//                g.drawLine(w - 1, 0, w - 1, h - 1);
//            } else {
//                if (tabButton.isSelected) {
//                    Effects.drawLinearGradient(g, LAF.CLR_SELECTED_ITEM_DARKER, LAF.CLR_SELECTED_ITEM_LIGHTER, 0, 0, w - 1, h, false);
//                } else {
//                    g.setColor(LAF.CLR_BACKGROUND_DARKER);
//                    g.fillRect(0, 0, w - 1, h - 1);
//                }
//                if (tabContainer.isFocused && tabButton.isSelected) {
//                    g.setColor(LAF.CLR_BORDER_FOCUSED);
//                } else {
//                    g.setColor(LAF.CLR_BORDER);
//                }
//                g.drawLine(0, 0, w - 1, 0);
//                g.drawLine(0, 0, 0, h - 1);
//                g.drawLine(w - 1, 0, w - 1, h - 1);
//            }
//
//        }
//    }
    
    public static class TabButton extends Button {

        public int selectedColor = 0xdcecff;

        public TabButton(String tabName) {
            super(tabName);
            normalfont = ResourceManager.defaultFont;
            align = Font.CENTER;
            backgroundcolor = 0xfafafa;
            focusBackgroundColor = 0x1967c9;
        }

        public TabButton(Image tabIcon) {
            super(tabIcon);
            backgroundcolor = 0xfafafa;
            focusBackgroundColor = 0x1967c9;
        }

        
        
        //#if Android || BigScreen
//#         public void paint() {
//# //            super.paint();
//#             (isSelected ? selectedfont : normalfont).drawString(BaseCanvas.g, text, (w - ((border + padding) << 1)) >> 1, (h - ((border + padding) << 1) - (isSelected ? selectedfont : normalfont).getHeight()) >> 1, Font.CENTER);
//#         }
        //#endif

        public void paintBackground() {
            if (painter != null) {
                if (isSelected || isFocused) {
                    painter.paintBackGround(0, 0 - 2, w, h, BaseCanvas.g);
                } else {
                    painter.paintBackGround(0, 0, w, h, BaseCanvas.g);
                }
                return;
            }
            LAF.paintTabButtonBG(this);
////            super.paintBackground();
//            if (isSelected || isFocused) {
//                if (isFocused) {
//                    BaseCanvas.g.setColor(focusBackgroundColor);
////                    BaseCanvas.g.setColor(0x1967c9);                    
////                    BaseCanvas.g.setColor(0xff487c);
//                } else {
//                    BaseCanvas.g.setColor(selectedColor);
////                    BaseCanvas.g.setColor(0xdcecff);                    
////                    BaseCanvas.g.setColor(0xff74a8);
//                }
//                BaseCanvas.g.fillRect(2, 2, w - 4, h - 2);
//                // vien dam           
//                BaseCanvas.g.setColor(0x323232);
//                BaseCanvas.g.fillRect(2, 0, w - 4, 1);
//
//                BaseCanvas.g.fillRect(0, 2, 1, h - 1);
//                BaseCanvas.g.fillRect(w - 1, 2, 1, h - 1);
//
//                BaseCanvas.g.fillRect(1, 1, 1, 1);
//                BaseCanvas.g.fillRect(w - 2, 1, 1, 1);
//// vien trang
//                BaseCanvas.g.setColor(0xffffff);
//                BaseCanvas.g.fillRect(2, 1, w - 4, 1);
//                BaseCanvas.g.fillRect(1, 2, 1, h - 1);
//                BaseCanvas.g.fillRect(w - 2, 2, 1, h - 1);
//
//                BaseCanvas.g.fillRect(2, 2, 1, 1);
//                BaseCanvas.g.fillRect(w - 3, 2, 1, 1);
//            } else {
//// nen
////                BaseCanvas.g.setColor(0xb6b6b6);
//                BaseCanvas.g.setColor(backgroundcolor);
////                BaseCanvas.g.setColor(0xfafafa);
//                BaseCanvas.g.fillRect(1, 3, w - 2, h - 2);
//// vien dam           
//                BaseCanvas.g.setColor(0x636c67);
//                BaseCanvas.g.fillRect(1, 2, w - 2, 1);
//                BaseCanvas.g.fillRect(0, 3, 1, h - 2);
//                BaseCanvas.g.fillRect(1, 3, 1, 1);
//                BaseCanvas.g.fillRect(w - 2, 3, 1, 1);
////                
//                BaseCanvas.g.setColor(0x323232);
//                BaseCanvas.g.fillRect(w - 1, 3, 1, h - 2);
//            }
//        }
    }
    }
}
