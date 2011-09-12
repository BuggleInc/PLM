package lessons.welcome.cells;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class Turmite extends ExerciseTemplated {
	final static int NOTURN = 1;
	final static int LEFT   = 2;
	final static int BACK   = 4;
	final static int RIGHT  = 8;
	
	BuggleWorld createWorld(String title, int nbSteps, int[][][]rule, int width, int height, int buggleX, int buggleY) {
		BuggleWorld bw = new BuggleWorld(title+" ("+nbSteps+" steps)", width, height);
				
		bw.setParameter(new Object[] {
			nbSteps,
			rule
		});
		
		new Buggle(bw,"ant",buggleX,buggleY,Direction.NORTH,Color.red,Color.red);
		
		bw.setDelay(1);
		bw.setVisibleGrid(false);
		return bw;
	}
	
	public Turmite(Lesson lesson) {
		super(lesson);
		tabName = "Turmite";

		BuggleWorld[] myWorlds = new BuggleWorld[] {
				 createWorld("crabe",           8342, new int[][][] {{{1, 2, 0}, {1, 2, 1}}, {{0, 1, 0}, {0, 1, 1}}}, 78, 72, 70, 33),
				 createWorld("snail",          10100, new int[][][]  {{{1, 1, 1}, {1, 8, 0}}, {{1, 2, 1}, {0, 1, 0}}}, 68, 72, 32, 33),
				 createWorld("stepped pyramid", 4800, new int[][][]  {{{0, 2, 1}, {0, 8, 0}}, {{1, 8, 1}, {1, 1, 0}}}, 65, 65, 5, 55), //6
		};
		
		setup(myWorlds);

	}
}