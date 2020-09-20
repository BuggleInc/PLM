package lessons.welcome.methods.slug;

import java.awt.Color;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class SlugTracking extends ExerciseTemplated {

	public SlugTracking(Lesson lesson) {
		super(lesson);

		BuggleWorld[] myWorlds = new BuggleWorld[2];

		BuggleWorld myWorld = new BuggleWorld("Forrest", 8, 7);
		for (int i = 5; i >= 2; i--)
			myWorld.setColor(5, i,Color.green);
		myWorld.setColor(4, 2,Color.green);
		for (int i = 2; i <= 4; i++)
			myWorld.setColor(3, i,Color.green);
		myWorld.setColor(2, 4,Color.green);
		for (int i = 4; i >= 1; i--)
			myWorld.setColor(1, i,Color.green);
		myWorld.setColor(0, 1,Color.green);
		myWorld.putTopWall(5, 5);
		myWorld.setColor(6, 4,Color.green);
		myWorld.setColor(6, 5,Color.green);
		myWorld.putLeftWall(7, 4);
		myWorld.putLeftWall(7, 5);
		try {
			myWorld.addBaggle(0, 1);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		SimpleBuggle hunter = new SimpleBuggle(myWorld, "Hunter", 5, 6, Direction.NORTH, Color.black, Color.lightGray);
		hunter.brushDown();
		myWorlds[0] = myWorld;

		myWorld = new BuggleWorld("Desert", 8, 7);
		for (int i = 5; i >= 2; i--)
		    myWorld.setColor(6, i,Color.green);
		myWorld.putTopWall(6, 2);
		myWorld.putLeftWall(6, 3);
		myWorld.putLeftWall(7, 2);
		myWorld.setColor(5, 2,Color.green);
		myWorld.setColor(5, 1,Color.green);
		myWorld.setColor(5, 0,Color.green);
		myWorld.setColor(4, 0,Color.green);
		myWorld.setColor(3, 0,Color.green);
		myWorld.setColor(2, 0,Color.green);
				       
		myWorld.setColor(3, 4,Color.green);
		for (int i = 4; i >= 1; i--)
			myWorld.setColor(2, i,Color.green);
		myWorld.setColor(4, 4,Color.green);

		try {
			myWorld.addBaggle(3, 4);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		myWorlds[1] = myWorld;

		hunter = new SimpleBuggle(myWorlds[1], "Hunter", 6, 6, Direction.NORTH, Color.black, Color.lightGray);
		hunter.brushDown();

		setup(myWorlds);
	}

}