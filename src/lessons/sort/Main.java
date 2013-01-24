package lessons.sort;

import jlm.core.model.lesson.Lesson;
import lessons.sort.bubble.*;
import lessons.sort.cocktail.*;
import lessons.sort.insertion.*;
import lessons.sort.miscellaneous.*;
import lessons.sort.selection.*;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		
		addExercise(new AlgBubbleSort1(this));
		addExercise(new AlgBubbleSort2(this));
		addExercise(new AlgBubbleSort3(this));
		
		addExercise(new AlgInsertionSort(this));
		addExercise(new AlgShellSort(this));
		
		addExercise(new AlgSelectionSort(this));

		addExercise(new AlgCocktailSort1(this));
		addExercise(new AlgCocktailSort2(this));
		addExercise(new AlgCocktailSort3(this));
		
		addExercise(new AlgCombSort(this));
		addExercise(new AlgCombSort11(this));
		addExercise(new AlgGnomeSort(this));
	}
}
