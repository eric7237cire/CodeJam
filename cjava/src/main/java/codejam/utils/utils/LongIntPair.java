package codejam.utils.utils;

public class LongIntPair {
    
    public long _first;
    public int _second;

    public LongIntPair(long f, int s) {
      _first = f;
      _second = s;
    }

    public int compareTo(LongIntPair o) {
      if (this.first() != o.first())
      {
        return _first > o._first ? 1 : -1;
      }
      else
        return this.second() - o.second();
    }

    public long first() { return _first; }
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
