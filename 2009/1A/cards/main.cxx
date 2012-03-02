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

typedef vector<double> VecDoub;

VecDoub calcAverageResults;

int main(int argc, char** args)
{

  LOG_OFF();

  if(argc < 2)
  {
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

  calcAverageResults.resize(41*40*40, -1.0);


  for(int test_case = 0; test_case < T; ++test_case)
  {

    do_test_case(test_case, input);
  }

  SHOW_TIME_END(g)
}

typedef vector<int> VecInt;

int fact(int x)
{
  int f = 1;

  for(int i = 1; i <= x; ++i)
  {
    f *= i ;
  }

  LOG_STR("X " << x << " F " << f);
  return f;
}

int fact(const int start, const int finish)
{
  int f = 1;

  for(int i = start; i <= finish; ++i)
  {
    f *= i ;
  }

  LOG_STR("start " << start << " Finish " << finish);
  return f;
}

long long nCk(int n, int k)
{
  LOG_STR("n " << n << " k " << k);
  long long r = 0;

  if(k > (n-k))
  {
    r = fact(k+1, n) / fact(n-k);
  }
  else
  {
    r = fact(n-k+1, n) / fact(k);
  }

  LOG(r);
  return r;
}

void nCkBigNumbers(int n, int k, vector<int>& numerator, vector<int>& denom)
{
  //n! / k! (n-k) !

  for(int i = 1; i <= n; ++i)
  {
    numerator.push_back(i);
  }

  for(int i = 1; i <= k; ++i)
  {
    denom.push_back(i);
  }

  for(int i = 1; i <= (n-k); ++i)
  {
    denom.push_back(i);
  }

}

double calc(const vector<int>& numerator, const vector<int>& denom)
{
  unsigned int ni = 0;
  unsigned int di = 0;

  double r = 1;

  const int LIMIT = 20000;

  while(ni < numerator.size())
  {
    r *= numerator[ni++];

    if(r >= LIMIT && di < denom.size())
    {
      r /= denom[di];
    }
  }

  while(di < denom.size())
  {
    r /= denom[di++];
  }

  return r;
}

int countOnes(int x)
{
  bitset<10> bs(x);
  return bs.count();
}

//C # of cards
//N # of cards in a set
void gen_combinations(VecInt& list, const int N, const int C)
{
  int end = (1 << C) - 1;

  LOG(N);
  LOG(C);
  LOG(end);

  const unsigned int expectedCount = nCk(C, N);

  for(int i = 0; i <= end; ++i)
  {
    LOG_STR(i << " 1s " << countOnes(i));

    if(countOnes(i) == N)
    {
      list.push_back(i);
      LOG_STR("Adding combin " << i);
    }
  }

  LOG(list.size());

  assert(expectedCount == list.size());
}



double calculateAverage(const int C, const int N, int taken)
{
  //LOG_ON();

  LOG_STR("Calc average C= " << C << " N= " << N << " Taken " << taken);

  const int index = C * (40 * 40) + N * 40 + taken;

  if(calcAverageResults[index] > 0)
  {
    return calcAverageResults[index];
  }


  if(taken == 0)
  {
    LOG_STR("First step, all cards count");
    return 1 + calculateAverage(C, N, N);
  }


  double avg = 0;

  assert(taken <= C);

  int remaining = C - taken;

  if(remaining == 0)
  {
    LOG_STR("No cards remaining, avg is 0");

    return 0;
  }


  LOG_STR("Total combinations C= " << C << " N= " << N << " Taken " << taken);
  //    int denom = nCk(C, N);
  vector<int> totalPossibleNum;
  vector<int> totalPossibleDenom;
  nCkBigNumbers(C, N, totalPossibleNum, totalPossibleDenom);

  //i is the # of cards that are in the deck that are currently not taken
  for(int i = 1; i <= remaining && i <= N; ++i)
  {
    vector<int> coefNum;
    vector<int> coefDenom;
    nCkBigNumbers(remaining, i, coefNum, coefDenom);
    nCkBigNumbers(taken, N-i, coefNum, coefDenom);

    coefNum.insert(coefNum.end(), totalPossibleDenom.begin(), totalPossibleDenom.end());
    coefDenom.insert(coefDenom.end(), totalPossibleNum.begin(), totalPossibleNum.end());

    //LOG_STR("Coeff i=" << i << " coef = " << coef);
    double calcResult = calc(coefNum, coefDenom);

    avg += (calcResult * (calculateAverage(C, N, taken+i) + 1));
  }

  //Special treatment for the i=0 case as its when the current state is repeated

  LOG_STR("denom2 C= " << C << " N= " << N << " Taken " << taken);

  LOG(avg);
  LOG(taken);

  vector<int> coefNum;
  vector<int> coefDenom;
  nCkBigNumbers(taken, N, coefNum, coefDenom);

  coefNum.insert(coefNum.end(), totalPossibleDenom.begin(), totalPossibleDenom.end());
  coefDenom.insert(coefDenom.end(), totalPossibleNum.begin(), totalPossibleNum.end());

  double zeroCoef = calc(coefNum, coefDenom);
  avg += zeroCoef;

  LOG(zeroCoef);

  assert(zeroCoef < 1);
  avg /= (1 - zeroCoef);

  calcAverageResults[index] = avg;
  return avg;
}

void do_test_case(const int test_case, ifstream& input)
{
  SHOW_TIME_BEGIN(tc);
  LOG_ON();
  LOG_OFF();

  int C, N;
  input >> C >> N;

  double average = calculateAverage(C, N, 0);

  cout << "Case #" << (1+test_case) << ": " << average << endl;
}

