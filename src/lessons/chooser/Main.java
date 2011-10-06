package lessons.chooser;

import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new LessonChooser(this));
	}
}
