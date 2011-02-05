#DEBUG_OUTPUT = ARGV.size == 0
DEBUG_OUTPUT = false
PRINT_TIME = false

require 'set'

input = File.new(ARGV[0]) if !DEBUG_OUTPUT

$start_time = Time.new
$overall_start_time = Time.new

class Prime
	attr_accessor :list
	
	def initialize
		memory = Array.new(1000000, true)
		@list = Array.new
		upperBound = (memory.size ** 0.5).to_i
		puts "Upperbound #{upperBound}" if DEBUG_OUTPUT
		for i in 2..upperBound
			next if memory[i] == false
			#puts "#{i} is prime"
			@list.push(i)
			
			Range.new(i+i, memory.size - 1).step(i).each do |c|			
				memory[c] = false
			end
		end
		
		for i in upperBound+1...memory.size
			if memory[i] 
				@list.push(i)
			end
		end
		
		
	end
	
end

#
#The first version of upper_bound returns the furthermost 
#iterator i in [first, last) such that, 
#for every iterator j in [first, i), value < *j is false
def upper_bound(value, array)
	start = 0
	stop  = array.length-1
		
	#edge cases
	return 0 if array.empty?
	return start if value < array[start]
	#return stop+1 if array[stop] < value
	
	while (true) do
		return stop+1 if !(value < array[stop])
		
		middle = (start + stop) / 2
		#puts "Middle #{middle} val #{value} start #{start} #{stop}"
				
		if value < array[middle]
			stop = middle - 1;
		else
			start = middle
			#Since we know stop is not a valid (> value), we can discard it
			stop -= 1 
		end
	end
	
	
end

class Calc
	
	def initialize(prime)
		@prime = prime
	end
		
	
	def countSets(a,b,p)
		puts "A Time %.6f" % (Time.new - $start_time) if PRINT_TIME
		
		raise if a==nil || b==nil
		count = b-a+1
		 
		puts "Count #{count} #{a} to #{b} with p=#{p}" if DEBUG_OUTPUT
		#primes must be <= b-a
		
		primeFactors = Array.new(b-a+1) { |idx| Array.new }
		
		firstPrimeIndexToConsider = upper_bound(p-1, @prime.list) 
		lastPrimeIndexToConsider = upper_bound(b-a, @prime.list)  #for i in 0..@prime.list.size
			
		if firstPrimeIndexToConsider >= lastPrimeIndexToConsider
			@count = count
			return count
		end
		
		mergedWith = Array.new(lastPrimeIndexToConsider-firstPrimeIndexToConsider) #{ |idx| idx }
		
		puts "B Time %.6f" % (Time.new - $start_time) if PRINT_TIME
		
		
		primeIdx = -1
		for primeListIdx in firstPrimeIndexToConsider...lastPrimeIndexToConsider 
			#puts "#{aPrime}"
			primeIdx += 1
			aPrime = @prime.list[primeListIdx]
			
			amodPrime = a % aPrime
			start = amodPrime == 0 ? a : a + (aPrime-amodPrime)
			
			if start+aPrime > b
				next
			end
			
			mergedWith[primeIdx] = primeIdx
			
			puts "Start of #{aPrime} is #{start}" if DEBUG_OUTPUT
						
			i = start
			
			until i > b
			#Range.new(start, b).step(aPrime).each do |i|			
				primeFactors[i-a].push(primeIdx)
				i += aPrime
				puts "Adding factor #{aPrime} to #{i}" if DEBUG_OUTPUT
			end
			
			
		end
		
		puts "C Time %.6f" % (Time.new - $start_time) if PRINT_TIME
		
		raise if primeFactors.size != b-a+1
		
		#This is basically finding how many 
		#independent graphs there are if nodes are primes
		#and edges mean there is a number in (a, b) that 
		#shares the 2 primes
		primeFactors.each do |factors|
			#puts "Prime Factors: #{factors}"
			next if factors.empty?
		
			#Because each number with a prime factor is considered
			#in the rest of the loop, count it out
			count -= 1
			
			#Since we are concerned with merging sets with
			#common factors, this does not link any prime
			#factor to any other
			next if factors.size == 1
		
			#Basically mergedWith is which prime factors
			#set we merged with
			curPrime = factors[0]
			
			#Find the unmerged set associated with the first factor
			#While this may cause some thrashing, the lowest prime factor
			#is probably what everything gets merged to
			until mergedWith[curPrime] == curPrime
				curPrime = mergedWith[curPrime]
			end
			
			unmergedSet = curPrime
			
			for i in 1...factors.size
				
				curPrime = factors[i]
				
				#Nothing to be done, already pointed correctly
				next if mergedWith[curPrime] == unmergedSet
				
				#Stop until we reach an unmerged set, and set it
				#Note that not all merged sets will be correctly updated, but 
				#it is sufficient to know that they are merged with something
				until mergedWith[curPrime] == curPrime
					prevPrime = curPrime
					curPrime = mergedWith[curPrime]
					
					#While this is not strictly needed, it helps reduce future edge traversal
					mergedWith[prevPrime] = unmergedSet
				end
				mergedWith[curPrime] = unmergedSet
				
			end
		end
		
		mergedWith.each_with_index do |m, mIdx|
			next if mIdx != m
			count += 1
		end
		
		puts "count is #{count}" if DEBUG_OUTPUT
		if PRINT_TIME
			puts "D Time %.6f" % (Time.new - $start_time) 
			puts 
			puts "Overall Time %.6f" % (Time.new - $overall_start_time) 
			puts
			$start_time = Time.new if PRINT_TIME
		end
		return count
		
		
	end
	
	

end
	
if !DEBUG_OUTPUT

	prime = Prime.new
	c = Calc.new(prime)
	
	for testCase in 1..input.readline.to_i
		a,b,p = input.readline.split.map{ |s| s.to_i}
	
		puts "Case ##{testCase}: %d" % c.countSets(a,b,p)
		
	end
end

end_time = Time.new
puts "Time %.6f" % (end_time - $start_time) if PRINT_TIME