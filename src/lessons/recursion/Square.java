package lessons.recursion;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.turtles.Turtle;
import universe.turtles.TurtleWorld;

public class Square extends ExerciseTemplated {

	public Square(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World myWorld = new TurtleWorld("WhiteBoard", 400, 400);

		new Turtle(myWorld, "Hawksbill", 200, 200);
		setup(myWorld);
	}
}
