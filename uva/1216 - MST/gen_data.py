import sys
import random
random.seed()
T = 5

MINY = -100000
MAXY = 100000
MINX = -100000
MAXX = 100000

MIN_PTS = 1 
MAX_PTS = 10
MIN_SEN = 3
MAX_SEN = 10

print( str(T) )

for t in range(T):
	n = random.randint(MIN_PTS, MAX_PTS)
	sen = random.randint(MIN_SEN, MAX_SEN)
	
	s = set() 
	print( str(sen) )
	
	while len(s) < n:
		x = random.randint(MINX, MAXX)
		y = random.randint(MINY, MAXY)
		
		pt = (x, y)
		s.add(pt)
		print( str(x) + ' ' + str(y)  )
	
	print("-1")


print("0")