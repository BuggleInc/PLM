package lessons.recursion.logo.koch;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class HexaKoch extends ExerciseTemplated {

	public HexaKoch(Game game, Lesson lesson) {
		super(game, lesson);

		/* Create initial situation */
		World[] myWorlds = new World[5];
		myWorlds[0] = new TurtleWorld(game, "hexaKoch(0,200)", 400, 400);
		myWorlds[0].setParameter(new Object[] {0,200.});		
		new Turtle(myWorlds[0], "Hawksbill", 100, 250, 0, Color.red);
		
		myWorlds[1] = new TurtleWorld(game, "hexaKoch(1,500)", 400, 400);
		myWorlds[1].setParameter(new Object[] {1,500.});		
		new Turtle(myWorlds[1], "Hawksbill", 100, 250, 0, Color.red);

		myWorlds[2] = new TurtleWorld(game, "hexaKoch(2,1400)", 400, 400);
		myWorlds[2].setParameter(new Object[] {2,1400.});		
		new Turtle(myWorlds[2], "Hawksbill", 100, 250, 0, Color.red);

		myWorlds[3] = new TurtleWorld(game, "hexaKoch(3,3500)", 400, 400);
		myWorlds[3].setParameter(new Object[] {3,3500.});		
		new Turtle(myWorlds[3], "Hawksbill", 100, 250, 0, Color.red);
		
		myWorlds[4] = new TurtleWorld(game, "hexaKoch(4,7000)", 400, 400);
		myWorlds[4].setParameter(new Object[] {4,7500.});		
		new Turtle(myWorlds[4], "Hawksbill", 100, 250,0, Color.red);

		/* TOO LARGE for the PLM on medium-range machines	
		myWorlds[5] = new TurtleWorld(game, "hexaKoch(5,15000)", 400, 400);
		myWorlds[5].setParameter(new Object[] {5,15000.});		
		new Turtle(myWorlds[5], "Hawksbill", 100, 250,0, Color.red);
	*/	
		setup(myWorlds);
	}
}
