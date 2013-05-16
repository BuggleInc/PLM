package lessons.sort;

import jlm.core.model.lesson.Lesson;
import lessons.sort.bubble1.AlgBubbleSort1;
import lessons.sort.bubble2.AlgBubbleSort2;
import lessons.sort.bubble3.AlgBubbleSort3;
import lessons.sort.cocktail1.AlgCocktailSort1;
import lessons.sort.cocktail2.AlgCocktailSort2;
import lessons.sort.cocktail3.AlgCocktailSort3;
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
