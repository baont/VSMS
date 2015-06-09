package vn.me.ui.geom;

public class Position {

    /** Vi tri x */
    public int x;
    /** Vi tri y*/
    public int y;
    /** Cach anchor Graphics.TOP|LEFT|BOTTOM|RIGHT khi ve tu vi tri nay.*/
    public int anchor;

    public Position() {
        x = 0;
        y = 0;
        anchor = 0;
    }

    public Position(int x, int y, int anchor) {
        this.x = x;
        this.y = y;
        this.anchor = anchor;
    }

}
