#include <string>
#include "DEC.h"
#include "DEC2.h"
#include "Fraction.h"
using namespace std;

DEC::DEC() {
	sign = 'p';
	length = 10000;
}
DEC::~DEC() {}
DEC::DEC(const DEC& y) {
	digits = y.digits;
	postdec = y.postdec;
	sign = y.sign;
	length = y.length;
	this->check();
}
DEC::DEC(const DEC& y, int l) {
	*this = DEC::DEC(y);
	length = l;
}
DEC::operator =(const DEC& y) {
	if (this == &y)
		return *this;
	digits.clear();
	postdec.clear();
	digits = y.digits;
	postdec = y.postdec;
	sign = y.sign;
	length = y.length;
	this->check();
}
DEC::DEC(int num) {
	length = 10000;
	int a=num, first = 0;
	if (num < 0) {
		sign = 'n';
		if (num == -2147483648)
			num++;
		num = num * -1; 
	}
	else sign = 'p';
	while (num > 0) {
		if (a == -2147483648 && first == 0){
			digits.push_front((num % 10) + 1);
			first++;
		}
		else
			digits.push_front(num % 10);
		num = num / 10;

	}
	this->check();
}

DEC::DEC(int num, int l) {
	*this = DEC::DEC(num);
	length = l;
}

DEC::DEC(string number)  {
	int a;
	length = 10000;
	digits.clear();
	if (number.at(0) == '-') sign='n';
	else sign = 'p';
	int start = 0;
	if ( !isdigit(number.at(0)) && number.at(0) != '.') start = 1;
	a=start;
	while (number.at(a)!='.'){
		digits.push_back(number.at(a) - '0');
		a++;
		if (a>=(number.length()))
			break;
	}
	a++;
	for (a;a<length && a < number.length();a++)
		postdec.push_back(number.at(a) - '0');
	this->check();
}

DEC::DEC(string number, int l) {
	*this = DEC::DEC(number);
	length = l;
}

DEC::operator int() const {
	int sum = 0;
	for (int i=0; i<(digits.size()); ++i)
	{ sum=sum*10 + digits[i]; }
	if (sign=='n')
		sum=sum*(-1);
	return sum;
}
DEC::operator string() const {
	string s="";
	char w=' ';
	int a=0;		
	if (sign=='n')
		s='-';
	for (a=0;a<digits.size();a++){
		w=digits[a]+48;
		s=s+w;}
	s=s+'.';
	for (a=0;a<postdec.size();a++){
		w=postdec[a]+48;
		s=s+w;}
	return s;
} 
DEC::operator DEC2() const {
	DEC2 a;
	a.digits = digits;
	a.postdec = postdec;
	return a;
} 
DEC& DEC::check(){
	while ((digits.size()>1)&&(digits[0]==0)){
		digits.pop_front();
	}
	while ((postdec.size()>1)&&(postdec[(postdec.size()-1)]==0)){
		postdec.pop_back();
	}

	if (digits.size()==0)
		digits.push_front(0);
	if (postdec.size()==0)
		postdec.push_front(0);
	if (digits.at(0)==0 && postdec.at((postdec.size()-1))==0){
		sign='p';
	}
	while (postdec.size() > length)
		postdec.pop_back();
	return *this;
}
bool DEC::operator==(const DEC& y) {
	int f = 0;
	if (sign != y.sign)
		return 0;
	if(digits.size() != y.digits.size())
		return 0;
	if(postdec.size() != y.postdec.size())
		return 0;
	for (int a=0;a < digits.size();a++)
		if (digits[a] != y.digits[a])
			f = 1;
	if (f == 1)
		return 0;
	for (int a=0;a < postdec.size();a++){
		if (postdec[a] != y.postdec[a])
			f = 1;}
	if (f == 0)
		return 1;
	else
		return 0;
}
bool DEC::operator!=(const DEC& y) {
	if (*this==y)
		return 0;
	else
		return 1;
}
bool DEC::operator>=(const DEC& y) {
	if ((*this>y)||(*this==y))
		return 1;
	else
		return 0;
}
bool DEC::operator<=(const DEC& y) {
	////cout << *this << endl << y << endl;
	if ((*this<y)||(*this==y))
		return 1;
	else
		return 0;
}

bool DEC::operator<(const DEC& y) {
	if (*this>=y)
		return 0;
	else
		return 1;
}

bool DEC::operator>(const DEC& y) {
	int a=0;
	if (*this == y)
		return 0;

	if (sign == 'n' && y.sign == 'p')
		return 0;
	else if (sign == 'p' && y.sign == 'n')
		return 1;
	if (sign == 'p' && y.sign == 'p')
		if (digits.size() > y.digits.size())
			return 1;
	if (sign == 'n' && y.sign == 'n')
		if (digits.size() > y.digits.size())
			return 0;
	if (sign == 'p' && y.sign == 'p')
		if (digits.size() < y.digits.size())
			return 0;
	if (sign == 'n' && y.sign == 'n')
		if (digits.size() < y.digits.size())
			return 1;

	for (a=0;a < digits.size();a++){//if same size and same sign but not equal
		if (digits[a] > y.digits[a])
			if (sign == 'p')
				return 1;
			else
				return 0;
		else if (digits[a] < y.digits[a])
			if (sign == 'p')
				return 0;
			else
				return 1;
	}
	for (a=0;(a < postdec.size()) && (a < y.postdec.size());a++){
		if (postdec[a] > y.postdec[a])
			if (sign == 'p')
				return 1;
			else
				return 0;
		else if (postdec[a] < y.postdec[a])
			if (sign == 'p')
				return 0;
			else
				return 1;
	}

	if (a < postdec.size())//if one postdec is longer
		if (sign == 'p')
			return 1;
		else
			return 0;
	if (a < y.postdec.size())
		if (sign == 'p')
			return 0;
		else
			return 1;
}

bool DEC::operator>(const int& y) {
	if (this->sign == 'p' && y < 0)
		return 1;
	if (this->sign == 'n' && y > 0)
		return 0;
	DEC big_int(2147483647), small_int(-2147483648);
	int x = 0;
	if (*this > big_int)
		return 1;
	if (*this < small_int)
		return 0;
	x = *this;
	if (x == y && (this->postdec[0] != 0 || this->postdec.size() != 1)){
		if (this->sign == 'n' && y < 0)
			return 0;
		else
			return 1;
	}
	if (x > y)
		return 1;
	else
		return 0;
}
bool DEC::operator<(const int& y) {
	if (*this>=y)
		return 0;
	else
		return 1;
}
bool DEC::operator>=(const int& y) {
	if ((*this>y)||(*this==y))
		return 1;
	else
		return 0;
}
bool DEC::operator<=(const int& y) {
	if ((*this<y)||(*this==y))
		return 1;
	else
		return 0;
}
bool DEC::operator!=(const int& y) {
	if (*this==y)
		return 0;
	else
		return 1;
}
bool DEC::operator==(const int& y) {
	if (this->sign == 'p' && y < 0)
		return 0;
	if (this->sign == 'n' && y > 0)
		return 0;
	DEC big_int(2147483647), small_int(-2147483648);
	int x = 0;
	if (*this > big_int)
		return 0;
	if (*this < small_int)
		return 0;
	x = *this;
	if (x == y && (this->postdec[0] != 0 || this->postdec.size() != 1)){
		return 0;
	}
	if (x == y)
		return 1;
	else
		return 0;
}
DEC operator+(const DEC& x, const DEC& y) {
	int a = 0, b = 0, c = 0, carry = 0, total = 0;
	DEC result, x2, y2;
	if (x.length > y.length)
		result.setLength(y.length);
	else
		result.setLength(x.length);
	x2 = x;//copies of x and y that can be changed
	y2 = y;
	if ((x.digits.size() == 1 && x.digits[0] == 0) && (x.postdec.size() == 1 && x.postdec[0] == 0)){//if x is 0
		result = y;
		result.check();
		return result;
	}
	if ((y.digits.size() == 1 && y.digits[0] == 0) && (y.postdec.size() == 1 && y.postdec[0] == 0)){//if y is 0
		result = x;
		result.check();
		return result;
	}
	//////////////////////////////////////////////first do postdec
	if (x.sign == y.sign){//same sign
		if (x.postdec.size() == y.postdec.size()){//same size
			for (a = x.postdec.size()-1;a >= 0;a--){//add the postdecs
				total = (x.postdec[a] + y.postdec[a]) + carry;
				if (total > 9)
					carry = 1;
				else
					carry = 0;
				result.postdec.push_front(total % 10);
			}
		}
		else {									//different size
			x2.sign = 'p';
			y2.sign = 'p';
			if (x2 > y2){//decide sign of result based on greater absolute value
				while (x2.postdec.size() < y2.postdec.size())//make sure postdec sizes are equal
					x2.postdec.push_back(0);
				while (x2.postdec.size() > y2.postdec.size()){
					result.postdec.push_front(x2.postdec[x2.postdec.size() - 1]);
					x2.postdec.pop_back();
				}
			}
			else{
				while (y2.postdec.size() < x2.postdec.size())//make sure postdec sizes are equal
					y2.postdec.push_back(0);
				while (y2.postdec.size() > x2.postdec.size()){
					result.postdec.push_front(y2.postdec[y2.postdec.size() - 1]);
					y2.postdec.pop_back();
				}
			}
			for (a = x2.postdec.size() - 1;a >= 0;a--){//add the postdecs 
				total = (x2.postdec[a] + y2.postdec[a]) + carry;
				if (total > 9)
					carry = 1;
				else
					carry = 0;
				result.postdec.push_front(total % 10);
			}
		}
	}
	else{					//different sign
		x2.sign = 'p';
		y2.sign = 'p';
		if (x2 == y2){
			result.digits.push_front(0);//negate to 0
			result.check();
			return result;
		}
		if (x2 > y2){//decide sign of result based on greater absolute value
			result.sign = x.sign;
			while (x2.postdec.size() < y2.postdec.size())//make sure postdec sizes are equal
				x2.postdec.push_back(0);
			while (x2.postdec.size() > y2.postdec.size()){
				result.postdec.push_front(x2.postdec[x2.postdec.size() - 1]);
				x2.postdec.pop_back();
			}
		}
		else{
			result.sign = y.sign;
			while (y2.postdec.size() < x2.postdec.size())//make sure postdec sizes are equal
				y2.postdec.push_back(0);
			while (y2.postdec.size() > x2.postdec.size()){
				result.postdec.push_front(y2.postdec[y2.postdec.size() - 1]);
				y2.postdec.pop_back();
			}
		}
		//now do subtracting
		if (x2 > y2){//x absolute value is greater
			int borrowed = 0;//flag that tells whether borrowing has carried through
			for (a = x2.postdec.size() - 1;a >= 0;a--){
				if (x2.postdec[a] < y2.postdec[a]){// if digit in greater # is smaller than digit in lesser #
					borrowed = 0;
					x2.postdec[a] += 10;
					int dp = 1,c;//dp tells whether borrowing from digits(0) or postdec(1), c keeps track of borrowing
					while (borrowed == 0){
						if (a == 0 || dp == 0){
							dp = 0;
							c = x2.digits.size() - 1;
						}
						else{
							dp = 1;
							c = a - 1;
						}
						for (b = 0;b != 1;c--){
							if (c < 0){
								dp = 0;
								break;
							}
							if (dp == 1){
								if (x2.postdec[c] != 0){
									x2.postdec[c]--;
									b = 1;
									borrowed = 1;
								}
								else
									x2.postdec[c] = 9;
							}
							if (dp == 0){
								if (x2.digits[c] != 0){
									x2.digits[c]--;
									b = 1;
									borrowed = 1;
								}
								else
									x2.digits[c] = 9;
							}
						}
					}
				}
				result.postdec.push_front(x2.postdec[a] - y2.postdec[a]);
			}
		}
		else{//y absolute value is greater
			int borrowed = 0;//flag that tells whether borrowing has carried through
			for (a = y2.postdec.size() - 1;a >= 0;a--){
				if (y2.postdec[a] < x2.postdec[a]){// if digit in greater # is smaller than digit in lesser #
					borrowed = 0;
					y2.postdec[a] += 10;
					int dp = 1,c;//dp tells whether borrowing from digits(0) or postdec(1), c keeps track of borrowing
					while (borrowed == 0){
						if (a == 0 || dp == 0){
							dp = 0;
							c = y2.digits.size() - 1;
						}
						else{
							dp = 1;
							c = a - 1;
						}
						for (b = 0;b != 1;c--){
							if (c < 0){
								dp = 0;
								break;
							}
							if (dp == 1){
								if (y2.postdec[c] != 0){
									y2.postdec[c]--;
									b = 1;
									borrowed = 1;
								}
								else
									y2.postdec[c] = 9;
							}
							if (dp == 0){
								if (y2.digits[c] != 0){
									y2.digits[c]--;
									b = 1;
									borrowed = 1;
								}
								else
									y2.digits[c] = 9;
							}
						}
					}
				}
				result.postdec.push_front(y2.postdec[a] - x2.postdec[a]);
			}
		}
	}
	/////////////////////////////////////////////////now take care of digits
	int x_dsize = 0, y_dsize = 0;
	if (x.sign == y.sign) {//if same sign
		result.sign = x.sign;
		a = 0; b = 0; total = 0;
		if (x2.digits.size() < y2.digits.size())//b = smaller digit size
			b = (x2.digits.size() - 1);
		else
			b = (y2.digits.size() - 1);
		x_dsize = x2.digits.size() - 1;
		y_dsize = y2.digits.size() - 1;
		for (a = b; a >= 0; a--) {//add up to highest digit place in smaller #
			total = x2.digits[x_dsize] + y2.digits[y_dsize] + carry;
			if (total > 9)
				carry = 1;
			else 
				carry = 0;
			result.digits.push_front(total % 10);
			x_dsize--;
			y_dsize--;
		}

		if (x_dsize < 0){//concatenate rest of larger #
			while (y_dsize >= 0){
				while (y_dsize >= 0 && (y2.digits[y_dsize] + carry) > 9){
					result.digits.push_front(0);
					y_dsize--;
				}
				if (y_dsize >= 0)
					result.digits.push_front(y2.digits[y_dsize] + carry);
				else
					result.digits.push_front(1);
				carry=0;
				y_dsize--;}}
		if (y_dsize < 0){
			while (x_dsize >= 0){
				while (x_dsize >= 0 && (x2.digits[x_dsize] + carry) > 9){
					result.digits.push_front(0);
					x_dsize--;
				}
				if (x_dsize >= 0)
					result.digits.push_front(x2.digits[x_dsize] + carry);
				else
					result.digits.push_front(1);
				carry=0;
				x_dsize--;}
		}
		if (carry == 1)
			result.digits.push_front(carry);
		result.check();
		return result;
	}
	else {//if different sign
		total=0;
		x_dsize = x2.digits.size() - 1;
		y_dsize = y2.digits.size() - 1;
		if (x2 > y2){//x absolute value is greater
			for (a = (y2.digits.size() - 1); a >= 0; a--) {
				if (x2.digits[x_dsize] >= y2.digits[y_dsize]){
					total = x2.digits[x_dsize] - y2.digits[y_dsize];
					result.digits.push_front(total);}
				else{
					x2.digits[x_dsize] += 10;
					if (x2.digits[x_dsize-1] == 0){
						int num = x_dsize - 1;
						while (x2.digits[num] == 0){
							x2.digits[num] = 9;
							num--;
						}
						x2.digits[num] -= 1;}
					else
						x2.digits[x_dsize-1] -= 1;
					total = x2.digits[x_dsize] - y2.digits[y_dsize];
					result.digits.push_front(total);}
				x_dsize--;
				y_dsize--;}
			if (x_dsize<0){
				while (y_dsize >= 0){
					if (y_dsize >= 0)
						result.digits.push_front(y2.digits[y_dsize]);
					y_dsize--;}}
			if (y_dsize < 0){
				while (x_dsize >= 0){
					if (x_dsize >= 0)
						result.digits.push_front(x2.digits[x_dsize]);
					x_dsize--;}}
			result.check();
			return result;
		}

		else if (y2 > x2){//y absolute value is greater
			for (a=x.digits.size() - 1; a >= 0; a--) {
				if (y2.digits[y_dsize] >= x2.digits[x_dsize]){
					total = y2.digits[y_dsize] - x2.digits[x_dsize];
					result.digits.push_front(total);}
				else{
					y2.digits[y_dsize] += 10;
					y2.digits[y_dsize-1] -= 1;
					total = y2.digits[y_dsize] - x2.digits[x_dsize];
					result.digits.push_front(total);}
				y_dsize--;
				x_dsize--;}
			if (y_dsize < 0){
				while (x_dsize >= 0){
					if (x_dsize >= 0)
						result.digits.push_front(x2.digits[x_dsize]);
					x_dsize--;}}
			if (x_dsize < 0){
				while (y_dsize >= 0){
					if (y_dsize >= 0)
						result.digits.push_front(y2.digits[y_dsize]);
					y_dsize--;}}
			result.check();
			return result;
		}
		else{
			result.check();
			return result;
		}
	}
}
DEC operator-(const DEC& x, const DEC& y) {//uses addition operator
	DEC result, x2, y2;
	if (x.length > y.length)
		result.setLength(y.length);
	else
		result.setLength(x.length);
	x2=x;
	x2.sign='p';
	y2=y;
	y2.sign='p';

	if (x.sign==y.sign){
		y2.sign='n';
		y2.check();
		result=x2+y2;
	}
	else
		result=x2+y2;
	x2.sign='p';
	y2.sign='p';
	if (x2>y2)
		result.sign=x.sign;
	else if (y.sign=='n')
		result.sign='p';
	else
		result.sign='n';
	result.check();
	return result;
}
DEC operator*(const DEC& x, const DEC& y) {
	DEC result, result1, x2, y2;
	if (x.length > y.length)
		result.setLength(y.length);
	else
		result.setLength(x.length);
	result1.setLength(result.length);
	if (!(x.postdec.size() == 1 && x.postdec[0] == 0)){
		for (int a = x.postdec.size() - 1, b = 0; a >= 0; a--){//makes x2 and y2 whole #'s to simplify multiplication
			b = x.postdec[a];
			x2.digits.push_front(b);
		}
	}
	for (int a = x.digits.size() - 1, b = 0; a >= 0; a--){
		b = x.digits[a];
		x2.digits.push_front(b);
	}
	if (!(y.postdec.size() == 1 && y.postdec[0] == 0)){
		for (int a = y.postdec.size() - 1, b = 0; a >= 0; a--){
			b = y.postdec[a];
			y2.digits.push_front(b);
		}
	}
	for (int a = y.digits.size() - 1, b = 0; a >= 0; a--){
		b = y.digits[a];
		y2.digits.push_front(b);
	}
	x2.setLength(result.length);
	y2.setLength(result.length);
	x2.check();
	y2.check();

	if (x.sign != y.sign)//gets the sign of the result
		result.sign='n';
	else
		result.sign='p';
	if (x2 == 0 || y2 == 0)
		return DEC(0,result.length);
	int total = 0, carry = 0;
	DEC sum(0,result.length), temp(0,result.length),temp2(0,result.length);
	temp.digits.clear();
	temp2.digits.clear();
	//the following code simply multilplies out each y2 digit times the x2 #, then adds all the products (normal multiplication)
	for (int b=y2.digits.size()-1, i=0, k=0, s=0, t=0; b>=0; b--,i++) {//multiplies the y digits times the x #
		k=x2.digits.size()-1;
		carry=0;
		for (int a=k; a>=0; a--) {
			total=x2.digits[k]*y2.digits[b]+carry;
			temp.digits.push_front(total%10);
			carry=total/10;
			k--;}
		if (carry>0)
			temp.digits.push_front(carry);
		for (int a=i;a>0;a--)
			temp.digits.push_back(0);
		carry = 0;
		s=sum.digits.size()-1;
		t=temp.digits.size()-1;
		for (int a=0; a<sum.digits.size() && a<temp.digits.size(); a++,s--,t--) {
			total=sum.digits[s]+temp.digits[t]+carry;
			if (total > 9)
				carry = 1;
			else
				carry = 0;
			temp2.digits.push_front(total %10);
		}
		if (carry == 0){
			for (; t>=0; t--)
				temp2.digits.push_front(temp.digits[t]);
			for (; s>=0; s--)
				temp2.digits.push_front(sum.digits[s]);
		}
		else {
			for (; t>=0; t--){
				total=temp.digits[t]+carry;
				if (total > 9)
					carry = 1;
				else
					carry = 0;
				temp2.digits.push_front(total %10);
			}
			for (; s>=0; s--){
				total=sum.digits[s]+carry;
				if (total > 9)
					carry = 1;
				else
					carry = 0;
				temp2.digits.push_front(total %10);
			}
			if (carry > 0)
				temp2.digits.push_front(1);
		}
		sum = temp2;
		temp2.digits.clear();
		temp.digits.clear();
	}
	sum.check();
	sum.sign = result.sign;
	result = sum;
	//the last part of the code copies result into result2 but with the right decimal point placement
	DEC result2;
	result2.sign = result.sign;
	result2.setLength(result.length);
	int w = 0, z = 0;
	if ((x.postdec.size() == 1 && x.postdec[0] == 0) && (y.postdec.size() == 1 && y.postdec[0] == 0))
		w = 0;
	else if (x.postdec.size() == 1 && x.postdec[0] == 0)
		w = y.postdec.size();
	else if (y.postdec.size() == 1 && y.postdec[0] == 0)
		w = x.postdec.size();
	else
		w = x.postdec.size() + y.postdec.size();
	for (;w > 0 && result.digits.size() > 0;w--){
		z = result.digits[result.digits.size() - 1];
		result2.postdec.push_front(z);
		result.digits.pop_back();
	}
	for (;w > 0;w--){
		result2.postdec.push_front(0);
	}
	while (result.digits.size() > 0){
		z = result.digits[result.digits.size() - 1];
		result2.digits.push_front(z);
		result.digits.pop_back();
	}
	result2.check();
	return result2;
}
DEC operator/ (const DEC& x, const DEC& y) {
	//cout << x << endl << endl << y << endl << endl;
	DEC result, z(0,1), x2, y2, x3(0,1), y3(0,1), x4(0,1), y4(0,1) , Zero(0,1), point(0,1);
	if (x.length > y.length)
		result.setLength(y.length);
	else
		result.setLength(x.length);
	if (x.sign == y.sign)
		result.sign = 'p';
	else
		result.sign = 'n';
	if (y.digits.size() == 1 && y.digits[0] == 1 && y.postdec.size() == 1 && y.postdec[0] == 0){//check if divided by one
		result = x;
		if (x.sign == y.sign)
			result.sign = 'p';
		else
			result.sign = 'n';
		result.check();
		return result;
	}
	x3 = x;
	y3 = y;
	x3.sign = 'p';
	y3.sign = 'p';
	if (x3 == y3){//check if x and y are the same #
		result.digits.push_back(1);
		result.check();
		return result;
	}
	try{
		//makes x2 and y2 whole #'s to simplify multiplication
		if (!(x.postdec.size() == 1 && x.postdec[0] == 0)){//if fractional part of x exists
			for (int a = x.postdec.size() - 1, b = 0; a >= 0; a--){//add fraction to front of x2
				b = x.postdec[a];
				////cout << b << endl;
				x2.digits.push_front(b);
				////cout << x2 << endl;
			}
		}
		for (int a = x.digits.size() - 1, b = 0; a >= 0; a--){//now add whole part of x to x2
			b = x.digits[a];
			x2.digits.push_front(b);
		}
		if (!(y.postdec.size() == 1 && y.postdec[0] == 0)){//do the same for y and y2
			for (int a = y.postdec.size() - 1, b = 0; a >= 0; a--){
				b = y.postdec[a];
				y2.digits.push_front(b);
			}
		}
		for (int a = y.digits.size() - 1, b = 0; a >= 0; a--){
			b = y.digits[a];
			y2.digits.push_front(b);
		}
		////cout << x2 << endl << y2 << endl;
		x2.check();
		y2.check();
		x2.sign='p';
		y2.sign='p';
		////cout << x2 << endl << y2 << endl;
		if (x2 == 0)//zero division
			return 0;
		if (y2 == 0)
			throw y;
		while (x2.digits[x2.digits.size() -1] == 0 && y2.digits[y2.digits.size() -1] == 0){//simplifies division
			x2.digits.pop_back();
			y2.digits.pop_back();
		}
		int b = 0,cnt = 0;
		DEC xdiv;//remainder during division
		xdiv.postdec.push_back(0);
		int count=0;//keeps zeros from adding up in front
		////cout << x2 << endl << y2 << endl;
		if (x2 == y2){// if answer is a multiple of 1
			result.digits.push_back(1);
			////cout << point << endl;
			point = point + DEC(x.digits.size() - y.digits.size());
			////cout << point << endl;
			if (x.digits.size() == 1 && x.digits[0] == 0)
				point = point - 1;
			////cout << point << endl;
			if (y.digits.size() == 1 && y.digits[0] == 0)
				point = point + 1;
			////cout << point << endl;
			DEC result2;
			result2.setLength(result.length);
			result2.sign = result.sign;
			if (point == 0){
				result2.postdec.push_back(1);
				result2.check();
				return result2;
			}
			if (point > 0){
				result2.digits.push_back(1);
					for (;point > 0;point--){
						result2.digits.push_back(0);
					}
			}
			else {
				point = point + 1;
				result2.postdec.push_back(1);
					for (;point < 0;point++){
						result2.postdec.push_front(0);
					}
			}
			result2.check();
			////cout << result2 << endl << endl;
			////cout << result2.digits[0] << endl;
			return result2;
		}
		if (x2 > y2){
			count = 1;
			for (b = 0;y2>xdiv;b++){
				////cout << b << endl;
				xdiv.digits.push_back(x2.digits[b]);
			}
			while (1){
				for (cnt = 0,z = Zero;z<=xdiv;cnt++){
					z = z + y2;
				}
				cnt--;
				z = z - y2;
				result.digits.push_back(cnt);
				////cout << result << endl;
				xdiv = xdiv - z;
				if (xdiv == Zero)
					xdiv.digits.pop_back();
				////cout << b << endl;
				if (b < x2.digits.size())
					xdiv.digits.push_back(x2.digits[b]);
				else
					break;
				b++;
			}
		}
		else
			xdiv = x2;//otherwise xdiv would = 0


		b=0;
		b = b + (x.postdec.size() - y.postdec.size());
		if (y.postdec.size() == 1 && y.postdec[0] == 0)
			b = b + 1;
		if (x.postdec.size() == 1 && x.postdec[0] == 0)
			b = b - 1;
		if (xdiv != Zero && xdiv.digits.size()!= 0){//finish division with remainder
			xdiv.digits.push_back(0);
			////cout << xdiv << endl;
			while (b < x.length){
				////cout << xdiv << endl;
				for (cnt = 0,z = Zero;z<=xdiv;cnt++){
					z = z + y2;
					////cout << z << endl;
				}
				cnt--;
				z = z - y2;
				if (count != 0 || cnt != 0)
					result.digits.push_back(cnt);
				if (cnt != 0)
					count = 1;
				xdiv = xdiv - z;
				if (xdiv == Zero)
					break;
				xdiv.digits.push_back(0);
				b++;
			}
		}

		point = x2.digits.size() - y2.digits.size();//helps decide decimal placement
		//cout << point << endl << endl;
		x3.digits = x.digits;
		y3.digits = y.digits;
		if (x3 > y3){//keeps decimal point from being one digit off
			x3=x2;
			y3=y2;
			while (x3.digits.size() > y3.digits.size())
				x3.digits.pop_back();
			while (y3.digits.size() > x3.digits.size())
				y3.digits.pop_back();
			if (x3 > y3)
				point = point + 1;
			//cout << point << endl << endl;
		}
		else {
			x3=x2;
			y3=y2;
			x4=x2;
			y4=y2;
			while (x3.digits.size() > y3.digits.size())
				x3.digits.pop_back();
			while (y3.digits.size() > x3.digits.size())
				y3.digits.pop_back();
			while (y4.digits[y4.digits.size()-1] == 0)
				y4.digits.pop_back();
			//cout << x4 << endl << endl << y4 << endl << endl;
			if (x3 <= y3 && x4 != y4)
				point = point - 1;
			//cout << point << endl << endl;
		}
		if (!(y.postdec.size() == 1 && y.postdec[0] == 0)){//helps decide decimal placement
			point = point + DEC(y.postdec.size());
		}
		//cout << point << endl << endl;
		if (!(x.postdec.size() == 1 && x.postdec[0] == 0)){//helps decide decimal placement
			point = point - DEC(x.postdec.size());
		}
		//cout << point << endl << endl;
		if (point < 0)//helps decide decimal placement
			point = point + 1;
		if (xdiv.digits.size() == 0 && point > 0)
			point = point + 1;
		//cout << point << endl << endl;

		if (!(y.postdec.size() == 1 && y.postdec[0] == 0)){
			for (int i=y.postdec.size();i > 0 && result.digits.size() != 0 && result.digits[0] == 0;i--)
				result.digits.pop_front();
		}
		for (int i=point;i<0;i++)
			result.digits.push_front(0);
		//cout << result << endl << endl;
		DEC result2;
		int a = 0;
		result2.setLength(result.length);
		result2.sign = result.sign;
		for (;point > 0 && result.digits.size() > 0;point--){
			result2.digits.push_back(result.digits[0]);
			result.digits.pop_front();
		}
		while (result.digits.size() > 0){
			result2.postdec.push_back(result.digits[0]);
			result.digits.pop_front();
		}
		result2.check();
		//cout << result2 << endl << endl;
		////cout << result2.digits[0] << endl;
		return result2;
	}
	catch (DEC){
		cout << "error, can't divide by zero" << endl;
		return 0;
	}
}
DEC operator%(const DEC& x, const DEC& y) {
	////cout << x << endl << y << endl;
	/*if (x.postdec.size() > 1 || y.postdec.size() > 1 || (x.postdec.size() == 1 && x.postdec[0] != 0) || (y.postdec.size() == 1 && y.postdec[0] != 0)){
		//cout << "Can only mod whole numbers." << endl;
		return x;
	}*/
	DEC result, mod, x2, y2;
	y2=y;
	y2.sign='p';
	x2=x;
	x2.sign='p';
	if (y2 > x2){
		return x;
	}
	if (y2 == x2){
		return 0;
	}
	x2.setLength(1);
	y2.setLength(1);
	result=x2/y2;
	result.postdec.clear();
	result=result*y2;
	mod=x2-result;
	if (x.sign=='n')
		mod.sign='n';
	else
		mod.sign='p';
	mod.check();
	return mod;
}
DEC operator+(const int& x, const DEC& y){
	DEC x2 = x, result;
	x2.setLength(y.length);
	result = x2 + y;
	result.check();
	return result;
}
DEC operator+(const DEC& x, const int& y){
	DEC y2 = y, result;
	y2.setLength(x.length);
	result = x + y2;
	result.check();
	return result;
}
DEC operator-(const int& x, const DEC& y){
	DEC x2 = x, result;
	x2.setLength(y.length);
	result = x2 - y;
	result.check();
	return result;
}
DEC operator-(const DEC& x, const int& y){
	DEC y2 = y, result;
	y2.setLength(x.length);
	result = x - y2;
	result.check();
	return result;
}
DEC operator*(const int& x, const DEC& y){
	DEC x2 = x, result;
	x2.setLength(y.length);
	result = x2 * y;
	result.check();
	return result;
}
DEC operator*(const DEC& x, const int& y){
	DEC y2 = y, result;
	y2.setLength(x.length);
	result = x * y2;
	result.check();
	return result;
}
DEC operator/(const int& x, const DEC& y){
	DEC x2 = x, result;
	x2.setLength(y.length);
	result = x2 / y;
	result.check();
	return result;
}
DEC operator/(const DEC& x, const int& y){
	DEC y2 = y, result;
	y2.setLength(x.length);
	result = x / y2;
	result.check();
	return result;
}
DEC operator%(const int& x, const DEC& y){
	DEC x2 = x, result;
	x2.setLength(y.length);
	result = x2 % y;
	result.check();
	return result;
}
DEC operator%(const DEC& x, const int& y){
	DEC y2 = y, result;
	y2.setLength(x.length);
	result = x % y2;
	result.check();
	return result;
}
DEC operator^(const DEC& x, const int& y){
	if (y == 0)
		return DEC(1,x.length);
	DEC p = x;
	for (int i = y; i > 1; i--)
		p = p * x;
	return p;
}
void operator++(DEC& y){
	DEC one(1,y.length);
	y = y + one;
}
void operator--(DEC& y){
	DEC one(1,y.length);
	y = y - one;
}
ostream& operator<< (ostream& os, const DEC& num) {
	int a = 0;
	if (num.sign == 'n') os << '-';
	for(a=0;a<(num.digits.size());a++)
		os << num.digits[a];
	os << '.';
	for(a=0;a < num.postdec.size();a++){
		os << num.postdec[a];
	}
	if (num.postdec.size() == 1 && num.postdec[0] == 0)
		return os;
	else
		if (a != num.length)
			os << '0';
	return os;
}
istream& operator>> (istream& is, DEC& num) {
	num.digits.clear();
	num.postdec.clear();
	int a = 0;
	int start = 0;
	string number;
	is >> number;
	if (number.at(0) == '-') num.sign='n';
	if (number.at(0) == '+') num.sign='p';
	if ( !isdigit(number.at(0)) && number.at(0) != '.') start = 1;
	a=start;
	while (number.at(a)!='.'){
		num.digits.push_back(number.at(a) - '0');
		a++;
		if (a>=(number.length()))
			break;
	}
	a++;
	for (;a<number.length();a++)
		num.postdec.push_back(number.at(a) - '0');
	num.check();
	return is;
}

//DEC DEC::sqrt(){
//	DEC x;
//	if (this->digits.size() > 1)
//		for (int a = this->digits.size();a > 1;a--)
//			x.digits.push_front(1);
//	else
//		x = 1;
//	for(int a = 0;a < 9;a++){
//		x = (x + (*this / x))/2;
//		////////cout << x << endl;
//	}
//	return x;
//}

DEC DEC::pwr(const int& y){
	if (y == 0)
		return DEC(1,length);
	DEC p = *this;
	for (int i = y; i > 1; i--)
		p = p * *this;
	return p;
}

DEC DEC::pwr(const Fraction& f){
	DEC result(0,length);
	result = (*this^(f.num)).root(f.den);
	return result;
}

DEC DEC::sqrt(){
	DEC x(1,length), xcopy, xcopy2;
	int count = 0, count2 = 0;
	while (x.digits.size() < (digits.size() / 2) + 1){//gets x closer to size of answer
		x = x * 10;
	}
	while (xcopy != x && count2 <=0){//runs formula until accurate at set length
		if (count%2 == 0)
			xcopy2 = x;
		xcopy = x;
		x = x + (1 / (2 * x)) * (*this - (x * x));
		if (xcopy2 == x)
			count2++;
		count++;

	}
	return x;
}

DEC DEC::root(DEC n){
	DEC x(1,length), xcopy, xcopy2;
	int count = 0, count2 = 0;
	while (x.digits.size() < (digits.size() / int(n)) + 1){//gets x closer to size of answer
		x = x * 10;
	}
	while (xcopy != x && count2 <=0){//runs formula until accurate at set length
		if (count%2 == 0)
			xcopy2 = x;
		xcopy = x;
		//x = ((1-(1/n))*x) + ((1/n)*((*this)/(x^(n-1))));//little slower
		x = x - (((x^n)-(*this))/(n*(x^((n-1)))));//little faster
		//cout << x << endl;
		if (xcopy2 == x)
			count2++;
		count++;
	}
	return x;
}

DEC DEC::factorial(){
	if (*this == 0)
		return DEC(1,length);
	if (postdec.size() == 1 && postdec[0] == 0){
		DEC x = *this, result = *this;
		x.sign = 'p';
		x = x - 1;
		for (;x > 0;x--){
			result = result * x;
		}
		if (sign == 'n')
			result.sign = 'n';
		return result;
	}
	else
		cout << "factorial must be whole" << endl;
	return DEC(0,length);
}

DEC DEC::dubfactorial(){
	if (*this == -1 || *this == 0 || *this == 1)
		return DEC(1,length);		
	if (postdec.size() == 1 && postdec[0] == 0 && sign == 'p'){
		DEC x = *this, result = *this;
		x = x - 2;
		for (;x > 0;x=x-2){
			result = result * x;
		}
		return result;
	}
	else
		cout << "factorial must be positive whole" << endl;
	return DEC(0,length);
}

DEC DEC::sin(){
	DEC x = *this, result = *this, fact(3,length);
	for (int exp = 3, i = 0; i < 20; i++,exp += 2, fact = fact + 2){
		if (i % 2 == 0)
			result = result - ((x^exp)/(fact.factorial()));
		else
			result = result + ((x^exp)/(fact.factorial()));
	}
	return result;
}

//DEC DEC::cos(){
//	DEC x = *this, result, factn = 0, neg(-1);
//	for (int n = 0; n < 15; n++, factn = factn + 1){
//		result = result + (((neg^n)*(x^(2*n)))/((2*factn).factorial()));
//	}
//	return result;
//}

DEC DEC::cos(){
	DEC x = *this, result = (1,length), fact = (2,length);
	for (int exp = 2, i = 0; i < 20; i++,exp += 2, fact = fact + 2){
		if (i % 2 == 0)
			result = result - ((x^exp)/(fact.factorial()));
		else
			result = result + ((x^exp)/(fact.factorial()));
	}
	return result;
}

DEC DEC::tan(){
	return (sin()/cos());
}

DEC DEC::arctan(){
	DEC x = *this, result = *this, stop;
	for (int n = 3, i = 0; 1 ; i++,n += 2){
		if (i % 2 == 0)
			result = result - ((x^n)/n);
		else
			result = result + ((x^n)/n);
		if (stop == result)
			return result;
		stop = result;
		//cout << result << endl;
	}
	return result;
}

DEC DEC::arcsin(){
	DEC x = *this, result = *this, stop,coe(1),mult,a(1),b(2);
	coe.length = x.length;
	mult.length = x.length;
	a.length = x.length;
	b.length = x.length;
	for (int n = 3; 1 ;n+=2){
		coe = coe * (a/b);
		a = a + 2;
		b = b + 2;
		result = result + (coe * ((x^n)/n));
		if (stop == result)
			return result;
		stop = result;
		//cout << result << endl;
	}
	return result;
}


