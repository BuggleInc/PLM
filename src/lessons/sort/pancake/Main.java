package lessons.sort.pancake;

import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new BasicPancake(this));
		addExercise(new BurnedPancake(this));
		addExercise(new GatesPancake(this));
		addExercise(new CohenPancake(this));
	}
}
