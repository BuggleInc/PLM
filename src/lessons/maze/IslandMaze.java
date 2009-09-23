package lessons.maze;

import java.awt.Color;

import jlm.core.Game;
import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.Entity;
import jlm.universe.World;
import universe.bugglequest.AbstractBuggle;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.exception.AlreadyHaveBaggleException;
import universe.bugglequest.exception.NoBaggleUnderBuggleException;

public class IslandMaze extends ExerciseTemplated {

	public IslandMaze(Lesson lesson) {
		super(lesson);
		tabName = "Escaper";
				
		/* Create initial situation */
		BuggleWorld myWorlds[] = new BuggleWorld[1];
		myWorlds[0] = new BuggleWorld("Labyrinth", 1, 1); 
		loadMap(myWorlds[0],"lessons/maze/IslandMaze");
		new Buggle(myWorlds[0], "Thésée", 4, 10, Direction.NORTH, Color.black, Color.lightGray);
		
		newSourceAliased("lessons.maze.Main","lessons.maze.WallFollowerMaze","Escaper");

		setup(myWorlds);		
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
	
	@Override
	public boolean check() {
		for (World w: Game.getInstance().getCurrentLesson().getCurrentExercise().getCurrentWorld())
			for (Entity e:w.getEntities()) {
				if (!((AbstractBuggle) e).isCarryingBaggle())
					return false;
			}
		return true;
	}
}
