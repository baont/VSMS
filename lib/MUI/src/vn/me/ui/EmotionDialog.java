package vn.me.ui;

import javax.microedition.lcdui.Image;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.LAF;
//import vn.me.ui.geom.Dimension;
//import vn.me.ui.geom.Rectangle;
import vn.me.ui.common.T;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.model.Command;

/**
 *
 * @author TamDinh
 */
public class EmotionDialog extends Dialog implements IActionListener {

    /** EditField de chen ky tu duoc chon vao*/
    public EditField ownerEditText;

    public EmotionDialog(final EditField owner) {
        super();
        this.ownerEditText = owner;
        cmdRight = new Command(-1, T.gL(0), this);//GameController.cmdCancel;
//        Dimension emoSize = new Dimension(Font.emoSize, Font.emoSize);
        cmdCenter = new Command(-2, T.gL(7), this);
        for (int i = 0; i < Font.emotions.length; i++) {
            Button btnEmotion = new Button(0);
            btnEmotion.padding = 1;//2;
            border = 1;
            btnEmotion.setMetrics(0, 0, Font.emoSize + 4, Font.emoSize + 4);
//            btnEmotion.w = Font.emoSize + 4;
//            btnEmotion.h = Font.emoSize + 4;
//            btnEmotion.setImage(Font.emotionsImage, new Rectangle(i * Font.emoSize, 0, emoSize));
//            btnEmotion.setImage(Font.emotionsImage, emoSize);
            Image img = Image.createImage(Font.emotionsImage, i * Font.emoSize, 0, Font.emoSize, Font.emoSize, 0);
            btnEmotion.setImage(img);
//            btnEmotion.setImage(Font.emotionsImage, emoSize, true);
            btnEmotion.text = Font.emotions[i];
            btnEmotion.cmdCenter = cmdCenter;
            addWidget(btnEmotion, false);
        }
        isAutoFit = true;
        columns = 5;
        setViewMode(WidgetGroup.VIEW_MODE_GRID);
        destX = x = 1;
        destY = ((Screen) BaseCanvas.getCurrentScreen()).container.h - h - LAF.LOT_ITEM_HEIGHT;
        y = ((Screen) BaseCanvas.getCurrentScreen()).container.h - LAF.LOT_ITEM_HEIGHT;
        isScrollableY = true;
        isLoop = true;
        getWidgetAt(0).requestFocus();
    }

    public void paintBackground() {
        BaseCanvas.g.setColor(LAF.CLR_BACKGROUND_DARKER);
//        BaseCanvas.g.fillRect(0, 0, BaseCanvas.w, BaseCanvas.h);
		BaseCanvas.g.fillRect(2, 2, w - 4, h - 4);
    }
    
    public void actionPerformed(Object o) {
        Command srcCmd = (Command) ((Object[]) o)[0];
        switch (srcCmd.id) {
            case -1:
                hide();
                break;
            case -2:
                hide();
                if (defaultFocusWidget == null) {
                    return;
                }
                if (ownerEditText == null) {
                    if (((Screen) BaseCanvas.getCurrentScreen()).chatEditField != null) {
                        ((Screen) BaseCanvas.getCurrentScreen()).chatEditField.isVisible = true;
                    }
                    ownerEditText = ((Screen) BaseCanvas.getCurrentScreen()).chatEditField;
                }
                ownerEditText.setText(ownerEditText.getText() + ((Button) defaultFocusWidget).text);
                ownerEditText.requestFocus();
                if (!ownerEditText.isVisible) {
                    ownerEditText.isVisible = true;
                    ((Screen) BaseCanvas.getCurrentScreen()).addWidget(ownerEditText);
                }
                break;
        }
    }
}
    
