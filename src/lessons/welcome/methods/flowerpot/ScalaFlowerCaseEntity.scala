package lessons.welcome.methods.flowerpot;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;
class ScalaFlowerCaseEntity extends SimpleBuggle {

	override def run() {
		growFlowers();
	}
	/* BEGIN SOLUTION */

	def makeFlower(c:Color) {
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
	}

	def line(colors:Array[Color], returnBack:Boolean) {
		var first = true;
		for (c <- colors) {
			if (!first)
				forward(4);
			makeFlower(c);
			first = false;
		}

		if (returnBack)
			backward(4*(colors.length-1));    
	}
	def RLforward(steps: Int) {
		right();
		forward(steps);
		left();
	}
	def LRforward(steps: Int) {
		left();
		forward(steps);
		right();
	}
	def boxes() {
		line(Array(Color.RED, Color.CYAN),true);        
		RLforward(4);
		line(Array(Color.PINK, Color.GREEN),true);

		LRforward(2);
		forward(2);
		line(Array(Color.ORANGE,Color.BLUE,Color.ORANGE),false);        

		LRforward(2);
		backward(2);      

		line(Array(Color.RED, Color.CYAN),true);        
		RLforward(4);
		line(Array(Color.PINK, Color.GREEN),true);
	}
	def growFlowers() {
		boxes();
		LRforward(1);
		backward(8);
		RLforward(5);
		boxes();
	}
	/* END SOLUTION */
}
