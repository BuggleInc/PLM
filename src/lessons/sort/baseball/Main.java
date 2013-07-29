package lessons.sort.baseball;

import jlm.core.model.lesson.Lesson;
import lessons.sort.baseball.SelectBaseball;
import lessons.sort.baseball.BaseballGameMorePlayers;

public class Main extends Lesson {

	protected void loadExercises() {
		addExercise(new NaiveBaseball(this));
		addExercise(new SelectBaseball(this));  
		addExercise(new BaseballGameMorePlayers(this));
	}
}
