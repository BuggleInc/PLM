package lessons.backtracking;

import jlm.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new ExKnapsack(this));

		exercisesLoaded = true;
	}
}
