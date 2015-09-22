package lessons.recursion.logo.koch;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class PentaKoch extends ExerciseTemplated {

	public PentaKoch(Game game, Lesson lesson) {
		super(game, lesson);

		/* Create initial situation */
		World[] myWorlds = new World[6];
		myWorlds[0] = new TurtleWorld(game, "pentaKoch(0,200)", 400, 400);
		myWorlds[0].setParameter(new Object[] {0,200.});		
		new Turtle(myWorlds[0], "Hawksbill", 100, 250, 0, Color.red);
		
		myWorlds[1] = new TurtleWorld(game, "pentaKoch(1,200)", 400, 400);
		myWorlds[1].setParameter(new Object[] {1,200.});		
		new Turtle(myWorlds[1], "Hawksbill", 75, 250, 0, Color.red);

		myWorlds[2] = new TurtleWorld(game, "pentaKoch(2,200)", 400, 400);
		myWorlds[2].setParameter(new Object[] {2,200.});		
		new Turtle(myWorlds[2], "Hawksbill", 50, 250, 0, Color.red);

		myWorlds[3] = new TurtleWorld(game, "pentaKoch(3,200)", 400, 400);
		myWorlds[3].setParameter(new Object[] {3,200.});		
		new Turtle(myWorlds[3], "Hawksbill", 35, 250, 0, Color.red);
		
		myWorlds[4] = new TurtleWorld(game, "pentaKoch(4,175)", 400, 400);
		myWorlds[4].setParameter(new Object[] {4,175.});		
		new Turtle(myWorlds[4], "Hawksbill", 25, 250,0, Color.red);

		myWorlds[5] = new TurtleWorld(game, "pentaKoch(5,150)", 400, 400);
		myWorlds[5].setParameter(new Object[] {5,150.});		
		new Turtle(myWorlds[5], "Hawksbill", 15, 250,0, Color.red);
		
		setup(myWorlds);
	}
}
