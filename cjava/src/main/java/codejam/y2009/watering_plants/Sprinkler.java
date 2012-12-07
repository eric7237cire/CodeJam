package codejam.y2009.watering_plants;

import codejam.utils.geometry.Circle;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public class Sprinkler implements Comparable<Sprinkler> {
	double r;
	Long plantsCovered;
	
	Circle circle;
	
	@Override
	public int compareTo(Sprinkler o) {
		return ComparisonChain.start().compare(r, o.r).result();
	}
	public Sprinkler(double r, long plantsCovered, Circle c) {
		super();
		Preconditions.checkArgument(plantsCovered > 0);
		this.r = r;
		this.plantsCovered = plantsCovered;
		this.circle = c;
	}
	@Override
	public String toString() {
		return "Sprinkler [r=" + r + ", plantsCovered=" + plantsCovered + "]";
	}
	
	
	
}
