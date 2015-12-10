package lessons.sort.dutchflag;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() {
		
		addExercise(new DutchFlagAlgo(getGame(), this));
	}
}
