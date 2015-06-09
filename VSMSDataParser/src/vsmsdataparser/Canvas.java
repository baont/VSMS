/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsmsdataparser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import vsmsdataparser.FontDataGen.Item;

/**
 *
 * @author bao
 */
public class Canvas extends JPanel {

    List<Item> items;
    Map<Character, Item> map;

    public Canvas() {
        Dimension size = new Dimension(500, 500);
        setSize(size);
        setPreferredSize(size);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(items.get(1).image, 0, 0, null);
        g.drawImage(items.get(2).image, 10, 0, null);
        g.drawImage(items.get(3).image, 20, 0, null);
        g.drawImage(items.get(4).image, 30, 0, null);

        drawString(g, "Xuất, hiện=nhiều nhất trong 30 ngày Ô tô", 10, 100);
        g.setColor(Color.red);
        g.fillRect(10, 100, 200, 1);
//        drawString(g, "ư", 10, 100);
    }

    void setItem(List<FontDataGen.Item> items) {
        this.items = items;
        map = new HashMap<>(items.size());
        items.stream().forEach((item) -> {
            map.put(item.ch, item);
        });
    }

    private void drawString(Graphics g, String str, int x, int y) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == ' ') {
                x += 7;
            } else {
                Item item = map.get(ch);
                if (item != null) {
//                g.drawImage(item.image, x + item.ox, y + item.oy, null);
                    g.drawImage(item.image, x + item.ox, y - item.oy, null);
//                    g.setColor(Color.red);
//                    g.drawRect(x + item.ox, y - item.oy, item.width, item.h);
                    x += item.width;
                }
            }

        }
    }
}
