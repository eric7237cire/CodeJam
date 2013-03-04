#include <iostream>
#include <cstdio>
#include <algorithm>
#include <cstring>
#include <string>
#include <cctype>
#include <stack>
#include <queue>
#include <vector>
#include <map>
#include <sstream>
#include <set>
#include <math.h>
using namespace std;

#define MAXN 22

int Orginal[MAXN],Rank[MAXN],Store[MAXN];
int N, X[MAXN];

void ReadOrginal() 
{
       int i;
       scanf("%d",&N);
       for(i = 0; i<N; i++)
          scanf("%d",&Orginal[i]);
}
void LIS() 
{
         int i, j, max;
             int largest = 0;
         for(i = 1; i<= N; i ++) 
        {
             max = 0;
             for(j = i-1; j >= 0; j --) 
            {
                 if(Store[i]>Store[j])
                   if(X[j] > max)
                      max = X[j];
             }
             X[i] = max+1;
             if(max>largest)
              largest = max;
          }
          printf("%d\n",largest+1 );
}

int main() 
{
       int temp, i ;
       ReadOrginal();
       while(scanf("%d",&temp) == 1) 
      {
          Store[temp] = Orginal[0];
          for(i = 1; i<N; i++) 
         {
             scanf("%d",&temp);
             Store[temp] = Orginal[i];
          }
          LIS();
       }
     return 0;
}
