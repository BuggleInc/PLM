package lessons.turmites;

import java.io.IOException;

import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import lessons.turmites.helloturmite.HelloTurmite;
import lessons.turmites.langton.Langton;
import lessons.turmites.langtoncolors.LangtonColors;
import lessons.turmites.turmitecreator.TurmiteCreator;

public class Main extends Lesson {

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new Langton(this));
		addExercise(new LangtonColors(this));
		addExercise(new HelloTurmite(this));
		addExercise(new TurmiteCreator(this));
	}
   
}
