#include <stdarg.h>

#ifndef UTIL_H
#define UTIL_H

#define ERROR 1
#define INFO 0 
#define DEBUG 0
#define TRACE 0
    
  void error(const char* pMsg, ...);
  
  void info(const char* pMsg, ...);
  
  void debug(const char* pMsg, ...);
  
  void trace(const char* pMsg, ...);

  #endif
