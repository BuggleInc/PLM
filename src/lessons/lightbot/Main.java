package lessons.lightbot;

import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new Board01TwoSteps(this));
		addExercise(new Board02Turn(this));
		addExercise(new Board03Jump(this));
		addExercise(new Board04Stairs(this));
		addExercise(new Board05Higher(this));
		addExercise(new Board06Func(this));
		addExercise(new Board07Repeat(this));
		addExercise(new Board08Rec(this));
		addExercise(new Board09Castle(this));
		addExercise(new Board10Wall(this));
		addExercise(new Board11Sea(this));
		addExercise(new Board12Escher(this));
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