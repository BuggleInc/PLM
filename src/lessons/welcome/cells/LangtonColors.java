package lessons.welcome.cells;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class LangtonColors extends ExerciseTemplated {
	Color[] allColors = {Color.white, Color.black, Color.blue, Color.cyan, Color.green, Color.orange, Color.red, 
			Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray};

	BuggleWorld createWorld(String rule, int nbSteps, int width, int height, int buggleX, int buggleY) {
		BuggleWorld bw = new BuggleWorld(rule+" ("+nbSteps+" steps)", width, height);
		
		Color[] colors = new Color[rule.length()];
		for (int i=0; i<rule.length(); i++)
			colors[i] = allColors[i];
		
		bw.setParameter(new Object[] {
			nbSteps,
			rule.toCharArray(),
			colors
		});
		
		new Buggle(bw,"ant",buggleX,buggleY,Direction.NORTH,Color.red,Color.black);
		
		bw.setVisibleGrid(false);
		bw.setDelay(1);
		return bw;
	}
	
	public LangtonColors(Lesson lesson) {
		super(lesson);
		tabName = "LangtonsAnt";

		BuggleWorld[] myWorlds = new BuggleWorld[] {
				 createWorld("RL",           12000, 100, 70, 66, 23),
				 createWorld("LLRR",         20000,  30, 30, 15, 15),
				 createWorld("LRRRRRLLR",     9000,  25, 24, 11, 12),
				 createWorld("LLRRRLRLRLLR", 33000, 100, 60, 80, 30),
				 createWorld("RRLLLRLLLRRR", 30000,  80, 90, 60, 28),
		};
		
		setup(myWorlds);

	}
}
