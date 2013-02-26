import random
nCases = 10
minX = -10
maxX = 10
minY = -10
maxY = 10
nPoints = 15
print(nCases)
for i in range(nCases):
	s = set()
	for j in range(nPoints):
		#print( j)
		x = random.randint(minX, maxX)
		y = random.randint(minY, maxY)
		if (x, y) in s:
			continue
		s.add( (x, y) )
		print(x , end=" " )
		print(y , end= "\n")
	print("")
	#print("\n")

