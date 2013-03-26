package lessons.recursion;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.turtles.Turtle;
import jlm.universe.turtles.TurtleWorld;

public class Star extends ExerciseTemplated {

	public Star(Lesson lesson) {
		super(lesson);

		/* Create initial situation */
		World myWorld = new TurtleWorld("WhiteBoard", 400, 400);

		new Turtle(myWorld, "Hawksbill", 100, 200);
		setup(myWorld);
	}
}
