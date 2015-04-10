package lessons.turmites.langtoncolors;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.bugglequest.SimpleBuggle;

public class LangtonColorsEntity extends SimpleBuggle {
	Color[] allColors = {Color.white, Color.black, Color.blue, Color.cyan, Color.green, Color.orange, Color.red, 
			Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray};

	/* BEGIN TEMPLATE */
	public void step(char[] rule, Color[] colors) {
		/* BEGIN SOLUTION */
		Color current = getGroundColor(); 
		for (int i=0;i<colors.length;i++) {
			if (current.equals(colors[i])) {
				switch (rule[i]) {
				case 'L': left(); break;
				case 'R': right(); break;
				default:
					getGame().getLogger().log("Unknown command associated to i="+i+": "+rule[i]);
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

		Color[] colors = new Color[rule.length];
		for (int i=0; i<rule.length; i++)
			colors[i] = allColors[i];

		for (int i=0;i<nbSteps;i++) {
			stepDone();
			step(rule,colors);
		}
	}

	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));

		try {
			switch(num){
			case 200 :
				out.write(((Integer)getParam(0)).toString());
				out.write("\n");
				out.flush();

				break;
			case 201:
				stepDone();
				break;
			case 202:
				char[] ch = (char[])getParam(1);
				String param="";
				for(int i=0;i<ch.length;i++){
					param+=ch[i];
				}
				out.write(param);
				out.write("\n");
				out.flush();
				break;

			default:
				super.command(command, out);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stepDone(){
		((lessons.turmites.universe.TurmiteWorld)world).stepDone();
	}
}
