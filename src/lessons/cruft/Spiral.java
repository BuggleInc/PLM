package lessons.cruft;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.turtles.Turtle;
import universe.turtles.TurtleWorld;

public class Spiral extends ExerciseTemplated {

	public Spiral(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		TurtleWorld myWorld = new TurtleWorld("WhiteBoard", 400, 400);

		new Turtle(myWorld, "Caretta", 200, 200);
		setup(myWorld);
	}
}
