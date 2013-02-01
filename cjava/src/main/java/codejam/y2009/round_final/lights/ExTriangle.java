package codejam.y2009.round_final.lights;

import java.util.Arrays;

import codejam.utils.geometry.Circle;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.Polygon;
import codejam.utils.geometry.Triangle;

import com.google.common.base.Preconditions;

public class ExTriangle  extends Triangle {
    Circle intCircle;
    
    public ExTriangle(Circle intCircle, Point p1, Point p2, Point p3) {
        //p1 is light
        super(p1,p2,p3);
        this.intCircle = intCircle;
        
        Preconditions.checkArgument(p1 != null);
        Preconditions.checkArgument(p2 != null);
        Preconditions.checkArgument(p3 != null);
        
        Preconditions.checkArgument(intCircle == null || intCircle.onCircle(p2));
        Preconditions.checkArgument(intCircle == null || intCircle.onCircle(p3));
    }


    double getArea() {
        double tArea = Polygon.area(Arrays.asList(p1,p2,p3));
        
        if (intCircle != null) {
            double segDist = p2.distance(p3);
            
            double segArea = intCircle.findSegmentArea(segDist);
            
            return tArea - segArea;
        }
        
        return tArea;
        
        
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ExTriangle [intCircle=" + intCircle +
        ", p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + "]";
    }
}