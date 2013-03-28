/* 5965387  	Prime Distance  	Accepted  	C++  	0.060  	2007-10-08 09:08:43 */
/* large prime */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

const int SIEVE_MAX = 1000020;
char sieve_list[SIEVE_MAX];
char FALSE = 1;
char TRUE = 0;
int SQRT_U;

int i, j, dist;
void sieve (unsigned int L, unsigned int U)
{
	dist = U - L + 1;

	// mark all to be TRUE
	memset (sieve_list, TRUE, sizeof (sieve_list));

	// mark even to be FALSE
	if (L & 1)
	{
		i = L + 1;
	}
	else
	{
		i = L;
	}

	i = (L % 2 != 0);

	while(i < dist)
	{
		sieve_list[i] = FALSE;
		i += 2;
	}

	SQRT_U = sqrt((double)U) + 1;

	for (i = 3; i <= SQRT_U; i += 2)
	{
		if ((i > L) &&
			(sieve_list[i - L] == FALSE))
		{
			continue;	
		}

		j = (L / i) * i;

		if (j < L)
		{
			j += i;
		}

		if (j == i)
		{
			j += i; // if j is a prime number, have to start from next one
		}

		// now start sieving
		j -= L; // change j to the index representing j

		while (j <= dist)
		{
			sieve_list[j] = FALSE;
			j += i;
		}
	}

	if (L <= 1)
	{
		sieve_list[1 - L] = FALSE;
	}

	if (L <= 2)
	{
		sieve_list[2 - L] = TRUE;
	}
}

unsigned int a, b;
int diff, max;
unsigned int min;
unsigned int n1, n2, n3, n4, lastPrime;
int main()
{
	while (scanf ("%u %u", &a, &b) == 2)
	{
		sieve(a, b);

		n1 = n2 = n3 = n4 = 0;
		max = 0;
		min = b + 1;
		lastPrime = -1;
		
		b -= a;

		for (i = 0; i <= b; ++i)
		{
			if (sieve_list[i] == TRUE)
			{
				if (lastPrime != -1)
				{
					diff = i - lastPrime;

					if (max < diff)
					{
						n1 = lastPrime;
						n2 = i;
						max = diff;
					}

					if (diff >=0 && min > diff)
					{
						n3 = lastPrime;
						n4 = i;
						min = diff;
					}
				}

				lastPrime = i;
			}
		}

		n1 += a;
		n2 += a;
		n3 += a;
		n4 += a;

		if ((n1 && n2 && n3 && n4) &&
			n1 != n2 &&
			n3 != n4)
		{
			printf ("%u,%u are closest, %u,%u are most distant.\n", n3, n4, n1, n2);
		}
		else
		{
			puts ("There are no adjacent primes.");
		}
	}

	return 0;
}