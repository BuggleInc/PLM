package lessons.bat.string1;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {

	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() {
		addExercise(new StringTimes(getGame(), this));
		addExercise(new FrontTimes(getGame(), this));
		addExercise(new StringBits(getGame(), this));
		addExercise(new StringSplosion(getGame(), this));
		addExercise(new Last2(getGame(), this));
		addExercise(new StringMatch(getGame(), this));
		addExercise(new StringX(getGame(), this));
		addExercise(new AltPairs(getGame(), this));
		addExercise(new StringYak(getGame(), this));
	}

}
