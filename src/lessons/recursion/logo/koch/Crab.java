package lessons.recursion.logo.koch;

import java.awt.Color;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Crab extends ExerciseTemplated {

	public Crab(Game game, Lesson lesson) {
		super(game, lesson);

		/* Create initial situation */
		World[] myWorlds = new World[6];
		myWorlds[0] = new TurtleWorld(game, "crab(0,200)", 400, 400);
		myWorlds[0].setParameter(new Object[] {0,200.});		
		new Turtle(myWorlds[0], "Hawksbill", 200, 300, -90, Color.red);
		
		myWorlds[1] = new TurtleWorld(game, "crab(1,200)", 400, 400);
		myWorlds[1].setParameter(new Object[] {1,200.});		
		new Turtle(myWorlds[1], "Hawksbill", 200, 300, -90, Color.red);

		myWorlds[2] = new TurtleWorld(game, "crab(2,200)", 400, 400);
		myWorlds[2].setParameter(new Object[] {2,200.});		
		new Turtle(myWorlds[2], "Hawksbill", 200, 300, -90, Color.red);

		myWorlds[3] = new TurtleWorld(game, "crab(3,200)", 400, 400);
		myWorlds[3].setParameter(new Object[] {3,200.});		
		new Turtle(myWorlds[3], "Hawksbill", 200, 300,-90, Color.red);
		
		myWorlds[4] = new TurtleWorld(game, "crab(4,200)", 400, 400);
		myWorlds[4].setParameter(new Object[] {4,200.});		
		new Turtle(myWorlds[4], "Hawksbill", 200, 300,-90, Color.red);

		myWorlds[5] = new TurtleWorld(game, "crab(7,200)", 400, 400);
		myWorlds[5].setParameter(new Object[] {7,200.});		
		new Turtle(myWorlds[5], "Hawksbill", 200, 300,-90, Color.red);
		
		/* TOO LARGE for the PLM on medium-range machines
		myWorlds[6] = new TurtleWorld(game, "crab(9,200)", 400, 400);
		myWorlds[6].setParameter(new Object[] {9,200.});		
		new Turtle(myWorlds[6], "Hawksbill", 200, 300,-90, Color.red);
		
		myWorlds[7] = new TurtleWorld(game, "crab(12,200)", 400, 400);
		myWorlds[7].setParameter(new Object[] {12,200.});		
		new Turtle(myWorlds[7], "Hawksbill", 200, 300,-90, Color.red);
		*/
		setup(myWorlds);
	}
}
