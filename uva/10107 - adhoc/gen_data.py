import sys
import random
random.seed()
LEN = 10000

input = open('input.txt', 'w');
fout = open('correct.txt', 'w');

myList = []

for i in range(LEN):
	n = random.randint(0,pow(2, 31) - 1)
	
	myList.append(n)
	myList.sort()
	
	input.write( str(n) )
	input.write("\n");
	
	if int(len(myList)) % 2 == 1:
		fout.write( str( int( myList[ int( (len(myList) + 1) / 2 ) - 1 ] ) ) );
	else:
		fout.write( str( int ( (
		myList[ int( len(myList) / 2) - 1 ] + 
		myList[ int( len(myList) / 2)  ] ) / 2 ) ) );
	
	fout.write("\n");
    # sys.stdout.write(str(1))
     #sys.stdout.write(" ")
     #sys.stdout.write(str(random.randint(1,10000)))
     #print("")

