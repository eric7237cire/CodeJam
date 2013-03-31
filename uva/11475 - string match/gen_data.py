import string
import random
chars = string.digits + string.ascii_letters + string.punctuation
chars = string.ascii_letters

T = 50
for t in range(T):
    lenstr = random.randint(1, 50001 )
    astr = ""
    for i in range(0, lenstr):
        astr += random.choice(chars)
    
    astr += astr[::-1]
    
    lenstr = random.randint(0, lenstr )
        
    #slice off lenstr
    print(astr[:-lenstr])
