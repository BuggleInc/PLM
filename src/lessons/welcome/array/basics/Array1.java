package lessons.welcome.array.basics;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

public class Array1 extends ExerciseTemplated {

	public Array1(Game game, Lesson lesson) {
		super(game, lesson);
		setToolbox();
		BuggleWorld[] myWorlds = new BuggleWorld[3];
				
		myWorlds[0] = new BuggleWorld(game, "Pattern 1",6,6);
		((BuggleWorld) myWorlds[0]).setColor(0,0,Color.red);
		((BuggleWorld) myWorlds[0]).setColor(0,1,Color.cyan);
		((BuggleWorld) myWorlds[0]).setColor(0,2,Color.green);
		((BuggleWorld) myWorlds[0]).setColor(0,3,Color.magenta);
		((BuggleWorld) myWorlds[0]).setColor(0,4,Color.orange);
		((BuggleWorld) myWorlds[0]).setColor(0,5,Color.pink);

		myWorlds[1] = new BuggleWorld(game, "Pattern 2",7,7);
		((BuggleWorld) myWorlds[1]).setColor(0,2,Color.red);
		((BuggleWorld) myWorlds[1]).setColor(0,6,Color.cyan);
		((BuggleWorld) myWorlds[1]).setColor(0,5,Color.green);
		((BuggleWorld) myWorlds[1]).setColor(0,4,Color.magenta);
		((BuggleWorld) myWorlds[1]).setColor(0,3,Color.orange);
		((BuggleWorld) myWorlds[1]).setColor(0,0,Color.pink);
		((BuggleWorld) myWorlds[1]).setColor(0,1,Color.yellow);

		myWorlds[2] = new BuggleWorld(game, "Pattern 3",8,8);
		((BuggleWorld) myWorlds[2]).setColor(0,0,Color.red);
		((BuggleWorld) myWorlds[2]).setColor(0,7,Color.cyan);
		((BuggleWorld) myWorlds[2]).setColor(0,1,Color.green);
		((BuggleWorld) myWorlds[2]).setColor(0,6,Color.magenta);
		((BuggleWorld) myWorlds[2]).setColor(0,2,Color.orange);
		((BuggleWorld) myWorlds[2]).setColor(0,5,Color.pink);
		((BuggleWorld) myWorlds[2]).setColor(0,3,Color.yellow);
		((BuggleWorld) myWorlds[2]).setColor(0,4,Color.black);

		new SimpleBuggle(myWorlds[0], "Picasso", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorlds[1], "Braque", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		new SimpleBuggle(myWorlds[2], "Ingres", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
		
		setup(myWorlds);
	}
}
