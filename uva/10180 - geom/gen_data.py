import sys
import random
import math 
random.seed()
T = 30000


MAX = .1



print( str(T) )

for t in range(T):
	r = random.random() * MAX
	p = 0
	list = []
	
	while len(list) < 4:
		x = -MAX + random.random() * MAX * 2
		y = -MAX + random.random() * MAX * 2
		if math.hypot(x, y) <= r + .00001:
			continue
			
		list.append(x)
		list.append(y)
	
	list.append(r)
	
	print( " ".join([ "%.5f" % x for x in list ] ) )
	


