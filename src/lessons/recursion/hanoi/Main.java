package lessons.recursion.hanoi;

import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new HanoiBoard(this));
		addExercise(new InterleavedHanoi(this));
		addExercise(new SplitHanoi1(this));
		addExercise(new SplitHanoi2(this));
	}
}
