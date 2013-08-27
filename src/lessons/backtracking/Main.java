package lessons.backtracking;

import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new ExKnapsack(this));
	}
}
