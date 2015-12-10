package lessons.recursion.cons;


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
		addExercise(new Length(getGame(), this));
		addExercise(new IsMember(getGame(), this));
		addExercise(new Occurrence(getGame(), this));
		addExercise(new Last(getGame(), this));
		addExercise(new Min(getGame(), this)); // uses an extra function
		addExercise(new Increasing(getGame(), this)); 

		addExercise(new ButLast(getGame(), this)); // uses cons or ::
		addExercise(new PlusOne(getGame(), this)); // uses cons or ::
		addExercise(new Remove(getGame(), this));  // uses cons or ::
		addExercise(new Nth(getGame(), this));
		addExercise(new Nfirst(getGame(), this));  // uses cons or ::
		addExercise(new Nlast(getGame(), this));   // uses cons or :: and extra functions
		addExercise(new ButNfirst(getGame(), this));
		addExercise(new ButNlast(getGame(), this)); // uses extra functions

		addExercise(new Reverse(getGame(), this)); // uses cons or :: and extra functions
		addExercise(new Concat(getGame(), this));  // uses cons or :: and extra functions
		
		addExercise(new AllDifferent(getGame(), this)); // This one is harder: O(n²) with an extra function, or you need to first sort the array
	}

}
