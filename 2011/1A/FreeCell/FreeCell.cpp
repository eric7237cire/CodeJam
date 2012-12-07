#include <iostream>
#include <fstream>
#include <vector>
#include <string> 
#include <boost/assert.hpp> 
using namespace std;

unsigned int gcd(unsigned int a, unsigned int b) 
{
	while (b != 0)
	{
		unsigned int t = b;
		b = a % b;
		a = t;
	}
	return a;
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

		unsigned int minDayWins = Pd / gcd100_Pd ;

		unsigned int minDayGames = minDayWins * 100 / Pd ;

		if (N >= minDayGames) 
		{
			cout << "Case #" << t << ": Possible" << endl;
		} else {
			cout << "Case #" << t << ": Broken" << endl;
		}
	}
}