package lessons.sort.basic;

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
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

public class Main extends Lesson {
	
	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() {
		
		addExercise(new AlgBubbleSort1(getGame(), this));
		addExercise(new AlgBubbleSort2(getGame(), this));
		addExercise(new AlgBubbleSort3(getGame(), this));
		
		addExercise(new AlgCocktailSort1(getGame(), this));
		addExercise(new AlgCocktailSort2(getGame(), this));
		addExercise(new AlgCocktailSort3(getGame(), this));

		addExercise(new AlgCombSort(getGame(), this));
		addExercise(new AlgCombSort11(getGame(), this));

		addExercise(new AlgGnomeSort(getGame(), this));
		
		addExercise(new AlgInsertionSort(getGame(), this));
		addExercise(new AlgShellSort(getGame(), this));
		
		addExercise(new AlgSelectionSort(getGame(), this));
		
	}
}
