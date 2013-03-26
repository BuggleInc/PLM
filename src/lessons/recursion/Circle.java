package lessons.recursion;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.turtles.Turtle;
import jlm.universe.turtles.TurtleWorld;

public class Circle extends ExerciseTemplated {

	public Circle(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World myWorld = new TurtleWorld("WhiteBoard", 400, 400);

		new Turtle(myWorld, "Caretta", 200, 200);
		setup(myWorld);
	}
}
