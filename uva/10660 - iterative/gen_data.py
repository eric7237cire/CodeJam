import sys
import random
random.seed()

if 1==2:
	for r in range(0, 5):
		for c in range(0, 5):
			arr = []
			for r2 in range(0, 5):
				for c2 in range(0, 5):
					arr.append( abs(r-r2) + abs(c - c2) )
					
			"-".join
			print ('{' + ', '.join(str(x) for x in arr) + '},')

	sys.exit()

T = 5
print(str(T))

for t in range(T):
	arr = [[0 for i in range(5)] for j in range(5)]
		
	m = 0
	for r in range(0, 5):
		for c in range(0, 5):
	
			pop = random.randint(0, 10000)
			if pop > 0:
				m = m + 1
			
			arr[r][c] = pop
			
	print(str(m))
	for r in range(0, 5):
		for c in range(0, 5):
		
			if pop > 0:
				print( str(r) + " " + str(c) + " " + str( arr[r][c] ) )
	
	


