DEBUG_OUTPUT = false
PRINT_TIME = false

require 'maxpath.rb'

input = File.new(ARGV[0])

start_time = Time.new

for testCase in 1..input.readline.to_i
	
	vec_size = input.readline.to_i 
	
	v0 = input.readline.split.map { |s| s.to_i }
	v1 = input.readline.split.map { |s| s.to_i }

	weights = Array.new(vec_size) { Array.new }
	
	min_weight = 0
	
	for i in 0...vec_size
		for j in 0...vec_size
			weights[i][j] = v0[i] * v1[j]
			min_weight = weights[i][j] if weights[i][j] < min_weight 
		end
	end
	
	for i in 0...vec_size
		for j in 0...vec_size
			weights[i][j] = weights[i][j] - min_weight			 
		end
	end
	
	printMatrix(weights) if DEBUG_OUTPUT
	
	cost = findMinimum(weights) + vec_size * min_weight
	
	puts "Case ##{testCase}: #{cost}"
		
end

end_time = Time.new
puts "Time %.6f" % (end_time - start_time) if PRINT_TIME