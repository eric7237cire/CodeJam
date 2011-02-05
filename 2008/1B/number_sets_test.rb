require 'test/unit'

DEBUG_OUTPUT = true

require 'number_sets.rb'	



class TC_MyTest < Test::Unit::TestCase
	def setup
		@p = Prime.new
	end
	
	def test_upperbound
		testArray = [1,2,4,5,7,7,8,11]
    ############ 0 1 2 3 4 5 6 7
		for i in 0..12
			#puts "Upperbound #{i} #{upper_bound(i, testArray)}"
		end
		
		assert(upper_bound(0, testArray) == 0)
		assert(upper_bound(1, testArray) == 1)
		assert(upper_bound(2, testArray) == 2)
		assert(upper_bound(3, testArray) == 2)
		assert(upper_bound(4, testArray) == 3)
		assert(upper_bound(5, testArray) == 4)
		assert(upper_bound(6, testArray) == 4)
		assert(upper_bound(7, testArray) == 6)
		assert(upper_bound(8, testArray) == 7)
		assert(upper_bound(9, testArray) == 7)
		assert(upper_bound(10, testArray) == 7)
		assert(upper_bound(11, testArray) == 8)
		assert(upper_bound(12, testArray) == 8)
		
		testArray = []
		
		assert(upper_bound(12, testArray) == 0)
		
		testArray = [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2]
		
		assert(upper_bound(0, testArray) == 0)
		assert(upper_bound(1, testArray) == testArray.size-1)
		assert(upper_bound(2, testArray) == testArray.size)
		assert(upper_bound(3, testArray) == testArray.size)
		
		testArray = [2,3,5,7,11,13]
		
		assert(upper_bound(6, testArray) == 3)
		
		testArray = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509]
		
		assert(testArray[upper_bound(502, testArray)] == 503)
	end
	
	def testFact
		
		
		
		assert(@p.list[0] == 2)
		assert(@p.list[1] == 3)
		assert(@p.list[2] == 5)
		assert(@p.list[-1] > 900000)
		
		c = Calc.new(@p)
		
		
		assert(c.countSets(35,  42,  3) == 4)
		
		
		
		assert(c.countSets(10 ** 11 + 9,  10 ** 11 + 16,  3) == 5)
		
		assert(c.countSets(10,  12,  2) == 2)	
		
		assert(c.countSets(1,  100,  2) == 12)
		
		assert(c.countSets(365,  952,  5) == 129)
		
		assert(c.countSets(10, 20, 5) == 9)
		
		assert(c.countSets(330,  990,  502) == 990-330+1)
		
		
		
		assert(c.countSets(11, 28, 6) == 14)
		
		
		assert(c.countSets(10, 20, 3) == 7)
		
		
		
		assert(c.countSets(11, 28, 5) == 18 - 10 + 4)

		assert(c.countSets(1000, 4000, 1001) < 3001 )
		
		#assert(c.countSets(11, 28, 11) == 16)
		
		
	end
end
