package lessons.welcome.cells;

import java.awt.Color;

public class LangtonColorsEntity extends jlm.universe.bugglequest.SimpleBuggle {
	/* BEGIN TEMPLATE */
public void step(char[] rule, Color[] colors) {
		/* BEGIN SOLUTION */
	    Color current = getGroundColor(); 
		for (int i=0;i<colors.length;i++) {
			if (current.equals(colors[i])) {
				switch (rule[i]) {
				case 'L': turnLeft(); break;
				case 'R': turnRight(); break;
				default:
					System.out.println("Unknown command associated to i="+i+": "+rule[i]);
				}
				
				setBrushColor(colors[(i+1) % colors.length]);
				brushDown();
				brushUp();
				
				forward();
				
				return;
			}
		}
		/* END SOLUTION */
}
	/* END TEMPLATE */

	@Override
	public void run() { 
		int nbSteps = (Integer)getParam(0);
		char[] rule = ((char[])getParam(1));
		Color[] colors = ((Color[])getParam(2));
		
		for (int i=0;i<nbSteps;i++) 
			step(rule,colors);
	}
}
