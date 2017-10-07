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
	    	
addSizeHint(50,265, 150,265);
addSizeHint(35,250, 35,150);
	 
house(100);
# END SOLUTION
