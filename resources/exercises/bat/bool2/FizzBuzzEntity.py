# BEGIN TEMPLATE
def fizzBuzz(a):
	# BEGIN SOLUTION
	if (a%5 == 0 and a%3 ==0):
		return "Fizz Buzz"
	elif (a%5 == 0):
		return "Buzz"
	elif (a%3 == 0):
		return "Fizz"
	return str(a)

# END SOLUTION
# END TEMPLATE
