package lessons.smn;

import jlm.core.model.lesson.Lesson;
import lessons.smn.baseball.BaseballGame;
import lessons.smn.pancake.burned.BurnedPancakePlate;
import lessons.smn.pancake.raw.RawPancakePlate;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new RawPancakePlate(this));
		addExercise(new BurnedPancakePlate(this));
		addExercise(new BaseballGame(this));
	}
}
