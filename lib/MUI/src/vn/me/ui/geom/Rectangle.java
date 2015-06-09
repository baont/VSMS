/*
 * SVN FILE: $Id$
 * IWIN TEAM
 * COPYRIGHT 2010 - 2011
 */
package vn.me.ui.geom;

/**
 * Represents a Rectangle position (x, y) and {@link Dimension} (width, height),
 * this is useful for measuring coordinates within the application.
 * 
 * @author An Nguyen
 */
public class Rectangle {

    public int x;
    public int y;
    public Dimension size;

    /**
     * Creates a new instance of Rectangle at position (x, y) and with 
     * predefine dimension
     * 
     * @param x the x coordinate of the rectangle
     * @param y the y coordinate of the rectangle
     * @param size the {@link Dimension} of the rectangle
     */
    public Rectangle(int x, int y, Dimension size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    /**
     * Creates a new instance of Rectangle at position (x, y) and with 
     * predefine width and height
     * 
     * @param x the x coordinate of the rectangle
     * @param y the y coordinate of the rectangle
     * @param w the width of the rectangle
     * @param h the height of the rectangle
     */
    public Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.size = new Dimension(w, h);
    }

    /**
     * Get width.
     * @return the width of the rectangle
     */
    public int getWidth() {
        return size.width;
    }

    /**
     * Get height.
     * @return the height of the rectangle
     */
    public int getHeight() {
        return size.height;
    }

    /**
     * Checks whether or not this Rectangle contains the point at the specified
     * location (rX, rY).
     *
     * @param rX the specified x coordinate
     * @param rY the specified y coordinate
     * @return true if the point (rX, rY) is inside this Rectangle;
     * false otherwise.
     */
    public boolean contains(int rX, int rY) {
        return x <= rX && y <= rY && x + size.width >= rX
                && y + size.height >= rY;
    }

    public boolean intersect(int x, int y, int w, int h) {
        boolean xOverlap = valueInRange(this.x, x, x + w)
                || valueInRange(x, this.x, this.x + this.size.width);

        boolean yOverlap = valueInRange(this.y, y, y + h)
                || valueInRange(y, this.y, this.y + this.size.height);

        return xOverlap && yOverlap;
    }

//////////Code cua mGo.
//	public boolean contains(int rX, int rY) {
//        return x <= rX && y <= rY && x + size.width - 1>= rX
//                && y + size.height - 1>= rY;
//    }

//    public boolean intersect(int x, int y, int w, int h) {
//        boolean xOverlap = valueInRange(this.x, x, x + w - 1)
//                || valueInRange(x, this.x, this.x + this.size.width - 1);

//        boolean yOverlap = valueInRange(this.y, y, y + h - 1)
//                || valueInRange(y, this.y, this.y + this.size.height - 1);

//        return xOverlap && yOverlap;
//    }

    public boolean intersect(Rectangle rect) {
        return intersect(rect.x, rect.y, rect.getWidth(), rect.getHeight());
    }

    public static boolean valueInRange(int x, int x1, int x2) {
        return x >= x1 && x <= x2;
    }

    public static boolean intersect(int x1, int y1, int w1, int h1,
            int x2, int y2, int w2, int h2) {
        boolean xOverlap = valueInRange(x1, x2, x2 + w2)
                || valueInRange(x2, x1, x1 + w1);

        boolean yOverlap = valueInRange(y1, y2, y2 + h2)
                || valueInRange(y2, y1, y1 + h1);

        return xOverlap && yOverlap;
    }

    public String toString() {
        return "Rectangle{" + "x=" + x + ", y=" + y + ", size=" + size + '}';
    }
}
