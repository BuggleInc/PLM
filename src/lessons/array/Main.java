package lessons.array;

import jlm.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new AverageValueOfArray(this));
		addExercise(new MaxValueOfArray(this));
		addExercise(new IndexOfMaxValueOfArray(this));
		exercisesLoaded = true;
	}
	
}
