testCases = STDIN.gets.to_i

class Coordinate
  attr_accessor :x, :y

  #lets us use just <=> to define rest of operators  
  include Comparable
  
  def initialize(x, y)
    @x, @y = x, y
  end
  
  #order coords from top/left to bottom/right
  def <=>(rhs)
    return @x <=> rhs.x if @y==rhs.y      
    return rhs.y <=> @y
  end
  
  def + (rhs)
    return Coordinate.new(@x + rhs.x, @y + rhs.y)
  end
  
  def to_s
    return "(#{x}, #{y})"
  end
  
  def == (rhs)
    return @x == rhs.x && @y == rhs.y
  end
  
  def eql? (rhs)
    return self == rhs
  end
  
  def hash
    return @x.hash + @y.hash
  end
end

class Direction < Coordinate
  attr_accessor :mask, :label
  
  def initialize(x, y, mask, label)
    super(x, y)
    @mask, @label = mask, label
  end
  
  NORTH = Direction.new(0, 1, 1, "North").freeze
  SOUTH = Direction.new(0, -1, 2, "South").freeze
  EAST = Direction.new(1, 0, 8, "East").freeze
  WEST = Direction.new(-1, 0, 4, "West").freeze
  
  def reverse
      return self.right.right  
  end
  
  def right
    return case self
    when NORTH
      EAST
    when SOUTH
      WEST
    when EAST
      SOUTH
    when WEST
      NORTH     
    end
  end
  
  def left
    return case self
    when NORTH
      WEST
    when SOUTH
      EAST
    when EAST
      NORTH
    when WEST
      SOUTH     
    end
  end
  
  def to_s    
    return @label
  end
end

for testCase in 1..testCases 
  pathFwd, pathRev = STDIN.gets.split;
  
  zeropos = pos = Coordinate.new(0, 0)
  dir = Direction::SOUTH #.dup
  
  squareState = Hash.new(0)
  
  handleStep = lambda do |step| 
    #puts "Read a step #{step}"
    case step
    when 'W'
      #puts "#{squareState[pos]} #{pos} |= mask #{dir.mask}"
      squareState[pos] |= dir.mask 
      pos += dir
      squareState[pos] |= dir.reverse.mask
    when 'R'
      dir = dir.right
    when 'L'
      dir = dir.left
    end
    #puts "Current pos #{pos}, direction #{dir}"
  end
  
  handlePath = lambda do |path|
    path.scan(/[WLR]/) { |step| handleStep.call(step) }    
  end
    
  handlePath.call(pathFwd)
  
  dir = dir.reverse
  exitPos = pos
  
  handlePath.call(pathRev)
  
  squareState.delete(zeropos)
  squareState.delete(exitPos)
  
  puts "Case ##{testCase}:"
  proc { |array| array.each_index do |idx|  
    #puts "#{idx} #{array.length} #{array[idx].y} "
    print squareState[array[idx]].to_s(16)
    puts if idx == array.length - 1 || (idx < (array.length - 2) && array[idx+1].y < array[idx].y)              
  end }.call(squareState.keys.sort)
     
#  squareState.keys.sort.each do |coord|
#    puts "" if coord.y != previtem.y 
#    previtem = coord
#    #puts "#{coord} is #{squareState[coord].to_s(16)}"
#    print squareState[coord].to_s(16)#  end
#  puts
    end