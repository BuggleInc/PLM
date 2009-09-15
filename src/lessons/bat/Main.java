package lessons.bat;

import jlm.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new SleepIn(this));
		addExercise(new NearHundred(this));
		exercisesLoaded = true;
	}

}
