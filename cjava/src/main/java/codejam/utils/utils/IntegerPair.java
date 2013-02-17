package codejam.utils.utils;

public class IntegerPair implements Comparable<IntegerPair> {
  Integer _first, _second;

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

  public Integer first() { return _first; }
  public Integer second() { return _second; }
}
