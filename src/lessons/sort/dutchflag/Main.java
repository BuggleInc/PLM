package lessons.sort.dutchflag;

import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		
		addExercise(new DutchFlagAlgo(this));
	}
}
