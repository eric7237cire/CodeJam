//DEC2 was created solely to be the base of the Fraction class so that the Fraction class
//could be used in DEC functions (Ex: DEC pwr(const Fraction&);) 
#include <iostream>
#include <deque>
using namespace std;
#ifndef DECIMAL2
#define DECIMAL2
class DEC2 {
private:
        deque<int> digits;//#'s left of decimal
		deque<int> postdec;//#'s right of decimal
        char sign;
		int length;
		DEC2& check();//makes sure # is in right format
public:
		friend class Fraction;
		friend class DEC;
        DEC2();
		~DEC2();
		DEC2(const DEC2&);
		DEC2(const DEC2&, int);
        DEC2(int num);
		DEC2(int num, int);
        DEC2(string);
		DEC2(string, int);
		operator =(const DEC2&);
        operator int() const;
        operator string() const;
		operator DEC() const;
        friend DEC2 operator+(const DEC2&, const DEC2&);
		friend DEC2 operator+(const int&, const DEC2&);
		friend DEC2 operator+(const DEC2&, const int&);
        friend DEC2 operator-(const DEC2&, const DEC2&);
		friend DEC2 operator-(const int&, const DEC2&);
		friend DEC2 operator-(const DEC2&, const int&);
        friend DEC2 operator*(const DEC2&, const DEC2&);
		friend DEC2 operator*(const int&, const DEC2&);
		friend DEC2 operator*(const DEC2&, const int&);
        friend DEC2 operator/(const DEC2&, const DEC2&);
		friend DEC2 operator/(const int&, const DEC2&);
		friend DEC2 operator/(const DEC2&, const int&);
        friend DEC2 operator%(const DEC2&, const DEC2&);
		friend DEC2 operator%(const int&, const DEC2&);
		friend DEC2 operator%(const DEC2&, const int&);
		friend DEC2 operator^(const DEC2&, const int&);
		friend void operator++(DEC2&);
		friend void operator--(DEC2&);
        friend ostream& operator<<(ostream&, const DEC2&);
        friend istream& operator>>(istream&, DEC2&);
        bool operator>(const DEC2&);
		bool operator>(const int&);
        bool operator<(const DEC2&);
		bool operator<(const int&);
        bool operator!=(const DEC2&);
		bool operator!=(const int&);
        bool operator>=(const DEC2&);
		bool operator>=(const int&);
        bool operator<=(const DEC2&);
		bool operator<=(const int&);
		bool operator==(const DEC2&);
		bool operator==(const int&);
		void setLength(int y){ length = y; }
		int getLength(){ return length; }
		DEC2 pwr(const int&);
		DEC2 pwr(const int&, const int&);//used for fractional powers
		DEC2 sqrt();
		DEC2 root(DEC2 n);
		DEC2 factorial();
		DEC2 sin();
		DEC2 cos();
		DEC2 tan();
		DEC2 arctan();
		
};
#endif