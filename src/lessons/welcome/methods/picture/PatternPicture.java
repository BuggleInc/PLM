package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class PatternPicture extends ExerciseTemplated {

	public PatternPicture(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();
		BuggleWorld myWorld =  new BuggleWorld(game, "World",8,8);
		
		for (int i=0;i<8;i++) {
			myWorld.putTopWall (i, 0);
			myWorld.putLeftWall(0, i);
		}
		
		new SimpleBuggle(myWorld, "Picasso", 0, 7, Direction.EAST, Color.black, Color.lightGray);
		
		setup(myWorld);
	}
}
