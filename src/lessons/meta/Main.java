package lessons.meta;

import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		System.out.println("Add Create world exo");
		addExercise(new HanoiCreateWorld(this));

		System.out.println("Add Create entity exo");
		addExercise(new HanoiCreateEntity(this));
	}
}
