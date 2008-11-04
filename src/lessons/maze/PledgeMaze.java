package lessons.maze;

import java.awt.Color;

import universe.bugglequest.AbstractBuggle;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;
import universe.bugglequest.exception.AlreadyHaveBaggleException;
import universe.bugglequest.exception.NoBaggleUnderBuggleException;


import lessons.Lesson;
import lessons.ExerciseTemplated;

public class PledgeMaze extends ExerciseTemplated {

	public PledgeMaze(Lesson lesson) {
		super(lesson);
		name = "Perdu au milieu des îlots";
		tabName = "PledgeMaze";
				
		/* Create initial situation */
		BuggleWorld myWorld = new BuggleWorld("Labyrinth",1,1);

		new Buggle(myWorld, "Thésée", 4, 10, Direction.NORTH, Color.black, Color.lightGray);
		setup(myWorld);
	}

	// to shorten loading time	
	@Override
	protected void computeAnswer(){
		AbstractBuggle b = (AbstractBuggle)answerWorld[0].entities().next();
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
