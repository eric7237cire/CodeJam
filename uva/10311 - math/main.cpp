#include<stdio.h>

char sieve[10010];
int primes[10000], indx;

void init(){
    long i, j;

    for(i=4;i<=10000;i+=2) sieve[i]=1;
    for(primes[indx++]=2, i=3;i<=10000;i+=2){
        if(sieve[i]==0){
            primes[indx++]=i;
            for(j=i*i;j<=10000;j+=i) sieve[j]=1;
        }
    }

    sieve[1]=1;
}

int is_prime(long n){
    register int i;

    if(n<=10000) return !sieve[n];
    for(i=0;i<indx && primes[i]*primes[i]<=n ;i++){
        if(n%primes[i]==0) return 0;
    }

    return 1;
}

int main(){
    long i, j, n;

    init();
    while(scanf("%ld", &n)!=EOF){
        i=0;
        if(n>4){
            if(n&1){
                if(is_prime(n-2)) i=2;
            }
            else{
                i=n/2-1;
                if(i%2==0) i--;
                for( ; i ;i-=2){
                    if(is_prime(i) && is_prime(n-i)) break;
                }
            }
        }

        if(i>0) printf("%ld is the sum of %ld and %ld.\n", n, i, n-i);
        else printf("%ld is not the sum of two primes!\n", n);
    }

    return 0;
}