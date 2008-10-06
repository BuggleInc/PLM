package lessons.welcome;

import java.awt.Color;

import jlm.bugglequest.Buggle;
import jlm.bugglequest.Direction;
import jlm.bugglequest.World;
import jlm.exception.AlreadyHaveBaggleException;

import lessons.ExerciseTemplated;
import lessons.Lesson;

public class SlugHunting extends ExerciseTemplated {

	public SlugHunting(Lesson lesson) {
		super(lesson);
		name = "Chasse à la limace";
		tabName = "Program";

		World[] myWorlds = new World[2];

		World myWorld = new World("Forrest", 7, 7);
		for (int i = 5; i >= 2; i--)
			myWorld.getCell(6, i).setColor(Color.green);
		myWorld.getCell(5, 2).setColor(Color.green);
		for (int i = 2; i <= 4; i++)
			myWorld.getCell(4, i).setColor(Color.green);
		myWorld.getCell(3, 4).setColor(Color.green);
		for (int i = 4; i >= 1; i--)
			myWorld.getCell(2, i).setColor(Color.green);
		myWorld.getCell(1, 1).setColor(Color.green);
		myWorld.getCell(0, 1).setColor(Color.green);
		try {
			myWorld.getCell(0, 1).newBaggle();
		} catch (AlreadyHaveBaggleException e) {
			e.printStackTrace();
		}
		myWorlds[0] = myWorld;

		myWorld = new World("Desert", 7, 7);
		for (int i = 5; i >= 2; i--)
			myWorld.getCell(6, i).setColor(Color.green);
		myWorld.getCell(6, 2).putTopWall();
		myWorld.getCell(6, 3).putLeftWall();
		myWorld.getCell(0, 2).putLeftWall();
		myWorld.getCell(5, 2).setColor(Color.green);
		myWorld.getCell(5, 1).setColor(Color.green);
		myWorld.getCell(5, 0).setColor(Color.green);
		myWorld.getCell(4, 0).setColor(Color.green);
		myWorld.getCell(3, 0).setColor(Color.green);
		myWorld.getCell(2, 0).setColor(Color.green);

		myWorld.getCell(3, 4).setColor(Color.green);
		for (int i = 4; i >= 1; i--)
			myWorld.getCell(2, i).setColor(Color.green);
		myWorld.getCell(4, 4).setColor(Color.green);

		try {
			myWorld.getCell(3, 4).newBaggle();
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