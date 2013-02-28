/*This is a dynamic programming problem with a few tricks thrown in. 
 We keep three arrays: minPails, the fewest pails needed to get to a
 given number of quarts; lastPail, the pail tacked on to a smaller
 sequence to find the current best solution for a given number of
 quarts; and nLast, the number of identical pails at the end of the current best sequence.

 We iterate through the set of pails, each time scanning from
 0 quarts to Q quarts to try to improve a sequence by seeing
 if we can achieve it using fewer pails than we've found so far.
 We basically try to add as many of the current pail as we can to
 earlier sequences in order to improve them; note that an
 improvement is not just a reduction in the number of pails
 required but also finding a set of pails which are smaller than the previously used ones.

 There's one exception, though: let's say we've got two pails
 of size 4 and 5. If we've already found out that we can get
 20 quarts with only a 4-quart pail, then when we iterate
 with the 5-quart pail minPails[25] will be set to 2 because
 achieving 20 quarts with only a 5-quart pail isn't an improvement
 over what we already have. Thus if we can't improve minPails
 with a new pail, but we can match what we already have, we'll
 overwrite its old value anyway; this way, when we remember
 that 20 quarts can also be achieved with a single 5-quart pail,
 we can use it again to get to 25 quarts with a single pail.
 Because of this overwriting, we need to remember how many times
 a pail has to be filled to get to a given capacity, so we store that in the array nLast.

 By iterating through the set of pails this way, the lists we are
 storing refer to the smallest set of pails which use the pail sizes
 we've seen most recently. In order to come up with the smallest pails
 first, then, we simply sort the pails in descending order before beginning the
 DP so that the most recently used pails are the smallest ones. After having
 gone through every pail, then, we backtrack using the nLast and lastPail
 arrays, and we are done.
 */

/*
 ID: eric7231
 PROG: milk4
 LANG: C++
 */
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi;
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi;
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int, int> ii;
typedef pair<uint, uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

#define MAXQ 20005  
#define MAXP 105  

const int INF =  numeric_limits<int>::max();

void findMinPails();
void backtrack();
int betterSequence(int a, int b);

int nQuart, nPail;

int pails[MAXP];
vi minPails;
vi lastPail;
vi nLast;
int finalSet[MAXP], nFinal;

int main() {
	FILE *fin = fopen("milk4.in", "r");
	fscanf(fin, "%d %d", &nQuart, &nPail);

	for (int i = 0; i < nPail; i++)
		fscanf(fin, "%d", &(pails[i]));
	fclose(fin);

	sort(pails, pails + nPail, greater<int>());

	findMinPails();
	backtrack();

	FILE *fout = fopen("milk4.out", "w");
	fprintf(fout, "%d", nFinal);

	FOR(i, 0, nFinal)
		fprintf(fout, " %d", finalSet[i]);
	fprintf(fout, "\n");

	return 0;
}

void findMinPails() {
	//Indicate that nothing is possible yet
	lastPail.assign(1 + nQuart, -1);
	minPails.assign(1 + nQuart, INF);
	nLast.assign(1 + nQuart, -1);

	minPails[0] = 0;

	for (int i = 0; i < nPail; i++) {
		vi tempMP( minPails );
		vi tempLP(lastPail);
		vi tempNL(nLast);

		assert(tempMP.size() == nQuart + 1);

		for (int j = pails[i]; j <= nQuart; j++) {
			int prev = j - pails[i];

			if (tempMP[prev] >= INF) {
				continue;
			}

			//case 1: try using more of this pail
			if (tempLP[prev] == pails[i]) {
				tempLP[j] = pails[i];  //last pail stays same
				tempMP[j] = tempMP[prev]; // min pails stays same
				tempNL[j] = tempNL[prev] + 1; //number of last used + 1
			}

			//case 2: try using just one of this pail
			// because we have to
			if (tempLP[prev] != pails[i]) {
				tempLP[j] = pails[i];  // change last pail
				tempMP[j] = tempMP[prev] + 1;  //min pails + 1
				tempNL[j] = 1;  //first one used
			}

			//case 3: try using just one of this pail
			/* because it's better */
			if (minPails[prev] < INF   //was possible before pail
					&& (minPails[prev] + 1 < tempMP[j]   //lower minimum
						|| (minPails[prev] + 1 == tempMP[j] // same minimum but better sequence
								&& betterSequence(prev, j - tempNL[j] * pails[i]))
						)
				)
			{
				tempLP[j] = pails[i];   //last pail
				tempMP[j] = minPails[prev] + 1;
				tempNL[j] = 1;
			}

		}

		for (int j = pails[i]; j <= nQuart; j++)
		{
			if (tempMP[j] <= minPails[j]) {
				minPails[j] = tempMP[j];
				lastPail[j] = tempLP[j];
				nLast[j] = tempNL[j];
			}
		}
	}
}

void backtrack() {
	int curr = nQuart;
	nFinal = minPails[nQuart];

	for (int j = 0; j < nFinal; j++) {
		finalSet[j] = lastPail[curr];
		curr -= lastPail[curr] * nLast[curr];
	}
}

int betterSequence(int a, int b)
//See if the sequence arriving at "a" is better than that arriving at "b"  
// Assume the two sequences have equal lengths  
		{
	while (a && b) {
		if (lastPail[a] < lastPail[b])
			return 1;
		if (lastPail[a] > lastPail[b])
			return 0;

		a -= nLast[a] * lastPail[a];
		b -= nLast[b] * lastPail[b];
	}

	if (a)
		return 0;
	return 1;
}

