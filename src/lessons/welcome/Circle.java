package lessons.welcome;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.turtles.Turtle;
import universe.turtles.TurtleWorld;

public class Circle extends ExerciseTemplated {

	public Circle(Lesson lesson) {
		super(lesson);
		name = "Trois petits tours";

		/* Create initial situation */
		TurtleWorld myWorld = new TurtleWorld("WhiteBoard", 400, 400);

		new Turtle(myWorld, "Caretta", 200, 200);
		setup(myWorld);
	}
}
