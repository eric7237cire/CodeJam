UPPER_LIMIT = 10
COMBO_SIZE = 4
cells = (1..UPPER_LIMIT).to_a

#cells = (1..20).to_a

fixed_vals = [ ]

cells = cells - fixed_vals
combos = cells.combination(COMBO_SIZE - fixed_vals.length).to_a

puts combos.length 
combos.each { |combo| 

	print UPPER_LIMIT, ' ', COMBO_SIZE, "\n"
	puts (fixed_vals + combo).sort().join(' ')
}

