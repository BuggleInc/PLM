package lessons.meta;

import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;

public class MetaExercise extends ExerciseTemplated {
	public MetaExercise(Lesson lesson) {
		super(lesson);
		lastResult = new ExecutionProgress();
	}
	@Override
	public void reset() {
		lastResult = new ExecutionProgress();
		super.reset();
	}
	
	@Override
	public void check() {
	}

	protected void error(String msg) {
		error(msg,null);
	}
	protected void error(String msg, Exception e) {
		System.err.println(msg);
		lastResult.totalTests++;
		lastResult.details += msg;
		if (e != null) {
			lastResult.details += "(exception raised)";
			e.printStackTrace();
		}
	}

}
