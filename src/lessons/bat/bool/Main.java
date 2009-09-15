package lessons.bat.bool;

import jlm.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new SleepIn(this));
		addExercise(new MonkeyTrouble(this));
		addExercise(new NearHundred(this));
		addExercise(new SumDouble(this));
		addExercise(new Diff21(this));
		addExercise(new ParotTrouble(this));
		addExercise(new Makes10(this));
		addExercise(new PosNeg(this));
		addExercise(new IcyHot(this));
		addExercise(new In1020(this));
		addExercise(new HasTeen(this));
		addExercise(new LoneTeen(this));
		addExercise(new CountTeen(this));
		addExercise(new Close10(this));
		addExercise(new In3050(this));
		addExercise(new Max1020(this));
		addExercise(new LastDigit(this));
		exercisesLoaded = true;
	}

}
