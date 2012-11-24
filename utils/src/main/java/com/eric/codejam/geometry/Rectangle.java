package com.eric.codejam.geometry;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class Rectangle {
    public final int x1, y1, x2, y2;

    public Rectangle(int x1, int y1, int x2, int y2) {
        super();
        Preconditions.checkArgument(x2 >= x1);
        Preconditions.checkArgument(y2 >= y1);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public String toString() {
        return "Rectangle [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2="
                + y2 + "]";
    }
    
    public boolean touches(Rectangle r2) {
        Range<Integer> r1_x = Ranges.closed(x1, x2);
        Range<Integer> r1_x_ex = Ranges.closed(x1-1, x2+1);
        Range<Integer> r2_x = Ranges.closed(r2.x1, r2.x2);
        Range<Integer> r1_y = Ranges.closed(y1, y2);
        Range<Integer> r1_y_ex = Ranges.closed(y1-1, y2+1);
        Range<Integer> r2_y = Ranges.closed(r2.y1, r2.y2);
        
        return ( r1_x_ex.isConnected(r2_x) && r1_y.isConnected(r2_y) )
                || ( r1_x.isConnected(r2_x) && r1_y_ex.isConnected(r2_y) );
    }
    
}