package lessons.lightbot;

import jlm.lesson.Lesson;

public class Main extends Lesson {
	public Main() {
		loadExercises();
	}
	@Override
	protected void loadExercises() {
		addExercise(new TwoSteps(this));
		exercisesLoaded = true;
	}
}
