DEBUG_OUTPUT = ARGV.size == 0
PRINT_TIME = false

input = File.new(ARGV[0]) if !DEBUG_OUTPUT

start_time = Time.new

def printMatrix(matrix, output = $stdout)
	output << " weights = [\n";
	matrix.each_with_index do |row, rowIdx|
		output << "[ "
		row.each_with_index do |cell, colIdx|
			output << "#{(nil ? "nil" : cell.to_s)}#{colIdx != row.size() - 1 ? ', ' : '], '}"
		end
		output << "\n" 
	end
	output << "]\n"
end

class Fact

	def initialize
		
	end
	
	def combin(n, k)
		c = 1
		for i in n-k+1 .. n
			c *= i
		end
		
		c /= fact(k)
		
		return c
		#return fact(n) / (fact(k) * fact(n-k))	
	end
	
	def fact(n)
		
		
		mem = case
	when n==0
		1
	when n==1
		1
	when n==3
		6
	else
		retval = 1
		for i in 1..n
			retval*=i			
		end
		retval
	end
		
		return mem
	
	end
	
end

class Calc
	
	
	def initialize(*args)
		@modLists = Array.new(3) { |idx| Array.new(3, 0) }
		
		if args.size > 6
			n,a,b,c,d,x0,y0,m,@fact = args
			puts "Coordinates" if DEBUG_OUTPUT
			x,y = x0, y0
			addTree(x,y)
			puts "#{x}, #{y}" if DEBUG_OUTPUT
			for i in 1..n-1
				x = (a * x + b) % m
				y = (c * y + d) % m
				puts "#{x}, #{y}" if DEBUG_OUTPUT
				addTree(x, y)
			end
		else
			@fact = Fact.new
		end
		
				

	end
	
	def addTree(x, y)
		@modLists[x % 3][y % 3] += 1
		#puts "#{x % 3}, #{y % 3}"
	end
	
	def countTriangles
		#Find all 0,0,0 ; 0,0,0
		printMatrix(@modLists) if DEBUG_OUTPUT
		
		count = 0
		
		for x in 0..2
			puts "Counting X={x}0,0,0 = #{count}" if DEBUG_OUTPUT
			count += countValidYCenter(@modLists[x])
			puts "Counted = #{count}" if DEBUG_OUTPUT
			puts  if DEBUG_OUTPUT
			
			puts "Counting X=0,1,2 Y={x}" if DEBUG_OUTPUT		
			count += @modLists[0][x] * @modLists[1][x] * @modLists[2][x]
			puts "Counted = #{count}" if DEBUG_OUTPUT
		end
		
		puts "Counting X=0,1,2 Y=0,1,2" if DEBUG_OUTPUT
		count += @modLists[0][0]  * @modLists[1][1] * @modLists[2][2]
		puts "Counted X=0,1,2 Y=0,1,2 #{count}" if DEBUG_OUTPUT
		
		count += @modLists[0][0]  * @modLists[1][2] * @modLists[2][1]
		puts "Counted X=0,1,2 Y=0,2,1 #{count}" if DEBUG_OUTPUT
		
		count += @modLists[0][1]  * @modLists[1][0] * @modLists[2][2]
		puts "Counted X=0,1,2 Y=1,0,2 #{count}" if DEBUG_OUTPUT
		
		count += @modLists[0][1]  * @modLists[1][2] * @modLists[2][0]
		puts "Counted X=0,1,2 Y=1,2,0 #{count}" if DEBUG_OUTPUT
		
		count += @modLists[0][2]  * @modLists[1][1] * @modLists[2][0]
		puts "Counted X=0,1,2 Y=2,1,0 #{count}" if DEBUG_OUTPUT
		
		count += @modLists[0][2]  * @modLists[1][0] * @modLists[2][1]
		puts "Counted X=0,1,2 Y=2,0,1 #{count}" if DEBUG_OUTPUT
		puts "Counted = #{count}" if DEBUG_OUTPUT
		
		return count
	end
	
	def countValidYCenter(yCounts)
		puts "Counting Y's #{yCounts}" if DEBUG_OUTPUT
		counts = 0
		
		for y in 0..2
			if yCounts[y] >= 3
				
				counts += @fact.combin(yCounts[y], 3)
				puts "Counted Y=0,0,0 = #{counts}" if DEBUG_OUTPUT
			end
		end
		
		counts += yCounts[0] * yCounts[1] * yCounts[2]
		puts "Counted Y=0,1,2  #{yCounts[0]} * #{yCounts[1]} * #{yCounts[2]} = #{counts}" if DEBUG_OUTPUT
		return counts
	end
	

end
	


if !DEBUG_OUTPUT
	fact = Fact.new
	for testCase in 1..input.readline.to_i
		n,a,b,c,d,x0,y0,m = input.readline.split.map{ |s| s.to_i}

		c = Calc.new(n,a,b,c,d,x0,y0,m,fact)		
		num = c.countTriangles( )
		puts "Case ##{testCase}: %d" % num
		
	end
end

if DEBUG_OUTPUT
	require 'test/unit'
	
	class TC_MyTest < Test::Unit::TestCase
	  def setup
			
	  	
	  end
		
		def testFact
			c = Fact.new
			
			assert(c.fact(3) == 6)
			assert(c.fact(4) == 24)
			
			assert(c.combin(4, 3) == 4)
		end
	
	  def testNumbers
	  	
			#assert(findFirstThreeDigits(0) == 98985)
	  	
			c = Calc.new
			c.addTree(0, 0)
			c.addTree(0, 1)
			c.addTree(0, 2)
			
			assert(c.countTriangles == 1)
			
			c = Calc.new
			c.addTree(0, 1)
			c.addTree(1, 0)
			c.addTree(2, 2)
			
			assert(c.countTriangles == 1)
			
			c = Calc.new
			c.addTree(0,0)
			c.addTree(0,1)
			c.addTree(0,2)
			c.addTree(0,1)
			c.addTree(0,2)
			c.addTree(1,1)
			c.addTree(1,1)
			c.addTree(1,1)
			c.addTree(2,2)
			
			assert(c.countTriangles == 8)
			
			
			c = Calc.new
			c.addTree(2,0)
			c.addTree(2,0)
			c.addTree(2,0)
			c.addTree(0,1)
			c.addTree(0,1)
			c.addTree(0,1)			
			c.addTree(1,2)
			c.addTree(1,2)
			c.addTree(1,2)
			
			assert(c.countTriangles == 30)
			
			c.addTree(2,0)
			assert(c.countTriangles == 42)
			
			c = Calc.new
			c.addTree(0, 2)
			c.addTree(1, 2)
			c.addTree(2, 2)
			c.addTree(0, 2)
			c.addTree(1, 2)
			c.addTree(2, 2)
			
			assert(c.countTriangles == 8)
			
			assert(Calc.new(4, 10, 7, 1, 2, 0, 1, 20).countTriangles == 1)
			assert(Calc.new(6, 2, 0, 2, 1, 1, 2, 11).countTriangles == 2)
	  	
	  end
	end
end
end_time = Time.new
puts "Time %.6f" % (end_time - start_time) if PRINT_TIME