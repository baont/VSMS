/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.me.ui.interfaces;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author baont
 */
public interface IWidgetPainter {
    public void paintBackGround(int x, int y, int w, int h, Graphics g);
    public void paintContent(int x, int y, int w, int h, Graphics g);
}
