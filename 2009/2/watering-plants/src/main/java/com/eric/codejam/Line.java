package com.eric.codejam;

import com.google.common.base.Preconditions;

public class Line {
    private double m;
    private double b;

    private enum Type {
        NORMAL, VERTICAL, HORIZONTAL
    }

    private Type type;

    public Line(double m, double b) {
        super();
        this.m = m;
        this.b = b;
        type = Type.NORMAL;
    }

    public Line(Point a, Point b) {
        if (DoubleComparator.compareStatic(a.getX(), b.getX()) == 0) {
            type = Type.VERTICAL;
            this.b = a.getX();
            this.m = Double.POSITIVE_INFINITY;
            return;
        }

        if (DoubleComparator.compareStatic(a.getY(), b.getY()) == 0) {
            type = Type.HORIZONTAL;
            this.b = a.getY();
            this.m = 0;
            return;
        }
        type = Type.NORMAL;
        m = (a.getY() - b.getY()) / (a.getX() - b.getX());
        this.b = a.getY() - m * a.getX();
    }

    /**
     * @return the m
     */
    public double getM() {
        return m;
    }

    /**
     * @param m the m to set
     */
    public void setM(double m) {
        this.m = m;
    }

    /**
     * @return the b
     */
    public double getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(double b) {
        this.b = b;
    }

    public Point getPointGivenX(double x) {
        Preconditions.checkState(type == Type.NORMAL);
        return new Point(x, m * x + b);

    }

    public Point getPointGivenY(double y) {
        Preconditions.checkState(type == Type.NORMAL);
        return new Point((y - b) / m, y);
    }

    public boolean onLine(Point a) {
        if (type == Type.NORMAL) {
            Point b = getPointGivenY(a.getY());
            return a.equals(b);
        } else if (type == Type.HORIZONTAL) {
            return DoubleComparator.compareStatic(a.getY(), b) == 0;
        } else if (type == Type.VERTICAL) {
            return DoubleComparator.compareStatic(a.getX(), b) == 0;
        }

        throw new IllegalStateException("huh");

    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(b);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(m);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (DoubleComparator.compareStatic(b, other.b) != 0)
			return false;
		if (DoubleComparator.compareStatic(m, other.m) != 0)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Line [m=" + m + ", b=" + b + ", type=" + type + "]";
	}
    
    
}
