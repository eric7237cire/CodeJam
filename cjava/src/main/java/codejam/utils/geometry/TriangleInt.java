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
    
    
}
