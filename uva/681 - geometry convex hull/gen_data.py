import sys
import random
random.seed()
T = 30
MINX = 1
MAXX = 512
MINY = 1
MAXY = 512

print(str(T))
for t in range(T):
	n = random.randint(3, 512)
	
	print( str(n) )
	
	x, y = random.randint(MINX, MAXX), random.randint(MINY, MAXY)
	print( str(x) + ' ' + str(y) )
	for p in range(2, n):
		x2, y2 = random.randint(MINX, MAXX), random.randint(MINY, MAXY)
		print( str(x2) + ' ' + str(y2) )
	
	print( str(x) + ' ' + str(y) )
	
	if t < T-1:
		print( str(-1) )
	

