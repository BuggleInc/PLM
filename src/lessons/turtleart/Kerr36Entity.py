# BEGIN TEMPLATE
# BEGIN SOLUTION 
	
addSizeHint(130, 85, 150, 85)
addSizeHint(190, 115, 190, 135)

def branch(length):
		forward(length)
		left(45)
		forward(length)
		right(90)
		forward(length)
		left(45)
		forward(length)

		left(90)
		forward(length)
		right(45)
		forward(length)
		right(135)
		forward(length)
		left(45)
		forward(length)

		right(90)
		forward(length)
		left(45)
		forward(length)
		right(135)
		forward(length)
		right(45)
		forward(length)

		left(90)
		forward(length)
		left(45)
		forward(length)
		right(90)
		forward(length)
		left(45)
		forward(length)
		right(180)

for i in range(0,8):
	branch(20)
	right(45)


# END SOLUTION
# END TEMPLATE
