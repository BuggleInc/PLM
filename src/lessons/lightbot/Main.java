package lessons.lightbot;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() {
		addExercise(new Board01TwoSteps(getGame(), this));
		addExercise(new Board02Turn(getGame(), this));
		addExercise(new Board03Jump(getGame(), this));
		addExercise(new Board04Stairs(getGame(), this));
		addExercise(new Board05Higher(getGame(), this));
		addExercise(new Board06Func(getGame(), this));
		addExercise(new Board07Repeat(getGame(), this));
		addExercise(new Board08Rec(getGame(), this));
		addExercise(new Board09Castle(getGame(), this));
		addExercise(new Board10Wall(getGame(), this));
		addExercise(new Board11Sea(getGame(), this));
		addExercise(new Board12Escher(getGame(), this));
	}
}
/* L6: not enough space? try creating functions 
 * L7: reusing functions is great for repetitive tasks
 * L8: putting "fun" back in functions!
 * L9: now you're thinking like a programmer
 * L10: do you feel your mind getting numb yet?
 * L11: second to last level... up to it?
 * L12: The Final Level! Don't get dizzy! 
 */