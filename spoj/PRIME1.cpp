#include "stdio.h"

#include <vector>
#include <cmath>
#include <cstring>
using namespace std;


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

typedef unsigned int uint;

bool miller_rabin_32(uint n);

bool isPrime(uint n) {
	return miller_rabin_32(n);
}

int modular_exponent_32(int base, int power, int modulus) {
	unsigned long long result = 1;
	for (int i = 31; i >= 0; i--) {
		result = (result*result) % modulus;
		if ((power & (1 << i)) != 0) {
			result = (result*base) % modulus;
		}
	}
	return (int)result; // Will not truncate since modulus is an int
}




bool miller_rabin_pass_32(int a, int n) {
	int d = n - 1;
	int s = 0;

	while ((d % 2) == 0) {
		d /= 2;
		s++;
	}

	int a_to_power = modular_exponent_32(a, d, n);
	if (a_to_power == 1) return true;
	for (int i = 0; i < s-1; i++) {
		if (a_to_power == n-1) return true;
		a_to_power = modular_exponent_32(a_to_power, 2, n);
	}
	if (a_to_power == n-1) return true;
	return false;
}

bool miller_rabin_32(uint n) {
	if (n <= 1) return false;
	else if (n == 2) return true;
	else if (miller_rabin_pass_32( 2, n) &&
		(n <= 7  || miller_rabin_pass_32( 7, n)) &&
		(n <= 61 || miller_rabin_pass_32(61, n)))
		return true;
	else
		return false;
}



const int MAX_SIEVE = 1000000;

vector<int> primes;
bool vbIsPrime[MAX_SIEVE+1];

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)

void generatePrimes( int maxPrime ) 
{
	memset( vbIsPrime, 1, sizeof vbIsPrime); //.assign(maxPrime + 1, true);
	vbIsPrime[0] = false;
	vbIsPrime[1] = false;
	
	primes.clear();

	//Since we are eliminating via prime factors, a factor is at most sqrt(n)
	int upperLimit = static_cast<int>(sqrt(maxPrime));

	for(int i = 2; i <= upperLimit; ++i) {
		if (!vbIsPrime[i]) {
			continue;
		}

		//Loop through all multiples of the prime factor i.  Start with i*i, because the rest
		//were already covered by previous factors.  Ex, i == 7, we start at 49 because 7*2 through 7*6 
		//we already covered by previous prime factors.
		for(int j = i * i; j <= maxPrime; j += i) {
			vbIsPrime[j] = false;
		}
	}

	for(int i = 0; i <= maxPrime; ++i) {
		if (vbIsPrime[i])
			primes.push_back(i);
	}

}

//1 000 000 000

//const int MAX_SIEVE = 31623;

int main()
{
	generatePrimes(MAX_SIEVE);
	int m,n;
	int T;
	next_int(T); //scanf("%d", &T);
	for(int t=1; t <= T; ++t)
	{
		next_int(m);
		next_int(n);
		//scanf("%d%d", &m, &n);
				
		FORE(i, m, n)
		{
			if (i <= MAX_SIEVE)
			{
				if (vbIsPrime[i])
				{
					write_int(i);
					write_char('\n');
				}
					
					
					
				continue;
			}
			
			if (isPrime(i))
			{
				write_int(i);
				write_char('\n');
			}
//				printf("%d\n", i);
				
				/*
			continue;
			
			bool ok = true;
			FOR(pIdx, 0, primes.size())
			{
				if (primes[pIdx] >= i)
					break;
					
				if (i % primes[pIdx] == 0)
				{
					ok = false;
					break;
				}
			}
			if (ok)
				printf("%d\n", i);*/
		}
		
		if (t != T)
			write_char('\n'); //printf("\n");
	}
	
	write_flush();
	return 0;   
}
