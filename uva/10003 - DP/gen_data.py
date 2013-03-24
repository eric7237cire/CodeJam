import sys
import random
random.seed()
T = 1000
MINX = 1
MAXX = 512
MINY = 1
MAXY = 512

for t in range(T):
	l = random.randint(2, 1000)
	
	print( str(l) )
	
	nCuts = random.randint(1, min(50, l - 1))
	
	cuts = []
	
	while len(cuts) < nCuts:
		cut = random.randint(1, l - 1)
		if not cut in cuts:
			cuts.append(cut)
			
	print( str( len(cuts) ))
	cuts.sort
	
	print (" ".join(str(x) for x in sorted(cuts)))
		


print("0")