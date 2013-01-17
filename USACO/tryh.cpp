/*
ID: yangchess1
LANG: C++
PROG: humble
*/


#include <fstream>
#include <iostream>

using namespace std;


const long long int    MAX_PRIME    = 100;
const long long int    MAX_INDEX    = 100000;
const long long int    INFINITY    = 10000000000;


int main ()
{
    ifstream in;
    ofstream out;
    long long int    chumble, cprime, primes[MAX_PRIME], hprimes[MAX_PRIME], humble[MAX_INDEX + 1], next;
    long long int    a, x;


    //Set all hprimes (index for primes search in main loop)
    for (a = 0;a < MAX_PRIME;a++)
        hprimes[a] = 0;


    //Set first humble number as 1, which is false, but for easier calcs
    humble[0] = 1;


    //Input
    in.open ("humble.in");
    in >> cprime >> chumble;
    
    for (a = 0;a < cprime;a++)
        in >> primes[a];


    in.close ();


    //Find the (a + 1)th humble number, until the (chumble + 1)th humble number, but that is the answer since we included 1 as a humble number
    for (a = 0;a < chumble;a++)
    {
        next = INFINITY;


        for (x = 0;x < cprime;x++)
        {
            //While the current product is less than the last number, increase the humble number portion
            for (;primes[x] * humble[hprimes[x]] <= humble[a];hprimes[x]++);


            if (primes[x] * humble[hprimes[x]] < next)
                next = primes[x] * humble[hprimes[x]];
        }


        humble[a + 1] = next;
    }

    for(int i = 0; i < chumble; ++i)
    {
        cout << i << ": " << humble[i] << endl;
    }

    //Output answer
    out.open ("humble.out");
    out << humble[chumble] << "\n";
    out.close ();


    return 0;
}
