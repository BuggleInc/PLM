package lessons.maze;

import java.awt.Color;

import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.Entity;
import jlm.universe.World;
import jlm.universe.bugglequest.AbstractBuggle;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import jlm.universe.bugglequest.exception.NoBaggleUnderBuggleException;

public class RandomMouseMaze extends ExerciseTemplated {

	public RandomMouseMaze(Lesson lesson) {
		super(lesson);
		tabName = "RandomMouseMaze";
				
		/* Create initial situation */
		BuggleWorld myWorlds[] = new BuggleWorld[2];
		myWorlds[0] = new BuggleWorld("Swiss cheese", 4, 4); 
		loadMap(myWorlds[0],"resources/maze/maps/RandomMouseMaze");
		new Buggle(myWorlds[0], "Thésée", 0, 3, Direction.NORTH, Color.black, Color.lightGray);
		
		myWorlds[1] = new BuggleWorld("Blue cheese", 4, 4); 
		loadMap(myWorlds[1],"resources/maze/maps/RandomMouseMaze2");
		new Buggle(myWorlds[1], "ZoroRorronoa", 0, 3, Direction.NORTH, Color.black, Color.lightGray);
		
		setup(myWorlds);
	}

	// Skip random solver since it might take too much time to find solution.
	@Override
	protected void computeAnswer(){
		AbstractBuggle b = (AbstractBuggle)answerWorld[0].entities().next();
		b.setPosFromLesson(3, 3);
		b.turnBack();
		
		AbstractBuggle b2 = (AbstractBuggle)answerWorld[1].entities().next();
		b2.setPosFromLesson(3, 3);
		b2.setDirection(Direction.EAST);
		
		try {
			b.pickUpBaggle();
			b2.pickUpBaggle();
		} catch (NoBaggleUnderBuggleException e) {
			e.printStackTrace();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void check() {
		lastResult = new ExecutionProgress();
		for (World w: this.getCurrentWorld()) 
			for (Entity e:w.getEntities()) {
				lastResult.totalTests++;
				if (!((AbstractBuggle) e).isCarryingBaggle())
					lastResult.details += "Buggle "+e.getName()+" didn't find the baggle";
				else
					lastResult.passedTests++;
			}
	}
}
