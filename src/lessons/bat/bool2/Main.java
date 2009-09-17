package lessons.bat.bool2;

import jlm.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new AlarmClock(this));
		addExercise(new AnswerCell(this));
		addExercise(new BlueTicket(this));
		addExercise(new CaughtSpeeding(this));
		addExercise(new CigarParty(this));
		addExercise(new DateFashion(this));
		addExercise(new GreenTicket(this));
		addExercise(new In1To10(this));
		addExercise(new InOrder(this));
		addExercise(new InOrderEqual(this));
		addExercise(new LastDigit(this));
		addExercise(new LessBy10(this));
		addExercise(new MaxMod5(this));
		addExercise(new NearTen(this));
		addExercise(new RedTicket(this));
		addExercise(new ShareDigit(this));
		addExercise(new SortaSum(this));
		addExercise(new SquirrelPlay(this));
		addExercise(new TeaParty(this));
		addExercise(new TeenSum(this));
		addExercise(new TwoAsOne(this));
		addExercise(new WithoutDoubles(this));
		exercisesLoaded = true;
	}

}
