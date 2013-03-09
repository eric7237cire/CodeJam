#include <iostream>
#include <cstdio>
#include <cstring>
#include <cmath>
using namespace std;
const int maxn = 1200;
//------------------------------
bool isPrime[maxn];
int Prime[maxn], f[maxn][20][197];
int n,k,m;
//------------------------------
void sieve();
void solve();
//------------------------------
int main() {
 sieve();
 while (true)
 {
  cin >> n >> k;
  if (n==0 && k==0) break;
  solve();
 }
 return 0;
}
//------------------------------
void sieve() {
 memset(isPrime,true,sizeof(isPrime));
 isPrime[1] = false;
 int i = 2;
 while (i<=int(sqrt(maxn)))
 {
  if (isPrime[i])
  {
   int j = i;
   while (j<=maxn)
   {
    j = j+i;
    isPrime[j] = false;
   }
  }
  i++;
 }
 m = 0;
 for (int i=2; i<=maxn; i++) 
  if (isPrime[i]) Prime[++m] = i;
}
//------------------------------
void solve() {
 for (int i=0; i<=n; i++)
  for (int j=0; j<=k; j++)
   for (int h=0; h<=m; h++) f[i][j][h] = 0;
 
 for (int i=1; i<=n; i++)
  if (isPrime[i])
   for (int j=1; j<=m; j++)
   {
    if (Prime[j]<i) continue;
    else if (Prime[j]>=i) f[i][1][j] = 1;
   }
   
 for (int i=1; i<=n; i++)
  for (int j=2; j<=k; j++)
   for (int h=1; h<=m; h++)
   {
    if (Prime[h]<=i) f[i][j][h] = f[i-Prime[h]][j-1][h-1]+f[i][j][h-1];
    else f[i][j][h] = f[i][j][h-1];
   }
 int i;
 for (i=m; i>=1; i--)
  if (Prime[i]<=n) break;
 cout << f[n][k][i] << endl;
}
//---