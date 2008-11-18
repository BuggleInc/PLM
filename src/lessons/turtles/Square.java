package lessons.turtles;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import universe.turtles.Turtle;
import universe.turtles.TurtleWorld;

public class Square extends ExerciseTemplated {

	public Square(Lesson lesson) {
		super(lesson);
		name = "Square";
		tabName = "Square";
				
		/* Create initial situation */
		TurtleWorld myWorld = new TurtleWorld("WhiteBoard",400,400);

		new Turtle(myWorld, "Thésée");
		setup(myWorld);
	}
}
