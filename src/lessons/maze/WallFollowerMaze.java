package lessons.maze;

import java.awt.Color;

import jlm.bugglequest.AbstractBuggle;
import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;
import jlm.exception.AlreadyHaveBaggleException;
import jlm.exception.NoBaggleUnderBuggleException;

import lessons.Lesson;
import lessons.ExerciseTemplated;

public class WallFollowerMaze extends ExerciseTemplated {

	public WallFollowerMaze(Lesson lesson) {
		super(lesson);
		name = "Longer les murs";
		tabName = "WallFollowerMaze";
				
		/* Create initial situation */
		World myWorld = new World("Labyrinth",1,12);
		new Buggle(myWorld, "Thésée", 1, 11, Direction.NORTH, Color.black, Color.lightGray);
		setup(myWorld);
	}

	
	// to shorten loading time
	@Override
	protected void computeAnswer(){
		AbstractBuggle b = answerWorld[0].buggles().next();
		b.setPos(11, 5);
		try {
			b.pickUpBaggle();
		} catch (NoBaggleUnderBuggleException e) {
			e.printStackTrace();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}		
	}
}
