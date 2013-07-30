

# BEGIN TEMPLATE
def step():
	# BEGIN SOLUTION
	if getGroundColor() == Color.white:
		turnRight()
		setBrushColor(Color.black)
		brushDown()
		brushUp()
		forward()
	else:
		turnLeft()
		setBrushColor(Color.white)
		brushDown()
		brushUp()
		forward()
	# END SOLUTION
# END TEMPLATE
		
for i in range(getParam(0)):
	step()
	stepDone()
