#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <cstring>
#include <algorithm>

using namespace std;

#define mp make_pair 
#define pb push_back 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define all(c) (c).begin(),(c).end() 
#define MIN(a,b) ((a)<(b)?(a):(b))
#define MAX(a,b) ((a)<(b)?(b):(a))

typedef unsigned int uint;
typedef long long ll;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<bool> vb;
typedef vector<vb> vvb;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
typedef vector<ii> vii;
typedef vector<vii> vvii;


const double tolerance = 1e-15;
template<typename T> 
int cmp(T a, T b, T epsilon = tolerance)
{
	T dif = a - b;
	if (abs(dif) <= epsilon)
	{
		return 0;
	}
	
	if (dif > 0)
	{
		return 1; //a > b
	}
	
	return -1;
}  

///////begin
//you can change the size to your requirement(here (1<<16) means 2^16)
const int MAX_INPUT_BUFFER_SIZE = 1 << 16 ;
const int MAX_OUTPUT_BUFFER_SIZE = 1 << 22 ;
size_t inputBufferSize = 0;
size_t curCharInputBuffer = 0;
size_t curOutputBufferSize = 0;
int  bufL2 = 0 ;
char inputBuffer[ MAX_INPUT_BUFFER_SIZE ];
char outputBuffer[ MAX_OUTPUT_BUFFER_SIZE ] ;

char next_char() 
{
	//this means that nothing is loaded into the bufL1 array 
	if( curCharInputBuffer == inputBufferSize ) 
	{
		inputBufferSize = fread( inputBuffer , sizeof( char ), MAX_INPUT_BUFFER_SIZE , stdin ) ;//let's load the array using fread 
		//this is just to check if there's no input
		if( inputBufferSize == 0 ) 
		{ 
			return 0; 
		}
		
		curCharInputBuffer = 0 ;
	}
 
	return inputBuffer[ curCharInputBuffer++ ] ;//nowL1 points to the current character in the buf1 array
}

char next_nonwsChar()
{
	char c;
	do {
		c = next_char();
		//printf("1 Read %c\n", c);
	} while( isspace(c) );
	
	return c;
}

void next_int(int& val) 
{
	char c ;
	do {
		c = next_char() ;
	} while( c < '-' ) ;//this while loop will continue until it finds '-' or ' ' or '\n'

	val = 0 ;
	if( c == '-' ) 
	{
		//this means the current integer is negative
		for( c = next_char() ; c >= '0' ; c = next_char() ) 
		{
			val = val * 10 + ( c - '0' ) ;//get the integer
		}
		val *= -1; ;//return the negative number
	}
	else 
	{
		for( ; c >= '0' ; c = next_char() ) 
		{
			val = val * 10 + ( c - '0' ) ;
        }
		//return the positive number
	}
}

void next_string(char* strBuf)
{
	char c;
	do {
		c = next_char();
		//printf("1 Read %c\n", c);
	} while( isspace(c) );
	
	do {
		
		*strBuf++ = c;
		c = next_char();
		//printf("2 Read %c\n", c);
		
	} while( !isspace(c) );
	
	/*do {
		c = next_char();
		printf("3 Read %c\n", c);
	} while( isspace(c) );
	*/
}

void write_flush() 
{
	fwrite( outputBuffer , sizeof( char ) , curOutputBufferSize , stdout ) ;//flushes the output to stdout
	curOutputBufferSize = 0 ;//pointer returns to the beginning since output buffer array(buf2) has been flushed
}

void write_char( char c ) 
{
	outputBuffer[ curOutputBufferSize++ ] = c ;//bufL2 points to current index in the output buffer array(buf2)  
	if( curOutputBufferSize == MAX_OUTPUT_BUFFER_SIZE ) 
	{//array full so let's flush it
		write_flush() ;
	}
}

void write_str(  const char * str )
{
	while( *str != '\0' )
	{
		write_char( *str++ );
	}
}

const char digit_pairs[201] = {
  "00010203040506070809"
  "10111213141516171819"
  "20212223242526272829"
  "30313233343536373839"
  "40414243444546474849"
  "50515253545556575859"
  "60616263646566676869"
  "70717273747576777879"
  "80818283848586878889"
  "90919293949596979899"
};

void write_int( int val ) 
{
	if(val==0)
    {
        write_char('0');
		return;
    }
	
	int size;
    if(val>=10000)
    {
        if(val>=10000000)
        {
            if(val>=1000000000)
                size=10;
            else if(val>=100000000)
                size=9;
            else 
                size=8;
        }
        else
        {
            if(val>=1000000)
                size=7;
            else if(val>=100000)
                size=6;
            else
                size=5;
        }
    }
    else 
    {
        if(val>=100)
        {
            if(val>=1000)
                size=4;
            else
                size=3;
        }
        else
        {
            if(val>=10)
                size=2;
            else
                size=1;
        }
    }
	
	char* c = &outputBuffer[ curOutputBufferSize ];
	curOutputBufferSize += size;
	
	c += size - 1;
	
	while(val>=100)
    {
       int pos = val % 100;
       val /= 100;
       *(short*)(c-1)=*(short*)(digit_pairs+2*pos); 
       c-=2;
    }
    while(val>0)
    {
        *c--='0' + (val % 10);
        val /= 10;
    }
	
	//recursive function to load the number to output buffer array(buf2)
	/*if( n >= 10 ) 
	{
		write_int( n / 10 ) ;
	}
	write_char( '0' + n % 10 ) ;*/
}
////end