package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsPictureLarge extends ExerciseTemplated {

	public MethodsPictureLarge(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();
		BuggleWorld myWorld =  new BuggleWorld(game, "World",45,45);
		myWorld.setDelay(5);
		new SimpleBuggle(myWorld, "Picasso", 0, 44, Direction.EAST, Color.black, Color.lightGray);
				
		setup(myWorld);
	}
}
