package codejam.utils.geometry;



public class TriangleInt {
    //x, y
    final public PointInt p1;

    final public PointInt p2;
    final public PointInt p3;
    
    
    public TriangleInt(int x1, int y1, int x2, int y2, int x3, int y3) {
        super();
        this.p1 = new PointInt(x1,y1);
        this.p2 = new PointInt(x2,y2);
        this.p3 = new PointInt(x3,y3);
    }
    
    
    public boolean pointInTriangle(Point p) {
        if (Line.sameSide(p, p3.toPoint(), p1.toPoint(), p2.toPoint())
                && Line.sameSide(p, p2.toPoint(), p1.toPoint(), p3.toPoint())
                && Line.sameSide(p, p1.toPoint(), p2.toPoint(), p3.toPoint()))
            return true;
        else
            return false;
    }
}
