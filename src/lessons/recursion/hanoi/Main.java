package lessons.recursion.hanoi;

import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new HanoiBoard(this));
	}
}
