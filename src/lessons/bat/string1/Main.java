package lessons.bat.string1;

import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new StringTimes(this));
		addExercise(new FrontTimes(this));
		addExercise(new StringBits(this));
		addExercise(new StringSplosion(this));
		addExercise(new Last2(this));
		addExercise(new ArrayCount9(this));
		addExercise(new ArrayFront9(this));
		addExercise(new Array123(this));
		addExercise(new StringMatch(this));
		addExercise(new StringX(this));
		addExercise(new AltPairs(this));
		addExercise(new StringYak(this));
		addExercise(new Array667(this));
		addExercise(new NoTriples(this));
		addExercise(new Has271(this));
	}

}
