#make && ./star_wars.exe C-small-practice.in > c_test.txt && diff c_test.txt c_small.txt
#make && ./perm_rle.exe D-small-practice.in > d_test.txt && diff d_test.txt d_small.txt
exec = "portal.exe"
letter = "B"
smallLetter = letter.downcase
size = "small"
 

exec("make && ./#{exec} #{letter}-#{size}-practice.in > test.txt && diff test.txt #{smallLetter}_#{size}.txt")
  
size = "large"  
exec("make && ./#{exec} #{letter}-#{size}-practice.in > test.txt && diff test.txt #{smallLetter}_#{size}.txt")
