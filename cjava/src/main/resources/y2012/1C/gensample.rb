
TOTAL  = [200, 180]

MAX_BLOCK = [30, 20]

TYPES = [3, 3]

ITERATIONS = 100
r = Random.new
puts ITERATIONS

ITERATIONS.times do
	blocks = [Array.new, Array.new]
	(0..1).each do |i| 
	
		len_left = TOTAL[i]
		
		until len_left == 0 do
			blockSize = r.rand(1..[len_left, MAX_BLOCK[i]].min)
			
			type = r.rand(1..TYPES[i])
			
			blocks[i].push( [ blockSize, type ] )
#			puts "block #{blockSize}  #{type} "
			len_left = len_left - blockSize
		end
	
	
	end
	
	puts "#{blocks[0].length} #{blocks[1].length}"
	
	(0..1).each do |i|
		blocks[i].each do |block|
			print "#{block[0]} #{block[1]}   "	
		end
		puts
	end
	puts
end
