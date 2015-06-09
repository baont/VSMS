package vn.me.ui.model;

import vn.me.ui.Screen;
import vn.me.ui.common.T;
import vn.me.ui.interfaces.IActionListener;

public class Command {

    /**
     * Command id
     */
    public int id;
    /**
     * Dữ liệu kèm theo command. Trong HTML là : URL
     *
     */
    public Object datas;
    /**
     * Text se hien thi tren command
     */
    public String caption;
    /**
     * Icon hien thi ben canh text
     */
//    public Image icon;
    /**
     * Action tương tác với người dùng khi được click hoặc nhấn phím.
     */
    public IActionListener action;
    public static final IActionListener muiCommandAction = new IActionListener() {

        public void actionPerformed(Object o) {
            Command cmd = (Command) ((Object[]) o)[0];
            switch (cmd.id) {
                case -1:
                case -2:
                    Screen.currentDialog.hide();
                    break;
            }
        }
    };
    /**
     * Command dung de dong Dialog.
     */
    public static final Command cmdOK = new Command(-1, T.gL(6), muiCommandAction);
    public static final Command cmdCancel = new Command(-2, T.gL(0), muiCommandAction);

    /**
     * @param text
     * @param actionHandler
     */
    public Command(String text, IActionListener actionHandler) {
        this(-1, text, actionHandler);
    }

    /**
     * @param id
     * @param text
     * @param actionHandler : {@link IActionListener}
     */
    public Command(int id, String text, IActionListener actionHandler) {
//        caption = text;
        this(-1, text, null, actionHandler);
        this.id = id;
    }

    public Command(int id, String text, Object data, IActionListener actionHandler) {
        this.datas = data;
        this.id = id;
        caption = text;
        action = actionHandler;
    }

    /**
     * @param image : Image
     * @param actionHandler : {@link IActionListener}
     */
//    public Command(Image image, IActionListener actionHandler) {
////        icon = image;
////        action = actionHandler;
//        this(-1, null, image, actionHandler);
//    }
    /**
     * @param id
     * @param text
     * @param image
     * @param actionHandler : {@link IActionListener}
     */
//    public Command(int id, String text, Image image, IActionListener actionHandler) {
//        this.id = id;
//        caption = text;
//        icon = image;
//        action = actionHandler;
//    }
    /**
     * Xu ly action
     */
    public void actionPerformed(Object o) {
        if (action != null) {
            action.actionPerformed(o);
        }
    }
    //---------------------------------------------------
    //****************************************************
    // tam cho tuong thich Fire
//    public int getSize() {
//        return 5;
//    }
//    public boolean isMultiple;
//
//    public boolean isMultiple() {
//        return isMultiple;
//    }
//
//    public boolean isMenuCommand() {
//        return false;
//    }
}
