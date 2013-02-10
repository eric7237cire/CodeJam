package codejam.utils.geometry;

import java.util.List;

public class Polygon {
    public static double area(List<Point> points) {
        double sum = 0;
        for(int i = 0; i < points.size() - 1; ++i) {
            
            sum += points.get(i).x() * points.get(i+1).y();  
        }
        
        sum += points.get(points.size()-1).x() * points.get(0).y();
        
        for(int i = 0; i < points.size() - 1; ++i) {
            
            sum -= points.get(i+1).x() * points.get(i).y();  
        }
        
        sum -= points.get(0).x() * points.get(points.size()-1).y();
        
        return Math.abs(sum) / 2;
    }
}
