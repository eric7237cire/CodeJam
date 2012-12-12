#include <time.h>
//#include "tri_logger.hpp"

#include <vector>
#include <map>
#include <set>
#ifndef UTIL_H
#define UTIL_H


#ifndef SHOW_TIME
#define SHOW_TIME 0
#endif

//#undef assert
//#define assert(x) ((void)0)


#if SHOW_TIME
double diffclock(clock_t clock1,clock_t clock2);
#define SHOW_TIME_BEGIN(A) clock_t begin_##A=clock();
#define SHOW_TIME_END(A) clock_t end_##A=clock(); cout << "Time elapsed: " #A << " " << double(diffclock(end_##A,begin_##A)) << " ms" << endl;
#else
#define SHOW_TIME_BEGIN(A) 
#define SHOW_TIME_END(A) 
#endif


using namespace std;


#define LOG(name) \
	do { cout << __FILE__ \
	<< __LINE__ << #name \
	<< " = " << (name) << endl; } while(false)


#define LOG(name) do{}while(false)
#define LOG_STR(name) do{}while(false)

void trim(string& str);
template<typename T> bool isBetween(T a, T b, T n);

template<typename C, typename T> bool isMember(const C& aSet, const T& value);
template<typename T> ostream& operator<<(ostream& os, const vector<T>& vect);

template<typename K, typename V> ostream& operator<<(ostream& os, const map<K, V>& m);

template<typename K, typename V> ostream& operator<<(ostream& os, const map<K, V>& m) 
{
  for(typename map<K, V>::const_iterator it = m.begin();
    it != m.end();
    ++it)
  {
    os << "Key: " << it->first << ", Value: " << it->second << endl; 
  }
  return os;  
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


template<typename T> ostream& operator<<(ostream& os, const vector<T>& vect)
{
  //os << "Size [" << vect.size() << "] " << endl;
  
  typename std::vector<T>::const_iterator it;
  
  for(it = vect.begin(); 
    it != vect.end(); ++it) {
    os << *it << endl;   
  }
  os << endl;
  
  os << "Size [" << vect.size() << "] " << endl;
  return os;
}

template<typename T> ostream& operator<<(ostream& os, const set<T>& vect)
{
  //os << "Size [" << vect.size() << "] " << endl;
  
  typename std::set<T>::const_iterator it;
  
  for(it = vect.begin(); 
    it != vect.end(); ++it) {
    os << *it << endl;   
  }
  os << endl;
  
  os << "Size [" << vect.size() << "] " << endl;
  return os;
}

#endif
