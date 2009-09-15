package lessons.bat;

import jlm.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new SleepIn(this));
		addExercise(new NearHundred(this));
		addExercise(new Makes10(this));
		addExercise(new SumDouble(this));
		addExercise(new Diff21(this));
		addExercise(new ParotTrouble(this));
		exercisesLoaded = true;
	}

}
