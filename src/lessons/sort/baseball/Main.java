package lessons.sort.baseball;

import jlm.core.model.lesson.Lesson;
import lessons.sort.baseball.BaseballGame;
import lessons.sort.baseball.BaseballGameMorePlayers;

public class Main extends Lesson {

	protected void loadExercises() {
		addExercise(new NaiveBaseball(this));
		addExercise(new BaseballGame(this));  
		addExercise(new BaseballGameMorePlayers(this));
	}
}
