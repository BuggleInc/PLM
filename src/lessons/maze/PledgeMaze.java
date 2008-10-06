package lessons.maze;

import java.awt.Color;

import lessons.Lesson;
import lessons.ExerciseTemplated;
import bugglequest.core.AbstractBuggle;
import bugglequest.core.Buggle;
import bugglequest.core.Direction;
import bugglequest.core.World;
import bugglequest.exception.AlreadyHaveBaggleException;
import bugglequest.exception.NoBaggleUnderBuggleException;

public class PledgeMaze extends ExerciseTemplated {

	public PledgeMaze(Lesson lesson) {
		super(lesson);
		name = "Perdu au milieu des îlots";
		tabName = "PledgeMaze";
				
		/* Create initial situation */
		World myWorld = new World("Labyrinth",1,1);

		new Buggle(myWorld, "Thésée", 4, 10, Direction.NORTH, Color.black, Color.lightGray);
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
