package lessons.welcome.methods.basics;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouse extends ExerciseTemplated {

	public MethodsDogHouse(Game game, Lesson lesson) {
		super(game, lesson);
		BuggleWorld myWorld =  new BuggleWorld(game, "World",7,7);
		new SimpleBuggle(myWorld, "Puppy", 0, 6, Direction.EAST, Color.red, Color.red);
		
		setup(myWorld);
	}
}
