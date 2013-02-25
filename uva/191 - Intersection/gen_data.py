import random
nCases = 100000
minX = -100
maxX = 100
minY = -100
maxY = 100
print(nCases)
for i in range(nCases):
	for j in range(4):
		#print( j)
		print( random.randint(minX, maxX), end=" " )
		print( random.randint(minY, maxY), end= "\n" if j == 3 else  " " )
	#print("\n")

