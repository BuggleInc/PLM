package lessons.sort;

import jlm.lesson.Lesson;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

public class Main extends Lesson {
	public Main() {
		super();	
		name = "Algorithmes de tri";
		about = "Cette leçon vous permet d'expérimenter avec les algorithmes de tri classiques " +
				"(et certaines optimisations moins courantes de ceux-ci). L'objectif est double: " +
				"il s'agit tout d'abord de mieux comprendre le principe de ces algorithmes en les codant vous-même. " +
				"Mais même si vous ne codez pas les algorithmes, vous pouvez utiliser le mode démo pour " +
				"organiser des \"courses\" entre ces algorithmes afin d'expérimenter en pratique ce " +
				"qu'implique la différence de complexité asymptotique entre deux algorithmes. " +
				"<p>Un exercice sur les tris récursifs (en particulier QuickSort et MergeSort) est prévu à l'avenir.";

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
