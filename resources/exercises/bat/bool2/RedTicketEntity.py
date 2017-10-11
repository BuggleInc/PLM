def redTicket(a, b, c):
	if (a == b and b == c and c == 2):
		return 10
	elif (a == b and b == c):
		return 5
	elif (b != a and c != a):
		return 1
	else:
		return 0
