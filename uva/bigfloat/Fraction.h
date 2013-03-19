//Fraction class allows numbers to stay in fractional form
#include <iostream>
#include "DEC.h"
#include "DEC2.h"
using namespace std;

#ifndef FRAC
#define FRAC
class Fraction {
public:
	DEC2 num, den;
	Fraction() : num(0,100), den(1,100) { }
	Fraction(DEC2 a) : num(a,100), den(1,100) { }
	Fraction(DEC2 a, DEC2 b) : num(a,100), den(b,100) { }
	Fraction(DEC2 a, DEC2 b, int l) : num(a,l), den(b,l) { }
	Fraction operator =(const Fraction&);
	Fraction operator =(const int);
	Fraction operator =(const DEC2&);
    operator DEC2() const;
	friend Fraction operator+(const Fraction&, const Fraction&);
	friend Fraction operator+(const Fraction&, const int&);
	friend Fraction operator+(const int&, const Fraction&);
	friend Fraction operator-(const Fraction&, const Fraction&);
	friend Fraction operator-(const Fraction&, const int&);
	friend Fraction operator-(const int&, const Fraction&);
	friend Fraction operator*(const Fraction&, const Fraction&);
	friend Fraction operator*(const Fraction&, const int&);
	friend Fraction operator*(const int&, const Fraction&);
	friend Fraction operator/(const Fraction&, const Fraction&);
	friend Fraction operator/(const Fraction&, const int&);
	friend Fraction operator/(const int&, const Fraction&);
	friend Fraction operator^(const Fraction&, const int&);
	void reduce();	
	void setLength(int);
	friend ostream& operator<<(ostream&, const Fraction&);
};
#endif