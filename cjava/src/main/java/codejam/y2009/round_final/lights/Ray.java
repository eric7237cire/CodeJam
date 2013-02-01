package codejam.y2009.round_final.lights;

import codejam.utils.geometry.Circle;
import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.utils.DoubleFormat;

public  class Ray
{
    Line line;
    double ang;
    Point light;
    
    
    Circle pillar;
    /**
     * There are 2 rays to each pillar, first means that it comes
     * first in the ordering
     */
    boolean first; 
    
    /**
     * What the ray hits afterwards
     */
    Circle circleBehind;
    Point pointBehind;
    double distBehind;
    
    int cornerIdx;

    /**
     * 
     * @param line Point 1 is the light, Point 2 is the end of the ray
     * @param light
     * @param pillar
     */
    public Ray(Line line, Point light, Circle pillar) {
        super();
        this.line = line;
        this.ang = line.getVector().polarAngle();
        this.light = light;
        this.pillar = pillar;
        
        this.distBehind = Double.MAX_VALUE;
    }
    
    public Ray(Point light, int cornerIndex) {
        super();
        this.line = new Line(light, Main.corners[cornerIndex]);
        this.cornerIdx = cornerIndex;
        this.ang = line.getVector().polarAngle();
        this.light = light;
        this.pillar = null;
    }

    @Override
    public String toString()
    {
        return "Ray [line=" + line + ", ang=" + 
    DoubleFormat.df6.format(ang) + ", First?=" + first + ", pillar=" + pillar + " crc behind " + circleBehind + " point behind " + pointBehind;
    }
    
    
}