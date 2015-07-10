/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsms;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import vn.me.core.BaseCanvas;
import vn.me.ui.Dialog;

/**
 *
 * @author bao
 */
public class WaitDialog extends Dialog{
    private static Image waitCircle;
    private static int circleSize;
    private static final int FRAME_NUM = 6;
    
    private long lastAnimateTime;
    private int currentFrame;
    
    static {
        try {
            waitCircle = Image.createImage("/waitCircle.png");
            circleSize = waitCircle.getHeight() / FRAME_NUM;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void paintComponent() {
        BaseCanvas.g.drawRegion(waitCircle, 0, circleSize*currentFrame, 
                circleSize, circleSize, 0, BaseCanvas.hw, BaseCanvas.hh, Graphics.HCENTER | Graphics.VCENTER);
        long l = System.currentTimeMillis();
        if (l - lastAnimateTime > 100) {
            currentFrame = (currentFrame + 1) % FRAME_NUM;
            lastAnimateTime = l;
        } 
    }
    
}
