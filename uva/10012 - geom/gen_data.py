import random
nCases = 50
print(nCases)
for i in range(nCases):
    m = random.randint(1, 8)
    nums =  []
    nums.append( str(m) )
    
    for j in range(m):
        r = 0.001 + 1.0 * random.random()
        nums.append( "%0.3f" % r)
        
    print (" ".join( nums) )
	
