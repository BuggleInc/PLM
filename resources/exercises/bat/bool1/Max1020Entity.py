def max1020(a, b):
	A = max(a,b)
	B = min(a,b)
	if (A<21 and A>9):
		return A
	if (B<21 and B>9):
		return B
	return 0
