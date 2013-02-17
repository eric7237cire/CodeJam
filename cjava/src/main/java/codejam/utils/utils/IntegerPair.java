package codejam.utils.utils;

public class IntegerPair implements Comparable<IntegerPair> {
  int _first, _second;

  public IntegerPair(Integer f, Integer s) {
    _first = f;
    _second = s;
  }

  public int compareTo(IntegerPair o) {
    if (this.first() != o.first())
      return this.first() - o.first();
    else
      return this.second() - o.second();
  }

  public int first() { return _first; }
  public int second() { return _second; }
  
  public void setFirst(int f) 
  {
      _first = f;
  }
  
  public void setSecond(int s)
  {
      _second = s;
  }
  
  public void addSecond(int add)
  {
      _second += add;
  }

@Override
public String toString()
{
    return "(" + _first + ", " + _second + ")";
}
  
  
}
