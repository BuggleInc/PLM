

# BEGIN TEMPLATE
def step():
	# BEGIN SOLUTION
	if getGroundColor() == Color.white:
		right()
		setBrushColor(Color.black)
		brushDown()
		brushUp()
		forward()
	else:
		left()
		setBrushColor(Color.white)
		brushDown()
		brushUp()
		forward()
	# END SOLUTION
# END TEMPLATE
		
for i in range(getParam(0)):
	step()
	stepDone()
