package recursion.logo.sierpinski;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Sierpinski extends ExerciseTemplated {

	public Sierpinski() {
		super("Sierpinski", "Sierpinski");

		/* Create initial situation */
		World[] myWorlds = new World[5];
		myWorlds[0] = new TurtleWorld("sierpinski(0,300.)", 400, 400);
		myWorlds[0].setParameter(new Object[] {0, 300.});		
		new Turtle(myWorlds[0], "Hawksbill", 350, 350, -180, Color.red);
		
		myWorlds[1] = new TurtleWorld("sierpinski(1,300.)", 400, 400);
		myWorlds[1].setParameter(new Object[] {1, 300.});		
		new Turtle(myWorlds[1], "Hawksbill", 350, 350, -180, Color.red);

		myWorlds[2] = new TurtleWorld("sierpinski(2,300.)", 400, 400);
		myWorlds[2].setParameter(new Object[] {2, 300.});		
		new Turtle(myWorlds[2], "Hawksbill", 350, 350, -180, Color.red);

		myWorlds[3] = new TurtleWorld("sierpinski(3,300.)", 400, 400);
		myWorlds[3].setParameter(new Object[] {3, 300.});		
		new Turtle(myWorlds[3], "Hawksbill", 350, 350,-180, Color.red);

		myWorlds[4] = new TurtleWorld("sierpinski(5,300.)", 400, 400);
		myWorlds[4].setParameter(new Object[] {5, 300.});		
		new Turtle(myWorlds[4], "Hawksbill", 350, 350,-180, Color.red);

		setup(myWorlds);
	}	
	
}
