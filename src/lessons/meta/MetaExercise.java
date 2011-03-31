package lessons.meta;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;

public class MetaExercise extends ExerciseTemplated {
	boolean error = false;

	public MetaExercise(Lesson lesson) {
		super(lesson);
	}
	@Override
	public void reset() {
		error = false;
		super.reset();
	}
	
	@Override
	public boolean check() {
		return !error;
	}

	protected void error(String msg) {
		error(msg,null);
	}
	protected void error(String msg, Exception e) {
		System.err.println(msg);
		error = true;
		if (e != null)
			e.printStackTrace();		
	}

}
