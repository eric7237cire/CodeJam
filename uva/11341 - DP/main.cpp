//STARTCOMMON
#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <cstring>

using namespace std;

#define mp make_pair 
#define pb push_back 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define all(c) (c).begin(),(c).end() 
#define MIN(a,b) ((a)<(b)?(a):(b))
#define MAX(a,b) ((a)<(b)?(b):(a))

typedef unsigned int uint;
typedef long long ll;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<bool> vb;
typedef vector<vb> vvb;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
typedef vector<ii> vii;
typedef vector<vii> vvii;


const double tolerance = 0.000002;
template<typename T> 
int cmp(T a, T b, T epsilon = tolerance)
{
	T dif = a - b;
	if (abs(dif) <= epsilon)
	{
		return 0;
	}
	
	if (dif > 0)
	{
		return 1; //a > b
	}
	
	return -1;
}  


//STOPCOMMON

// dp[i][j] =  courses 0..i ; time used 
int dp[10][101];
int examResults[10][101];

int main() {

	int T;
	scanf("%d", &T);
	
	while(T--)
	{
		memset(dp, 0, sizeof dp);
		int nExams, nHours;
		
		
		scanf("%d%d", &nExams, &nHours);
		
		FOR(i, 0, nExams) FORE(j, 1, nHours)
		{
			scanf("%d", &examResults[i][j]);
			//assert( j == 1 || examResults[i][j-1] <= examResults[i][j-1]);
		}
		
		//Fill in exam 1
		FORE(hoursSpent, 1, nHours)
			dp[0][hoursSpent] = examResults[0][hoursSpent] >= 5 ? examResults[0][hoursSpent] : 0;
			
		FOR(examIdx, 1, nExams)
		{
			FORE(totalHoursSpent, 1, nHours)
			{
				FORE(hoursSpent, 1, totalHoursSpent)
				{
					//printf("%d %d %d\n", examIdx, totalHoursSpent, hoursSpent);
					if (examResults[examIdx][hoursSpent] < 5)
						continue;
				
					int hoursLeft = totalHoursSpent - hoursSpent;
					assert(hoursLeft >= 0 && hoursLeft < 101);
					
					//Impossible to pass previous exams
					if (dp[examIdx-1][hoursLeft] == 0)
						continue;
						
					dp[examIdx][totalHoursSpent] = max( dp[examIdx][totalHoursSpent],
						dp[examIdx-1][hoursLeft] + examResults[examIdx][hoursSpent]);
		
				}
			}
		}
					
		if (dp[nExams-1][nHours] == 0)
			puts("Peter, you shouldn't have played billiard that much.");
		else
			//printf("Maximal possible average mark - %.2lf.\n", ((double)ans) / nExams);
			printf("Maximal possible average mark - %.2lf.\n", (1e-9 + dp[nExams-1][nHours]) / nExams);
		
	}
	return 0;
}
