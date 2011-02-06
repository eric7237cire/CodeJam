#Given a list of starting hex values, computes the initial cost matrix for all diaganels
startTime = Time.new.to_f
require 'maxpath'

class Hex
		
	#y is the row, 0 for origin
	#x goes by 2's, each alternating row is offset
	
	#diags are the longest lines in the hex / \ and --
	attr_accessor :x, :y, :label, :diag1, :diag2, :diag3
	
	def initialize x, y, l
		@x, @y, @label = x, y, l
		@diag1, @diag2, @diag3 = -@x == @y, @x == @y, @y == 0
	end
	
	#Number of jumps from one hex to another
	def distance(toHex)
		fromHex = self
		cy = (fromHex.y - toHex.y).abs  # change in y
		cx = (fromHex.x - toHex.x).abs  # change in x
		dist = (cy > cx) ? cy : (cy + (cx-cy) / 2)  #each cy gives you a free x move.  Each jump is 2
		return dist
	end
			
end

def calcRelCosts(costs)
	requiredCost = 0
	size = costs.size
	relCosts = Array.new(size) { |idx| Array.new(size, 0) }
		
	#Reduce each row by the row min
	(0...size).each do |i|
		pieceCosts = costs[i].select{ |c| c != nil }
		pieceCostsMin = pieceCosts.length > 0 ? pieceCosts.min : 0
		requiredCost += pieceCostsMin
							
		(0...size).each do |j|
			#puts "Min piece #{i} pos #{j} #{pieceCosts.min}"
			relCosts[i][j] = costs[i][j] - pieceCostsMin 
		end
	end
	
	#Reduce each col by the row min
	(0...size).each do |colIdx|
		colValues = relCosts.map{ |row| row[colIdx] }
		pieceCostsMin = colValues.min
		
		requiredCost += pieceCostsMin
							
		relCosts.each{ |row| row[colIdx] = row[colIdx] - pieceCostsMin }			

	end
	
	
	
	return relCosts, requiredCost
end


#Does a N choose 2 sort to get a local minimum on the diagonal
def diagSort(relCosts)
	didSwap = false
	if !relCosts.empty? #&& relCosts.length == relCosts[0].length
		for diagNum in 0...relCosts.length
			for checkNum in 0...relCosts.length
				next if diagNum == checkNum
				#puts "Checking #{diagNum} with #{checkNum}"
				statusQuoCost = relCosts[diagNum][diagNum] + relCosts[checkNum][checkNum]
				checkingCost = relCosts[diagNum][checkNum] + relCosts[checkNum][diagNum]
				
				if statusQuoCost > checkingCost 
					#puts "We found a cheaper one! Swapping #{diagNum} and #{checkNum}"
					relCosts[diagNum], relCosts[checkNum] = relCosts[checkNum], relCosts[diagNum]
					didSwap = true
					#printCosts(relCosts)
				end
			end
		end
	end
	return didSwap
end

#Prints costs where each row is a piece, each column is a position		
def printCosts(costs, showDiag)
	#puts "Length #{costs.length} Width #{costs.length > 0 ? costs[0].length : 0}"
	puts costs.length
	puts costs.length > 0 ? costs[0].length : 0
	costs.each_with_index do |pieceCosts, pcIdx|
		pieceCosts.each_with_index do |cost, costIdx|
			if pcIdx == costIdx && showDiag
				print "[#{cost}]".rjust(6)
			else
				print "#{cost}".rjust(5)
			end
		end
		puts
	end
	puts
end

		
def sumDiagonal(costs)
	sum = 0
	costs.each_with_index do |pieceCosts, pcIdx|
		sum += costs[pcIdx][pcIdx]				
	end
	return sum
end

def processTestCases(input, testCases)
	
	(1..testCases).each do |testCase|
	
		#Numbering hexes from left to right, these are the starting positions
		startPos = input.readline.split.map { |pos| pos.to_i }
			
		#These are the move costs associated with the hexagons
		hexValues  = input.readline.split.map { |pos| pos.to_i }
			
		#The length of the middle row
		hexSize = startPos.length
		
		#The length of the top and bottom rows
		smallHexRowLen = (hexSize + 1) / 2
		
		hexNum = 1
		y = hexSize - smallHexRowLen
		hexes = Array.new
		
		createHexes=Proc.new do |rowLen| 
				
			(-(rowLen - 1)..rowLen - 1).step(2) do |x|
				hexes.push(Hex.new(x, y, hexNum))
				hexNum+=1
			end
		
			y -= 1
		end
		
		for rowLen in smallHexRowLen..hexSize
			createHexes.call(rowLen)
		end
		
		(hexSize-1).downto(smallHexRowLen).each do |rowLen|
			createHexes.call(rowLen)
		end	
			 	
		posValues = startPos.zip(hexValues).sort
		
		#puts posValues
		
		minCost = nil
		
		diagData = Array.new
					
		["diag1", "diag2", "diag3"].each do |diag|
	#		print "#{diag} is "	
			goalHexes = case diag
				when "diag1" then hexes.select{ |hex| hex.diag1 }
				when "diag2" then hexes.select{ |hex| hex.diag2 }
				when "diag3" then hexes.select{ |hex| hex.diag3 }					
			end
	#			
	#		goalHexes.each do |hex|
	#			print "#{hex.label} "
	#		end
	#		puts
			#puts
			costs = Array.new(hexSize) { |idx| Array.new(hexSize) }
				
			piece = 0
						
			posValues.map{ |label, value| [hexes[label - 1], value]}.each do |startHex, moveCost|
				#print "Hex starting at #{startHex.label}, costing #{startHex.moveCost}: "
				pos = 0
				goalHexes.each do |goalHex|
					costs[piece][pos] = startHex.distance(goalHex) * moveCost
					pos += 1
				end
				piece += 1
				#puts
			end
			
			
			relCosts, requiredCost = calcRelCosts(costs)
			
			#mega insertion sort, hooooo!!!!
#			until (diagSort(relCosts) == false)				
#			end
			
			diagData.push( { "relCosts" => relCosts, 
			"requiredCost" => requiredCost }			 )
		
			localMin = requiredCost + sumDiagonal(relCosts) 
			minCost = localMin if minCost == nil || localMin < minCost
				
		end
		
	  diagData.each do |dd|
	  	requiredCost = dd["requiredCost"]
	  	relCosts = dd["relCosts"]
	  	next if dd["requiredCost"] > minCost
	  
			actualCost = requiredCost + findMinimum(relCosts)
			
			minCost = actualCost if actualCost < minCost	
	  end
				
		puts "Case ##{testCase}: #{minCost}" 
		
	end
end



input = File.new(ARGV[0] != nil ? ARGV[0] : "hexinput.txt")
	
testCases = input.readline.to_i
OUTPUT=false
HOUTPUT = false

processTestCases(input, testCases)

endTime = Time.new.to_f
puts "Total time %.6f" % (endTime - startTime)
