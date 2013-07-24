package lessons.sort.baseball;

import jlm.core.model.lesson.Lesson;
import lessons.sort.baseball.easy.BaseballGame;
import lessons.sort.baseball.medium.BaseballGameMorePlayers;

public class Main extends Lesson {
	
	protected void loadExercises() {
		addExercise(new BaseballGame(this));
		addExercise(new BaseballGameMorePlayers(this));
	}
}
