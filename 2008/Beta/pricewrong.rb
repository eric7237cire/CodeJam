require 'set'

class Node
	attr_reader :index, :value, :level, :parent
	
	def Node.reset
		@@nodes = Array.new
		@@maxLevel = 0
	end
	
	def Node.nodes
		@@nodes
	end
	
	def Node.maxLevel
		@@maxLevel
	end
	
	def initialize parent, index, value
		@parent, @index, @value = parent, index, value
		
		if @parent == nil
			@level = 0
		else			
			@@nodes.push(self)
			@level = @parent.level + 1
			@@maxLevel = [@level, @@maxLevel].max
		end
		
		@children = Array.new		
	end
	
	def add(newNode)
		if @value < newNode.value
			@children.push(Node.new(self, newNode.index, newNode.value))
		else
			return
		end
		
		@children.each do |child|
			child.add(newNode)				
		end		
	end
	
	def names(nameList)
		retVal = Array.new
		n = self
		until n.parent == nil 
			retVal.push(nameList[n.index])
			n = n.parent
		end
		
		return retVal
	end
		
end

startTime = Time.new.to_f

testCases = ARGF.readline.to_i

for testCase in 1..testCases
	print "Case ##{testCase}: " 
	
	Node::reset
	
	names = ARGF.readline.split
	values = ARGF.readline.split.map { |v| v.to_i }
		
	root = Node.new(nil, -1, 0)
		
	values.each_with_index { |val, idx| root.add(Node.new(nil, idx, val)) }
	
	maxNodes = Node::nodes.select{ |n| n.level == Node::maxLevel }.map{ |n| (names - n.names(names)).sort }.min.join(' ')
	puts maxNodes	
		
end

endTime = Time.new.to_f

#puts "Time taken #{endTime-startTime}"