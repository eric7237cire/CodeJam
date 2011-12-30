#include <stdarg.h>
#include "util.h"
#include <stdio.h>
#include <iostream>
#include <time.h>
#include <set>
#include <map>

using namespace std;

unsigned int SetBit(unsigned int anInt, unsigned int pos)
{
  return anInt | (1 << (pos - 1));
}




double diffclock(clock_t clock1,clock_t clock2)
{
	double diffticks=clock1-clock2;
	double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
	return diffms;
} 

template<typename T> ostream& print_cont(ostream& os, const T& cont)
{
  cout << "Size [" << cont.size() << "] " ;
  
  typename T::const_iterator it;
  
  for(it = cont.begin(); 
    it != cont.end(); ++it) {
    cout << *it << ", ";   
  }
  cout << endl;
  return os;
}

template ostream& print_cont(ostream& os, const set<pair<int, int> >& cont);

template<typename S, typename T> std::ostream& operator<<(std::ostream& os, const std::pair<S, T>& pair)
{
  os << "First " << pair.first << ", Second " << pair.second << endl;
  return os;  
}

template std::ostream& operator<<(std::ostream& os, const std::pair<int, int>& pair);

template<typename T> ostream& operator<<(ostream& os, const deque<T>& vect)
{
  cout << "Size [" << vect.size() << "] " ;
  
  typename std::deque<T>::const_iterator it;
  
  for(it = vect.begin(); 
    it != vect.end(); ++it) {
    cout << *it << ", ";   
  }
  cout << endl;
  return os;
}


template<typename T> ostream& operator<<(ostream& os, const vector<T>& vect)
{
  cout << "Size [" << vect.size() << "] " ;
  
  typename std::vector<T>::const_iterator it;
  
  for(it = vect.begin(); 
    it != vect.end(); ++it) {
    cout << *it << ", ";   
  }
  cout << endl;
  return os;
}

template<typename T> ostream& operator<<(ostream& os, const set<T>& vect)
{
  cout << "Size [" << vect.size() << "] " ;
  
  typename std::set<T>::const_iterator it;
  
  for(it = vect.begin(); 
    it != vect.end(); ++it) {
    cout << *it << ", ";   
  }
  cout << endl;
  return os;
}

template<typename K, typename V> ostream& operator<<(ostream& os, const map<K, V>& map)
{
  cout << "Size [" << map.size() << "] " ;
  
  typename std::map<K, V>::const_iterator it;
  
  for(it = map.begin(); 
    it != map.end(); ++it) {
    cout << "[" << it->first << ", " << it->second << "] ";   
  }
  cout << endl;
  return os;
}

template std::ostream& operator<<(std::ostream& os, const std::vector<int>& vect);

template std::ostream& operator<<(std::ostream& os, const std::set<int>& vect);

template std::ostream& operator<<(std::ostream& os, const std::map<int, set<int> >& vect);

  

template std::ostream& operator<<(ostream& os, const deque<int>& vect);
