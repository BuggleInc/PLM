# BEGIN SOLUTION 
def makeFlower(c):
		setBrushColor(c);
		brushDown();
		forward(2);
		backward();
		left();
		forward();
		backward(2);
		forward();
		setBrushColor(Color.YELLOW);
		brushUp();
		right();  
		backward();

def line(colors, returnBack):
		first = True
		for c in colors:
			if (not first):
				forward(4)
			makeFlower(c);
			first = False

		if (returnBack):
			backward(4*(len(colors)-1))    
def RLforward(steps):
		right();
		forward(steps);
		left();
def LRforward(steps):
		left();
		forward(steps);
		right();
	
def boxes():
		line( (Color.RED, Color.CYAN),True);        
		RLforward(4);
		line( (Color.PINK, Color.GREEN),True);

		LRforward(2);
		forward(2);
		line( (Color.ORANGE,Color.BLUE,Color.ORANGE),False);        

		LRforward(2);
		backward(2);      

		line( (Color.RED, Color.CYAN),True);        
		RLforward(4);
		line( (Color.PINK, Color.GREEN),True);
		

def growFlowers():
		boxes();
		LRforward(1);
		backward(8);
		RLforward(5);
		boxes();
# END SOLUTION 

growFlowers()