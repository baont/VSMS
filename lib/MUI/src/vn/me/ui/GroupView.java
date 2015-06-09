package vn.me.ui;

import vn.me.ui.common.LAF;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.interfaces.IListModel;
import vn.me.ui.model.Command;

/**
 *
 * @author PCGPS
 */
public class GroupView extends WidgetGroup implements IActionListener {

    public ListItem btnTitle;
    public WidgetGroup childrendContainer;
    public boolean isFolded = true;
    public int maxH = -1;

    public GroupView(IListModel entry, int x, int y, int w, int h, byte collapseSoftButtonPos) {
	super(x, y, w, h);
	padding = 1;
//	border = 0;
//    padding = 0;
	isScrollableX = false;
	isScrollableY = false;
	btnTitle = new ListItem(entry, 0, 0, w, LAF.LOT_IMAGE_ITEM_HEIGHT);
	btnTitle.setMetrics(x, y, w, btnTitle.h);
	if (collapseSoftButtonPos == 0) {
	    btnTitle.cmdCenter = new Command(0, "", this);
	} else {
	    btnTitle.cmdRight = new Command(0, "", this);
	}
	childrendContainer = new WidgetGroup(WidgetGroup.VIEW_MODE_LIST);
	childrendContainer.setMetrics(0, 0, w, 0);
	addWidget(btnTitle, false);
	isAutoFit = true;
	setViewMode(VIEW_MODE_LIST);
	childrendContainer.isScrollableY = true;

	// khởi tạo chưa có childrend nên isFocusable = false

	childrendContainer.isFocusable = false;
    }

    public void addWidget(Widget wid) {
	childrendContainer.addWidget(wid, true);
	if (maxH == -1 || childrendContainer.preferredSize.height < maxH) {
	    childrendContainer.h = childrendContainer.preferredSize.height;
            if ( parent instanceof WidgetGroup )
	    ((WidgetGroup)parent).doLayout();
	}
	// 
	
	if (childrendContainer.isFocusable == false) {
	    childrendContainer.isFocusable = true;
	}
    }

    public void actionPerformed(Object o) {
	setFolded(!isFolded);
    }

    public void setFolded(boolean isFolded) {
	if (this.isFolded == isFolded) {
	    return;
	}
	if (isFolded) {
	    removeWidget(childrendContainer);
	} else {
	    addWidget(childrendContainer, true);
	}
	this.isFolded = isFolded;
        if ( parent instanceof WidgetGroup )
	((WidgetGroup)parent).doLayout();
    }
}
