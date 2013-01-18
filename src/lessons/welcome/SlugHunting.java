package lessons.welcome;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class SlugHunting extends ExerciseTemplated {

	public SlugHunting(Lesson lesson) {
		super(lesson);

		BuggleWorld[] myWorlds = new BuggleWorld[2];

		BuggleWorld myWorld = new BuggleWorld("Forrest", 7, 7);
		for (int i = 5; i >= 2; i--)
			myWorld.setColor(6, i,Color.green);
		myWorld.setColor(5, 2,Color.green);
		for (int i = 2; i <= 4; i++)
			myWorld.setColor(4, i,Color.green);
		myWorld.setColor(3, 4,Color.green);
		for (int i = 4; i >= 1; i--)
			myWorld.setColor(2, i,Color.green);
		myWorld.setColor(1, 1,Color.green);
		myWorld.setColor(0, 1,Color.green);
		try {
			myWorld.newBaggle(0, 1);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		myWorlds[0] = myWorld;

		myWorld = new BuggleWorld("Desert", 7, 7);
		for (int i = 5; i >= 2; i--)
		    myWorld.setColor(6, i,Color.green);
		myWorld.putTopWall(6, 2);
		myWorld.putLeftWall(6, 3);
		myWorld.putLeftWall(0, 2);
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
			myWorld.newBaggle(3, 4);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		myWorlds[1] = myWorld;

		Buggle hunter = new Buggle(myWorlds[0], "Hunter", 6, 6, Direction.NORTH, Color.black, Color.lightGray);
		hunter.brushDown();

		hunter = new Buggle(myWorlds[1], "Hunter", 6, 6, Direction.NORTH, Color.black, Color.lightGray);
		hunter.brushDown();

		setup(myWorlds);
	}

}