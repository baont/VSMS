/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsms;

import vn.me.ui.Font;
import vn.me.core.BaseCanvas;
import vn.me.ui.Button;
import vn.me.ui.common.ResourceManager;

/**
 *
 * @author bao
 */
public class Item extends Button{
    private static final Font f = ResourceManager.biFontBlack;
    public String name;
    
    public Item(String text) {
        super(text);
        setFont(f, f);
        setSize(BaseCanvas.w, f.getHeight() + 8);
        this.name = text;
    }

//    public void paint() {
//        f.drawString(BaseCanvas.g, text, x, y, align);
//    }
    
}
