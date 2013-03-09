import sys
import random
random.seed()
len = 10

for i in range(1, len):
		sys.stdout.write(str(random.randint(1,1000000)))
		# sys.stdout.write(str(1))
		#sys.stdout.write(str(i))
		sys.stdout.write(" ")
		sys.stdout.write(str(random.randint(1,1000000)))
		#sys.stdout.write(str(random.randint(1,10000)))
		print("")

