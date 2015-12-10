package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsPicture extends ExerciseTemplated {

	public MethodsPicture(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();
		BuggleWorld myWorld =  new BuggleWorld(game, "World",15,15);
		myWorld.setDelay(20);
		new SimpleBuggle(myWorld, "Picasso", 0, 14, Direction.EAST, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
