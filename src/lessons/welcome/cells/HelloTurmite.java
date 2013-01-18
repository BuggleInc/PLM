package lessons.welcome.cells;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bugglequest.BuggleWorld;

public class HelloTurmite extends ExerciseTemplated {
	final static int NOTURN = 1;
	final static int LEFT   = 2;
	final static int BACK   = 4;
	final static int RIGHT  = 8;
	
	public HelloTurmite(Lesson lesson) {
		super(lesson);
		tabName = "Turmite";

		BuggleWorld[] myWorlds = new BuggleWorld[] {
				new TurmiteWorld("crabe (8342 steps)",           8342, new int[][][] {{{1, 2, 0}, {1, 2, 1}}, {{0, 1, 0}, {0, 1, 1}}}, 78, 72, 8, 33),
				new TurmiteWorld("snail (10100 steps)",         10100, new int[][][] {{{1, 1, 1}, {1, 8, 0}}, {{1, 2, 1}, {0, 1, 0}}}, 68, 72, 36, 33),
				new TurmiteWorld("stepped pyramid (4800 steps)", 4800, new int[][][] {{{0, 2, 1}, {0, 8, 0}}, {{1, 8, 1}, {1, 1, 0}}}, 65, 65, 60, 55), //6
		};
		
		setup(myWorlds);

	}
}