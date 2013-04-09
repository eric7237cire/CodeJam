#include <cstdio>
#include <iostream>
#include <string.h>

using namespace std;

int main()
{

while(1){

int r,c;

scanf("%d %d",&r,&c);

if(r==0 && c==0)
break;

char m[r+2][c+2];

int i,j;

/*memset(m,46,sizeof(m));*/

for(i=0;i<r+2;i++)
for(j=0;j<c+2;j++)
m[i][j]='.';

for(i=1;i<=r;i++)
for(j=1;j<=c;j++)
cin>>m[i][j];


int res=0;

for(i=1;i<=r;i++){
for(j=1;j<=c;j++){
	if(m[i][j]!='*')continue;
	          if(m[i][j-1]=='*') continue;
                  if(m[i-1][j-1]=='*') continue;
                  if(m[i-1][j]=='*') continue;
                  if(m[i-1][j+1]=='*') continue;
                  if(m[i][j+1]=='*') continue;
                  if(m[i+1][j+1]=='*') continue;
                  if(m[i+1][j]=='*') continue;
                  if(m[i+1][j-1]=='*') continue;
	res++;
}

}

printf("%d\n",res);

}

return 0;
}