package jlm.universe.array;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
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
