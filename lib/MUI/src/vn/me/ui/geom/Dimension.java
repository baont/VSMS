/*
 * SVN FILE: $Id$
 * IWIN TEAM
 * COPYRIGHT 2010 - 2011
 */
package vn.me.ui.geom;

/**
 * Utility class that holds a width and height that represents a dimension of 
 * a component or element
 * 
 * @author An Nguyen
 */
public class Dimension {

    public int width;
    public int height;
    
    /**
     * Creates a new instance of Dimension
     */
    public Dimension() {
    }

    /**
     * CCreates a new instance of Dimension with width and height
     * 
     * @param width the Dimension width
     * @param height the Dimension height
     */
    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return "Dimension{" + "width=" + width + ", height=" + height + '}';
    }

//    /**
//     * @inheritDoc
//     */
//    public String toString() {
//        return "width = " + width + " height = " + height;
//    }

//    /**
//     * @inheritDoc
//     */
//    public int hashCode() {
//        int hash = 3;
//        hash = 71 * hash + this.width;
//        hash = 71 * hash + this.height;
//        return hash;
//    }

    /**
     * @inheritDoc
     */
//    public boolean equals(Object o) {
//        if (o instanceof Dimension) {
//            return ((Dimension) o).width == width && ((Dimension) o).height == height;
//        }
//        return false;
//    }
//
}
