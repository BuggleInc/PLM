package lessons.welcome.methods.slug;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;

public class SlugSnail extends ExerciseTemplated {

	public SlugSnail(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();

		BuggleWorld[] myWorlds = new BuggleWorld[2];

		BuggleWorld myWorld = new BuggleWorld(game, "Kitty", 8, 7);
		for (int i = 5; i >= 2; i--)
			myWorld.setColor(6, i,Color.pink);
		myWorld.setColor(6, 1, Color.orange);
		myWorld.setColor(5, 2,Color.pink);
		for (int i = 2; i <= 4; i++)
			myWorld.setColor(4, i,Color.pink);
		myWorld.setColor(3, 4,Color.pink);
		for (int i = 4; i >= 1; i--)
			myWorld.setColor(2, i,Color.pink);
		myWorld.setColor(1, 1,Color.pink);
		myWorld.setColor(0, 1,Color.pink);
		myWorld.setParameter(new Object[] {Color.pink});
		
		try {
			myWorld.addBaggle(0, 1);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		myWorlds[0] = myWorld;

		myWorld = new BuggleWorld(game, "Snail", 8, 7);
		for (int i = 5; i >= 2; i--)
		    myWorld.setColor(6, i,Color.orange);
		myWorld.putTopWall(6, 2);
		myWorld.putLeftWall(6, 3);
		myWorld.putLeftWall(7, 2);
		myWorld.setColor(5, 2,Color.orange);
		myWorld.setColor(5, 1,Color.orange);
		myWorld.setColor(5, 0,Color.orange);
		myWorld.setColor(4, 0,Color.orange);
		myWorld.setColor(3, 0,Color.orange);
		myWorld.setColor(2, 0,Color.orange);
		myWorld.setColor(4, 2,Color.pink);
				       
		myWorld.setColor(3, 4,Color.orange);
		for (int i = 4; i >= 1; i--)
			myWorld.setColor(2, i,Color.orange);
		myWorld.setColor(4, 4,Color.orange);
		myWorld.setParameter(new Object[] {Color.orange});

		try {
			myWorld.addBaggle(3, 4);
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		myWorlds[1] = myWorld;

		SimpleBuggle hunter = new SimpleBuggle(myWorlds[0], "Hunter", 6, 6, Direction.NORTH, Color.black, Color.lightGray);
		hunter.brushDown();

		hunter = new SimpleBuggle(myWorlds[1], "Hunter", 6, 6, Direction.NORTH, Color.black, Color.lightGray);
		hunter.brushDown();

		setup(myWorlds);
	}

}