import sys
import random
random.seed()
T = 2
MINX = 1
MAXX = 512
MINY = 1
MAXY = 512

print( str(T)  )

for t in range(T):
	print( "" )
	N = random.randint(1, 2)
	
	for n in range(N): 
		print( "%.1f" % (random.randint(0, 500) / 10.0) 
			+ " %.1f" % (random.randint(0, 500) / 10.0) )
	

