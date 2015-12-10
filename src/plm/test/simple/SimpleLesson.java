package plm.test.simple;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;

public class SimpleLesson extends Lesson {

	public SimpleLesson(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new SimpleExercise(getGame(), this));
	}

}
