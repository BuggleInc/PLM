package lessons.recursion.circle;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Circle extends ExerciseTemplated {

	public Circle(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World myWorld = new TurtleWorld("WhiteBoard", 400, 400);

		new Turtle(myWorld, "Caretta", 200, 200);
		setup(myWorld);
	}
}
