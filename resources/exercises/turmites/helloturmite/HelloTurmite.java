package turmites.helloturmite;


import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.turmites.TurmiteWorld;

public class HelloTurmite extends ExerciseTemplated {
	final static int NOTURN = 1;
	final static int LEFT   = 2;
	final static int BACK   = 4;
	final static int RIGHT  = 8;

	public HelloTurmite() {
		super("HelloTurmite", "HelloTurmite");
		tabName = "Turmite";

		BuggleWorld[] myWorlds = new BuggleWorld[] { // Name, #steps, rules, worldWidth, worldHeight, buggleX, buggleY
				new TurmiteWorld("crabe (8342 steps)",           8342, 
						         new int[][][] {{{1, LEFT, 0}, {1, LEFT, 1}}, {{0, NOTURN, 0}, {0, NOTURN, 1}}}, 
						         78, 72, 8, 33),
				new TurmiteWorld("snail (10100 steps)",         10100, 
						         new int[][][] {{{1, NOTURN, 1}, {1, RIGHT, 0}}, {{1, LEFT, 1}, {0, NOTURN, 0}}}, 
						         68, 72, 36, 33),
				new TurmiteWorld("stepped pyramid (4800 steps)", 4800, 
						         new int[][][] {{{0, LEFT, 1}, {0, RIGHT, 0}}, {{1, RIGHT, 1}, {1, NOTURN, 0}}}, 
						         65, 65, 60, 55), 
		};

		setup(myWorlds);

	}
}