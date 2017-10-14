# BEGIN TEMPLATE
def lessBy10(a, b, c):
	# BEGIN SOLUTION
	return ((a - b) >= 10) or ((b - a) >= 10) or ((b - c) >= 10) or ((c - b) >= 10) or ((a - c) >= 10) or ((c - a) >= 10)

# END SOLUTION
# END TEMPLATE
