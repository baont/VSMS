/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsmsdataparser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author bao
 */
public class FontDataGen {

    private static final String data = "" +
"		{char=\"!\",width=5,x=0,y=5,w=2,h=12,ox=2,oy=12},\n" +
"		{char=\"$\",width=9,x=2,y=4,w=9,h=15,ox=0,oy=13},\n" +
"		{char=\"%\",width=15,x=11,y=5,w=13,h=12,ox=1,oy=12},\n" +
"		{char=\"(\",width=6,x=24,y=5,w=4,h=15,ox=1,oy=12},\n" +
"		{char=\")\",width=5,x=28,y=5,w=5,h=15,ox=0,oy=12},\n" +
"		{char=\"*\",width=7,x=33,y=5,w=7,h=6,ox=0,oy=12},\n" +
"		{char=\"+\",width=10,x=40,y=7,w=8,h=8,ox=1,oy=10},\n" +
//"		{char=\",\",width=5,x=48,y=15,w=3,h=5,ox=1,oy=2},\n" +
"		{char=\"-\",width=7,x=51,y=11,w=5,h=3,ox=1,oy=6},\n" +
"		{char=\".\",width=5,x=56,y=15,w=2,h=2,ox=2,oy=2},\n" +
"		{char=\"/\",width=5,x=58,y=5,w=5,h=12,ox=0,oy=12},\n" +
"		{char=\"0\",width=10,x=63,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"1\",width=10,x=71,y=5,w=5,h=12,ox=2,oy=12},\n" +
"		{char=\"2\",width=9,x=76,y=5,w=9,h=12,ox=0,oy=12},\n" +
"		{char=\"3\",width=9,x=85,y=5,w=8,h=12,ox=0,oy=12},\n" +
"		{char=\"4\",width=10,x=93,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"5\",width=9,x=101,y=5,w=9,h=12,ox=0,oy=12},\n" +
"		{char=\"6\",width=10,x=110,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"7\",width=10,x=118,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"8\",width=10,x=126,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"9\",width=10,x=134,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\":\",width=4,x=142,y=8,w=2,h=9,ox=1,oy=9},\n" +
"		{char=\"<\",width=10,x=144,y=6,w=8,h=10,ox=1,oy=11},\n" +
//"		{char=\"=\",width=10,x=152,y=8,w=8,h=6,ox=1,oy=9},\n" +
"		{char=\">\",width=10,x=160,y=6,w=8,h=10,ox=1,oy=11},\n" +
"		{char=\"?\",width=9,x=168,y=5,w=9,h=12,ox=0,oy=12},\n" +
"		{char=\"A\",width=11,x=177,y=5,w=13,h=12,ox=-1,oy=12},\n" +
"		{char=\"B\",width=11,x=190,y=5,w=9,h=12,ox=1,oy=12},\n" +
"		{char=\"C\",width=12,x=199,y=5,w=11,h=12,ox=1,oy=12},\n" +
"		{char=\"D\",width=12,x=210,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"E\",width=11,x=220,y=5,w=9,h=12,ox=1,oy=12},\n" +
"		{char=\"F\",width=10,x=229,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"G\",width=13,x=237,y=5,w=11,h=12,ox=1,oy=12},\n" +
"		{char=\"H\",width=12,x=248,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"I\",width=5,x=258,y=5,w=2,h=12,ox=2,oy=12},\n" +
"		{char=\"J\",width=9,x=260,y=5,w=7,h=12,ox=1,oy=12},\n" +
"		{char=\"K\",width=11,x=267,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"L\",width=9,x=277,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"M\",width=14,x=285,y=5,w=12,h=12,ox=1,oy=12},\n" +
"		{char=\"N\",width=12,x=297,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"O\",width=13,x=307,y=5,w=12,h=12,ox=1,oy=12},\n" +
"		{char=\"P\",width=11,x=319,y=5,w=9,h=12,ox=1,oy=12},\n" +
"		{char=\"Q\",width=13,x=328,y=5,w=12,h=13,ox=1,oy=12},\n" +
"		{char=\"R\",width=12,x=340,y=5,w=11,h=12,ox=1,oy=12},\n" +
"		{char=\"S\",width=11,x=351,y=5,w=10,h=12,ox=0,oy=12},\n" +
"		{char=\"T\",width=11,x=361,y=5,w=9,h=12,ox=1,oy=12},\n" +
"		{char=\"U\",width=12,x=370,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"V\",width=11,x=380,y=5,w=11,h=12,ox=0,oy=12},\n" +
"		{char=\"W\",width=16,x=391,y=5,w=16,h=12,ox=0,oy=12},\n" +
"		{char=\"X\",width=11,x=407,y=5,w=12,h=12,ox=0,oy=12},\n" +
"		{char=\"Y\",width=11,x=419,y=5,w=12,h=12,ox=0,oy=12},\n" +
"		{char=\"Z\",width=11,x=431,y=5,w=9,h=12,ox=1,oy=12},\n" +
"		{char=\"a\",width=10,x=440,y=8,w=8,h=9,ox=1,oy=9},\n" +
"		{char=\"b\",width=9,x=448,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"c\",width=9,x=456,y=8,w=8,h=9,ox=1,oy=9},\n" +
"		{char=\"d\",width=10,x=464,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"e\",width=10,x=472,y=8,w=8,h=9,ox=1,oy=9},\n" +
"		{char=\"f\",width=6,x=480,y=5,w=5,h=12,ox=1,oy=12},\n" +
"		{char=\"g\",width=10,x=485,y=8,w=8,h=12,ox=1,oy=9},\n" +
"		{char=\"h\",width=9,x=493,y=5,w=7,h=12,ox=1,oy=12},\n" +
"		{char=\"i\",width=4,x=500,y=5,w=2,h=12,ox=1,oy=12},\n" +
"		{char=\"j\",width=4,x=502,y=5,w=4,h=15,ox=-1,oy=12},\n" +
"		{char=\"k\",width=8,x=506,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"l\",width=4,x=514,y=5,w=2,h=12,ox=1,oy=12},\n" +
"		{char=\"m\",width=14,x=516,y=8,w=12,h=9,ox=1,oy=9},\n" +
"		{char=\"n\",width=9,x=528,y=8,w=7,h=9,ox=1,oy=9},\n" +
"		{char=\"o\",width=10,x=535,y=8,w=8,h=9,ox=1,oy=9},\n" +
"		{char=\"p\",width=9,x=543,y=8,w=8,h=12,ox=1,oy=9},\n" +
"		{char=\"q\",width=10,x=551,y=8,w=8,h=12,ox=1,oy=9},\n" +
"		{char=\"r\",width=6,x=559,y=8,w=5,h=9,ox=1,oy=9},\n" +
"		{char=\"s\",width=9,x=564,y=8,w=8,h=9,ox=0,oy=9},\n" +
"		{char=\"t\",width=6,x=572,y=4,w=5,h=13,ox=1,oy=13},\n" +
"		{char=\"u\",width=9,x=577,y=8,w=7,h=9,ox=1,oy=9},\n" +
"		{char=\"v\",width=8,x=584,y=8,w=9,h=9,ox=0,oy=9},\n" +
"		{char=\"w\",width=12,x=593,y=8,w=12,h=9,ox=0,oy=9},\n" +
"		{char=\"x\",width=8,x=605,y=8,w=9,h=9,ox=0,oy=9},\n" +
"		{char=\"y\",width=8,x=614,y=8,w=9,h=12,ox=0,oy=9},\n" +
"		{char=\"z\",width=10,x=623,y=8,w=8,h=9,ox=1,oy=9},\n" +
"		{char=\"Á\",width=11,x=631,y=1,w=13,h=16,ox=-1,oy=16},\n" +
"		{char=\"Â\",width=11,x=644,y=2,w=13,h=15,ox=-1,oy=15},\n" +
"		{char=\"Ô\",width=13,x=657,y=1,w=12,h=16,ox=1,oy=16},\n" +
"		{char=\"à\",width=10,x=669,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"á\",width=10,x=677,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"â\",width=10,x=685,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"ã\",width=10,x=693,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"è\",width=10,x=701,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"é\",width=10,x=709,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"ê\",width=10,x=717,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"ì\",width=5,x=725,y=5,w=4,h=12,ox=0,oy=12},\n" +
"		{char=\"í\",width=5,x=729,y=5,w=4,h=12,ox=1,oy=12},\n" +
"		{char=\"ò\",width=10,x=733,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"ó\",width=10,x=741,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"ô\",width=10,x=749,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"õ\",width=10,x=757,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"ù\",width=9,x=765,y=5,w=7,h=12,ox=1,oy=12},\n" +
"		{char=\"ú\",width=9,x=772,y=5,w=7,h=12,ox=1,oy=12},\n" +
"		{char=\"ý\",width=8,x=779,y=5,w=9,h=15,ox=0,oy=12},\n" +
"		{char=\"Ă\",width=11,x=788,y=2,w=13,h=15,ox=-1,oy=15},\n" +
"		{char=\"ă\",width=10,x=801,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"Đ\",width=12,x=809,y=5,w=11,h=12,ox=0,oy=12},\n" +
"		{char=\"đ\",width=10,x=820,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"ĩ\",width=5,x=828,y=5,w=6,h=12,ox=0,oy=12},\n" +
"		{char=\"ũ\",width=9,x=834,y=5,w=7,h=12,ox=1,oy=12},\n" +
"		{char=\"ơ\",width=12,x=841,y=8,w=10,h=9,ox=1,oy=9},\n" +
"		{char=\"ư\",width=12,x=851,y=8,w=10,h=9,ox=1,oy=9},\n" +
"		{char=\"ạ\",width=10,x=861,y=8,w=8,h=12,ox=1,oy=9},\n" +
"		{char=\"ả\",width=10,x=869,y=3,w=8,h=14,ox=1,oy=14},\n" +
"		{char=\"ấ\",width=10,x=877,y=2,w=8,h=15,ox=1,oy=15},\n" +
"		{char=\"ầ\",width=10,x=885,y=2,w=8,h=15,ox=1,oy=15},\n" +
"		{char=\"ẩ\",width=10,x=893,y=0,w=8,h=17,ox=1,oy=17},\n" +
"		{char=\"ẫ\",width=10,x=901,y=1,w=8,h=16,ox=1,oy=16},\n" +
"		{char=\"ậ\",width=10,x=909,y=5,w=8,h=15,ox=1,oy=12},\n" +
"		{char=\"ắ\",width=10,x=917,y=1,w=8,h=16,ox=1,oy=16},\n" +
"		{char=\"ằ\",width=10,x=925,y=1,w=8,h=16,ox=1,oy=16},\n" +
"		{char=\"ẳ\",width=10,x=933,y=0,w=8,h=17,ox=1,oy=17},\n" +
"		{char=\"ẵ\",width=10,x=941,y=1,w=8,h=16,ox=1,oy=16},\n" +
"		{char=\"ặ\",width=10,x=949,y=5,w=8,h=15,ox=1,oy=12},\n" +
"		{char=\"ẹ\",width=10,x=957,y=8,w=8,h=12,ox=1,oy=9},\n" +
"		{char=\"ẻ\",width=10,x=965,y=3,w=8,h=14,ox=1,oy=14},\n" +
"		{char=\"ẽ\",width=10,x=973,y=5,w=8,h=12,ox=1,oy=12},\n" +
"		{char=\"ế\",width=10,x=981,y=2,w=8,h=15,ox=1,oy=15},\n" +
"		{char=\"ề\",width=10,x=989,y=2,w=8,h=15,ox=1,oy=15},\n" +
"		{char=\"ể\",width=10,x=997,y=0,w=8,h=17,ox=1,oy=17},\n" +
"		{char=\"ễ\",width=10,x=1005,y=1,w=8,h=16,ox=1,oy=16},\n" +
"		{char=\"ệ\",width=10,x=1013,y=5,w=8,h=15,ox=1,oy=12},\n" +
"		{char=\"ỉ\",width=4,x=1021,y=3,w=4,h=14,ox=0,oy=14},\n" +
"		{char=\"ị\",width=4,x=1025,y=5,w=2,h=15,ox=1,oy=12},\n" +
"		{char=\"ọ\",width=10,x=1027,y=8,w=8,h=12,ox=1,oy=9},\n" +
"		{char=\"ỏ\",width=10,x=1035,y=3,w=8,h=14,ox=1,oy=14},\n" +
"		{char=\"ố\",width=10,x=1043,y=2,w=8,h=15,ox=1,oy=15},\n" +
"		{char=\"ồ\",width=10,x=1051,y=2,w=8,h=15,ox=1,oy=15},\n" +
"		{char=\"ổ\",width=10,x=1059,y=0,w=8,h=17,ox=1,oy=17},\n" +
"		{char=\"ỗ\",width=10,x=1067,y=1,w=8,h=16,ox=1,oy=16},\n" +
"		{char=\"ộ\",width=10,x=1075,y=5,w=8,h=15,ox=1,oy=12},\n" +
"		{char=\"ớ\",width=12,x=1083,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"ờ\",width=12,x=1093,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"ở\",width=12,x=1103,y=3,w=10,h=14,ox=1,oy=14},\n" +
"		{char=\"ỡ\",width=12,x=1113,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"ợ\",width=12,x=1123,y=8,w=10,h=12,ox=1,oy=9},\n" +
"		{char=\"ụ\",width=9,x=1133,y=8,w=7,h=12,ox=1,oy=9},\n" +
"		{char=\"ủ\",width=9,x=1140,y=3,w=7,h=14,ox=1,oy=14},\n" +
"		{char=\"ứ\",width=12,x=1147,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"ừ\",width=12,x=1157,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"ử\",width=12,x=1167,y=3,w=10,h=14,ox=1,oy=14},\n" +
"		{char=\"ữ\",width=12,x=1177,y=5,w=10,h=12,ox=1,oy=12},\n" +
"		{char=\"ự\",width=12,x=1187,y=8,w=10,h=12,ox=1,oy=9},\n" +
"		{char=\"ỳ\",width=8,x=1197,y=5,w=9,h=15,ox=0,oy=12},\n" +
"		{char=\"ỵ\",width=9,x=1206,y=8,w=9,h=12,ox=0,oy=9},\n" +
"		{char=\"ỷ\",width=9,x=1215,y=2,w=9,h=18,ox=0,oy=15},\n" +
"		{char=\"ỹ\",width=9,x=1224,y=5,w=9,h=15,ox=0,oy=12}";

    private static List<String> extractLines(String data) {
        List<String> lines = new LinkedList<>();
        int firstBracketIndex = -1;
        int scndBracketIndex;
        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            if (ch == '{') {
                firstBracketIndex = i;
            } else if (ch == '}') {
                scndBracketIndex = i;
                String line = data.substring(firstBracketIndex + 1, scndBracketIndex);
                lines.add(line);
            }
        }
        return lines;
    }

    public static class Item {

        public char ch;
        public int width;
        public int x;
        public int y;
        public int w;
        public int h;
        public int ox;
        public int oy;
        Image image;

        public Item() {
        }

        public Item(String line) {
            try {
                String[] parts = line.split(",");
                String i = parts[0].split("=")[1];
                ch = i.charAt(1);
                width = Integer.parseInt(parts[1].split("=")[1]);
                x = Integer.parseInt(parts[2].split("=")[1]);
                y = Integer.parseInt(parts[3].split("=")[1]);
                w = Integer.parseInt(parts[4].split("=")[1]);
                h = Integer.parseInt(parts[5].split("=")[1]);
                ox = Integer.parseInt(parts[6].split("=")[1]);
                oy = Integer.parseInt(parts[7].split("=")[1]);
            } catch (Exception ex) {
                System.out.println(line);
                ex.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "Item{" + "ch=" + ch + ", width=" + width + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", ox=" + ox + ", oy=" + oy + '}';
        }

        public void setImage(Image image) {
            this.image = image;
        }
    }

    Image image;

    public static void main(String[] args) {
        List<String> lines = extractLines(data);

        List<Item> items = lines.stream().map(line -> new Item(line)).collect(Collectors.toList());
        Item phay = new Item();
        phay.ch = ',';
        phay.width = 5;
        phay.x = 48;
        phay.y = 15;
        phay.w = 3;
        phay.h = 5;
        phay.ox = 1;
        phay.oy = 2;
        items.add(phay);
        Item bang = new Item();
        bang.ch = '=';
        bang.width = 10;
        bang.x = 152;
        bang.y = 8;
        bang.w = 8;
        bang.h = 6;
        bang.ox = 1;
        bang.oy = 9;
        items.add(bang);

        JFrame editorFrame = new JFrame("Image Demo");
        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("12.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        for (Item item : items) {
            item.setImage(image.getSubimage(item.x, item.y, item.w, item.h));
        }

        Canvas canvas = new Canvas();
        canvas.setItem(items);
        editorFrame.getContentPane().add(canvas, BorderLayout.CENTER);

        editorFrame.pack();
        editorFrame.setLocationRelativeTo(null);
        editorFrame.setVisible(true);

        writeToFile(items, null, 19);
    }

    private static void writeToFile(List<Item> items, String fileName, int height) {
        int maxW = items.stream().mapToInt(item -> item.width).max().getAsInt();
        int maxOy = items.stream().mapToInt(item -> item.oy).max().getAsInt();
        int maxFooter = items.stream().mapToInt(item -> item.h - item.oy).max().getAsInt();

        BufferedImage image = new BufferedImage(maxW + 1,
                height * items.size(), BufferedImage.TYPE_4BYTE_ABGR);

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            int y = i * height;
            int xx = 1 + item.ox;
            int yy = i * height + height - maxFooter - item.oy;

            Graphics g = image.getGraphics();
            g.drawImage(item.image, xx, yy, null);
            System.out.print((item.width + 1) + ", ");
//            g.setColor(Color.red);
//            g.fillRect(0, i * height + height, image.getWidth(), 1);
        }
        System.out.println();

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.print(item.ch);
        }
        System.out.println();

        try {
            File outputfile = new File("saved.png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
