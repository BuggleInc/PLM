package lessons.recursion;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import universe.turtles.Turtle;
import universe.turtles.TurtleWorld;

public class Star extends ExerciseTemplated {

	public Star(Lesson lesson) {
		super(lesson);
		name = "Turtles are super-stars";

		/* Create initial situation */
		TurtleWorld myWorld = new TurtleWorld("WhiteBoard", 400, 400);

		new Turtle(myWorld, "Hawksbill", 100, 200);
		setup(myWorld);
	}
}
