import sys
import random
random.seed()
len = 100
sys.stdout.write(str(len))
sys.stdout.write(" ")
for i in range(len):
     sys.stdout.write(str(random.randint(1,len)))
    # sys.stdout.write(str(1))
     sys.stdout.write(" ")
print("")
print("0")     

