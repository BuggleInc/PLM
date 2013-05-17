package lessons.smn;

import jlm.core.model.lesson.Lesson;
import lessons.smn.baseball.easy.BaseballGame;
import lessons.smn.baseball.medium.BaseballGameMorePlayers;
import lessons.smn.pancake.burned.BurnedPancakePlate;
import lessons.smn.pancake.raw.RawPancakePlate;

public class Main extends Lesson {
	
	protected void loadExercises() {
		addExercise(new RawPancakePlate(this));
		addExercise(new BurnedPancakePlate(this));
		addExercise(new BaseballGame(this));
		addExercise(new BaseballGameMorePlayers(this));
	}
}
