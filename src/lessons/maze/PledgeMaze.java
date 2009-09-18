package lessons.maze;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Direction;
import universe.bugglequest.AbstractBuggle;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.exception.AlreadyHaveBaggleException;
import universe.bugglequest.exception.NoBaggleUnderBuggleException;

public class PledgeMaze extends ExerciseTemplated {

	public PledgeMaze(Lesson lesson) {
		super(lesson);
		tabName = "IslandMaze";
				
		/* Create initial situation */
		BuggleWorld myWorlds[] = new BuggleWorld[1];
		myWorlds[0] = new BuggleWorld("Labyrinth", 4, 4); 
		loadMap(myWorlds[0],"lessons/maze/PledgeMaze");

		new Buggle(myWorlds[0], "Thésée", 12, 14, Direction.NORTH, Color.black, Color.lightGray);
		setup(myWorlds);
	}

	// to shorten loading time	
	@Override
	protected void computeAnswer(){
		AbstractBuggle b = (AbstractBuggle)answerWorld[0].entities().next();
		b.setPos(19, 19);
		b.setDirection(Direction.EAST);
		try {
			b.pickUpBaggle();
		} catch (NoBaggleUnderBuggleException e) {
			e.printStackTrace();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}		
	}
}
