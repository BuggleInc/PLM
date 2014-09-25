package plm.test.simple;

import java.io.IOException;

import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;

public class Main extends Lesson {

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new SimpleExercise(this));
	}

}
