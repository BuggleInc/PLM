package lessons.sort;

import jlm.core.model.lesson.Lesson;
import lessons.sort.bubble.AlgBubbleSort1;
import lessons.sort.bubble.AlgBubbleSort2;
import lessons.sort.bubble.AlgBubbleSort3;
import lessons.sort.cocktail.AlgCocktailSort1;
import lessons.sort.cocktail.AlgCocktailSort2;
import lessons.sort.cocktail.AlgCocktailSort3;
import lessons.sort.comb.AlgCombSort;
import lessons.sort.comb11.AlgCombSort11;
import lessons.sort.gnome.AlgGnomeSort;
import lessons.sort.insertion.AlgInsertionSort;
import lessons.sort.selection.AlgSelectionSort;
import lessons.sort.shell.AlgShellSort;

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
