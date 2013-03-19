// ==============================================================
//
//                      Trevor Q Foley
//                      Class BigFloatDec
//
//  ------------------------------------------------------------
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// ==============================================================

#define _CRT_SECURE_NO_WARNINGS

#include "BigFloat.h"


BigFloatDec::BigFloatDec(int digits)
{
	DecimalDigits = digits;
	digits += DEC_TENS_IN_32BIT*2; // just to be safe
	size_ints = digits / DEC_TENS_IN_32BIT;

	data = (unsigned int*)malloc(sizeof(int)*size_ints);
	if(data == NULL)
		printf("ERROR could not malloc\n");

	for(int i=0; i<size_ints; i++)
		data[i]=0;
}

int BigFloatDec::set_with_string(char * decimal, int exp)
{
	// this function can be slow
	int pos=0;
	int mod;
	this->exp = exp;

	while(decimal[pos] != '\0')
	{
		if(pos/DEC_TENS_IN_32BIT > size_ints)
		{
			printf("Error over flow\n\n");
			return -2;
		}
		if((decimal[pos] < '0' || decimal[pos] > '9') && (decimal[pos] != '.'))
		{
			printf("Error only numbers and decimal points allowed\n\n");
			return -1;
		}
		if(decimal[pos] >= '0' || decimal[pos] <= '9')
		{
			mod = pos%(DEC_TENS_IN_32BIT);
			int temp=1;
			for(int i=0; i<DEC_TENS_IN_32BIT-mod-1; i++)
				temp*=10;

			if(mod == 0)
				data[(pos)/(DEC_TENS_IN_32BIT)] = temp*(decimal[pos]-'0');
			else
				data[(pos)/(DEC_TENS_IN_32BIT)] += temp*(decimal[pos]-'0');
		}
		pos++;
	}
	return 0;
}

int BigFloatDec::print(char * fname)
{
	//FILE* fh;
	//fh = fopen(fname, "w+");

	printf("Fraction Portion in Decimal:\n");

	//printf(" %d.", data[0]/(DEC_MAX/10));
	printf(" X.");
	printf("%.4d ", data[0]%((data[0]/(DEC_MAX/10))*(DEC_MAX/10))  );
	for(int i=1; i<size_ints-1; i++)
	{
		printf(" %.5d ", data[i]);
	}
	//printf("x10^ %d\n",exp);
	printf("\n");
	//fclose(fh);
	return 0;
}

int BigFloatDec::copy(class BigFloatDec * a)
{
	for(int i=0; i<size_ints; i++)
		data[i] = a->data[i];
	exp = a->exp;
	sign = a->sign;
	return 0;
}

int BigFloatDec::add(BigFloatDec *a, BigFloatDec *b)
{
	int expdiff;
	int expoffset;
	int del_a=0;
	int del_b=0;
	int t;
	int r;
	
	if(this == a) // sort of interesting case 
	{
		a = new BigFloatDec(DecimalDigits);
		a->copy(this);
		del_a = 1;
	}

	if(this == b)
	{
		b = new BigFloatDec(DecimalDigits);
		b->copy(this);
		del_b = 1;
	}

	BigFloatDec *temp;
	if(b->exp > a->exp)
	{
		temp = a;
		a=b;
		b=temp;
	}
	copy(a);// a is biggest, copy it to THIS

	expoffset = exp-b->exp;
	expdiff = expoffset%DEC_TENS_IN_32BIT;
	expoffset /= DEC_TENS_IN_32BIT;

	//printf("ADD\n");
	//printf("expoffset: %d \n\n", expoffset);
	//printf("expdiff: %d \n\n", expdiff);


	//grab data from b and add to a
	t = 0;
	for(int j=0; j<size_ints-expoffset; j++)
	{
		t += b->data[j];
		r=1;
		for(int i=0; i<expdiff; i++)
		{
			r*=10;
			t /=10;
		}
		data[j+expoffset]+=t;
		t = b->data[j]%r;
		t*=DEC_MAX;
	}
	// now run through data in reverse order to deal with carrys between 32-bit ints
	for(int j=size_ints-1; j>0; j--)
	{
		if(data[j] > DEC_MAX)
			data[j-1]++;
		data[j] %= DEC_MAX;
	}

	// if we need to shift everything back one
	if(data[0] > DEC_MAX)
	{
		for(int j=1; j<size_ints; j++)
		{
			data[j] += (data[j-1]%10)*DEC_MAX;
			data[j-1] = data[j-1]/10;
		}
		data[size_ints-1] = data[size_ints-1]/10;
		exp++;
	}


	return 0;
}


int BigFloatDec::divide_by_two()
{
	exp-=1;
	for(int i=0; i<size_ints; i++)
	{
		data[i] *= 5;
	}

	if(data[0] >= DEC_MAX)
	{
		for(int j=1; j<size_ints; j++)
		{
			data[j] += (data[j-1]%10)*DEC_MAX;
			data[j-1] = data[j-1]/10;
		}
		data[size_ints-1] = data[size_ints-1]/10;
		exp++;
	}

	for(int j=size_ints-1; j>0; j--)
	{
		if(data[j] > DEC_MAX)
			data[j-1]+=data[j]/DEC_MAX;
		data[j] %= DEC_MAX;
	}




	return 0;
}

BigFloatDec::~BigFloatDec()
{
	free(data);
}