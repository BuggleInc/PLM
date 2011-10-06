package lessons.backtracking;

import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new ExKnapsack(this));
	}
}
