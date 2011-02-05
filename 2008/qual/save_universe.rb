input = File.new(ARGV[0])

testCases = input.readline.to_i

for testCase in 1..testCases
	num_s = input.readline.to_i
	s = Array.new
	num_s.times { s.push(input.readline) }
	
	num_q = input.readline.to_i
	
	q = Array.new
	num_q.times { q.push(input.readline) }	
		
	#current_s = nil
	s_changes = 0
	cur_q = 0
	
	until (cur_q == q.size())
		s_potential_progress = s.map do |search| 
			idx = q.slice(cur_q...q.size).index(search) 
			idx == nil ? q.size() : idx + cur_q
		end
		
	  cur_q = s_potential_progress.max
	  s_changes += 1
	end
	
	s_changes -= 1 if s_changes > 0
#	q.each do |query|
#		if query != current_s && s.member?(query)
#			s_changes += 1 if current_s != nil
#			current_s = query
#		end
#	end
		
	puts "Case ##{testCase}: #{s_changes}"
end