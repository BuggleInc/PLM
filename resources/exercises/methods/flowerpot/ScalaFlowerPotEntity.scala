package lessons.welcome.methods.flowerpot;

import java.awt.Color;
import plm.universe.bugglequest.SimpleBuggle;

class ScalaFlowerPotEntity extends SimpleBuggle {

	override def run() {
	    growFlowers();
	}
	/* BEGIN SOLUTION */
	def makeFlower(c: Color) {
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
	}

	def line(c1:Color, c2: Color) {
	    makeFlower(c1);
	    forward(3);
	    makeFlower(c2);
	    backward(5);    
	}
	def halfLine(c:Color) {
	    forward(2);
	    makeFlower(c);
	    backward(3);
	}
	def growFlowers() {
	    line(Color.RED, Color.CYAN);
	    
	    right();    
	    forward(2);
	    left();
	    halfLine(Color.ORANGE);
	    right();
	    forward(2);
	    left();
	    
	    line(Color.PINK, Color.GREEN);
	}
	/* END SOLUTION */
}
