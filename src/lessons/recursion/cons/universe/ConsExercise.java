package lessons.recursion.cons.universe;

import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.bat.BatExercise;

// http://stackoverflow.com/questions/12695297/proper-lists-and-recursive-tail-in-python

public abstract class ConsExercise extends BatExercise {

	public ConsExercise(Lesson lesson) {
		super(lesson);
		// TODO Auto-generated constructor stub
	}

	public IntSequence cons(int head, IntSequence tail){
		return new IntSequence(head, tail);
	}


	protected void setup(World[] ws) {
		super.setup(ws,
				/* Extra imports */
				"import lessons.recursion.cons.universe.*;"+
				"import java.util.List;",
				/* Extra entity body */
				"public IntSequence cons(int head, IntSequence tail){"+
				"  return new IntSequence(head, tail);"+
				"}");
	}
}
