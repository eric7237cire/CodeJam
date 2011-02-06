require 'set'

DEBUG_OUTPUT = true

MAX_NODES = 1000

def yIdx(y)
	return y - MAX_NODES
end

#Represents a bipartite graph where edges only go from
#X to Y vertices
class EqualityGraph
	attr_accessor :edges, :x_vertices, :y_vertices
	
	def initialize( weights )
		@labelX = Array.new
		@labelY = Array.new
		@weights = weights
	end
	
	def clearEdges
		@edge_cache = Array.new(@x_vertices.size) { |idx| Array.new(@x_vertices.size, -1) }
	end
	
	def isEdge(xIdx, y)
		yIdx = yIdx(y)
		is_edge = @edge_cache[xIdx][yIdx]
		if is_edge == 0
			return false
		elsif is_edge == 1
			return true
		else			
			@edge_cache[xIdx][yIdx] = (@weights[xIdx][yIdx] == @labelX[xIdx] + @labelY[yIdx]) ? 1 : 0
			return isEdge(xIdx, y)
		end
	end
	
	def initialMatch(eq_match)
		@weights.each_with_index do |xy, xIdx|
			xy.each_index do |yIdx|
				#yIdx = xy.index(@labelX[xIdx])
				eq_match.add_edge_if_possible(xIdx, yIdx + MAX_NODES) if isEdge(xIdx, yIdx + MAX_NODES)
			end
			
		end			end
	
	def generateLabelFunctions()
		@x_vertices = Set.new
		@y_vertices = Set.new
		
		@weights.each_with_index do |xy, xIdx|
			@x_vertices.add(xIdx)
			@y_vertices.add(xIdx + MAX_NODES)
			
			@labelX.push( xy.select{ |w| w != nil}.max )
		end
			
		@labelY = Array.new(y_vertices.size, 0)
		
		clearEdges
		
		raise if @edge_cache.size != @x_vertices.size
		raise if @edge_cache[0].size != @x_vertices.size
		raise if @edge_cache[@x_vertices.size - 1].size != @x_vertices.size
		
		puts "LabelX #{@labelX}" if DEBUG_OUTPUT
		puts "LabelY #{@labelY}" if DEBUG_OUTPUT
	end
	
	def get_neighbors_of_x_vertices(x_vertices)
		neigh = Set.new
		x_vertices.each do |x|
			@y_vertices.each do |y|
				neigh.add(y) if isEdge(x, y) == true
			end			 
		end
		
		puts "Getting Neighbors of EQ for #{x_vertices}" if DEBUG_OUTPUT
		printMatrix(@edge_cache) if DEBUG_OUTPUT
		
		return neigh
		#return @edges.select{ |e| x_vertices.member?(e[0]) }.map{ |e| e[1]}
	end

	#Purpose is to grow the equality graph, as no augmenting path has been found
	#So that there are more y nodes to process and grow the alternating tree.

	def growEqualityGraph(s_vertices, t_vertices, s_neighbors, s_neighbors_not_in_t) #weights, s_vertices, t_vertices, s_neighbors_not_in_t, s_neighbors)
		
		#update labels
		
		
		labelUpdateVal = nil
		
		#We want to grow T in order to up the chance we can have a match
		
		unconnected_y_vertices = @y_vertices - t_vertices
		
		puts "Update labels, matching some thing in S #{s_vertices.to_a} and not T #{unconnected_y_vertices.to_a}"  if DEBUG_OUTPUT
							
		s_vertices.each do |xIdx|
			unconnected_y_vertices.each do |y|
					
				yIdx = yIdx(y)
		#						
				next if @weights[xIdx][yIdx] == nil
				#puts "looking at #{x} #{xIdx} #{y} #{yIdx} ..label vals #{labelX[xIdx]} + #{labelY[yIdx]} - weights[xIdx][yIdx]"  if DEBUG_OUTPUT
				candidate = @labelX[xIdx] + @labelY[yIdx] - @weights[xIdx][yIdx]
				labelUpdateVal = candidate if labelUpdateVal == nil || candidate < labelUpdateVal
			end
		end
			
		#Todo probably the cells matching candidate and exactly
		#the ones that are the new lines in the equality subgraph
		
		puts "Label Updating Value #{labelUpdateVal}"  if DEBUG_OUTPUT
			
			#Now adjust the label values accordingly
		#				#This adjustment will keep the equality graph the same but add an edge
		s_vertices.each do |xIdx|
			@labelX[xIdx] -= labelUpdateVal
		end
			
		t_vertices.each do |y|		
			@labelY[yIdx(y)] += labelUpdateVal
		end
					
		#@edges = Set.new
		#puts "Arg: #{@edges.to_a}" if DEBUG_OUTPUT
		
		#New eq graph has same edges if x is part of s && y is part of t or
		#if x,y not part s,t respectively
		#so we just have to blow away stuff in s, but not t and t but not s
		
		clearEdges		
#		not_s_vertices = x_vertices - s_vertices
#		
#		@edges.reject! { |e| s_vertices.member?(e[0]) != t_vertices.member?(e[1]) }
#		
		s_vertices.each do |x|
			unconnected_y_vertices.each do |y|
				#puts "genEqGraph x=[#{x}] y=[#{y}] weight=#{weights[xIdx][yIdx]} labelX=#{labelX[xIdx]} labelY=#{labelY[yIdx]}"  if DEBUG_OUTPUT
				if isEdge(x, y) == true
					puts "Adding #{y} to s_neighbors #{s_neighbors.to_a}" if DEBUG_OUTPUT
					s_neighbors.add(y)
					s_neighbors_not_in_t.push(y)					
				end
			end
		end
		
		puts "LabelX #{@labelX}"  if DEBUG_OUTPUT
		puts "LabelY #{@labelY}"  if DEBUG_OUTPUT		
		puts "New Equality graph\n#{to_s}"  if DEBUG_OUTPUT			
					
	end
	
	def generateEqualityGraph()
			
		@x_vertices.each do |x|
			@y_vertices.each do |y|
				
				xIdx = x 
				yIdx = yIdx(y)
				#puts "genEqGraph x=[#{x}] y=[#{y}] weight=#{weights[xIdx][yIdx]} labelX=#{labelX[xIdx]} labelY=#{labelY[yIdx]}"  if DEBUG_OUTPUT
				if @weights[xIdx][yIdx] == @labelX[xIdx] + @labelY[yIdx]
					@edge_cache[xIdx][yIdx] = 1
				end
			end
		end
				
	end
	
	
	
	def to_s
		s = "Equality GRAPH "
		printMatrix(@edge_cache, s) if DEBUG_OUTPUT
#		@edges.each do |edge|
#			s << "E(#{edge[0]}, #{edge[1]}) "
#		end
		return s
	end
end

class MatchGraph 
	attr_accessor :unconnected_x_vertices
	attr_accessor :alt_tree_node_parents, :alt_tree_x_nodes
	attr_accessor :x_vertices, :y_vertices
	
	def initialize(_vx, _vy)
		@x_connections = Array.new(_vx.size, -1)
		@y_connections = Array.new(_vy.size, -1)
		
		@x_vertices = _vx
		@y_vertices = _vy	
		
		@unconnected_x_vertices = @x_vertices.size
		
		@alt_tree_node_parents = Array.new(2 * MAX_NODES, -1)
		@alt_tree_x_nodes = Array.new
		
	end
	
	#Pre - this is a match
	#Post still a match
	def add_edge(x, y)
		raise if @x_connections[x] != -1
		raise if @y_connections[yIdx(y)] != -1
		
		@x_connections[x] = y
		@y_connections[yIdx(y)] = x
		
		@unconnected_x_vertices -= 1
		
#		raise if @x_connected_vertices.member?(xy[0])
#		raise if @y_connected_vertices.member?(xy[1])
	
	end
	
	def add_edge_if_possible(x, y)
		return if @x_connections[x] != -1
		return if @y_connections[yIdx(y)] != -1
		
		add_edge(x, y)
		
	end
	
	def get_free_x_vertex
		@x_connections.each_with_index do |y, xIdx| 
			return xIdx if y == -1
		end
	end
	
	def is_y_vertex_free(y)
		return @y_connections[yIdx(y)] == -1
	end
	
	#Assuming vertex is not free, returns the other vertex it is attached to
	def get_connected_x_vertex(y_vertex)
		y_index = yIdx(y_vertex)
		raise if @y_connections[y_index] == -1
		return @y_connections[y_index]
	end
	
	def isPerfectMatch
		#A perfect match is where there is an edge to every vertex and each vertex is only used once
		
		#Assuming that this graph is a match, ie, every vertex has degree of max 1
		return @unconnected_x_vertices == 0
			
	end


	def sumEdgeWeights(weights)
		sum = 0
		
		@x_connections.each_with_index do |y, xIdx|
			yIdx = yIdx(y)			
			sum += weights[xIdx][yIdx]
		end
		
		return sum
	end
	
	#Assuming this graph is a match, uses augmentingPath to generate a new match
	def generateMatch(augmentingPathTopNode, freeX)
		#A match is sub graph where a vertex can only be used once
		newMatch = MatchGraph.new(@x_vertices, @y_vertices)
		
		#S and T form an alternating tree.  Meaning every path originating
		#from S is an alternaing path
		
		#Because y is free, s_vertices[0] to y form an augmenting path
		
		#the rest of s/t could be different alternating paths.
				
		#All we now is that s[0] is the base of an alternating tree.  y_vertex could be directly connected to the base
	
		raise if augmentingPathTopNode == nil
				
		aug_y = augmentingPathTopNode
		aug_x = @alt_tree_node_parents[aug_y]
#			match.add_edge( [aug_x, aug_y] )
		while true #aug_x != freeX
#				aug_path_edge = [aug_x, aug_y]
			puts "Aug path edge #{aug_x}, #{aug_y} " if DEBUG_OUTPUT
			newMatch.add_edge( aug_x, aug_y )
			
			break if aug_x == freeX
	#		raise if !eq.edges.member?(aug_path_edge)
	#		raise if eq_match.edges.member?(aug_path_edge)
			
			aug_y = @alt_tree_node_parents[aug_x]
			#raise if aug_y == -1
			prev_aug_x = aug_x
			aug_x = @alt_tree_node_parents[aug_y]
	#		raise if aug_x == -1
			
			#matching_edge = [prev_aug_x, aug_y]
			 
	#		raise if !eq.edges.member?(matching_edge)
	#		raise if !eq_match.edges.member?(matching_edge)
			
			
		end
		
		#path is something like x0, y1, x1, y2 where x0-y1 are unmatched, x1-y2 are matched, etc
			#grow the match by inverting them, so x0-y1 is matched
				
		#Add the remaining edges		
		@x_connections.each_with_index do |y, x|
			next if y == -1
			newMatch.add_edge_if_possible(x, y)
		end
		
		#match.refreshVertices
					
		#@alt_tree_node_parents.map!{ |p| -1 }
		return newMatch
	end
	
	def to_s
		"Match: #{@x_connections} #{@y_connections}"
	end
end

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

def convertMinToMaxProblem(matrix)
	maxValue = matrix.flatten.max
	return matrix.map{ |row| row.map{ |cell| maxValue - cell} }, maxValue * matrix.length
end

#require 'ruby_prof'

def findMinimum(weights)
	
#	RubyProf.start
	#printMatrix(weights, $out)
	maxWeights, diagMax = convertMinToMaxProblem(weights)
	
	cost = diagMax - findMaximum(maxWeights)
	#$out << "\nassert(findMinimum(weights) == #{cost.to_i}\n\n"
	
#	result = RubyProf.stop
#									
#	printer = RubyProf::FlatPrinter.new(result)
#	printer.print(STDOUT)		
	
	return cost	
end



def growMatch(y, eq_match, eq, freeX )
	puts "y[#{y}] is free!"  if DEBUG_OUTPUT
					
	#We have just found an augmenting path that we can grow our match with
	
	# |sIdx| should be equal to |tIdx| - 1
	
	
	#We need to check each edge in the alternating tree because the 
	#augmenting path can fork off the tree at any point
	
		
	augmentingPathTopNode = nil
	
	eq_match.alt_tree_x_nodes.reverse_each do |x|
		if eq.isEdge(x, y) == true
			eq_match.alt_tree_node_parents[y] = x
			augmentingPathTopNode = y
			break
		end
	end

	raise if augmentingPathTopNode == nil
		
	oldMatchSize = eq_match.x_vertices.size - eq_match.unconnected_x_vertices
	
	puts "Old eq match #{eq_match.to_s}"  if DEBUG_OUTPUT
	
	eq_match = eq_match.generateMatch(augmentingPathTopNode, freeX)
	
	puts "New eq match #{eq_match.to_s}"  if DEBUG_OUTPUT
	
	newMatchSize = eq_match.x_vertices.size - eq_match.unconnected_x_vertices
	
	# number of edges must have increased by 1
	raise "Edges did not increase.  Old ##{oldMatchSize}  New ##{newMatchSize}}" if newMatchSize != oldMatchSize + 1
	
	return eq_match	
	
end

def growTree(s_vertices, t_vertices, y, eq, eq_match)
	#z is e {X}
	z = eq_match.get_connected_x_vertex(y)
	s_vertices.push(z) #if !s_vertices.member?(z)
	t_vertices.push(y) #if !t_vertices.member?(y)
	
	puts "Adding a matched edge to alternating tree #{z}, #{y}" if DEBUG_OUTPUT
	eq_match.alt_tree_node_parents[z] = y
	
	#We grow the altenating tree
	#We know that y-z is a matching edge
	#We also know that y is a neighbor of {X}, so that 
	#the edge from some s e {X}, N(S), must be unmatched
	
	#tree goes x0, y1, x1, y2, x2...
	#always ends in X
		
	eq_match.alt_tree_x_nodes.reverse_each do |x|
		if eq.isEdge(x, y) == true
			puts "Adding alt tree parent #{y} is #{x}" if DEBUG_OUTPUT
			eq_match.alt_tree_node_parents[y] = x			
			break
		end
	end
	
	eq_match.alt_tree_x_nodes.push(z)		
	
	return z
end 


#weights is an array such that w(x,y) == weights[x][y]
#x/y_vertices are an array of vertex names 
def findMaximum(weights)
	
	puts "Weights"  if DEBUG_OUTPUT
	printMatrix(weights)  if DEBUG_OUTPUT
	# l(x) == l[x]
		
	eq = EqualityGraph.new(weights)
	eq.generateLabelFunctions()
	
	#Generate equality graph
	
	eq.generateEqualityGraph()
	
	#Pick an abitrary matching in the equality subgraph
	
	eq_match = MatchGraph.new(eq.x_vertices, eq.y_vertices)
	
	eq.initialMatch(eq_match)	
	
	puts "Equality Match\n#{eq_match.to_s}"  if DEBUG_OUTPUT
	
	#puts "Is Equality Match perfect? #{eq_match.isPerfectMatch}"  if DEBUG_OUTPUT
			
	until(eq_match.isPerfectMatch)
		#Pick a free vertex in X
		freeX = eq_match.get_free_x_vertex
		
		eq_match.alt_tree_x_nodes.push(freeX)
			
		puts "\nMAJOR STEP Picked a free X: #{freeX}"  if DEBUG_OUTPUT
		
		s_vertices = [ freeX ]
		t_vertices = Array.new
		
		#Though sets s and t should be enough, will keep track of the alternating paths 
		#originating at freeX
		
		
		s_neighbors = eq.get_neighbors_of_x_vertices(s_vertices).to_set
		
		s_neighbors_not_in_t = s_neighbors.to_a
		
		until(false)
			puts "S = #{s_vertices.to_a} N(S) = #{s_neighbors.to_a} T= #{t_vertices.to_a}"  if DEBUG_OUTPUT
				
			if s_neighbors.size == t_vertices.size
				puts "\nSTEP No more s_neighbors to process, growing eq" if DEBUG_OUTPUT
				old_size = s_neighbors.size
				
				eq.growEqualityGraph(s_vertices, t_vertices, s_neighbors, s_neighbors_not_in_t)
				
				raise "s neighbors did not increase" if s_neighbors.size <= old_size						
			else
				puts "\nSTEP Picking a new Y not yet in T from S neighbors" if DEBUG_OUTPUT
				#pick y
				y = s_neighbors_not_in_t.pop #(s_neighbors - t_vertices).find { true } 
				if eq_match.is_y_vertex_free(y)
					#Reset S and T
					raise "T and S are out of wack" if s_vertices.size != t_vertices.size + 1					
					eq_match = growMatch(y, eq_match, eq, freeX)
					puts "Grew match Equality Graph was #{eq}" if DEBUG_OUTPUT
					break
				else
					puts "y[#{y}] is matched"  if DEBUG_OUTPUT
					new_s_node = growTree(s_vertices, t_vertices, y, eq, eq_match)
					
					new_s_neighbors = eq.get_neighbors_of_x_vertices( [new_s_node] )# - t_vertices
					s_neighbors.merge( new_s_neighbors )
					#new_s_neighbors could intesect with t
					s_neighbors_not_in_t = (s_neighbors - t_vertices).to_a
				end
			end 
		end
	end
	
	puts "Sum is #{eq_match.sumEdgeWeights(weights)}"  if DEBUG_OUTPUT
	
	return eq_match.sumEdgeWeights(weights)
end


weights = [
			[0, 1, 2, 3, 0],
			[0, 0, 3, 6, 0],
			[3, 0, 3, 6, 6],
			[2, 0, 4, 4, 5],
			[0, 3, 2, 1, 4]]		
			
findMinimum(weights)

weights = [
		 [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 28, 42, 56, 70, 84, 98, 112, 126, 140, 154, 168, 182, 196, 210, 224, 238, 252], 
		 [ 129, 86, 43, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 43, 86, 129, 172, 215, 258, 301, 344, 387, 430, 473, 516, 559, 602, 645, 688], 
		 [ 133, 126, 119, 112, 105, 98, 91, 84, 77, 70, 63, 56, 49, 42, 35, 28, 21, 14, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], 
		 [ 57, 38, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 38, 57, 76, 95, 114, 133, 152, 171, 190, 209, 228, 247, 266, 285, 304, 323, 342, 361], 
		 [ 350, 336, 322, 308, 294, 280, 266, 252, 238, 224, 210, 196, 182, 168, 154, 140, 126, 112, 98, 84, 70, 56, 42, 28, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], 
		 [ 144, 120, 96, 72, 48, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 48, 72, 96, 120, 144, 168, 192, 216, 240, 264, 288, 312, 336, 360, 384, 408, 432, 456, 480, 504], 
		 [ 140, 120, 100, 80, 60, 40, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400], 
		 [ 374, 340, 306, 272, 238, 204, 170, 136, 102, 68, 34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 68, 102, 136, 170, 204, 238, 272, 306, 340, 374, 408, 442, 476, 510, 544, 578], 
		 [ 360, 336, 312, 288, 264, 240, 216, 192, 168, 144, 120, 96, 72, 48, 24, 0, 0, 0, 0, 0, 0, 0, 24, 48, 72, 96, 120, 144, 168, 192, 216, 240, 264, 288, 312, 336, 360], 
		 [ 336, 308, 280, 252, 224, 196, 168, 140, 112, 84, 56, 28, 0, 0, 0, 0, 0, 28, 56, 84, 112, 140, 168, 196, 224, 252, 280, 308, 336, 364, 392, 420, 448, 476, 504, 532, 560], 
		 [ 384, 372, 360, 348, 336, 324, 312, 300, 288, 276, 264, 252, 240, 228, 216, 204, 192, 180, 168, 156, 144, 132, 120, 108, 96, 84, 72, 60, 48, 36, 24, 12, 0, 0, 0, 0, 0], 
		 [ 264, 242, 220, 198, 176, 154, 132, 110, 88, 66, 44, 22, 0, 0, 0, 0, 22, 44, 66, 88, 110, 132, 154, 176, 198, 220, 242, 264, 286, 308, 330, 352, 374, 396, 418, 440, 462], 
		 [ 46, 0, 0, 0, 46, 92, 138, 184, 230, 276, 322, 368, 414, 460, 506, 552, 598, 644, 690, 736, 782, 828, 874, 920, 966, 1012, 1058, 1104, 1150, 1196, 1242, 1288, 1334, 1380, 1426, 1472, 1518], 
		 [ 44, 42, 40, 38, 36, 34, 32, 30, 28, 26, 24, 22, 20, 18, 16, 14, 12, 10, 8, 6, 4, 2, 0, 0, 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24], 
		 [ 185, 148, 111, 74, 37, 0, 0, 37, 74, 111, 148, 185, 222, 259, 296, 333, 370, 407, 444, 481, 518, 555, 592, 629, 666, 703, 740, 777, 814, 851, 888, 925, 962, 999, 1036, 1073, 1110], 
		 [ 108, 99, 90, 81, 72, 63, 54, 45, 36, 27, 18, 9, 0, 0, 9, 18, 27, 36, 45, 54, 63, 72, 81, 90, 99, 108, 117, 126, 135, 144, 153, 162, 171, 180, 189, 198, 207], 
		 [ 600, 560, 520, 480, 440, 400, 360, 320, 280, 240, 200, 160, 120, 80, 40, 0, 0, 40, 80, 120, 160, 200, 240, 280, 320, 360, 400, 440, 480, 520, 560, 600, 640, 680, 720, 760, 800], 
		 [ 231, 224, 217, 210, 203, 196, 189, 182, 175, 168, 161, 154, 147, 140, 133, 126, 119, 112, 105, 98, 91, 84, 77, 70, 63, 56, 49, 42, 35, 28, 21, 14, 7, 0, 0, 7, 14], 
		 [ 986, 957, 928, 899, 870, 841, 812, 783, 754, 725, 696, 667, 638, 609, 580, 551, 522, 493, 464, 435, 406, 377, 348, 319, 290, 261, 232, 203, 174, 145, 116, 87, 58, 29, 0, 0, 29], 
		 [ 124, 93, 62, 31, 0, 31, 62, 93, 124, 155, 186, 217, 248, 279, 310, 341, 372, 403, 434, 465, 496, 527, 558, 589, 620, 651, 682, 713, 744, 775, 806, 837, 868, 899, 930, 961, 992], 
		 [ 540, 520, 500, 480, 460, 440, 420, 400, 380, 360, 340, 320, 300, 280, 260, 240, 220, 200, 180, 160, 140, 120, 100, 80, 60, 40, 20, 0, 0, 20, 40, 60, 80, 100, 120, 140, 160], 
		 [ 49, 0, 0, 0, 49, 98, 147, 196, 245, 294, 343, 392, 441, 490, 539, 588, 637, 686, 735, 784, 833, 882, 931, 980, 1029, 1078, 1127, 1176, 1225, 1274, 1323, 1372, 1421, 1470, 1519, 1568, 1617], 
		 [ 880, 836, 792, 748, 704, 660, 616, 572, 528, 484, 440, 396, 352, 308, 264, 220, 176, 132, 88, 44, 0, 0, 0, 0, 0, 0, 44, 88, 132, 176, 220, 264, 308, 352, 396, 440, 484], 
		 [ 460, 440, 420, 400, 380, 360, 340, 320, 300, 280, 260, 240, 220, 200, 180, 160, 140, 120, 100, 80, 60, 40, 20, 0, 0, 0, 0, 0, 0, 0, 20, 40, 60, 80, 100, 120, 140], 
		 [ 594, 567, 540, 513, 486, 459, 432, 405, 378, 351, 324, 297, 270, 243, 216, 189, 162, 135, 108, 81, 54, 27, 0, 0, 0, 0, 0, 0, 0, 0, 27, 54, 81, 108, 135, 162, 189], 
		 [ 391, 374, 357, 340, 323, 306, 289, 272, 255, 238, 221, 204, 187, 170, 153, 136, 119, 102, 85, 68, 51, 34, 17, 0, 0, 0, 0, 0, 0, 0, 0, 17, 34, 51, 68, 85, 102], 
		 [ 525, 504, 483, 462, 441, 420, 399, 378, 357, 336, 315, 294, 273, 252, 231, 210, 189, 168, 147, 126, 105, 84, 63, 42, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 42, 63], 
		 [ 1036, 999, 962, 925, 888, 851, 814, 777, 740, 703, 666, 629, 592, 555, 518, 481, 444, 407, 370, 333, 296, 259, 222, 185, 148, 111, 74, 37, 0, 0, 0, 0, 0, 0, 0, 0, 0], 
		 [ 512, 480, 448, 416, 384, 352, 320, 288, 256, 224, 192, 160, 128, 96, 64, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352], 
		 [ 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 54, 81, 108, 135, 162, 189, 216, 243, 270, 297, 324, 351, 378, 405, 432, 459, 486, 513, 540, 567, 594, 621, 648], 
		 [ 475, 450, 425, 400, 375, 350, 325, 300, 275, 250, 225, 200, 175, 150, 125, 100, 75, 50, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 50, 75, 100], 
		 [ 154, 140, 126, 112, 98, 84, 70, 56, 42, 28, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 28, 42, 56, 70, 84, 98, 112, 126, 140, 154], 
		 [ 120, 110, 100, 90, 80, 70, 60, 50, 40, 30, 20, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90], 
		 [ 368, 345, 322, 299, 276, 253, 230, 207, 184, 161, 138, 115, 92, 69, 46, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 46, 69, 92, 115], 
		 [ 38, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 38, 76, 114, 152, 190, 228, 266, 304, 342, 380, 418, 456, 494, 532, 570, 608, 646, 684, 722], 
		 [ 66, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, 66, 99, 132, 165, 198, 231, 264, 297, 330, 363, 396, 429, 462, 495, 528, 561], 
		 [ 125, 100, 75, 50, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350], 
		 ]
		 
		 #findMinimum(weights) 

TEST = !DEBUG_OUTPUT

if TEST
	
	require 'matching_test.rb'	
	


end
