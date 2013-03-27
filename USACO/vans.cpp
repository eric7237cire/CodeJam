

/*

ID: eric7231

PROG: vans

LANG: C++

*/

#include <cstdio>

#include <cstring>

#define MAX 1001

#define MOVE 8

#define LMAX 80

#define threshold 100000000





class BigNum{

public:

friend BigNum operator+(BigNum a, BigNum b);

void print_out();

void cf(int);

BigNum();

private:

unsigned long num[LMAX];

int len;

};



BigNum ans[MAX][MOVE], res;

int N;



int main()

{

freopen("vans.in", "r", stdin);

freopen("vans.out", "w", stdout);



ans[0][2].cf(1), ans[0][6].cf(1);



scanf("%d", &N);



if(N < 2) printf("0\n");    

else

{



for(int i = 1; i < N - 1; i++)

{

ans[i][0] = ans[i - 1][2] + ans[i - 1][7];

ans[i][2] = ans[i - 1][0] + ans[i - 1][3] + ans[i - 1][5] + ans[i - 1][6];

ans[i][3] = ans[i - 1][2];

ans[i][5] = ans[i - 1][2] + ans[i - 1][7];

ans[i][6] = ans[i - 1][0] + ans[i - 1][5] + ans[i - 1][6];

ans[i][7] = ans[i - 1][2] + ans[i - 1][7];

}

res = ans[N - 2][2] + ans[N - 2][7];

res = res + res;

res.print_out();

}



return 0;

}







BigNum :: BigNum()

{

memset(num, 0, sizeof(num));

len = 1;       

}



void BigNum :: cf(int n)

{

num[0] = n;

}



void BigNum :: print_out()

{

printf("%d", num[len-1]);



for(int i=1; i<len; i++)

printf("%08d", num[len-i-1]);



putchar('\n');



}



BigNum operator+(BigNum a, BigNum b)

{

int max_len;



if(a.len > b.len)

{

max_len = a.len;

for(int i = b.len; i < max_len; i++)

b.num[i] = 0;

}

else

{

max_len = b.len;

for(int i = a.len; i < max_len; i++)

a.num[i] = 0;

}



for(int i = 0; i < max_len; i++)

a.num[i] += b.num[i];

//plus



for(int i = 0; i < max_len; i++)

if(a.num[i] >= threshold)

{

a.num[i] -= threshold;



if(i+2 > max_len)

{

a.num[i + 1] = 1;

max_len = i+2;

}

else

a.num[i + 1]++;



}

//CARRY

a.len=max_len;



return a;

}