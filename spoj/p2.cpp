#include <stdio.h>

int main() {
int s;
scanf("%d",&s);
while(s--) {
int r,c,x=0,p=1;
scanf("%d",&c);
do x += (r = c/(p*=5)); while(r);
printf("%d\n",x);
}
return 0;
}