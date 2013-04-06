MAX = 300

for i in range(1, MAX):
	for j in range(i, MAX):
		for k in range(j, MAX):
			if i+j > k:
				print(" ".join( str(x) for x in [i, j, k]))