#include <iostream>
using namespace std;

#include <stdlib.h>
#include <ctype.h>
#include <limits.h>
#include "bigint.h"
#include "tvector.h"

// #define DEBUG

const int BASE = 10;

// author: Owen Astrachan
//
// BigInts are implemented using a Vector<char> to store
// the digits of a BigInt
// Currently a number like 5,879 is stored as the vector {9,7,8,5}
// i.e., the least significant digit is the first digit in the vector;
// for example, GetDigit(0) returns 9 and getDigit(3) returns 5.
// All operations on digits should be done using private
// helper functions:
//
// int  GetDigit(k)        -- return k-th digit
// void ChangeDigit(k,val) -- set k-th digit to val
// void AddSigDigit(val)   -- add new most significant digit val
//
// by performing all ops in terms of these private functions we
// make implementation changes simpler
//
// I/O operations are facilitated by the ToString() member function
// which converts a BigInt to its string (ASCII) representation

BigInt::BigInt()
// postcodition: bigint initialized to 0
   : mySign(positive),
     myDigits(1,'0'),
     myNumDigits(1)
{
	// all fields initialized in initializer list
#ifdef DEBUG    
        cout << "default constructor called" << endl;
#endif	
}

BigInt::BigInt(const BigInt & rhs)
    : mySign(rhs.mySign),
      myDigits(rhs.myDigits),
      myNumDigits(rhs.myNumDigits)
{
#ifdef DEBUG    
    cout << "copy constructor called with " << rhs << endl;
#endif    
}

BigInt::~BigInt()
{
#ifdef DEBUG
    cout << "destructor called for " << *this << endl;
#endif

}

const BigInt & BigInt::operator = (const BigInt & rhs)
{
#ifdef DEBUG
	cout << "assignment operator called with " << rhs << endl;
#endif

	mySign = rhs.mySign;
	myNumDigits = rhs.myNumDigits;
	myDigits = rhs.myDigits;
	return *this;
}



BigInt::BigInt(int num)
// postcondition: bigint initialized to num
   : mySign(positive),
     myDigits(1,'0'),
     myNumDigits(0)
{
    // check if num is negative, change state and num accordingly

   unsigned int copy;   // use this to avoid problems with INT_MIN != -INT_MAX
 
#ifdef DEBUG    
    cout << "int constructor called  " << num << endl;
#endif   
    if (num < 0)
    {   mySign = negative;
        if (num == INT_MIN)
        {   copy = unsigned(INT_MAX) + 1;
        }
        else
        {   copy = -1 * num;
        }
    }
    else
    {   copy = num;
    }

    // handle least-significant digit of num (handles num == 0)

    AddSigDigit(copy % BASE);
    copy = copy / BASE;

    // handle remaining digits of num

    while (copy != 0)
    {   AddSigDigit(copy % BASE);
        copy = copy / BASE;
    }
}


BigInt::BigInt(const string & s)
// precondition: s consists of digits only, optionally preceded by + or -
// postcondition: bigint initialized to integer represented by s
//                if s is not a well-formed BigInt (e.g., contains non-digit
//                characters) then an error message is printed and abort called
  : mySign(positive),
    myDigits(s.length(),'0'),
    myNumDigits(0)
{
    int k;
    int limit = 0;

#ifdef DEBUG    
    cout << "string constructor called s = " << s << endl;
#endif   

    if (s.length() == 0)
    {   myDigits.resize(1);
        AddSigDigit(0);
        return;
    }
    if (s[0] == '-')
    {   mySign = negative;
        limit = 1;
    }
    if (s[0] == '+')
    {   limit = 1;
    }
    // start at least significant digit

    for(k=s.length() - 1; k >= limit; k--)
    {   if (! isdigit(s[k]))
        {   cerr << "badly formed BigInt value = " << s << endl;
            abort();
        }
        AddSigDigit(s[k]-'0');
    }
    Normalize();
}


const BigInt & BigInt::operator -=(const BigInt & rhs)
// postcondition: returns value of bigint - rhs after subtraction
{
    int diff;
    int borrow = 0;

    int k;
    int len = NumDigits();

    if (this == &rhs)         // subtracting self?
    {
        *this = 0;
        return *this;
    }

    // signs opposite? then lhs - (-rhs) = lhs + rhs

    if (IsNegative() != rhs.IsNegative())
    {
        *this +=(-1 * rhs);
        return *this;
    }
    // signs are the same, check which number is larger
    // and switch to get larger number "on top" if necessary
    // since sign can change when subtracting
    // examples: 7 - 3 no sign change,     3 - 7 sign changes
    //          -7 - (-3) no sign change, -3 -(-7) sign changes
    if (IsPositive() && (*this) < rhs ||
        IsNegative() && (*this) > rhs)
    {
        *this = rhs - *this;
        if (IsPositive()) mySign = negative;
        else              mySign = positive;   // toggle sign
        return *this;
    }
    // same sign and larger number on top

    for(k=0; k < len; k++)
    {
        diff = GetDigit(k) - rhs.GetDigit(k) - borrow;
        borrow = 0;
        if (diff < 0)
        {   diff += BASE;
            borrow = 1;
        }
        ChangeDigit(k,diff);
    }
    Normalize();
    return *this;
}

const BigInt & BigInt::operator +=(const BigInt & rhs)
// postcondition: returns value of bigint + rhs after addition
{

    int sum;
    int carry = 0;

    int k;
    int len = NumDigits();         // length of larger addend

    if (this == &rhs)              // to add self, multiply by 2
    {
      *this *= 2;
      return *this;
    }

    if (rhs == 0)                   // zero is special case
    {
   return *this;
    }

    if (IsPositive() != rhs.IsPositive())    // signs not the same, subtract
    {
        *this -= (-1 * rhs);
        return *this;
    }

    // process both numbers until one is exhausted

    if (len < rhs.NumDigits())
    {
        len = rhs.NumDigits();
    }
    for(k=0; k < len; k++)
    {
        sum = GetDigit(k) + rhs.GetDigit(k) + carry;
        carry = sum / BASE;
        sum = sum % BASE;

        if (k < myNumDigits)
        {
            ChangeDigit(k,sum);
        }
        else
        {
            AddSigDigit(sum);
        }
    }
    if (carry != 0)
    {
        AddSigDigit(carry);
    }
    return *this;
}

BigInt operator +(const BigInt & lhs, const BigInt & rhs)
// postcondition: returns a bigint whose value is lhs + rhs
{
    BigInt result(lhs);
    result += rhs;
    return result;
}

BigInt operator -(const BigInt & lhs, const BigInt & rhs)
// postcondition: returns a bigint whose value is lhs + rhs
{
    BigInt result(lhs);
    result -= rhs;
    return result;
}

string BigInt::ToString() const
// postcondition: returns string equivalent of BigInt
{
    int k;
    int len = NumDigits();
    string s = "";

    if (IsNegative())
    {
        s = '-';
    }
    for(k=len-1; k >= 0; k--)
    {
        s += char('0'+GetDigit(k));
    }
    return s;
}

int BigInt::ToInt() const
// precondition: INT_MIN <= self <= INT_MAX
// postcondition: returns int equivalent of self
{
    int result = 0;
    int k;
    if (INT_MAX < *this) return INT_MAX;
    if (*this < INT_MIN) return INT_MIN;

    for(k=NumDigits()-1; k >= 0; k--)
    {
        result = result * BASE + GetDigit(k);
    }
    if (IsNegative())
    {
        result *= -1;
    }
    return result;
}

double BigInt::ToDouble() const
// precondition: DBL_MIN <= self <= DLB_MAX
// postcondition: returns double equivalent of self
{
    double result = 0.0;
    int k;
    for(k=NumDigits()-1; k >= 0; k--)
    {
        result = result * BASE + GetDigit(k);
    }
    if (IsNegative())
    {
        result *= -1;
    }
    return result;
}

ostream & operator <<(ostream & out, const BigInt & big)
// postcondition: big inserted onto stream out
{
    out << big.ToString();
    return out;
}

istream & operator >> (istream & in, BigInt & big)
// postcondition: big extracted from in, must be whitespace delimited
{
    string s;
    in >> s;
    big = BigInt(s);
    return in;
}

bool operator == (const BigInt & lhs, const BigInt & rhs)
// postcondition: returns true if lhs == rhs, else returns false
{
    return lhs.Equal(rhs);
}

bool BigInt::Equal(const BigInt & rhs) const
// postcondition: returns true if self == rhs, else returns false
{

    if (NumDigits() != rhs.NumDigits() || IsNegative() != rhs.IsNegative())
    {
        return false;
    }
    // assert: same sign, same number of digits;

    int k;
    int len = NumDigits();
    for(k=0; k < len; k++)
    {
        if (GetDigit(k) != rhs.GetDigit(k)) return false;
    }

    return true;
}

bool operator != (const BigInt & lhs, const BigInt & rhs)
// postcondition: returns true if lhs != rhs, else returns false
{
    return ! (lhs == rhs);
}

bool operator < (const BigInt & lhs, const BigInt & rhs)
// postcondition: return true if lhs < rhs, else returns false
{
    return lhs.LessThan(rhs);
}

bool BigInt::LessThan(const BigInt & rhs) const
// postcondition: return true if self < rhs, else returns false
{
    // if signs aren't equal, self < rhs only if self is negative

    if (IsNegative() != rhs.IsNegative())
    {
        return IsNegative();
    }

    // if # digits aren't the same must check # digits and sign

    if (NumDigits() != rhs.NumDigits())
    {
        return (NumDigits() < rhs.NumDigits() && IsPositive()) ||
               (NumDigits() > rhs.NumDigits() && IsNegative());
    }

    // assert: # digits same, signs the same

    int k;
    int len = NumDigits();

    for(k=len-1; k >= 0; k--)
    {
        if (GetDigit(k) < rhs.GetDigit(k)) return IsPositive();
        if (GetDigit(k) > rhs.GetDigit(k)) return IsNegative();
    }
    return false;      // self == rhs
}

bool operator > (const BigInt & lhs, const BigInt & rhs)
// postcondition: return true if lhs > rhs, else returns false
{
    return rhs < lhs;
}

bool operator <= (const BigInt & lhs, const BigInt & rhs)
// postcondition: return true if lhs <= rhs, else returns false
{
    return lhs == rhs || lhs < rhs;
}
bool operator >= (const BigInt & lhs, const BigInt & rhs)
// postcondition: return true if lhs >= rhs, else returns false
{
    return lhs == rhs || lhs > rhs;
}

void BigInt::Normalize()
// postcondition: all leading zeros removed
{
    int k;
    int len = NumDigits();
    for(k=len-1; k >= 0; k--)        // find a non-zero digit
    {
        if (GetDigit(k) != 0) break;
        myNumDigits--;               // "chop" off zeros
    }
    if (k < 0)    // all zeros
    {
        myNumDigits = 1;
        mySign = positive;
    }
}


const BigInt & BigInt::operator *=(int num)
// postcondition: returns num * value of BigInt after multiplication
{
    int carry = 0;
    int product;               // product of num and one digit + carry
    int k;
    int len = NumDigits();

    if (0 == num)              // treat zero as special case and stop
    {
        *this = 0;
        return *this;
    }

    if (BASE < num|| num < 0)        // handle pre-condition failure
    {
        *this *= BigInt(num);
        return *this;
    }

    if (1 == num)              // treat one as special case, no work
    {
        return *this;
    }

    for(k=0; k < len; k++)     // once for each digit
    {
        product = num * GetDigit(k) + carry;
        carry = product/BASE;
        ChangeDigit(k,product % BASE);
    }

    while (carry != 0)         // carry all digits
    {
        AddSigDigit(carry % BASE);
        carry /= BASE;
    }
    return *this;
}


BigInt operator *(const BigInt & a, int num)
// postcondition: returns a * num
{
    BigInt result(a);
    result *= num;
    return result;
}

BigInt operator *(int num, const BigInt & a)
// postcondition: returns num * a
{
    BigInt result(a);
    result *= num;
    return result;
}


const BigInt & BigInt::operator *=(const BigInt & rhs)
// postcondition: returns value of bigint * rhs after multiplication
{
    // uses standard "grade school method" for multiplying

   if (IsNegative() != rhs.IsNegative())
   {
      mySign = negative;
   }
   else
   {
      mySign = positive;
   }

    BigInt self(*this);                        // copy of self
    BigInt sum(0);                             // to accumulate sum
    int k;
    int len = rhs.NumDigits();                 // # digits in multiplier

    for(k=0; k < len; k++)
    {
       sum += self * rhs.GetDigit(k);          // k-th digit * self
       self *= BASE;                           // add a zero
    }
    *this = sum;
    return *this;
}

BigInt operator *(const BigInt & lhs, const BigInt & rhs)
// postcondition: returns a bigint whose value is lhs * rhs
{
    BigInt result(lhs);
    result *= rhs;
    return result;
}

int BigInt::NumDigits() const
// postcondition: returns # digits in BigInt
{
    return myNumDigits;
}

int BigInt::GetDigit(int k) const
// precondition: 0 <= k < NumDigits()
// postcondition: returns k-th digit
//                (0 if precondition is false)
//                Note: 0th digit is least significant digit
{
    if (0 <= k && k < NumDigits())
    {
        return myDigits[k] - '0';
    }
    return 0;
}

void BigInt::ChangeDigit(int k, int value)
// precondition: 0 <= k < NumDigits()
// postcondition: k-th digit changed to value
//                Note: 0th digit is least significant digit
{
    if (0 <= k && k < NumDigits())
    {
        myDigits[k] = char('0' + value);
    }
    else
    {
        cerr << "error changeDigit " << k << " " << myNumDigits << endl;
    }
}

void BigInt::AddSigDigit(int value)
// postcondition: value added to BigInt as most significant digit
//                Note: 0th digit is least significant digit
{
    if (myNumDigits >= myDigits.length())
    {
        myDigits.resize(myDigits.length() * 2);
    }
    myDigits[myNumDigits] = char('0' + value);
    myNumDigits++;
}

bool BigInt::IsNegative() const
// postcondition: returns true iff BigInt is negative
{
    return mySign == negative;
}

bool BigInt::IsPositive() const
// postcondition: returns true iff BigInt is positive
{
   return mySign == positive;
}


void BigInt::DivideHelp(const BigInt & lhs, const BigInt & rhs,
                        BigInt & quotient, BigInt & remainder)
// postcondition: quotient = lhs / rhs
//                remainder = lhs % rhs     
{
    if (lhs < rhs)             // integer division yields 0
    {                          // so avoid work and return
        quotient = 0;        
        remainder = lhs;
        return;
    }
    else if (lhs == rhs)       // again, avoid work in special case
    {
        quotient = 1;
        remainder = 0;
        return;
    }
    
    BigInt dividend(lhs);      // make copies because of const-ness
    BigInt divisor(rhs);
    quotient = remainder = 0;
    int k,trial;
    int zeroCount = 0;

    // pad divisor with zeros until # of digits = dividend
    
    while (divisor.NumDigits() < dividend.NumDigits())
    {
        divisor *= BASE;
        zeroCount++;
    }

    // if we added one too many zeros chop one off
    
    if (divisor > dividend)
    {
        divisor /= BASE;
        zeroCount--;
    }


    // algorithm: make a guess for how many times dividend part
    // goes into divisor, adjust the guess, subtract out and repeat
    
    int divisorSig = divisor.GetDigit(divisor.NumDigits()-1);
    int dividendSig;
    BigInt hold;    
    for(k=0; k <= zeroCount; k++)
    {
        dividendSig = dividend.GetDigit(dividend.NumDigits()-1);
        trial =
            (dividendSig*BASE + dividend.GetDigit(dividend.NumDigits()-2))
            /divisorSig;
        
        if (BASE <= trial)   // stay within base
        {
            trial = BASE-1;
        }
        while ((hold = divisor * trial) > dividend)
        {
            trial--;
        }

        // accumulate quotient by adding digits to quotient
        // and chopping them off of divisor after adjusting dividend
        
        quotient *= BASE;
        quotient += trial;
        dividend -= hold;
        divisor /= BASE;       
        divisorSig = divisor.GetDigit(divisor.NumDigits()-1);        
    }
    remainder = dividend;
}

const BigInt & BigInt::operator /=(const BigInt & rhs)
// postcondition: return BigInt / rhs after modification
{
    BigInt quotient,remainder;
    bool resultNegative = (IsNegative() != rhs.IsNegative());
    mySign = positive;      // force myself positive
    
    // DivideHelp does all the work
    
    if (rhs.IsNegative())
    {
        DivideHelp(*this,-1*rhs,quotient,remainder);
    }
    else
    {
        DivideHelp(*this,rhs,quotient,remainder);       
    }
    *this = quotient;
    mySign = resultNegative ? negative : positive;
    Normalize();
    return *this;
}

BigInt operator / (const BigInt & lhs, const BigInt & rhs)
// postcondition: return lhs / rhs
{
    BigInt result(lhs);
    result /= rhs;
    return result;
}

const BigInt & BigInt::operator /=(int num)
// precondition: 0 < num < BASE     
// postcondition: returns BigInt/num after modification
{
    int carry = 0;
    int quotient;
    int k;
    int len = NumDigits();

    if (num > BASE)     // check precondition
    {
        return operator /= (BigInt(num));
    }
    if (0 == num)       // handle division by zero 
    {
        cerr << "division by zero error" << endl;
		abort();
    }
    else
    {
        for(k=len-1; k >= 0; k--)  // once for each digit
        {
            quotient = (BASE*carry + GetDigit(k));
            carry = quotient % num;
            ChangeDigit(k, quotient / num);
        }
    }
    Normalize();
    return *this;
}

BigInt operator /(const BigInt & a, int num)
// postcondition: returns a / num     
{
    BigInt result(a);
    result /= num;
    return result;
}

BigInt operator %(const BigInt & lhs, const BigInt & rhs)
// postcondition: returns lhs % rhs     
{
    BigInt result(lhs);
    result %= rhs;
    return result;
}


const BigInt & BigInt::operator %=(const BigInt & rhs)
// postcondition: returns BigInt % rhs after modification     
{
    BigInt quotient,remainder;
    bool resultNegative = IsNegative();
    
    // DivideHelp does all the work
    
    if (rhs.IsNegative())
    {
        DivideHelp(*this,-1 * rhs,quotient,remainder);
    }
    else
    {
        DivideHelp(*this,rhs,quotient,remainder);       
    }
    *this = remainder;
    mySign = resultNegative ? negative : positive;   
    return *this;
}

