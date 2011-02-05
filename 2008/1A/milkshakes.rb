DEBUG_OUTPUT = false
PRINT_TIME = true

input = File.new(ARGV[0])

start_time = Time.new

for testCase in 1..input.readline.to_i
	
	num_flavors, num_customers = input.readline.to_i, input.readline.to_i 
	
	constraints = Array.new	
	choices = Array.new(num_flavors, 0)
	
	num_customers.times do 
		data = input.readline.split.map { |s| s.to_i }
		custData = Array.new(num_flavors, nil)
		
		t = data.delete_at(0)
		
		i = 0
		until i == t
			custData[ data[2*i] - 1 ] = data[2*i + 1]
			i+=1
		end
		
		#Force moves, this saves a bit of time
		if t == 1 && data[1] == 1
			choices[ data[0] - 1 ] = 1
		else
			constraints.push(custData)			
		end
	end
	
	possible = true
	choice_made = true
	
	while possible && choice_made
		choice_made = false
		
		constraints.each_with_index do |custData, custIdx|
			next if custData == nil
			satisfied = false
			
			#Is this customer satisfied with one of the milkshakes?
			choices.each_with_index do |malted, flavor|
				if custData[flavor] == malted
					satisfied = true
					break
				end
			end
			
			next if satisfied
				
			#Find a malted flavor and switch it
			#If we find one, then since we never switch a malted back,
			#the customer should no longer be processed
			choices.each_with_index do |malted, flavor|
				if custData[flavor] == 1
					raise if choices[flavor] != 0
					choices[flavor] = 1
					satisfied = true
					choice_made = true
					constraints[custIdx] = nil
					break
				end
			end
			
			next if satisfied
			
			#If we are here, not possible
			possible = false
			break
		end
	end
	
		
	
	print "Case ##{testCase}: "
	if possible == true
		choices.each { |c| print "#{c} " }
		puts
	else
		puts "IMPOSSIBLE"
	end
		
end

end_time = Time.new
#puts "Time %.6f" % (end_time - start_time) if PRINT_TIME