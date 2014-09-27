package lessons.recursion.cons;


import java.io.IOException;

import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;

public class Main extends Lesson {

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new Length(this));
		addExercise(new IsMember(this));
		addExercise(new Occurrence(this));
		addExercise(new Last(this));
		
		// The next ones are using cons or ::
		addExercise(new PlusOne(this));		
		addExercise(new Remove(this));		
		
		addExercise(new AllDifferent(this)); // This one is harder: O(n²) with an extra function, or you need to first sort the array
	}

}
