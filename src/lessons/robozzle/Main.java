package lessons.robozzle;

import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {

	// see http://http://www.robozzle.com/
	
	@Override
	protected void loadExercises() {
		addExercise(new Level66(this));
		exercisesLoaded = true;
	}
	
}
