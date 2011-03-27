#make && ./star_wars.exe C-small-practice.in > c_test.txt && diff c_test.txt c_small.txt
#make && ./perm_rle.exe D-small-practice.in > d_test.txt && diff d_test.txt d_small.txt
#exec = "portal.exe"
#exec = "classroom.exe"
exec = "what_are_birds.exe"
letter = "A"
smallLetter = letter.downcase
size = "small"
 
#system("touch classroom.cxx")
system("make mode=release && ./#{exec} #{letter}-#{size}-practice.in > test.txt && diff test.txt #{smallLetter}_#{size}.txt")
#system("make && ./#{exec} #{letter}-#{size}-practice.in > test.txt && diff test.txt #{smallLetter}_#{size}.txt")
  
size = "large"  
ex = "make && ./#{exec} #{letter}-#{size}-practice.in > test.txt && diff test.txt #{smallLetter}_#{size}.txt"
puts ex
#exec(ex)
