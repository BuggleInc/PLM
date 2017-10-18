package plm.universe.cons;

import plm.core.model.lesson.ExerciseTemplated;

public abstract class ConsExercise extends ExerciseTemplated {

	public ConsExercise(String name) {
		super(name);
	}

	public RecList cons(int head, RecList tail){
		return new RecList(head, tail);
	}

	protected RecList data(int[] d) {
		return RecList.fromArray(d);
	}

	/*
	protected void setup(World[] ws) {
		super.setup(ws,
				/* Extra imports */
	/*			"import lessons.recursion.cons.universe.*;"+
				"import java.util.Vector;",
				/* Extra entity body */
	/*			"public RecList cons(int head, RecList tail){"+
				"  return new RecList(head, tail);"+
				"}");
	}
	*/
}
