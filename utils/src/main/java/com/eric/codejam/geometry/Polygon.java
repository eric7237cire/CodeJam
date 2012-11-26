package com.eric.codejam.geometry;

import java.util.List;

public class Polygon {
    public static double area(List<Point> points) {
        double sum = 0;
        for(int i = 0; i < points.size() - 1; ++i) {
            
            sum += points.get(i).getX() * points.get(i+1).getY();  
        }
        
        sum += points.get(points.size()-1).getX() * points.get(0).getY();
        
        for(int i = 0; i < points.size() - 1; ++i) {
            
            sum -= points.get(i+1).getX() * points.get(i).getY();  
        }
        
        sum -= points.get(0).getX() * points.get(points.size()-1).getY();
        
        return Math.abs(sum) / 2;
    }
}
