package vn.me.ui.common;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import vn.me.core.BaseCanvas;

/**
 *
 * @author TamDinh
 */
public class Effects {

    public static boolean isCacheLinearGradient = true;
    public static boolean isRadialGradientCache = true;
    public static Hashtable linearGradientCache;
    public static Hashtable radialGradientCache;
    

    /**
     * Scale theo chiều cao. Chiều rộng tính dựa theo chiều cao.
     *
     * @param image
     * @param height
     * @return
     */
    public static Image scale(Image image, int height) {
        int width = (image.getWidth() * height) / image.getHeight();
        return scale(image, width, height);
    }

    /**
     * Co dãn 1 hình với kích thước width và height cho trước.
     */
    public static Image scale(Image image, int width, int height) {
        int srcWidth = image.getWidth();
        int srcHeight = image.getHeight();
        // no need to scale
        if (srcWidth == width && srcHeight == height) {
            return image;
        }
        int[] currentArray = new int[srcWidth];
        int[] destinationArray = new int[width * height];
        scaleArray(image, srcWidth, srcHeight, height, width, currentArray, destinationArray);
        return Image.createRGBImage(destinationArray, width, height, true);
    }

    private static void scaleArray(Image currentImage, int srcWidth, int srcHeight, int height, int width, int[] currentArray, int[] destinationArray) {
        // Horizontal Resize
        int yRatio = (srcHeight << 16) / height;
        int xRatio = (srcWidth << 16) / width;
        int xPos = xRatio >> 1;
        int yPos = yRatio >> 1;

        // if there is more than 16bit color there is no point in using mutable
        // images since they won't save any memory
        for (int y = 0; y
                < height; y++) {
            int srcY = yPos >> 16;
            currentArray = getRGB(currentImage, 0, srcY, srcWidth, 1);//currentImage.getRGB(currentArray, 0, 0, srcY, srcWidth, 1);
            //currentImage.getRGB(currentArray, 0, srcWidth, 0, srcY, srcWidth, 1);
            for (int x = 0; x < width; x++) {
                int srcX = xPos >> 16;
                int destPixel = x + y * width;
                if ((destPixel >= 0 && destPixel < destinationArray.length) && (srcX < currentArray.length)) {
                    destinationArray[destPixel] = currentArray[srcX];
                }
                xPos += xRatio;
            }
            yPos += yRatio;
            xPos = xRatio >> 1;
        }
    }
    //#if !iwin_lite

    public static void drawLinearGradient(Graphics graphics, int startColor, int endColor, int x, int y, int width, int height, boolean horizontal, int arcSize) {
        if (width < 0 || height < 0) {
            return;
        }
        
        if (isCacheLinearGradient || arcSize > 0) {
            Image img = findCachedGradient(linearGradientCache, startColor, endColor, 0, width, height, horizontal ? 0 : 1);
            if (img == null) {
                img = Image.createImage(width, height);
                int[] rgb0 = null;
                if (arcSize > 0) {
                    img.getGraphics().setColor(0);
                    img.getGraphics().fillRoundRect(0, 0, width, height, arcSize, arcSize);
                    rgb0 = getRGB(img);
                }
                fillLinearGradient(img.getGraphics(), startColor, endColor, 0, 0, width, height, horizontal);
                // Transperent mau trang neu arcSize > 0
                if (arcSize > 0 && rgb0 != null) {
                    int[] rgb = getRGB(img);
                    // drop alpha component (make it transparent) on pixels that are still at default color
                    for (int iter = 0; iter < rgb.length; iter++) {
                        if (rgb0[0] == rgb[iter] || rgb0[iter] == 0xffffffff || rgb0[iter] == 0xfff8fcf8) {
                            rgb[iter] &= 0x00ffffff;
                        }
                    }
                    img = Image.createRGBImage(rgb, width, height, true);
                }
                if (linearGradientCache == null) {
                    linearGradientCache = new Hashtable();
                }
                storeCachedGradient(img, linearGradientCache, startColor, endColor, 0, width, height, horizontal ? 0 : 1);
            }
            graphics.drawImage(img, x, y, BaseCanvas.TOPLEFT);
        } else {
            fillLinearGradient(graphics, startColor, endColor, x, y, width, height, horizontal);
        }
    }

    public static void paintSeparator(int x, int y, int w, boolean isHorizontal) {
        Effects.drawLinearGradient(BaseCanvas.g, 0x4b779a, 0xffffffff,
                x,
                y,
                isHorizontal ? w >> 1 : 1, isHorizontal ? 1 : w >> 1, true);
        Effects.drawLinearGradient(BaseCanvas.g, 0xffffffff, 0x4b779a,
                x + (w >> 1),
                y,
                isHorizontal ? w >> 1 : 1, isHorizontal ? 1 : w >> 1, true);
    }

    public static void drawLinearGradient(Graphics graphics, int startColor, int endColor, int x, int y, int width, int height, boolean horizontal) {
        drawLinearGradient(graphics, startColor, endColor, x, y, width, height, horizontal, 0);
    }

    public static void drawLinearGradientBorder(Graphics graphics, int startColor, int endColor, int x, int y, int width, int height, boolean horizontal, int arc_size) {
        drawLinearGradient(graphics, startColor, endColor, x, y, width, height, horizontal, arc_size);
    }

    /**
     * Draws a radial gradient in the given coordinates with the given colors,
     * doesn't take alpha into consideration when drawing the gradient. Notice
     * that a radial gradient will result in a circular shape, to create a
     * square use fillRect or draw a larger shape and clip to the appropriate
     * size.
     *
     * @param graphics the graphics context
     * @param startColor the starting RGB color
     * @param endColor the ending RGB color
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the region to be filled
     * @param height the height of the region to be filled
     * @param relativeX indicates the relative position of the gradient within
     * the drawing region. Should * 100 when give to this method.
     * @param relativeY indicates the relative position of the gradient within
     * the drawing region. Should * 100 when give to this method.
     * @param relativeSize indicates the relative size of the gradient within
     * the drawing region. Should * 100 when give to this method.
     */
    public static void drawRectRadialGradient(Graphics graphics, int startColor, int endColor, int x, int y, int width, int height, int relativeX, int relativeY, int relativeW, int relativeH) {
        if (isRadialGradientCache) {
            Image img = findCachedGradient(radialGradientCache, startColor, endColor, 1, relativeW, relativeH, 0);

            if (img != null) {
                graphics.drawImage(img, x, y, BaseCanvas.TOPLEFT);
            } else {
                img = Image.createImage(width, height);
                Graphics imageGraphics = img.getGraphics();

                imageGraphics.setColor(endColor);
                imageGraphics.fillRect(0, 0, width, height);

                fillRadialGradient(imageGraphics, startColor, endColor, relativeX, relativeY, relativeW, relativeH);

                graphics.drawImage(img, x, y, BaseCanvas.TOPLEFT);
                if (radialGradientCache == null) {
                    radialGradientCache = new Hashtable();
                }
                storeCachedGradient(img, radialGradientCache, startColor, endColor, 1, relativeW, relativeH, 0);
            }
        } else {
            graphics.setColor(endColor);
            graphics.fillRect(x, y, width, height);
            fillRadialGradient(graphics, startColor, endColor, relativeX, relativeY, relativeW, relativeH);
        }
    }

    public static void fillLinearGradient(Graphics graphics, int startColor, int endColor, int x, int y, int width, int height, boolean horizontal) {
        int sourceR = startColor >> 16 & 0xff;
        int sourceG = startColor >> 8 & 0xff;
        int sourceB = startColor & 0xff;
        int destR = endColor >> 16 & 0xff;
        int destG = endColor >> 8 & 0xff;
        int destB = endColor & 0xff;
        int right = x + width;
        int bottom = y + height;
        if (horizontal) {
            for (int iter = 0; iter < width; iter++) {
                updateGradientColor(graphics, sourceR, sourceG, sourceB, destR,
                        destG, destB, width, iter);
                graphics.drawLine(x + iter, y, x + iter, bottom);
            }
        } else {
            for (int iter = 0; iter < height; iter++) {
                updateGradientColor(graphics, sourceR, sourceG, sourceB, destR,
                        destG, destB, height, iter);
                graphics.drawLine(x, y + iter, right, y + iter);
            }
        }
    }

    public static void fillRadialGradient(Graphics graphics, int startColor, int endColor, int x, int y, int width, int height) {
        int sourceR = startColor >> 16 & 0xff;
        int sourceG = startColor >> 8 & 0xff;
        int sourceB = startColor & 0xff;
        int destR = endColor >> 16 & 0xff;
        int destG = endColor >> 8 & 0xff;
        int destB = endColor & 0xff;
        //int oldColor = graphics.getColor();
        int originalHeight = height;
        while (width > 0 && height > 0) {
            updateGradientColor(graphics, sourceR, sourceG, sourceB, destR,
                    destG, destB, originalHeight, height);
            graphics.fillArc(x, y, width, height, 0, 360);
            x++;
            y++;
            width -= 2;
            height -= 2;
        }
        //graphics.setColor(oldColor);
    }

    private static void updateGradientColor(Graphics graphics, int sourceR, int sourceG, int sourceB, int destR,
            int destG, int destB, int distance, int offset) {
        //int a = calculateGraidentChannel(sourceA, destA, distance, offset);
        int r = calculateGraidentChannel(sourceR, destR, distance, offset);
        int g = calculateGraidentChannel(sourceG, destG, distance, offset);
        int b = calculateGraidentChannel(sourceB, destB, distance, offset);
        int color = /*
                 * ((a << 24) & 0xff000000) |
                 */ ((r << 16) & 0xff0000)
                | ((g << 8) & 0xff00) | (b & 0xff);
        graphics.setColor(color);
    }

    /**
     * Converts the color channel value according to the offest within the
     * distance
     */
    private static int calculateGraidentChannel(int sourceChannel, int destChannel, int distance, int offset) {
        if (sourceChannel == destChannel) {
            return sourceChannel;
        }
        int ratio = (offset << 10) / distance;

        int pos = (Math.abs(sourceChannel - destChannel) * ratio) >> 10;

        if (sourceChannel > destChannel) {
            return sourceChannel - pos;
        } else {
            return sourceChannel + pos;
        }
    }

    private static void storeCachedGradient(Image img, Hashtable cache, int startColor, int endColor, int type, int width, int height, int subType) {
        int[] key;
        key = new int[]{startColor, endColor, type, width, height, subType};
        cache.put(key, img);
    }

    private static Image findCachedGradient(Hashtable cache, int startColor, int endColor, int type, int width, int height, int subType) {
        if (cache != null) {
            Enumeration e = cache.keys();
            while (e.hasMoreElements()) {
                int[] current = (int[]) e.nextElement();
                if (current[0] == startColor
                        && current[1] == endColor
                        && current[2] == type
                        && current[3] == width
                        && current[4] == height
                        && current[5] == subType) {

                    return (Image) cache.get(current);
                }
            }
        }
        return null;
    }

    public static Image reflectionImage(Image image, int color, int addedHeight) {
        int w = image.getWidth();
        int h = image.getHeight();
        Image img;
        Graphics g;
        (g = (img = Image.createImage(w, h + addedHeight)).getGraphics()).setColor(color);
        g.fillRect(0, 0, w, h + addedHeight);
        g.drawImage(image, 0, 0, 20);
        int ai[] = new int[w];
        for (int i1 = 0; i1 < addedHeight; i1++) {
            int j1;
            if ((j1 = h - 1 - (i1 * h) / addedHeight) != -1) {
//                image.getRGB(ai, 0, w, 0, j1, w, 1);
                ai = getRGB(image, 0, j1, w, 1);
            }
            int k1 = 255 - (i1 * 255) / addedHeight;
            for (int l1 = 0; l1 < w; l1++) {
                int i2 = ai[l1] >> 24;
                int j2 = ((k1 & i2) * k1) / 255;
                ai[l1] = ai[l1] & 0xffffff;
                ai[l1] = ai[l1] | j2 << 24;
            }
            g.drawRGB(ai, 0, w, 0, h + i1, w, 1, true);
        }

        return img;
    }

    /**
     * Thay đổi màu từ một màu này sang màu khác. dùng để đổi màu font. Nếu khác
     * transparent thì chuyển sang màu mới.
     *
     * @param image
     * @param fromColor : Có thể không dùng
     * @param toColor : Màu mới.
     * @return Hình mới.
     */
    public static Image changeColor(Image image, int toColor) {
        int w = image.getWidth();
        int h = image.getHeight();
        int[] argb = getRGB(image);
//        if (((fromColor >> 24) & 0xff) > 0) {
//            for (int i = argb.length; --i >= 0;) {
//                if (argb[i] == fromColor) {
//                    argb[i] = toColor;
//                }
//            }
//        } else {
        for (int i = argb.length; --i >= 0;) {
            if (((argb[i] >> 24) & 0xff) > 0) { // Neu khac transperent color -> chuyen sang mau moi.
                argb[i] = toColor;
            }
        }
//        }

        return Image.createRGBImage(argb, w, h, true);
    }
    
    public static Image changeColor(Image image, int fromColor, int toColor) {
        int w = image.getWidth();
        int h = image.getHeight();
        int[] argb = getRGB(image);
        if (((fromColor >> 24) & 0xff) > 0) {
            for (int i = argb.length; --i >= 0;) {
                if (argb[i] == fromColor) {
                    argb[i] = toColor;
                }
            }
        } else {
            for (int i = argb.length; --i >= 0;) {
                if (((argb[i] >> 24) & 0xff) > 0) { // Neu khac transperent color -> chuyen sang mau moi.
                    argb[i] = toColor;
                }
            }
        }

        return Image.createRGBImage(argb, w, h, true);
    }


    public static Image createImageTransparent(Image img, int opaque) {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] rgb = getRGB(img);//new int[w * h];
        img.getRGB(rgb, 0, w, 0, 0, w, h);
        for (int i = 0; i < rgb.length; i++) {
            if (((rgb[i] >>> 24) & 0xff) == 255) {
                rgb[i] = (opaque << 24) | (rgb[i] & 0x00ffffff);
            }
        }
        return Image.createRGBImage(rgb, w, h, true);
    }

    public static Image createGrayImage(Image img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] rgb = getRGB(img);//new int[w * h];
        img.getRGB(rgb, 0, w, 0, 0, w, h);
        for (int x = 0; x < rgb.length; x++) {
            rgb[x] = getGray(rgb[x]);
        }
        return Image.createRGBImage(rgb, w, h, true);
    }

    private static int getGray(int c) {
        int[] p = new int[4];
        p[0] = (int) ((c & 0xFF000000) >>> 24); // Opacity level
        p[1] = (int) ((c & 0x00FF0000) >>> 16); // Red level
        p[2] = (int) ((c & 0x0000FF00) >>> 8); // Green level
        p[3] = (int) (c & 0x000000FF); // Blue level

        int nc = p[1] / 3 + p[2] / 3 + p[3] / 3;
        // a little bit brighter
        nc = nc / 2 + 127;

        p[1] = nc;
        p[2] = nc;
        p[3] = nc;
        int gc = (p[0] << 24 | p[1] << 16 | p[2] << 8 | p[3]);
        return gc;
    }

    public static Image createTransparentCircle(int w, int h, int color, int opaque) {
        Image img = Image.createImage(w, h);
        Graphics g = img.getGraphics();
        g.setColor(color);
        //   g.drawArc(0, 0, w, h, 0, 360);
        g.fillArc(0, 0, w, h, 0, 360);
        int[] rgb = getRGB(img);//new int[w * h];
//        img.getRGB(rgb, 0, w, 0, 0, w, h);
        //    drop alpha component (make it transparent) on pixels that are still at default color
        for (int i = 0; i < rgb.length; ++i) {
            if (rgb[i] == 0xffffffff) {
                rgb[i] &= 0x00ffffff;
            }
        }

        // Make it transparent
        for (int i = 0; i < rgb.length; i++) {
            if (((rgb[i] >>> 24) & 0xff) == 255) {
                rgb[i] = (rgb[i] & 0x00ffffff) | (opaque << 24);
            }
        }
        return Image.createRGBImage(rgb, w, h, true);
    }

    /**
     * Làm hình thành transparent khi là màu trắng.
     */
    public static Image makeTransparentColor(Image img) {
        int[] rgb = getRGB(img);
        // drop alpha component (make it transparent) on pixels that are still at default color
        for (int i = 0; i < rgb.length; ++i) {
            if (rgb[i] == 0xffffffff || rgb[i] == 0xfff8fcf8) {
                rgb[i] &= 0x00ffffff;
//                //#if DefaultConfiguration
//                imageData = "Có vào " + String.valueOf(rgb[i]);
//                //#endif
            }
        }
        // create a new image with the pixel data and set process alpha flag to true
        return Image.createRGBImage(rgb, img.getWidth(), img.getHeight(), true);
    }

    public static int[] getRGB(Image image) {
        return getRGB(image, 0, 0, image.getWidth(), image.getHeight());
    }

    //#endif
    /**
     * Hàm này dùng để thay thế cho các thiết bị có hàm getRGB bị lỗi.
     *
     * @param image
     * @return
     */
    public static int[] getRGB(Image image, int x, int y, int width, int height) {
        int length = width * height;
        int mask1[] = new int[length]; // Black mask
        int mask2[] = new int[length]; // White mask
        Image buff = Image.createImage(width, height);
        Graphics g = buff.getGraphics();
        g.setColor(0x000000);
        g.fillRect(0, 0, width, height);
        g.drawRegion(image, x, y, width, height, 0, 0, 0, BaseCanvas.TOPLEFT);
        buff.getRGB(mask1, 0, width, 0, 0, width, height); // Black if transparent
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, width, height);
        g.drawRegion(image, x, y, width, height, 0, 0, 0, BaseCanvas.TOPLEFT);
        buff.getRGB(mask2, 0, width, 0, 0, width, height); // White if transparent
        buff = null;
        int imageRGB[] = new int[width * height];
        image.getRGB(imageRGB, 0, width, x, y, width, height);

        for (int i = 0; i < length; i++) {
            if (((mask1[i] & 0x00FFFFFF) == 0)
                    && ((mask2[i] & 0x00FFFFFF) == 0x00FFFFFF)) {
                // Set the pixel as transparent on the white pixels of the white mask
                // and the black of the black mask
                imageRGB[i] = 0x00000000;
            }
        }
        return imageRGB;
    }

    //#if !iwin_lite
    public static Image rotateImage(Image image, int angle) {
        if (angle == 0) {
            return image;
        } else if (angle != 180 && angle != 90 && angle != 270) {
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();

        int[] rowData = new int[width];
        int[] rotatedData = new int[width * height];

        int rotatedIndex = 0;

        for (int i = 0; i < height; i++) {
            image.getRGB(rowData, 0, width, 0, i, width, 1);

            for (int j = 0; j < width; j++) {
                rotatedIndex =
                        angle == 90 ? (height - i - 1) + j * height
                        : (angle == 270 ? i + height * (width - j - 1)
                        : width * height - (i * width + j) - 1);

                rotatedData[rotatedIndex] = rowData[j];
            }
        }

        if (angle == 90 || angle == 270) {
            return Image.createRGBImage(rotatedData, height, width, true);
        } else {
            return Image.createRGBImage(rotatedData, width, height, true);
        }
    }

    public static void clearCache() {
        if (linearGradientCache != null) {
            linearGradientCache.clear();
            linearGradientCache = null;
        }
        if (radialGradientCache != null) {
            radialGradientCache.clear();
            radialGradientCache = null;
        }
    }
//#endif

    /**
     * Vẽ border cho một hình chữ nhật bất kì, dùng để vễ cho các Widget.
     *
     * @param x Toạ độ x.
     * @param y Toạ độ y.
     * @param w Chiều rộng.
     * @param h Chiều cao.
     * @param borderImageArray Mảng hình chứa 6 hình để tạo nên border theo
     * index sau: - 0 : góc trái trên. - 1 : thanh ngang. - 2 : góc phải trên. -
     * 3 : thanh dọc. - 4 : góc phải dưới. - 5 : góc trái dưới.
     */
    public static void paintBorder(int x, int y, int w, int h, Image[] borderImageArray) {

        BaseCanvas.g.translate(x, y);
        int cornerW = borderImageArray[0].getWidth();
        int cornerH = borderImageArray[0].getHeight();
        int sideW = borderImageArray[1].getWidth();
        int sideH = borderImageArray[3].getHeight();
        int time = (w - 2 * cornerW) / sideW;

        //ve 2 thanh ngang.
        for (int i = 0; i < time; i++) {
            BaseCanvas.g.drawImage(borderImageArray[1], cornerW + i * sideW, 0, 0);
            BaseCanvas.g.drawImage(borderImageArray[1], cornerW + i * sideW, h - borderImageArray[1].getHeight(), 0);
        }
        BaseCanvas.g.drawImage(borderImageArray[1], w - cornerW - sideW, 0, 0);
        BaseCanvas.g.drawImage(borderImageArray[1], w - cornerW - sideW, h - borderImageArray[1].getHeight(), 0);

        //ve 2 tahnh doc.
        time = (h - 2 * cornerH) / sideH;
        for (int i = 0; i < time; i++) {
            BaseCanvas.g.drawImage(borderImageArray[3], 0, cornerH + i * sideH, 0);
            BaseCanvas.g.drawImage(borderImageArray[3], w - cornerW, cornerH + i * sideH, 0);
        }
        BaseCanvas.g.drawImage(borderImageArray[3], 0, h - cornerH - sideH, 0);
        BaseCanvas.g.drawImage(borderImageArray[3], w - cornerW, h - cornerH - sideH, 0);


        //ve cac corner.
        BaseCanvas.g.drawImage(borderImageArray[0], 0, 0, 0);
        BaseCanvas.g.drawImage(borderImageArray[2], w - cornerW, 0, 0);
        BaseCanvas.g.drawImage(borderImageArray[4], w - cornerW, h - cornerH, 0);
        BaseCanvas.g.drawImage(borderImageArray[5], 0, h - cornerH, 0);
        BaseCanvas.g.translate(-x, -y);
    }
    
    //#if Android || BigScreen
//#      public static Hashtable buttonCatch = new Hashtable();
//#      public static Image createButtonBG(Image image, int w, int h) {
//#          Image img = getButtonBG(w, h);
//#          if (img != null) {
//#              return img;
//#          }
//#          img = Image.createImage(w, h);
//#          int[] rgbData = new int[w*h];
//#          img.getRGB(rgbData, 0, 1, 1, 1, 1, 1);
//#          int backColor = rgbData[0];
//#          
//#          Image imgHeader = null;
//#          if(h != image.getHeight()) {
//#              imgHeader = scale(Image.createImage(image, 0, 0, image.getWidth() - 1, image.getHeight(), 0), h);
//#          } else {
//#              imgHeader = Image.createImage(image, 0, 0, image.getWidth() - 1, image.getHeight(), 0);
//#          }
//#          
//#          Image imgBody = scale(Image.createImage(image, image.getWidth() - 1, 0, 1, image.getHeight(), 0), w - (imgHeader.getWidth() << 1), h);
//#          Graphics g = img.getGraphics();
//#          g.drawImage(imgHeader, 0, 0, 0);
//#  //        g.drawImage(image, 0, 0, 0);
//#          g.drawImage(imgBody, imgHeader.getWidth(), 0, 0);
//#          g.drawRegion(imgHeader, 0, 0, imgHeader.getWidth(), imgHeader.getHeight(), Sprite.TRANS_MIRROR, img.getWidth(), 0, BaseCanvas.TOPRIGHT);
//#          rgbData = new int[w*h];
//#          img.getRGB(rgbData, 0, w, 0, 0, w, h);
//#          for (int i = 0; i < rgbData.length; ++i) {
//#              if (rgbData[i] == backColor) {
//#                  rgbData[i] &= 0x00ffffff;
//#              }
//#          }
//#          img = Image.createRGBImage(rgbData, w, h,true);
//#          saveButtonImgOnCatch(img);
//#          return img;
//#      }
//#      
//#      public static void saveButtonImgOnCatch(Image img) {
//#          int[] key;
//#          key = new int[]{img.getWidth(), img.getHeight()};
//#          buttonCatch.put(key, img);
//#          
//#          
//#      }
//#      
//#      private static Image getButtonBG(int w, int h) {
//#          if (buttonCatch != null) {
//#              Enumeration e = buttonCatch.keys();
//#              while (e.hasMoreElements()) {
//#                  int[] current = (int[]) e.nextElement();
//#                  if (current[0] == w
//#                          && current[1] == h) {
//#  
//#                      return (Image) buttonCatch.get(current);
//#                  }
//#              }
//#          }
//#          return null;
//#      }
    //#endif
}
