package lessons.sort;

import jlm.core.model.lesson.Lesson;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new ExExchangeSort(this));
		addExercise(new ExSelectionSort(this));
		addExercise(new ExInsertionSort(this));
	}
}
