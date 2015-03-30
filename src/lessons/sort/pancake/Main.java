package lessons.sort.pancake;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() {
		addExercise(new BasicPancake(getGame(), this));
		addExercise(new BubblePancake(getGame(), this));
		addExercise(new BurnedPancake(getGame(), this));
		addExercise(new GatesPancake(getGame(), this));
		addExercise(new CohenPancake(getGame(), this));
	}
}
