package lessons.sort.baseball;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {

	public Main(Game game) {
		super(game);
	}

	protected void loadExercises() {
		addExercise(new NaiveBaseball(getGame(), this));
		addExercise(new SelectBaseball(getGame(), this));  
		addExercise(new InsertBaseball(getGame(), this));
		addExercise(new BubbleBaseball(getGame(), this));
	}
}
