package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class Array2 extends ExerciseTemplated {

	public Array2(Lesson lesson) {
		super(lesson);
		BuggleWorld[] myWorlds = new BuggleWorld[3];

		myWorlds[0] = new BuggleWorld("Pattern 1",6,6);
		myWorlds[0].getCell(0,0).setColor(Color.red);
		myWorlds[0].getCell(0,1).setColor(Color.cyan);
		myWorlds[0].getCell(0,2).setColor(Color.green);
		myWorlds[0].getCell(0,3).setColor(Color.magenta);
		myWorlds[0].getCell(0,4).setColor(Color.orange);
		myWorlds[0].getCell(0,5).setColor(Color.pink);
		myWorlds[0].getCell(1,0).addContent("1");
		myWorlds[0].getCell(2,0).addContent("2");
		myWorlds[0].getCell(3,0).addContent("3");
		myWorlds[0].getCell(4,0).addContent("4");
		myWorlds[0].getCell(5,0).addContent("5");

		myWorlds[1] = new BuggleWorld("Pattern 2",7,7);
		myWorlds[1].getCell(0,2).setColor(Color.red);
		myWorlds[1].getCell(0,6).setColor(Color.cyan);
		myWorlds[1].getCell(0,5).setColor(Color.green);
		myWorlds[1].getCell(0,4).setColor(Color.magenta);
		myWorlds[1].getCell(0,3).setColor(Color.orange);
		myWorlds[1].getCell(0,0).setColor(Color.pink);
		myWorlds[1].getCell(0,1).setColor(Color.yellow);
		myWorlds[1].getCell(1,0).addContent("1");
		myWorlds[1].getCell(2,0).addContent("2");
		myWorlds[1].getCell(3,0).addContent("3");
		myWorlds[1].getCell(4,0).addContent("3");
		myWorlds[1].getCell(5,0).addContent("2");
		myWorlds[1].getCell(6,0).addContent("1");

		myWorlds[2] = new BuggleWorld("Pattern 3",8,8);
		myWorlds[2].getCell(0,0).setColor(Color.red);
		myWorlds[2].getCell(0,7).setColor(Color.cyan);
		myWorlds[2].getCell(0,1).setColor(Color.green);
		myWorlds[2].getCell(0,6).setColor(Color.magenta);
		myWorlds[2].getCell(0,2).setColor(Color.orange);
		myWorlds[2].getCell(0,5).setColor(Color.pink);
		myWorlds[2].getCell(0,3).setColor(Color.yellow);
		myWorlds[2].getCell(0,4).setColor(Color.black);
		myWorlds[2].getCell(1,0).addContent("7");
		myWorlds[2].getCell(2,0).addContent("6");
		myWorlds[2].getCell(3,0).addContent("5");
		myWorlds[2].getCell(4,0).addContent("4");
		myWorlds[2].getCell(5,0).addContent("5");
		myWorlds[2].getCell(6,0).addContent("6");
		myWorlds[2].getCell(7,0).addContent("7");

		for (int w=0;w<3;w++) {
			for (int i=0;i<myWorlds[w].getHeight();i++) {
				myWorlds[w].getCell(i, 0).putTopWall();
				myWorlds[w].getCell(0, i).putLeftWall();
			}
		}

		new Buggle(myWorlds[0], "Picasso", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorlds[1], "Braque", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorlds[2], "Ingres", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);

		setup(myWorlds);
	}
}
