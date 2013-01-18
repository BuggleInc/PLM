package lessons.recursion;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.turtles.Turtle;
import jlm.universe.turtles.TurtleWorld;

public class SpiralUse extends ExerciseTemplated {

	public SpiralUse(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World[] myWorlds = new World[5];
		
		myWorlds[0] = new TurtleWorld("One", 400, 400);
		myWorlds[0].setParameter(new Integer[] {0});		
		new Turtle(myWorlds[0], "Hawksbill", 200, 200);

		myWorlds[1] = new TurtleWorld("Two", 400, 400);
		myWorlds[1].setParameter(new Integer[] {1});		
		new Turtle(myWorlds[1], "Hawksbill", 200, 200);

		myWorlds[2] = new TurtleWorld("Three", 400, 400);
		myWorlds[2].setParameter(new Integer[] {2});		
		new Turtle(myWorlds[2], "Hawksbill", 200, 200);

		myWorlds[3] = new TurtleWorld("Four", 400, 400);
		myWorlds[3].setParameter(new Integer[] {3});		
		new Turtle(myWorlds[3], "Hawksbill", 200, 200);

		myWorlds[4] = new TurtleWorld("Five", 400, 400);
		myWorlds[4].setParameter(new Integer[] {4});		
		new Turtle(myWorlds[4], "Hawksbill", 200, 200);
		
		setup(myWorlds);
	}
}
