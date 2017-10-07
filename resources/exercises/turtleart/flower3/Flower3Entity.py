# BEGIN TEMPLATE
# BEGIN SOLUTION 
	
angle = 45
addSizeHint(155,102,155,122)
for i in range(0,360/angle):
			right(45.0);
			forward(20.0);
			left(90.0);
			forward(20.0);
			right(45.0);
			forward(20.0);
			right(90.0);
			for j in range(0,2):
				forward(20.0);
				left(90.0);
				forward(20.0);
				left(90.0);
				forward(20.0);
			right(90.0);
			forward(20.0);
			right(45.0);
			forward(20.0);
			left(90.0);
			forward(20.0);
			right(45.0);
			right(180.0);
			right(angle);
hide();


# END SOLUTION
# END TEMPLATE
