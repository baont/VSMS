package vn.me.ui;

import vn.me.ui.interfaces.IActionListener;

/**
 * This class is used to create a multiple-exclusion scope for a set of 
 * RadioButtons. Creating a set of RadioButtons with the same ButtonGroup object
 * means that only one RadioButton can be selected amoung the ButtonGroup.
 * Initialy all RadioButtons are unselected.
 * 
 * @author Tam Dinh
 */
public class ButtonGroup extends WidgetGroup {

    /**
     * Dung de get cho nhanh. khi set dung ham setSelectecIndex
     */
    public int selectedIndex = -1;
    public IActionListener buttonSelectionChanged;

    /** 
     * Creates a new instance of ButtonsGroup 
     */
    public ButtonGroup(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    /**
     * Clears the selection such that none of the buttons in the ButtonGroup are selected.
     */
    public void clearSelection() {
        if (selectedIndex != -1) {
            if (selectedIndex < count()) {
                ((Button) getWidgetAt(selectedIndex)).isSelected = false;
                ((Button) getWidgetAt(selectedIndex)).isPressed = false;
                ((Button) getWidgetAt(selectedIndex)).isFocused = false;
            }
            selectedIndex = -1;
        }
    }

    /**
     * Selects the given radio button. Null to clear selection.
     * 
     * @param rb the button to set as selected
     */
    public void setSelected(Button rb) {
        if (rb != null) {
            for (int i = children.length; --i >= 0;) {
                if (rb == children[i]) {
                    setSelected(i);
                    break;
                }
            }
        } else {
            clearSelection();
        }
    }

    /**
     * Sets the selected Radio button by index
     * 
     * @param index the index of the radio button to mark as selected
     */
    public void setSelected(int index) {
        if (selectedIndex == index) {
            return;
        }
        clearSelection();
        ((Button) getWidgetAt(index)).isSelected = true;
        selectedIndex = index;
        defaultFocusWidget = ((Button) getWidgetAt(index));
        if (buttonSelectionChanged != null) {
            buttonSelectionChanged.actionPerformed(this);
        }
    }

}
