import sys
import random
random.seed()
T = 5

 

print( str(T) )

for t in range(T):
	v = random.randint(2, 9)
	
	edges = []
	
	s = set()
	
	for i in range(1, v):
		atLeastOne = False 
		
		while not atLeastOne:
			for j in range(i+1, v+1):
				hasEdge = random.randint(0, 1)
				if hasEdge == 0:
					continue;
				
				weight = random.randint(1, 100000)
				edges.append( (i, j, weight) )
				s.add( (i, j) )
				atLeastOne = True 
	
	print( str(v) + " " + str( len(edges) ) )
	
	for edge in edges:
		print ( str(edge[0]) + " " + str(edge[1]) + " " + str(edge[2]) )
	


print("0")