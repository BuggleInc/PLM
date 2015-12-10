package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PictureMono2 extends ExerciseTemplated {

	public PictureMono2(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();
		BuggleWorld myWorld =  new BuggleWorld(game, "World",21,21);
		myWorld.setDelay(20);
		new SimpleBuggle(myWorld, "Picasso", 0, 20, Direction.EAST, Color.black, Color.lightGray);

		setup(myWorld);
	}
}
