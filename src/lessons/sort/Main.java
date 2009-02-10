package lessons.sort;

import jlm.lesson.Lesson;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

public class Main extends Lesson {
	public Main() {
		super();	
		sequential = false;
	}
	@Override
	protected void loadExercises() {
		addExercise(new ExExchangeSort(this));
		addExercise(new ExSelectionSort(this));
		addExercise(new ExInsertionSort(this));
		exercisesLoaded = true;
	}
}
