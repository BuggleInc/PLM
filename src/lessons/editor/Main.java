package lessons.editor;

import java.io.IOException;

import lessons.editor.editor.Editor;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;

public class Main extends Lesson {

	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new Editor(getGame(), this));
	}
}
