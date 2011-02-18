#include <stdarg.h>
#include "util.h"
#include <stdio.h>
 
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

  
