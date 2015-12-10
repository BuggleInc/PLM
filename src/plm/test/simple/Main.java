package plm.test.simple;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;

public class Main extends Lesson {

	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new SimpleExercise(getGame(), this));
	}

}
