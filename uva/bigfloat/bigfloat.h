// ==============================================================
//
//                      Trevor Q Foley
//                      Class BigFloat
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

#pragma once

#include <stdio.h>

#include <malloc.h>

//number of 32bit chunks to hold values
#define SIZE (100)

#define DEC_TENS_IN_32BIT 5 // must match below
#define DEC_MAX 100000 // must be 10^DEC_TENS_IN_32BIT



class BigFloat
{
public:
	unsigned int *data;
	int exp;
	int sign;



private:
	int add_same_sign(class BigFloat * a, class BigFloat * b);
	int add_diff_sign(class BigFloat * a, class BigFloat * b);
	int add_diff_sign2(class BigFloat * a, class BigFloat * b);
	int fix_exp();
	
public:
	BigFloat();
	BigFloat(double a);
	BigFloat(char * hex_data, int exponent, int sign);

	int copy(class BigFloat * a);	
	int set_with_double(double a);
	int is_zero();

	int save_to_disk(char * filename);
	int load_from_disk(char * filename);

	int multiply_by_negative_one();
	int add(class BigFloat * a, class BigFloat * b);
	int subtract(class BigFloat * a, class BigFloat * b);
	int multiply(class BigFloat * a, class BigFloat * b);
	int divide(class BigFloat * a, class BigFloat * b);
	int reciprocal(class BigFloat * a);
	int compare(class BigFloat * a);
	int compare_sign(class BigFloat * a); // returns 1 if signs are the same, -1 if different

	int get_exp();
	int get_sign();
	
	double getdouble();
	float getfloat();
	
	int report();
	int print();
	int printAll();
	int print_fraction_portion_in_decimal(int num_digits, char *);
	int test_if_pi_to_nine_hundred_digits();
	
	~BigFloat();
};


class BigFloatDec
{
public:
	unsigned int *data;
	int exp;
	int sign;
	int size_ints;// gets computed from digits
	int DecimalDigits; // user selects how much should be kept track of in decimal
public:
	BigFloatDec(int digits);
	//BigFloatDec(char * dec_data, int exponent, int sign);

	int copy(class BigFloatDec * a);
	int set_with_string(char * decimal, int exp);
	int add(class BigFloatDec * a, class BigFloatDec * b);
	int divide_by_two();
	int print(char *);
	
	~BigFloatDec();
};