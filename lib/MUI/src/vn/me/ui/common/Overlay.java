/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.me.ui.common;

/**
 *
 * @author Bao
 */
public abstract class Overlay {
    public int id = -1;
    public boolean isInEffect;
    public boolean overCommandBar = false;
    
    public void start() {
        isInEffect = true;
    }
    
    public abstract void paint(); 
    public abstract void update(long currentTime);
}
