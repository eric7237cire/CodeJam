import sys
import random
random.seed()
len = 1000

for i in range(len):
     sys.stdout.write(str(random.randint(1,10000)))
    # sys.stdout.write(str(1))
     sys.stdout.write(" ")
     sys.stdout.write(str(random.randint(1,10000)))
     print("")

