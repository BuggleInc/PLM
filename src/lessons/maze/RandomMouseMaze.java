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

public class RandomMouseMaze extends ExerciseTemplated {

	public RandomMouseMaze(Lesson lesson) {
		super(lesson);
		name = "La souris folle";
		tabName = "RandomMouseMaze";
				
		/* Create initial situation */
		World myWorld = new World("Labyrinth",4,4);
		new Buggle(myWorld, "Thésée", 0, 3, Direction.NORTH, Color.black, Color.lightGray);
		setup(myWorld);
	}

	// Skip random solver since it might take too much time to find solution.
	@Override
	protected void computeAnswer(){
		AbstractBuggle b = answerWorld[0].buggles().next();
		b.setPos(3, 3);
		b.turnBack();
		
		try {
			b.pickUpBaggle();
		} catch (NoBaggleUnderBuggleException e) {
			e.printStackTrace();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}	
	}
	
}
