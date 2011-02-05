DEBUG_OUTPUT = false

input = File.new(ARGV[0])

def integral_circle(x, r)
	y = circle(x, r)
	return (	x * y + Math.atan( x / y ) * r ** 2 ) / 2
end

def circle(x, r)
	return ( r ** 2 - x ** 2 ) ** 0.5
end

def in_circle(x, y, r)
	return (x ** 2 + y ** 2) <= r ** 2
end

start_time = Time.new

for testCase in 1..input.readline.to_i
	
	fly_radius, racket_radius, t, chord_radius, gap_len = input.readline.split.map { |s| s.to_f }
	
	puts "Fly radius #{fly_radius}" if DEBUG_OUTPUT
	puts "Racket Radius #{racket_radius}" if DEBUG_OUTPUT
	puts "Racket outer circle radius #{t}" if DEBUG_OUTPUT
	puts "Chord radius #{chord_radius}" if DEBUG_OUTPUT
	puts "Gap #{gap_len}" if DEBUG_OUTPUT
	
	prob = case
	when gap_len < 2 * fly_radius
		1
	when fly_radius > racket_radius - t
		1
	else
					
		total_area = Math::PI * racket_radius ** 2
		miss_area = 0
		
		#Go through squares with x1 < x2 ; y1 < y2
		#These squares are where the fly can get through
		
		#To go from x1 to x2 or y1 to y2		
		next_position2_inc = gap_len - 2 * fly_radius
		
		#To go from x2 to x1' or y2 to y1'
		next_position1_inc = 2 * chord_radius + 2 * fly_radius
		
		#The first x1 and y1
		first_position = chord_radius + fly_radius
		
		#The lenght of a full square + chords
		square_length = next_position1_inc + next_position2_inc
		
		#The actual radius we generally care about
		inner_radius = racket_radius - t - fly_radius
		
		#Go up chord + fly, when do wo intersect
		x_stop = circle(chord_radius + fly_radius, inner_radius)
		
		x1 = first_position		
		
		num_cols = ( (x_stop-x1) / square_length).ceil
		
		#Are that a fly can get through
		regular_square_area = next_position2_inc * next_position2_inc
		
		raise "x_stop:[#{x_stop}] x1:[#{x1}]" if x1 > x_stop
		
		for col in 0...num_cols		
			x2 = x1 + next_position2_inc
			x2 = x_stop if col == num_cols - 1 && x_stop < x2
			
			y1 = first_position
			y_stop = circle(x1, inner_radius )
			
			y_arc_intersection = circle(x2, inner_radius) 
			
			num_squares = ( (y_stop-y1) / square_length).ceil

			#The real time saver.  We need to know the first y2 that is < y_arc_intersection
			#
			# y2_0 + square_length * lowest_arc_intersection_square_num < y_arc_intersection
			# square_length * lowest_arc_intersection_square_num < y_arc_intersection - y2_0
			# lowest_arc_intersection_square_num > (y_arc_intersection - y2_0) / square_length (dividing switches comparison)
			# lowest_arc_intersection_square_num == ceil ( ... ) 				
			lowest_arc_intersection_square_num = ( (y_arc_intersection - (y1 + next_position2_inc) ) / square_length).ceil
				
			#Catch up y1
			y1 += lowest_arc_intersection_square_num * square_length 
			
			#Init missed area with all the regular squares
			slice_miss_area = (lowest_arc_intersection_square_num ) * regular_square_area
			
			for square_num in lowest_arc_intersection_square_num...num_squares
				y2 = y1 + next_position2_inc
				y2 = y_stop if square_num == num_squares - 1 && y_stop < y2
				puts "  y1:[#{y1}] y2:[#{y2}]" if DEBUG_OUTPUT
								
				#This means an arc is going through the square				
			  x1_arc_intersection = circle(y2, inner_radius)
				x2_arc_intersection = circle(y1, inner_radius)
				
				puts "  X1 arc #{x1_arc_intersection} x2 arc #{x2_arc_intersection}" if DEBUG_OUTPUT
				
				#take care of the curve
				x1_arc_intersection = x1 > x1_arc_intersection ? x1 : x1_arc_intersection 
				x2_arc_intersection = x2 < x2_arc_intersection ? x2 : x2_arc_intersection

				slice_miss_area += ( (y2-y1) * (x2_arc_intersection-x1) )

				area_under_curve = integral_circle(x2_arc_intersection, inner_radius) - integral_circle(x1_arc_intersection, inner_radius)
				rect_area = y2 * (x2_arc_intersection - x1_arc_intersection)
				arc_area = rect_area - area_under_curve 
				
				#Subtract what the curve cut off
				slice_miss_area -= arc_area
											
				y1 = y2 + next_position1_inc
				
				puts "  Slice Miss area: #{slice_miss_area} total area: #{slice_total_area}" if DEBUG_OUTPUT
			end
			
			miss_area += slice_miss_area			
			x1 = x2 + next_position1_inc
		end
		
		miss_area *= 4
		
		1 - miss_area / total_area
	end
		
	puts "Case ##{testCase}: " + "%.6f" % prob
		
end

end_time = Time.new
#puts "Time %.6f" % (end_time - start_time)