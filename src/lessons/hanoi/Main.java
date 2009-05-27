package lessons.hanoi;

import jlm.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new HanoiBoard(this));
		exercisesLoaded = true;
	}
}
