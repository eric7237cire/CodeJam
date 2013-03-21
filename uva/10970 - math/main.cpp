//STARTCOMMON
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)
//STOPCOMMON

#include "stdio.h"
int main() {

	int M, N;
	

	while(2 == scanf("%d%d", &M, &N))
	{
		//each cut increases # of pieces by 1
		printf("%d\n", M*N - 1 );

		//scanf("%d", &nSeg);
		
	}
	return 0;
}
