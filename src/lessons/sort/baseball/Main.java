package lessons.smn;

import jlm.core.model.lesson.Lesson;
import lessons.smn.baseball.easy.BaseballGame;
import lessons.smn.baseball.medium.BaseballGameMorePlayers;

public class Main extends Lesson {
	
	protected void loadExercises() {
		addExercise(new BaseballGame(this));
		addExercise(new BaseballGameMorePlayers(this));
	}
}
