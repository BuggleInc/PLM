package loopdowhile;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class LoopDoWhile extends ExerciseTemplated {

	public LoopDoWhile() {
		super("LoopDoWhile", "LoopDoWhile");
		tabName = "Program";
		//setToolbox();
				
		BuggleWorld myWorld = new BuggleWorld("Yellow Submarine",13,7);
		for (int i=0;i<7;i++) {
			new SimpleBuggle(myWorld, "Beatles"+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
		    for (int j=6; j>i; j--)
		    	myWorld.setColor(i, j,Color.yellow);
		}
		for (int i=7;i<13;i++) {
			new SimpleBuggle(myWorld, "Beatles"+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
		    for (int j=0; j<i-6; j++)
		    	myWorld.setColor(i, 6-j,Color.yellow);
		}

    	setup(myWorld);
	}
}
