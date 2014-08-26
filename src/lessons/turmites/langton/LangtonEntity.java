package lessons.turmites.langton;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.bugglequest.SimpleBuggle;

public class LangtonEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	public void step() {
		/* BEGIN SOLUTION */
		if (getGroundColor().equals(Color.white)) {
			right();

			setBrushColor(Color.black);
			brushDown();
			brushUp();

			forward();
		} else {
			left();

			setBrushColor(Color.white);
			brushDown();
			brushUp();

			forward();				
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	@Override
	public void run() { 
		int nbSteps = (Integer)getParam(0); 
		for (int i=0;i<nbSteps;i++) {
			step();
			stepDone();
		}
	}
	
	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		switch(num){
		case 200 :
			try {
				out.write(((Integer)getParam(0)).toString());
				out.write("\n");
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 201:
			stepDone();
			break;
		default:
			super.command(command, out);
		}
	}
	
			
		
	
	public void stepDone(){
		((lessons.turmites.universe.TurmiteWorld)world).stepDone();
	}
}
