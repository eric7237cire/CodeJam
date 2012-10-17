UPPER_LIMIT = 20
COMBO_SIZE = 3
cells = (1..UPPER_LIMIT).to_a

fixed_vals = [1, 9]

cells = cells - fixed_vals
combos = cells.combination(COMBO_SIZE).to_a

puts combos.length 
combos.each { |combo| 

	print UPPER_LIMIT, ' ', COMBO_SIZE + fixed_vals.length, "\n"
	puts (fixed_vals + combo).sort().join(' ')
}

