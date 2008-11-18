package lessons.turtles;

import lessons.ExerciseTemplated;
import lessons.Lesson;
import universe.turtles.Turtle;
import universe.turtles.TurtleWorld;

public class Square extends ExerciseTemplated {

	public Square(Lesson lesson) {
		super(lesson);
		name = "Whiteboard";
		tabName = "Square";
				
		/* Create initial situation */
		TurtleWorld myWorld = new TurtleWorld("Labyrinth",400,400);

		new Turtle(myWorld, "Thésée");
		setup(myWorld);
	}
}
