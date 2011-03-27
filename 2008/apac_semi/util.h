#include <time.h>
#include "tri_logger.hpp"
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

void trim(string& str);
template<typename T> bool isBetween(T a, T b, T n);

template<typename C, typename T> bool isMember(const C& aSet, const T& value);

#endif
