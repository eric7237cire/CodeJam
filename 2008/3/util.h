#include <stdarg.h>
#include <time.h>
#include "tri_logger.hpp"

#ifndef UTIL_H
#define UTIL_H


#define SHOW_TIME 0
#define DEBUG_OUTPUT 0
//#undef assert
//#define assert(x) ((void)0)

#if INFO
#define LOG_STR_INFO LOG_STR
#define LOG_INFO LOG
#else
#define LOG_STR_INFO(str) do{}while(false)
#define LOG_INFO(str) do{}while(false)
#endif   

#if DEBUG
#define LOG_STR_DEBUG LOG_STR
#define LOG_DEBUG LOG
#else
#define LOG_STR_DEBUG(str) do{}while(false)
#define LOG_DEBUG(str) do{}while(false)
#endif

#if SHOW_TIME
#define SHOW_TIME_BEGIN(A) clock_t begin_##A=clock();
#define SHOW_TIME_END(A) clock_t end_##A=clock(); cout << "Time elapsed: " #A << " " << double(diffclock(end_##A,begin_##A)) << " ms"<< endl;
#else
#define SHOW_TIME_BEGIN(A) 
#define SHOW_TIME_END(A) 
#endif



#include <vector>
#include <ostream>

unsigned int SetBit(unsigned int anInt, unsigned int pos);

double diffclock(clock_t clock1,clock_t clock2);

template<typename T> std::ostream& print_cont(std::ostream& os, const T& cont);

template<typename S, typename T> std::ostream& operator<<(std::ostream& os, const std::pair<S, T>& pair);
//template<typename S, typename T> std::ostream& operator<<(std::ostream& os, const std::pair<S, T>& pair);

template<typename T> std::ostream& operator<<(std::ostream& os, const std::vector<T>& vect);

  void error(const char* pMsg, ...);
  
  void info(const char* pMsg, ...);
  
  void debug(const char* pMsg, ...);
  
  void trace(const char* pMsg, ...);
  
  

  #endif
