/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsms;

import vn.me.core.BaseCanvas;
import vn.me.ui.Screen;
import vn.me.ui.WidgetGroup;
import vn.me.ui.common.LAF;
import vn.me.ui.common.ResourceManager;

/**
 *
 * @author bao
 */
public class InforScreen extends Screen {
    private final String[] infor;
    private int camY;
    private static int lineH;
    
    public InforScreen(String infor) {
        super(true);
        this.infor = ResourceManager.biFontBlack.wrap(infor, BaseCanvas.w - 4);
        camY = 0;
        lineH = ResourceManager.biFontBlack.getHeight() + 2;
    }

    public void paintBackground() {
        super.paintBackground();
        int y = LAF.LOT_TITLE_HEIGHT + 2 + camY;
        for (int i = 0; i < infor.length; i++) {
            ResourceManager.biFontBlack.drawString(BaseCanvas.g, infor[i], 2, y, 0);
            y += lineH;
        }
    }

    public boolean checkKeys(int type, int keyCode) {
        boolean isCheck =  super.checkKeys(type, keyCode);
        if (isCheck) {
            return true;
        }
        
        if (!isCheck && type == 0) {
            switch (keyCode) {
                case BaseCanvas.KEY_UP:
                    camY += lineH;
                    return true;
                case BaseCanvas.KEY_DOWN:
                    camY -= lineH;
                    return true;
            }
        } 
        return false;
    }
    
}
