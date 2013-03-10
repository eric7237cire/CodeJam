import sys
import random
import itertools
a = [12,14,41,65,34,27,84,26,99,34]
D = 3
M = 2
count = 0
for com in itertools.combinations(a, M):
		#print(com)
		s = sum(com)
		#print(s)
		#print(s % 2)
		if (s % D == 0):
			print(com)
			count = count + 1

print (count)