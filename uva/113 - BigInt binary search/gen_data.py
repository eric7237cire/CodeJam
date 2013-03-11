
max = pow(10, 101) - 1;

k = 3
n = 200

for k in range(2, 201, 1):
	n = 1
	while( pow(k, n) < max):
		n = n + 1
	
	if n > 201:
		n = 201
	
	kn = pow(k, n-1)
	print ( n - 1 )
	print(  kn )
