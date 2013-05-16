package lessons.recursion.spiral;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.turtles.Turtle;
import jlm.universe.turtles.TurtleWorld;

public class Spiral extends ExerciseTemplated {

	public Spiral(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World[] myWorlds = new World[4];
		myWorlds[0] = new TurtleWorld("Square Pyramid", 400, 400);
		myWorlds[0].setParameter(new Integer[] {100,90,0,3});		
		new Turtle(myWorlds[0], "Hawksbill", 200, 200);
		
		myWorlds[1] = new TurtleWorld("Triangle Pyramid", 400, 400);
		myWorlds[1].setParameter(new Integer[] {50,120,0,6});		
		new Turtle(myWorlds[1], "Hawksbill", 200, 200);

		myWorlds[2] = new TurtleWorld("Pentagon", 400, 400);
		myWorlds[2].setParameter(new Integer[] {70,72,0,2});		
		new Turtle(myWorlds[2], "Hawksbill", 200, 200);

		myWorlds[3] = new TurtleWorld("Hexagon", 400, 400);
		myWorlds[3].setParameter(new Integer[] {25,60,0,6});		
		new Turtle(myWorlds[3], "Hawksbill", 200, 200);
		
		setup(myWorlds);
	}
}
