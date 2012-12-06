#include <iostream>
#include <fstream>
#include <vector>
#include <string> 
#include <boost/assert.hpp> 
using namespace std;
//using namespace boost;

unsigned int gcd(unsigned int u, unsigned int v)
{
  int shift;
 
  /* GCD(0,v) == v; GCD(u,0) == u, GCD(0,0) == 0 */
  if (u == 0) return v;
  if (v == 0) return u;
 
  /* Let shift := lg K, where K is the greatest power of 2
        dividing both u and v. */
  for (shift = 0; ((u | v) & 1) == 0; ++shift) {
         u >>= 1;
         v >>= 1;
  }
 
  while ((u & 1) == 0)
    u >>= 1;
 
  /* From here on, u is always odd. */
  do {
       /* remove all factors of 2 in v -- they are not common */
       /*   note: v is not zero, so while will terminate */
       while ((v & 1) == 0)  /* Loop X */
           v >>= 1;
 
       /* Now u and v are both odd. Swap if necessary so u <= v,
          then set v = v - u (which is even). For bignums, the
          swapping is just pointer movement, and the subtraction
          can be done in-place. */
       if (u > v) {
         unsigned int t = v; v = u; u = t;}  // Swap u and v.
       v = v - u;                       // Here v >= u.
     } while (v != 0);
 
  /* restore common factors of 2 */
  return u << shift;
}

int main(int argc, char** args)
{
	int T;
	cin >> T;
	for(int t = 1; t <= T; ++t) 
	{
		unsigned long long N;
		unsigned short Pg, Pd;
		cin >> N >> Pd >> Pg;
		//cout << "N: " << N << " Pd: " << Pd << " Pg: " << Pg << endl;
		if ( (Pg == 0 && Pd > 0) || (Pg == 100 && Pd != 100) ) 
		{
			cout << "Case #" << t << ": Broken" << endl;
			continue;
		}

		if (Pd == 0) 
		{
			cout << "Case #" << t << ": Possible" << endl;
			continue;
		}

		int gcd100_Pd = gcd(100,Pd);

		//cout << "GCD " << gcd100_Pd << endl;

		assert(gcd100_Pd != 0);
		unsigned int minDayWins = Pd / gcd100_Pd ;
		assert(Pd % gcd100_Pd == 0);

		assert(Pd != 0);
		unsigned int minDayGames = minDayWins * 100 / Pd ;

		//cout << "min day games " << minDayGames <<  " min day wins " << minDayWins << " pd " << Pd << " Pg " << Pg << endl;

		if (N >= minDayGames) 
		{
			cout << "Case #" << t << ": Possible" << endl;
		} else {
			cout << "Case #" << t << ": Broken" << endl;
		}
	}
}