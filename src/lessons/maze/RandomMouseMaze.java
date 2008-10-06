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
