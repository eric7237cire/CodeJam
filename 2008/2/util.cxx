#include <stdarg.h>
#include "util.h"
#include <stdio.h>
 
#include <time.h>

double diffclock(clock_t clock1,clock_t clock2)
{
	double diffticks=clock1-clock2;
	double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
	return diffms;
} 


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

  
