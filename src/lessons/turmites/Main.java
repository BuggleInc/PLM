package lessons.turmites;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import lessons.turmites.helloturmite.HelloTurmite;
import lessons.turmites.langton.Langton;
import lessons.turmites.langtoncolors.LangtonColors;
import lessons.turmites.turmitecreator.TurmiteCreator;

public class Main extends Lesson {

	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new Langton(getGame(), this));
		addExercise(new LangtonColors(getGame(), this));
		addExercise(new HelloTurmite(getGame(), this));
		addExercise(new TurmiteCreator(getGame(), this));
	}
   
}
