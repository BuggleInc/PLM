package lessons.welcome.methods.flowerpot;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;
public class FlowerCaseEntity extends SimpleBuggle {

	public void run() {
		growFlowers();
	}
	/* BEGIN SOLUTION */

	void makeFlower(Color c) {
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

	void line(Color[] colors, boolean returnBack) {
		boolean first = true;
		for (Color c:colors) {
			if (!first)
				forward(4);
			makeFlower(c);
			first = false;
		}

		if (returnBack)
			backward(4*(colors.length-1));    
	}
	void RLforward(int steps) {
		right();
		forward(steps);
		left();
	}
	void LRforward(int steps) {
		left();
		forward(steps);
		right();
	}
	void boxes() {
		line(new Color[]{Color.RED, Color.CYAN},true);        
		RLforward(4);
		line(new Color[]{Color.PINK, Color.GREEN},true);

		LRforward(2);
		forward(2);
		line(new Color[]{Color.ORANGE,Color.BLUE,Color.ORANGE},false);        

		LRforward(2);
		backward(2);      

		line(new Color[]{Color.RED, Color.CYAN},true);        
		RLforward(4);
		line(new Color[]{Color.PINK, Color.GREEN},true);
	}
	void growFlowers() {
		boxes();
		LRforward(1);
		backward(8);
		RLforward(5);
		boxes();
	}
	/* END SOLUTION */
}
