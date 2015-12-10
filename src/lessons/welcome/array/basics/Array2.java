package lessons.welcome.array.basics;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class Array2 extends ExerciseTemplated {

	public Array2(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();
		BuggleWorld[] myWorlds = new BuggleWorld[3];

		myWorlds[0] = new BuggleWorld(game, "Pattern 1",6,6);
		myWorlds[0].setColor(0,0,Color.red);
		myWorlds[0].setColor(0,1,Color.cyan);
		myWorlds[0].setColor(0,2,Color.green);
		myWorlds[0].setColor(0,3,Color.magenta);
		myWorlds[0].setColor(0,4,Color.orange);
		myWorlds[0].setColor(0,5,Color.pink);
		myWorlds[0].addContent(1,0,"1");
		myWorlds[0].addContent(2,0,"2");
		myWorlds[0].addContent(3,0,"3");
		myWorlds[0].addContent(4,0,"4");
		myWorlds[0].addContent(5,0,"5");

		myWorlds[1] = new BuggleWorld(game, "Pattern 2",7,7);
		myWorlds[1].setColor(0,2,Color.red);
		myWorlds[1].setColor(0,6,Color.cyan);
		myWorlds[1].setColor(0,5,Color.green);
		myWorlds[1].setColor(0,4,Color.magenta);
		myWorlds[1].setColor(0,3,Color.orange);
		myWorlds[1].setColor(0,0,Color.pink);
		myWorlds[1].setColor(0,1,Color.yellow);
		myWorlds[1].addContent(1,0,"1");
		myWorlds[1].addContent(2,0,"2");
		myWorlds[1].addContent(3,0,"3");
		myWorlds[1].addContent(4,0,"3");
		myWorlds[1].addContent(5,0,"2");
		myWorlds[1].addContent(6,0,"1");

		myWorlds[2] = new BuggleWorld(game, "Pattern 3",8,8);
		myWorlds[2].setColor(0,0,Color.red);
		myWorlds[2].setColor(0,7,Color.cyan);
		myWorlds[2].setColor(0,1,Color.green);
		myWorlds[2].setColor(0,6,Color.magenta);
		myWorlds[2].setColor(0,2,Color.orange);
		myWorlds[2].setColor(0,5,Color.pink);
		myWorlds[2].setColor(0,3,Color.yellow);
		myWorlds[2].setColor(0,4,Color.black);
		myWorlds[2].addContent(1,0,"7");
		myWorlds[2].addContent(2,0,"6");
		myWorlds[2].addContent(3,0,"5");
		myWorlds[2].addContent(4,0,"4");
		myWorlds[2].addContent(5,0,"5");
		myWorlds[2].addContent(6,0,"6");
		myWorlds[2].addContent(7,0,"7");

		for (int w=0;w<3;w++) {
			for (int i=0;i<myWorlds[w].getHeight();i++) {
				myWorlds[w].putTopWall(i,0);
				myWorlds[w].putLeftWall(0,i);
			}
		}

		new SimpleBuggle(myWorlds[0], "Picasso", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorlds[1], "Braque", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorlds[2], "Ingres", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);

		setup(myWorlds);
	}
}
