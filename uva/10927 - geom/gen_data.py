import sys
import random
random.seed()
T = 5

MINN = 1
MAXN = 100000
MINY = 0
MAXY = 10000
MINX = -100000
MAXX = 100000
MINZ = 0
MAXZ = 10000 

if 1==2:
	MAXY = 10
	MINX = -10
	MAXX = 10
	MINZ = 0
	MAXZ = 10


for t in range(T):
	n = random.randint(MINN, MAXN)
	
	s = set() 
	print( str(n) )
	
	while len(s) < n:
		x = random.randint(MINX, MAXX)
		y = random.randint(MINY, MAXY)
		z = random.randint(MINZ, MAXZ)
		
		if x == 0 and y == 0:
			continue
		pt = (x, y)
		if pt in s:
			continue
			
		s.add(pt)
		print( str(x) + ' ' + str(y) + ' ' + str(z) )
			


print("0")