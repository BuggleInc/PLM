package universe.array;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.World;

public class ArrayExercise extends ExerciseTemplated {

	
	public ArrayExercise(Lesson lesson) {
		super(lesson);
	}

	@Override
	protected void setup(World[] ws) {		
		worldDuplicate(ws);
		computeAnswer();
	}

	//@Override
	//public boolean check() {
	//	return false;
	//}
	
}
