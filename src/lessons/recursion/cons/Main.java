package lessons.recursion.cons;

import java.io.IOException;

import lessons.recursion.square.FourSquare;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;

public class Main extends Lesson {

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new FourSquare(this));

//		addExercise(new Length(this));		
	}

}
