import random

for i in range(10):
	pts = 0
	nPts = random.randint(3, 300)
	while pts < nPts:
		x = random.randint(0, 999)
		y = random.randint(0, 999)
		if x == 0 & y == 0 :
			continue
		print(x, y)
		pts = pts + 1
	print("0 0")


message="""
  5 5 8 7 14 11 4 8   20 15
12 6  18 21 0  0
5 5 8 8 14 13 0 0
5 5 25 17 20 23 10 11 20 14 15 11 0 0
1619 1866
2 0 0 2
8 0
100 101
117 8
16 0
75 76
1 1 2 2 3 3 117 14 4 4 5 5 6 6 7 7 8 8 9 9 10 10
 2 1 3 1 4 1 9999 1
 117 45 
 50 51
 75 26
 100 1
 
 
0 0
0 0
"""
print(message)
