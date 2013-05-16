package lessons.lightbot;

import jlm.core.model.lesson.Lesson;
import lessons.lightbot.mission1.Board01TwoSteps;
import lessons.lightbot.mission10.Board10Wall;
import lessons.lightbot.mission11.Board11Sea;
import lessons.lightbot.mission12.Board12Escher;
import lessons.lightbot.mission2.Board02Turn;
import lessons.lightbot.mission3.Board03Jump;
import lessons.lightbot.mission4.Board04Stairs;
import lessons.lightbot.mission5.Board05Higher;
import lessons.lightbot.mission6.Board06Func;
import lessons.lightbot.mission7.Board07Repeat;
import lessons.lightbot.mission8.Board08Rec;
import lessons.lightbot.mission9.Board09Castle;

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