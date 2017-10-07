package recursion.logo.polygonfractal;

import java.awt.Color;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class PolygonFractal extends ExerciseTemplated {

	public PolygonFractal() {
		super("PolygonFractal", "PolygonFractal");

		/* Create initial situation */
		World[] myWorlds = new World[7];
		myWorlds[0] = new TurtleWorld("polygonFractal(1,5,100,0.5)", 400, 400);
		myWorlds[0].setParameter(new Object[] {1,5,100.,.5});		
		new Turtle(myWorlds[0], "Hawksbill", 125, 250, -90, Color.red);
		
		myWorlds[1] = new TurtleWorld("polygonFractal(2,5,100,0.5)", 400, 400);
		myWorlds[1].setParameter(new Object[] {2,5,100.,.5});		
		new Turtle(myWorlds[1], "Hawksbill", 125, 250, -90, Color.red);
		
		myWorlds[2] = new TurtleWorld("polygonFractal(3,5,100,0.4)", 400, 400);
		myWorlds[2].setParameter(new Object[] {3,5,100.,.4});		
		new Turtle(myWorlds[2], "Hawksbill", 125, 250, -90, Color.red);

		myWorlds[3] = new TurtleWorld("polygonFractal(4,4,100,0.4)", 400, 400);
		myWorlds[3].setParameter(new Object[] {4,4,100.,.4});		
		new Turtle(myWorlds[3], "Hawksbill", 125, 250, -90, Color.red);

		myWorlds[4] = new TurtleWorld("polygonFractal(3,3,100,0.4)", 400, 400);
		myWorlds[4].setParameter(new Object[] {3,3,100.,.4});		
		new Turtle(myWorlds[4], "Hawksbill", 125, 250, -90, Color.red);
		
		myWorlds[5] = new TurtleWorld("polygonFractal(3,6,100,0.3)", 400, 400);
		myWorlds[5].setParameter(new Object[] {3,6,100.,.3});		
		new Turtle(myWorlds[5], "Hawksbill", 125, 250, -90, Color.red);
		
		myWorlds[6] = new TurtleWorld("polygonFractal(4,6,100,0.3)", 400, 400);
		myWorlds[6].setParameter(new Object[] {4,6,100.,.3});		
		new Turtle(myWorlds[6], "Hawksbill", 125, 250, -90, Color.red);
		
		setup(myWorlds);
	}
}
