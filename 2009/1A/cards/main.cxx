#include <fstream>
#include <iostream>
#include <iomanip>
#include <vector>
#include <numeric>
#include <algorithm>
#include <limits>
#include <map>
#include <set>
#include <sstream>
#include <deque>
#include <bitset>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>

#include "util.h"
#include <cstring>

using namespace std;


void do_test_case(int, ifstream& input);


typedef unsigned long long Int_t;

Int_t combinArray[41][41];

double averageArray[41][41][41];

int main(int argc, char** args)
{
    
    for(int i = 0; i <= 40; ++i) {
        for(int j = 0; j <= 40; ++j) {
            combinArray[i][j] = 0;
            
            for(int k = 0; k <= 40; ++k) {
                averageArray[i][j][k] = -1;   
            }
        }
    }
    
    //LOG_OFF();
    
  if (argc < 2) {

    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }

  ifstream input;
  input.open(args[1]);

  int T;
  input >> T;

  string dummy;
  getline(input, dummy);
  SHOW_TIME_BEGIN(g)



  for(int test_case = 0; test_case < T; ++test_case)
  {

    do_test_case(test_case, input);
  }

  SHOW_TIME_END(g)
}

typedef vector<int> VecInt;


Int_t nCk (int n, int k) {
    
    if (combinArray[n][k] != 0) {
        return combinArray[n][k];
    }
 
    if (k==0 || k==n) {
        combinArray[n][k] = 1;        
    } else {
        combinArray[n][k] = nCk(n-1, k-1) + nCk(n-1, k);   
    } 
    
    return combinArray[n][k];
}  


//C kinds of cards
//N number of cards in a set
//taken = number of cards in our possesion
double calculateAverage(const int C, const int N, const int taken) {
    //LOG_ON();
    
    if (averageArray[C][N][taken] >= 0) {
        return averageArray[C][N][taken];
    }
    
    //LOG_ON();
    LOG_STR("Calc average C= " << C << " N= " << N << " Taken " << taken);
    //LOG_OFF();
    
    
if (taken == 0) {
LOG_STR("First step, all cards count");
	averageArray[C][N][taken] = 1 + calculateAverage(C, N, N);
	return averageArray[C][N][taken];
}

    const int remaining = C - taken;

	if (remaining == 0) {
		LOG_STR("No cards remaining, avg is 0");
		
		averageArray[C][N][taken] = 0;
		return averageArray[C][N][taken];
	}


  if(taken == 0)
  {
    LOG_STR("First step, all cards count");
    return 1 + calculateAverage(C, N, N);
  }

    LOG_STR("Calc average 2 C= " << C << " N= " << N << " Taken " << taken);
    
    //Total # of combinations :  C kinds of cards, N total # of cards in a deck
    //int denom = nCk(C, N);
    
    Int_t coefSum = 0;
    int numCoef = min(remaining, N);
    vector<Int_t> coef(numCoef+1, 0);
    
    LOG(numCoef);
    for(int i = 0; i <= numCoef; ++i) {
        //LOG_ON();
        LOG_STR("coef loop Calc average C= " << C << " N= " << N << " Taken " << taken);
        //LOG(remaining);
        LOG(i);
        //LOG(taken);
        //LOG(N);
        
        coef[i] =  nCk(remaining, i) * nCk(taken, N-i);
        
        LOG(coef[i]);
        coefSum += coef[i];
        //LOG_OFF();
    }
    LOG(coef.size());
    
    const Int_t denom = coefSum; //nCk(C, N)
    //assert(denom == nCk(C, N));

    LOG(taken);
    LOG(N);
    
    double avg = 0;
    
    //i is the # of cards that are in the deck that are currently not taken
    for(int i = 1; i <= numCoef; ++i) {

        //Build decks where you add cards not currently owned (taken)
        //nCk(remaining, i) == choose i number of cards remaining
        //nCk(taken, N-i) == choose the rest of the cards from cards already owned
        //int coefTest = nCk(remaining, i) * nCk(taken, N-i);
        LOG_STR("for loop Calc average C= " << C << " N= " << N << " Taken " << taken);
        
        //coef / denom is the percentage of decks where exactly i new cards are taken 
        
        LOG_STR("Coeff i=" << i << " coef = " << coef[i] << " denom = " << denom);
	avg += (coef[i] * (calculateAverage(C, N, taken+i) + 1)  / denom );
    }

    //Special treatment for the i=0 case as its when the current state is repeated
    

//Percentage of decks where no more cards are taken * # of average decks 1
    double zeroCoef = ((double) coef[0]) / denom ;
    avg += zeroCoef;

    //coefSum += nCk(taken, N);
LOG(zeroCoef);
LOG(coefSum);
//LOG_OFF();
    assert(zeroCoef < 1);
    avg /= (1 - zeroCoef);
    
    //Explication
    
    //Let 
    //P_zero = Probability state is repeated (zero cards taken)
    //Avg_rest = Sum of Average where cards are taken (state not repeated)
    
    //Avergage = Avg_rest + P_zero (Average + 1)
    
    //The + 1 is because of the repetition
    //then algreba gives
    //Average = Avg_rest + P_zero*Average + P_zero
    //Average (1 - P_zero) = Avg_rest + P_zero
    //Average = Avg_rest + P_zero / (1-P_zero)

    averageArray[C][N][taken] = avg;
		return averageArray[C][N][taken];
	
}

void do_test_case(const int test_case, ifstream& input)
{
    SHOW_TIME_BEGIN(tc);
   // LOG_ON();
    LOG_OFF();
    
    int C, N;
    input >> C >> N;    
   
    double average = calculateAverage(C, N, 0);
    
    cout << "Case #" << (1+test_case) << ": " << average << endl;

}

