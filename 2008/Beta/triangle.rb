class Line
	attr_reader :slope, :len
	
	def initialize x1, y1, x2, y2
		if x2 == x1
			@slope = nil			
		else
			@slope = (y2 - y1) / (x2 - x1)
		end
		
		@len = ((y2 - y1) ** 2 + (x2 - x1) ** 2) ** 0.5
	end
	
	def isParallel(other)
		return @slope == other.slope
	end	
end

def lawCosines(a, b, c)
	#c2 = a2 + b2 – 2ab cos C
	puts a, b, c
	Math.acos (c ** 2 - a ** 2 - b ** 2) / (-2 * a * b) 
end

testCases = ARGF.readline.to_i

for testCase in 1..testCases
	print "Case ##{testCase}: " 
	x1, y1, x2, y2, x3, y3 = ARGF.readline.split.map{ |pt| pt.to_f }
		
	ab = Line.new(x1, y1, x2, y2)
	ac = Line.new(x1, y1, x3, y3)
	bc = Line.new(x2, y2, x3, y3)
	
	if ab.isParallel(ac) || ab.isParallel(bc) || ac.isParallel(bc)
		puts "not a triangle"
		next
	end
	
	angles = Array.new
	angles.push lawCosines(ab.len, ac.len, bc.len)
	angles.push lawCosines(ab.len, bc.len, ac.len)
	angles.push lawCosines(ac.len, bc.len, ab.len)
	
	desc1 = case
	when ab.len == ac.len || ab.len == bc.len || ac.len == bc.len
		"isosceles"
	else
		"scalene"
	end
	
	desc2 = case 
	when angles.find { |ang| (ang - Math::PI / 2).abs < 0.00000001 } != nil
		"right"
	when angles.find { |ang| ang > Math::PI / 2 } != nil
		"obtuse"
	else
		"acute"
	end
	
	puts "#{desc1} #{desc2} triangle"
end