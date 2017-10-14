# BEGIN TEMPLATE
def loneTeen(a, b):
	# BEGIN SOLUTION
	teenA = a>12 and a<20
	teenB = b>12 and b<20
	return  (teenA and not teenB) or (teenB and not teenA)

# END SOLUTION
# END TEMPLATE
