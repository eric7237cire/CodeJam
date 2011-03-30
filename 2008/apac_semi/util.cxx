#include <stdarg.h>
#include "util.h"
#include <stdio.h>
#include <iostream>
#include <time.h>
#include <set>
#include <stdarg.h>


#include <vector>
#include <ostream>
#include <set>


using namespace std;

unsigned int SetBit(unsigned int anInt, unsigned int pos)
{
  return anInt | (1 << (pos - 1));
}

void trim(string& str)
{
  string::size_type pos = str.find_last_not_of(' ');
  if(pos != string::npos) {
    str.erase(pos + 1);
    pos = str.find_first_not_of(' ');
    if(pos != string::npos) str.erase(0, pos);
  }
  else str.erase(str.begin(), str.end());
}


unsigned int SetBit(unsigned int anInt, unsigned int pos);

double diffclock(clock_t clock1,clock_t clock2);

template<typename T> std::ostream& print_cont(std::ostream& os, const T& cont);

template<typename S, typename T> std::ostream& operator<<(std::ostream& os, const std::pair<S, T>& pair);
//template<typename S, typename T> std::ostream& operator<<(std::ostream& os, const std::pair<S, T>& pair);

template<typename T> std::ostream& operator<<(std::ostream& os, const std::vector<T>& vect);

template<typename C, typename T> bool isMember(const C& aSet, const T& value)
{
  return aSet.find(value) != aSet.end();
};

typedef unsigned int uint;
template bool isMember(const set<pair<uint, uint> >& aSet, const pair<uint, uint>& value);
template bool isMember(const set<pair<int, int> >& aSet, const pair<int, int>& value);

template<typename T> bool isBetween(T a, T b, T n)
{
  if (n >= a && n <= b) {
    return true;
  }
  return false;
}


template bool isBetween(uint a, uint b, uint n);
template bool isBetween(int a, int b, int n);

template<typename C, typename T> bool remove(C& aSet, const T& value)
{
  typename C::iterator it = aSet.find(value);
  if (it == aSet.end()) {
    return false;
  }
  
  aSet.erase(it);
  return true;
}

template<typename T> bool removeAll(set<T>& aSet, const set<T>& aSetToRemove)
{
  typename set<T>::const_iterator it = aSetToRemove.begin();
  
  for( ; it != aSetToRemove.end(); ++it)
  {
    remove(aSet, *it);
  }
  
  return true;
}

double diffclock(clock_t clock1,clock_t clock2)
{
	double diffticks=clock1-clock2;
	double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
	return diffms;
} 

template<typename T> ostream& print_cont(ostream& os, const T& cont)
{
  os << "Size [" << cont.size() << "] " ;
  
  typename T::const_iterator it;
  
  for(it = cont.begin(); 
    it != cont.end(); ++it) {
    os << *it << ", ";   
  }
  os << endl;
  return os;
}

template ostream& print_cont(ostream& os, const set<pair<int, int> >& cont);

template<typename S, typename T> std::ostream& operator<<(std::ostream& os, const std::pair<S, T>& pair)
{
  os << "First " << pair.first << ", Second " << pair.second << endl;
  return os;  
}

template std::ostream& operator<<(std::ostream& os, const std::pair<int, int>& pair);



template std::ostream& operator<<(std::ostream& os, const std::vector<int>& vect);
template ostream& operator<<(ostream& os, const vector<double>& vect);
 



