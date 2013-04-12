#include <stdio.h>
#include <string.h>
 
int main()
{
	char card[64][64], word[64][64], *order[64];
	int i, j, k, n;
 
	while (scanf("%d", &n) == 1 && n > 0) {
		for (i = 0; i < n; i++)
			scanf("%s %s", card[i], word[i]);
 
		for (i = 0; i < n; i++)
			order[i] = "?";
 
		for (i = j = 0; i < n; i++) {
			for (k = strlen(word[i]);; j = (j + 1) % n)
				if (order[j][0] == '?' && --k <= 0) break;
			order[j] = card[i];
		}
 
		for (i = 0; i < n; i++)
			printf(i+1<n ? "%s " : "%s\n", order[i]);
	}
 
	return 0;
}