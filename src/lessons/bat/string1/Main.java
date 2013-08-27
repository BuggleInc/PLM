package lessons.bat.string1;

import plm.core.model.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new StringTimes(this));
		addExercise(new FrontTimes(this));
		addExercise(new StringBits(this));
		addExercise(new StringSplosion(this));
		addExercise(new Last2(this));
		addExercise(new StringMatch(this));
		addExercise(new StringX(this));
		addExercise(new AltPairs(this));
		addExercise(new StringYak(this));
	}

}
