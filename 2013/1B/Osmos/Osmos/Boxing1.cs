using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System;
internal struct Point : IComparable
{
    private readonly Int32 m_x, m_y;
    // Constructor to easily initialize the fields 
    public Point(Int32 x, Int32 y)
    {
        m_x = x;
        m_y = y;
    }
    // Override ToString method inherited from System.ValueType 
    public override String ToString()
    {
        // Return the point as a string 
        return String.Format("({0}, {1})", m_x, m_y);
    }
    // Implementation of type-safe CompareTo method 
    public Int32 CompareTo(Point other)
    {
        // Use the Pythagorean Theorem to calculate 
        // which point is farther from the origin (0, 0) 
        return Math.Sign(Math.Sqrt(m_x * m_x + m_y * m_y)
        - Math.Sqrt(other.m_x * other.m_x + other.m_y * other.m_y));
    }
    // Implementation of IComparable's CompareTo method 
    public Int32 CompareTo(Object o)
    {
        if (GetType() != o.GetType())
        {
            throw new ArgumentException("o is not a Point");
        }
        // Call type-safe CompareTo method 
        return CompareTo((Point)o);
    }
}
public static class Program
{
    public static void Main3()
    {
        // Create two Point instances on the stack. 
        Point p1 = new Point(10, 10);
        Point p2 = new Point(20, 20);
        // p1 does NOT get boxed to call ToString (a virtual method). 
        Console.WriteLine(p1.ToString());// "(10, 10)" 
        // p DOES get boxed to call GetType (a non-virtual method). 
        Console.WriteLine(p1.GetType());// "Point" 
        // p1 does NOT get boxed to call CompareTo. 
        // p2 does NOT get boxed because CompareTo(Point) is called. 
        Console.WriteLine(p1.CompareTo(p2));// "-1" 
        // p1 DOES get boxed, and the reference is placed in c. 
        IComparable c = p1;
        Console.WriteLine(c.GetType());// "Point" 
        // p1 does NOT get boxed to call CompareTo. 
        // Since CompareTo is not being passed a Point variable, 
        // CompareTo(Object) is called which requires a reference to 
        // a boxed Point. 
        // c does NOT get boxed because it already refers to a boxed Point. 
        Console.WriteLine(p1.CompareTo(c));// "0" 
        // c does NOT get boxed because it already refers to a boxed Point. 
        // p2 does get boxed because CompareTo(Object) is called. 
        Console.WriteLine(c.CompareTo(p2));// "-1" 
        // c is unboxed, and fields are copied into p2. 
        p2 = (Point)c;
        // Proves that the fields got copied into p2. 
        Console.WriteLine(p2.ToString());// "(10, 10)" 
    }
}
