package lessons.welcome;

import java.awt.Color;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

import universe.bugglequest.Buggle;
import universe.bugglequest.BuggleWorld;
import universe.bugglequest.Direction;

public class Array extends ExerciseTemplated {

	public Array(Lesson lesson) {
		super(lesson);
		name = "Tricot (et tableaux)";
		BuggleWorld[] myWorlds = new BuggleWorld[3];
		
		/*for (int i=0;i<8;i++) {
			myWorld.getCell(i, 0).putTopWall();
			myWorld.getCell(0, i).putLeftWall();
		}*/
		
		myWorlds[0] = new BuggleWorld("Pattern 1",6,6);
		myWorlds[0].getCell(0,0).setColor(Color.red);
		myWorlds[0].getCell(0,1).setColor(Color.cyan);
		myWorlds[0].getCell(0,2).setColor(Color.green);
		myWorlds[0].getCell(0,3).setColor(Color.magenta);
		myWorlds[0].getCell(0,4).setColor(Color.orange);
		myWorlds[0].getCell(0,5).setColor(Color.pink);
//		myWorlds[0].getCell(0,6).setColor(Color.yellow);

		myWorlds[1] = new BuggleWorld("Pattern 2",7,7);
		myWorlds[1].getCell(0,2).setColor(Color.red);
		myWorlds[1].getCell(0,6).setColor(Color.cyan);
		myWorlds[1].getCell(0,5).setColor(Color.green);
		myWorlds[1].getCell(0,4).setColor(Color.magenta);
		myWorlds[1].getCell(0,3).setColor(Color.orange);
		myWorlds[1].getCell(0,0).setColor(Color.pink);
		myWorlds[1].getCell(0,1).setColor(Color.yellow);

		myWorlds[2] = new BuggleWorld("Pattern 3",8,8);
		myWorlds[2].getCell(0,0).setColor(Color.red);
		myWorlds[2].getCell(0,7).setColor(Color.cyan);
		myWorlds[2].getCell(0,1).setColor(Color.green);
		myWorlds[2].getCell(0,6).setColor(Color.magenta);
		myWorlds[2].getCell(0,2).setColor(Color.orange);
		myWorlds[2].getCell(0,5).setColor(Color.pink);
		myWorlds[2].getCell(0,3).setColor(Color.yellow);
		myWorlds[2].getCell(0,4).setColor(Color.black);

		new Buggle(myWorlds[0], "Picasso", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorlds[1], "Braque", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new Buggle(myWorlds[2], "Ingres", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		
		setup(myWorlds);
	}
}
