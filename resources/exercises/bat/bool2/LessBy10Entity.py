def lessBy10(a, b, c):
	return ((a - b) >= 10) or ((b - a) >= 10) or ((b - c) >= 10) or ((c - b) >= 10) or ((a - c) >= 10) or ((c - a) >= 10)
