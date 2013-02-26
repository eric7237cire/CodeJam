import random
nCases = 100
minX = -40
maxX = 40
minY = -40
maxY = 40
nPoints = 400
print(nCases)
print("")
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

