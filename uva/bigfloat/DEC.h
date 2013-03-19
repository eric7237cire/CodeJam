#include <iostream>
#include <deque>
using namespace std;
#ifndef DECIMAL
#define DECIMAL
class DEC {
private:
        deque<int> digits;//#'s left of decimal
		deque<int> postdec;//#'s right of decimal
        char sign;
		int length;
		DEC& check();//makes sure # is in right format
public:
		friend class Fraction;
		friend class DEC2;
        DEC();
		~DEC();
		DEC(const DEC&);
		DEC(const DEC&, int);
        DEC(int num);
		DEC(int num, int);
        DEC(string);
		DEC(string, int);
		operator =(const DEC&);
        operator int() const;
        operator string() const;
		operator DEC2() const;
        friend DEC operator+(const DEC&, const DEC&);
		friend DEC operator+(const int&, const DEC&);
		friend DEC operator+(const DEC&, const int&);
        friend DEC operator-(const DEC&, const DEC&);
		friend DEC operator-(const int&, const DEC&);
		friend DEC operator-(const DEC&, const int&);
        friend DEC operator*(const DEC&, const DEC&);
		friend DEC operator*(const int&, const DEC&);
		friend DEC operator*(const DEC&, const int&);
        friend DEC operator/(const DEC&, const DEC&);
		friend DEC operator/(const int&, const DEC&);
		friend DEC operator/(const DEC&, const int&);
        friend DEC operator%(const DEC&, const DEC&);
		friend DEC operator%(const int&, const DEC&);
		friend DEC operator%(const DEC&, const int&);
		friend DEC operator^(const DEC&, const int&);
		friend void operator++(DEC&);
		friend void operator--(DEC&);
        friend ostream& operator<<(ostream&, const DEC&);
        friend istream& operator>>(istream&, DEC&);
        bool operator>(const DEC&);
		bool operator>(const int&);
        bool operator<(const DEC&);
		bool operator<(const int&);
        bool operator!=(const DEC&);
		bool operator!=(const int&);
        bool operator>=(const DEC&);
		bool operator>=(const int&);
        bool operator<=(const DEC&);
		bool operator<=(const int&);
		bool operator==(const DEC&);
		bool operator==(const int&);
		void setLength(int y){ length = y; }
		int getLength(){ return length; }
		DEC pwr(const int&);
		DEC pwr(const Fraction&);//used for fractional powers
		DEC sqrt();
		DEC root(DEC n);
		DEC factorial();
		DEC dubfactorial();
		DEC sin();
		DEC cos();
		DEC tan();
		DEC arctan();
		DEC arcsin();
		
};
#endif