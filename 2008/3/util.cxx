#include <stdarg.h>
#include "util.h"
#include <stdio.h>
#include <iostream>
#include <time.h>
#include <set>

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

template std::ostream& operator<<(std::ostream& os, const std::vector<int>& vect);

  void error(const char* pMsg, ...)
  {
    #if ERROR
    va_list arg;
    va_start(arg, pMsg);
    vprintf(pMsg, arg);
    va_end(arg);
     #endif 
  }
  
  void info(const char* pMsg, ...)
  {
    #if INFO
    va_list arg;
    va_start(arg, pMsg);
    vprintf(pMsg, arg);
    va_end(arg);
     #endif 
  }
  
  void debug(const char* pMsg, ...)
  {
    #if DEBUG
    va_list arg;
    va_start(arg, pMsg);
    vprintf(pMsg, arg);
    va_end(arg);
     #endif 
  }
  
  void trace(const char* pMsg, ...)
  {
    #if TRACE
    va_list arg;
    va_start(arg, pMsg);
    vprintf(pMsg, arg);
    va_end(arg);
     #endif 
  }

  
