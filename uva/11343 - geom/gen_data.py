import sys
import random
random.seed()
T = 500000
print(str(T))

MAX = 10
PTS = 100

for i in range(T):
	#n = random.randint(0, PTS)
	n = 2
	print(str(n))
	for p in range(n):
		pts = []
		for pt in range(4):
			pts.append( random.randint(0, MAX) )
			
		while pts[2] == pts[0] and pts[1] == pts[3] :
			pts[2] = random.randint(0, MAX)
			pts[3] = random.randint(0, MAX)
			
		print( " ".join( [ str(x) for x in pts ] ) )
		