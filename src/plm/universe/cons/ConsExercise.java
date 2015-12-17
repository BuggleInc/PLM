package plm.universe.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.bat.BatExercise;

public abstract class ConsExercise extends BatExercise {

	public ConsExercise(Game game, Lesson lesson) {
		super(game, lesson);
	}

	public RecList cons(int head, RecList tail){
		return new RecList(head, tail);
	}

	protected RecList data(int[] d) {
		return RecList.fromArray(d);
	}

	protected void setup(World[] ws) {
		super.setup(ws,
				/* Extra imports */
				"import lessons.recursion.cons.universe.*;"+
				"import java.util.Vector;",
				/* Extra entity body */
				"public RecList cons(int head, RecList tail){"+
				"  return new RecList(head, tail);"+
				"}");
	}
}
