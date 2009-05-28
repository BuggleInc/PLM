package lessons.meta;

import jlm.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new HanoiCreateWorld(this));
		exercisesLoaded = true;
	}
}
