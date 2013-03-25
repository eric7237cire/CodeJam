#include<cstdio>
#include<set>
using namespace std;
int main() {
 int n, k, val, i, num;
 long long res;
 multiset<int>::iterator it;
 multiset<int> s;
 while (scanf("%d", &n)) {
  if (n == 0) break;
  s.clear();
  res = 0;
  for (num = 0; num < n; num++) {
   scanf("%d", &k);
   for (i = 0; i < k; i++) {
    scanf("%d", &val);
    s.insert(val);
   }
   it = s.end();
   it--;
   res += *it - *s.begin();
   s.erase(it);
   s.erase(s.begin());
  }
  printf("%lld\n", res);
 }
 return 0;
}
 