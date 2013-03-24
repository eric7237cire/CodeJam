/////////////////////////////////
// 00834 - Continued Fractions
/////////////////////////////////
#include<cstdio>
#include<cmath>
#define EPS 1e-9
unsigned int a,b,k;
int main(void){
    while(scanf("%u %u\n",&a,&b)==2){
        k = a/b;
        printf("[%u",k);
        a -= (k*b);
        if(a){
			//printf("outer loop a=%u b=%u\n", a, b);
			//swaps a and b
            a ^= b ^= a ^= b;
			//printf("outer loop a=%u b=%u\n", a, b);
            k = a/b;
			
            printf(";%u",k);
            a -= k*b;
			//printf("\nstart inner look k=%u a=%u b=%u\n", k, a, b);
            while(a){
                a ^= b ^= a ^= b;
                k = a/b;
                printf(",%u",k);
                a -= k*b;
            }
        }
        printf("]\n");
    }
    return 0;
}