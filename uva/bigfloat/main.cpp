#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include "BigFloat.h"


int main()
{
	/*
	//SAMPLE
	// load a double into a
	BigFloat *a;
	a = new BigFloat(10.0);

	// load a double into b
	BigFloat *b;
	b = new BigFloat(3.0);

	// a = a*b
	a->divide(a,b);

	double A=10.00;
	double B=3.0000;
	A=A/B;

	printf("%E = %E\n\n", a->getdouble(), A);
	//END SAMPLE
	*/



	// --------------------------------------------------------------
	// PI TEST
	// --------------------------------------------------------------

//   infinity
//      ----
//      \     1    /   4      2      1      1    \
//pi =   |   ---- (   ---- - ---- - ---- - ----   )
//      /    16^k  \  8k+1   8k+4   8k+5   8k+6  /
//      ----
//       k=0

	BigFloat *sum;
	sum = new BigFloat(0.0);

	BigFloat *eight;
	eight = new BigFloat(8.0);
	BigFloat *six;
	six = new BigFloat(6.0);
	BigFloat *five;
	five = new BigFloat(5.0);
	BigFloat *four;
	four = new BigFloat(4.0);
	BigFloat *two;
	two = new BigFloat(2.0);
	BigFloat *one;
	one = new BigFloat(1.0);

	BigFloat *oneover16;
	oneover16 = new BigFloat(1.0/16.0);

	BigFloat *oneover16pow;
	oneover16pow = new BigFloat(16.0);


	
	BigFloat *k;
	k = new BigFloat(1.0);
	BigFloat *k1;
	k1 = new BigFloat(1.0);
	BigFloat *k4;
	k4 = new BigFloat(1.0);
	BigFloat *k5;
	k5 = new BigFloat(1.0);
	BigFloat *k6;
	k6 = new BigFloat(1.0);

	printf("\nRunning PI Test:      \n");

	for(int i=0; i<770; i++)
	{
		if(i%10==0)
			printf("  %d of 770\n", i);
		k1->set_with_double((double)i);
		k1->multiply(eight,k1);
		k4->copy(k1);
		k5->copy(k1);
		k6->copy(k1);

		k1->add(k1,one);
		k4->add(k4,four);
		k5->add(k5,five);
		k6->add(k6,six);


		k1->divide(four,k1);
		k4->divide(two,k4);
		k5->divide(one,k5);
		k6->divide(one,k6);

		k1->subtract(k1,k4);
		k1->subtract(k1,k5);
		k1->subtract(k1,k6);

		oneover16pow->multiply(oneover16pow,oneover16);

		k1->multiply(oneover16pow,k1);

		sum->add(sum,k1);

	}

	sum->test_if_pi_to_nine_hundred_digits();
	// --------------------------------------------------------------
	//  END PI TEST
	// --------------------------------------------------------------

}
