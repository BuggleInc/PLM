package lessons.turtles;

import jlm.lesson.Lesson;

public class Main extends Lesson {

	// see http://www.bfoit.org/itp/
	
	public Main() {
		super();
		setSequential(true);
		name = "Tortues";
		addExercise(new Square(this));
	}
	
}
