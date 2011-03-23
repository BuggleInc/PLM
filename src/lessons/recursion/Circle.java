package lessons.recursion;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.turtles.Turtle;
import universe.turtles.TurtleWorld;

public class Circle extends ExerciseTemplated {

	public Circle(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World myWorld = new TurtleWorld("WhiteBoard", 400, 400);

		new Turtle(myWorld, "Caretta", 200, 200);
		setup(myWorld);
	}
}
