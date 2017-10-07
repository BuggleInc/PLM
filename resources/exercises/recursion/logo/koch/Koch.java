package recursion.logo.koch;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Koch extends ExerciseTemplated {

	public Koch() {
		super("Koch", "Koch");

		/* Create initial situation */
		World[] myWorlds = new World[6];
		myWorlds[0] = new TurtleWorld("snowFlake(0,200)", 400, 400);
		myWorlds[0].setParameter(new Object[] {0,200.});		
		new Turtle(myWorlds[0], "Hawksbill", 100, 300, -90, Color.red);
		
		myWorlds[1] = new TurtleWorld("snowFlake(1,200)", 400, 400);
		myWorlds[1].setParameter(new Object[] {1,200.});		
		new Turtle(myWorlds[1], "Hawksbill", 100, 300, -90, Color.red);

		myWorlds[2] = new TurtleWorld("snowFlake(2,200)", 400, 400);
		myWorlds[2].setParameter(new Object[] {2,200.});		
		new Turtle(myWorlds[2], "Hawksbill", 100, 300, -90, Color.red);

		myWorlds[3] = new TurtleWorld("snowFlake(3,200)", 400, 400);
		myWorlds[3].setParameter(new Object[] {3,200.});		
		new Turtle(myWorlds[3], "Hawksbill", 100, 300,-90, Color.red);
		
		myWorlds[4] = new TurtleWorld("snowFlake(4,200)", 400, 400);
		myWorlds[4].setParameter(new Object[] {4,200.});		
		new Turtle(myWorlds[4], "Hawksbill", 100, 300,-90, Color.red);

		myWorlds[5] = new TurtleWorld("snowFlake(5,200)", 400, 400);
		myWorlds[5].setParameter(new Object[] {5,200.});
		new Turtle(myWorlds[5], "Hawksbill", 100, 300,-90, Color.red);
		
		setup(myWorlds);
	}
}
