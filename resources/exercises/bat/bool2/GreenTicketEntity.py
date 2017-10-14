# BEGIN TEMPLATE
def greenTicket(a, b, c):
	# BEGIN SOLUTION
	if (a == b and b == c):
		return 20
	elif (a == b or b == c or a == c):
		return 10
	else:
		return 0

# END SOLUTION
# END TEMPLATE
