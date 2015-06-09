package vn.me.ui;

import vn.me.core.BaseCanvas;
import vn.me.ui.common.LAF;
import vn.me.ui.common.T;
import vn.me.ui.model.Command;

public class Dialog extends WidgetGroup {
    public boolean useAppearAnimation = true;
    public boolean isEnd;
    /**
     * Widget trong container đang focus trước khi dialog showMenu. Khi đóng dialog phải focus lại.
     */
    public Widget focusedWid;
    /**
     * Dung de hien thi lien man hinh.
     */
    public boolean isModal = false;
    /**
     * Dung de hien thi lien man hinh.
     */
    public boolean isMultiDlg = false;
    
    /**
     * Khởi tạo Dialog với x = y = w = h = 0; 
     */
    public Dialog() {
        this(0, 0, 0, 0);
    }

    /**
     * Khởi tạo Dialog với x,y,w,h có sẳn; 
     * @param x : int - Tọa độ x 
     * @param y : int - Tọa độ y 
     * @param w : int - Chiều dài 
     * @param h : int - Chiều cao 
     * 
     */
    public Dialog(int x, int y, int w, int h) {
        super(x, y, w, h);
        isLoop = true;
        border = 3;
    }

    /**
     * Hiển thị một dialog.
     * @param dialogMustBeClosed : Có đóng dialog nào không. Nếu bằng null là không đóng.
     */
    public void show(Dialog dialogMustBeClosed) {
        if (dialogMustBeClosed != null) {
            ((Screen) BaseCanvas.getCurrentScreen()).hideDialog(dialogMustBeClosed);
        } else {
            ((Screen) BaseCanvas.getCurrentScreen()).showDialog(this);
        }
    }

    /**
     * Hiển thị một dialog.
     * @param dialogMustBeClosed : Có đóng dialog nào không. Nếu bằng null là không đóng.
     */
    public void show(boolean isCloseLastDialog) {
        if (isCloseLastDialog && Screen.currentDialog != null && !Screen.currentDialog.isMultiDlg) {
            ((Screen) BaseCanvas.currentScreen).hideDialog();
        }
        ((Screen) BaseCanvas.currentScreen).showDialog(this);
    }

    /**
     * Hide một dialog.
     */
    public void hide() {
        ((Screen) BaseCanvas.currentScreen).hideDialog(this);
    }

    /**
     * Vẽ border.
     */
    protected void paintBorder() {
        super.paintBorder(); // Paint Scrollbar.
        LAF.paintDialogBorder(0, 0, w, h);
    }
   
    /**
     * Vẽ Background 
     */
    public void paintBackground() {
        LAF.paintDialogBackground(this);
    }

    public void update() {
        super.update();
        if (useAppearAnimation) {
            if (x >= BaseCanvas.w) {
                isEnd = true;
            }
        }
    }
    
    /**
     * Check key
     * @param type : int 
     * @param keyCode : int - keyCode 
     */
    public boolean checkKeys(int type, int keyCode) {
        super.checkKeys(type, keyCode);
        return true;
    }

    /**
     * Hien thi 1 dialog thong bao gom co Noi dung thong bao,  {@link  Command} left,  {@link  Command} center,  {@link  Command} right
     * @param info : {@link String} 
     * @param left :  {@link  Command} - cmdLeft
     * @param center :  {@link  Command} - cmdCenter
     * @param right :  {@link  Command} - cmdRight
     */
    public static void showMessageDialog(String info, Command left, Command center, Command right) {
        showMessageDialog(info, left, center, right, false);
    }

    /**
     * Hien thi 1 dialog thong bao gom co Noi dung thong bao,  {@link  Command} left,  {@link  Command} center,  {@link  Command} right
     * @param info : {@link String} 
     * @param left :  {@link  Command} - cmdLeft
     * @param center :  {@link  Command} - cmdCenter
     * @param right :  {@link  Command} - cmdRight
     * @param isHideCurrentDlg :  {@link  boolean} - Co dong dialg truoc do hay khong
     */
    public static void showMessageDialog(String info, Command left, Command center, Command right, boolean isHideCurrentDlg) {
        new MessageDialog(info, left, center, right, 0).show(isHideCurrentDlg);
    }

    /**
     * Hien thi 1 dialog thong bao Loi gom co Noi dung thong bao,  {@link  Command} left,  {@link  Command} center,  {@link  Command} right
     * @param info : {@link String} 
     * @param left :  {@link  Command} - cmdLeft
     * @param center :  {@link  Command} - cmdCenter
     * @param right :  {@link  Command} - cmdRight
     * @param isHideCurrentDlg :  {@link  boolean} - Co dong dialg truoc do hay khong
     */
    public static void showErrorDialog(String info, Command left, Command center, Command right, boolean isHideCurrentDlg) {
        new MessageDialog(info, left, center, right, 1).show(isHideCurrentDlg);
    }
    
    public static void showErrorDialog(String info, Command center, boolean isHideCurrentDlg) {
        new MessageDialog(info, null, center, null, 1).show(isHideCurrentDlg);
    }
    
    public static void startOKDlg(String info) {
        startOKDlg(info, false);
    }

    public static void startOKDlg(String info, boolean isCloseCurrentDialog) {
        Dialog.showMessageDialog(info, null, Command.cmdOK, null, isCloseCurrentDialog);
    }
    
    public static void startWaitDialog(String info) {
        startWaitDialog(info, false);
    }

    public static void startWaitDialog() {
        startWaitDialog(true);
    }

    public static void startWaitDialog(boolean isCloseCurrentDialog) {
        startWaitDialog(T.gL(22), isCloseCurrentDialog);
    }

    public static void startWaitDialog(String info, boolean isCloseCurrentDialog) {
        new MessageDialog(info, null, Command.cmdCancel, null, 2).show(isCloseCurrentDialog);
    }
    
    public static void startQuestionDialog(String info, Command cmdCenter, Command cmdL) {
        Dialog.showMessageDialog(info, null, cmdCenter, cmdL, true);
    }

    public static void startErrorDlg(String info, boolean isClosePreviousDialog) {
        Dialog.showErrorDialog(info, null, Command.cmdOK, null, isClosePreviousDialog);
    }

    public static void startErrorDlg(String info) {
        startErrorDlg(info, true);
    }
    
    public static void closeWaitDialog() {
        if ((Screen.currentDialog) != null) {
            if ((Screen.currentDialog) instanceof MessageDialog && ((MessageDialog) Screen.currentDialog).type == 2) {
                Screen.currentDialog.hide();
            }
        }
    }
    
    public static InputDialog showInputDialog(String text, Command ok, Command cancel, int type) {
        InputDialog dlg = new InputDialog(text, ok, cancel == null ? Command.cmdCancel : cancel, type);
        dlg.show(true);
        return dlg;
    }
    
    public static InputDialog showInputSerialPIN(String text, String[] labels, int[] inputType, Command ok, Command back) {
        //#if DefaultConfiguration
        InputDialog a = new InputDialog(text, labels, inputType, ok, back);
        //#else
//#         InputDialog a = new InputDialog(text, labels, inputType, ok, back);
//# 
        //#endif
        a.isMultiDlg = true;
        a.show(true);
        return a;
    }

    public void onClose() {
        if (useAppearAnimation) {
            destX = BaseCanvas.w;
        } else {
            isEnd = true;
        }
    }
}
