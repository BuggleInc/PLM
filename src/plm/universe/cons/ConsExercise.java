package plm.universe.cons;

import plm.universe.World;
import plm.universe.bat.BatExercise;

public abstract class ConsExercise extends BatExercise {

	public ConsExercise(String id, String name) {
		super(id, name);
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
