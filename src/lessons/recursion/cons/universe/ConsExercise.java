package lessons.recursion.cons.universe;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;

public abstract class ConsExercise extends BatExercise {

	public ConsExercise(Lesson lesson) {
		super(lesson);
	}

        public RecList cons(int head, RecList tail) { return new RecList(head, tail); }
}
