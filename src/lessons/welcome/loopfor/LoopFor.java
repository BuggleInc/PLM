package lessons.welcome.loopfor;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class LoopFor extends ExerciseTemplated {

	public LoopFor(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "Program";
		setToolbox();
		
		BuggleWorld myWorld = new BuggleWorld(game, "Kitchen",7,7);
		for (int i=0;i<7;i++) {
			new SimpleBuggle(myWorld, "Hungry"+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
		    
		    try {
				myWorld.addBaggle(i, 6-i);
			} catch (AlreadyHaveBaggleException e) {
				e.printStackTrace();
			}
		}
		
		setup(myWorld);
	}
}
