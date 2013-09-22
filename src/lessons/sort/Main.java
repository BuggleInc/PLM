package lessons.sort;

import lessons.sort.basic.bubble.AlgBubbleSort1;
import lessons.sort.basic.bubble.AlgBubbleSort2;
import lessons.sort.basic.bubble.AlgBubbleSort3;
import lessons.sort.basic.cocktail.AlgCocktailSort1;
import lessons.sort.basic.cocktail.AlgCocktailSort2;
import lessons.sort.basic.cocktail.AlgCocktailSort3;
import lessons.sort.basic.comb.AlgCombSort;
import lessons.sort.basic.comb.AlgCombSort11;
import lessons.sort.basic.gnome.AlgGnomeSort;
import lessons.sort.basic.insertion.AlgInsertionSort;
import lessons.sort.basic.selection.AlgSelectionSort;
import lessons.sort.basic.shell.AlgShellSort;
import plm.core.model.lesson.Lesson;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		
		addExercise(new AlgBubbleSort1(this));
		addExercise(new AlgBubbleSort2(this));
		addExercise(new AlgBubbleSort3(this));
		
		addExercise(new AlgCocktailSort1(this));
		addExercise(new AlgCocktailSort2(this));
		addExercise(new AlgCocktailSort3(this));

		addExercise(new AlgCombSort(this));
		addExercise(new AlgCombSort11(this));

		addExercise(new AlgGnomeSort(this));
		
		addExercise(new AlgInsertionSort(this));
		addExercise(new AlgShellSort(this));
		
		addExercise(new AlgSelectionSort(this));
		
	}
}
