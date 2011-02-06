class Fcomp
  B_LIMIT = 1000
  D_LIMIT = 1000
  F_MAX_LIMIT = 2 ** 32
    
  def initialize
    @cache = Array.new(D_LIMIT)
    @cache.map! { Array.new(B_LIMIT) }
  end
  
  def findFMax(d, b)
    
    retVal = case
    when d < D_LIMIT && b < B_LIMIT && @cache[d][b] != nil
      @cache[d][b]
    when b == 1  #breaking 1 egg means we can figure out d levels
      d
    when b == 2 #Happens to be the sum formula
      ( d * (d+1) ) / 2
    when b == d - 1 #Arrange f(d,b) in a downward triangle
      2 ** d - 2
    when d <= b  
      2 ** d - 1 
    when b > 2 && d >= 2954
      -1
    when b > 3 && d >= 568
      -1
    when b > 4 && d >= 221
      -1
    when b >= 6 && d > 122
      -1
    when b > 16 && d >= 33
      -1
    else
      lhs = findFMax(d - 1, b - 1)
      rhs = findFMax(d - 1, b)
      if lhs == -1 || rhs == -1
        -1
      else
        1 + lhs + rhs
      end        
    end
    
    retVal = -1 if retVal > F_MAX_LIMIT
    
    if d < D_LIMIT && b < B_LIMIT 
      if @cache[d][b] == nil
        @cache[d][b] = retVal
      end            
    end
        
    return retVal     
  end
  
  def findDMin(f, d, b)
    d = 1
    
    until(findFMax(d, b) >= f)
      d+=1
    end
    
    return d
  end
  
  def findBMin(f, d, b)
    b = 1
    
    until(findFMax(d, b) >= f || findFMax(d, b) == -1)
      #puts "D: #{d} B: #{b} fmax: #{findFMax(d, b)}"
      b+=1
    end
    
    return b
  end  
end 

testCases = ARGF.readline().to_i

fComp = Fcomp.new 

testCases.times do |testCase|
  f, d, b = ARGF.readline.split().map { |str| str.to_i }    
  puts "Case ##{testCase+1}: #{fComp.findFMax(d, b)} #{fComp.findDMin(f, d, b)} #{fComp.findBMin(f, d, b)}"  
end

runTests = false

if runTests
  
require 'test/unit'

class TC_MyTest < Test::Unit::TestCase
  def setup
    @fComp = Fcomp.new
  end

 # def teardown
 # end
def tests
  test_stuff(3, 3, 3, 7, 2, 1)
  test_stuff(7, 5, 3, 25, 3, 2)
  test_stuff(1, 122, 6, 4258490215, 1, 1)
  test_stuff(1, 82, 7, 4181044987, 1, 1)
  test_stuff(1, 83, 7, -1, 1, 1)
  test_stuff(1, 82, 8, -1, 1, 1)
  test_stuff(1, 220, 5, 4199307189, 1, 1)
  test_stuff(1, 221, 5, -1, 1, 1)
  test_stuff(1, 220, 6, -1, 1, 1)
end

def testMax
  1.upto(100).each do |d|
    1.upto(100).each do |b|
      assert( @fComp.findFMax(d, b) < Fcomp::F_MAX_LIMIT, "#{d} #{b} exceeded limit") 
    end           
  end
end
  
 def test_stuff(f, d, b, ansFmax, ansDmin, ansBmin)
   fmax, dmin, bmin = @fComp.findFMax(d, b), @fComp.findDMin(f, d, b), @fComp.findBMin(f, d, b)
   assert(fmax == ansFmax, "Fmax wrong #{fmax} == #{ansFmax}")
   assert(dmin == ansDmin)
   assert(bmin == ansBmin, "#{bmin} == #{ansBmin}")
 end
   
end

end