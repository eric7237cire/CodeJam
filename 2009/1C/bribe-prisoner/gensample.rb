UPPER_LIMIT = 15
COMBO_SIZE = 3
cells = (1..UPPER_LIMIT).to_a
combos = cells.combination(COMBO_SIZE).to_a
puts combos.length
combos.each { |combo| 

	print UPPER_LIMIT, ' ', COMBO_SIZE, "\n"
	puts combo.join(' ')
}

