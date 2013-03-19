#include "Fraction.h"
using namespace std;

Fraction Fraction::operator =(const Fraction& y) {
	if (this == &y)
		return *this;
	num = y.num;
	den = y.den;
	return *this;
}

Fraction Fraction::operator =(const int y) {
	num = y;
	den = 1;
	return *this;
}

Fraction Fraction::operator =(const DEC2 &y) {
	num = y;
	den = 1;
	return *this;
}

Fraction::operator DEC2() const{
	DEC2 result;
	result = num/den;
	return result;
}

Fraction operator+(const Fraction& a, const Fraction& b){
	Fraction result;
	result.num = b.num * a.den + a.num * b.den;
	result.den = b.den * a.den;
	result.reduce();
	return result;
}

Fraction operator+(const Fraction& a, const int& b){
	Fraction result, B(DEC2(b,100));
	result = a + B;
	return result;
}

Fraction operator+(const int& b, const Fraction& a){
	Fraction result, B(DEC2(b,100));
	result = a + B;
	return result;
}

Fraction operator-(const Fraction& a, const Fraction& b){
	Fraction result;
	result.num = a.num * b.den - b.num * a.den;
	result.den = b.den * a.den;
	result.reduce();
	return result;
}

Fraction operator-(const Fraction& a, const int& b){
	Fraction result, B(DEC2(b,100));
	result = a - B;
	return result;
}

Fraction operator-(const int& b, const Fraction& a){
	Fraction result, B(DEC2(b,100));
	result = a - B;
	return result;
}

Fraction operator*(const Fraction& a, const Fraction& b){
	Fraction result;
	result.num = a.num * b.num;
	result.den = b.den * a.den;
	result.reduce();
	return result;
}

Fraction operator*(const Fraction& a, const int& b){
	Fraction result;
	result.num = a.num * b;
	result.den = a.den;
	result.reduce();
	return result;
}

Fraction operator*(const int& b, const Fraction& a){
	Fraction result;
	result.num = a.num * b;
	result.den = a.den;
	result.reduce();
	return result;
}

Fraction operator/(const Fraction& b, const Fraction& a){
	Fraction result;
	result.num = a.num * b.den;
	result.den = b.den * a.num;
	result.reduce();
	return result;
}

Fraction operator/(const int& b, const Fraction& a){
	Fraction result;
	result.num = a.num;
	result.den = a.den * b;
	result.reduce();
	return result;
}

Fraction operator/(const Fraction& a, const int& b){
	Fraction result;
	result.num = a.num;
	result.den = a.den * b;
	result.reduce();
	return result;
}

Fraction operator^(const Fraction& x, const int& y){
	if (y == 0)
		return Fraction(1,1);
	Fraction p = x;
	for (int i = y; i > 1; i--){
		p.num = p.num * x.num;
		p.den = p.den * x.den;
	}
	p.reduce();
	return p;
}

void Fraction::reduce() {
	DEC2 a=num, b=den, olda;
	/*while (b > 0){
		a = a%b;
		a = a+b;
		b = b-a;
		a = a-b;
	}*/
	while (a != 0) {
		olda = a;
		a = b%a;
		b = olda;
	}
	num=num/b;
	den=den/b;
	if (den<0)
	{ num=-num; den=-den; }
}

void Fraction::setLength(int l){
	num.length = l;
	den.length = l;
}

ostream& operator<<(ostream& os, const Fraction& f) {
	os << "<" << f.num << "," << f.den << ">";
	return os;
}

