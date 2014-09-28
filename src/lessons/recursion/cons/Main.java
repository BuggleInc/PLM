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
		addExercise(new Min(this)); // uses an extra function
		addExercise(new Increasing(this)); 

		addExercise(new ButLast(this)); // uses cons or ::
		addExercise(new PlusOne(this)); // uses cons or ::
		addExercise(new Remove(this));  // uses cons or ::
		addExercise(new Nth(this));
		addExercise(new Nfirst(this));  // uses cons or ::
		addExercise(new Nlast(this));   // uses cons or :: and extra functions
		addExercise(new ButNfirst(this));
		addExercise(new ButNlast(this)); // uses extra functions

		addExercise(new Reverse(this)); // uses cons or :: and extra functions
		addExercise(new Concat(this));  // uses cons or :: and extra functions
		
		addExercise(new AllDifferent(this)); // This one is harder: O(n²) with an extra function, or you need to first sort the array
	}

}
