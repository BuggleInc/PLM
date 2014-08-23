# BEGIN SOLUTION
def house(len):
	forward(len);
	    
	right(30);
	for i in range(3):
		forward(len);
		right(120);
	    
	right(60);    
	for i in range(3):
	    	forward(len);
	    	right(90);

addSizeHint(35,120, 35,150);
addSizeHint(80,150, 100,150);

for i in range(4):
	house(30);
	leveCrayon();
	right(90);
	forward(50);
	left(90);
	penDown();
# END SOLUTION

