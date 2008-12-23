package lessons.sort;

import jlm.lesson.Lesson;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

/*
 * FIXME:
 * 
 * sometimes if swapped between basic world and performance world, we got a :
 * 
 * Exception in thread "Thread-411" java.util.ConcurrentModificationException
 *	at java.util.AbstractList$Itr.checkForComodification(AbstractList.java:372)
 *	at java.util.AbstractList$Itr.next(AbstractList.java:343)
 *	at jlm.universe.World.notifyWorldUpdatesListeners(World.java:145)
 *	at universe.sort.SortingEntity.stepUI(SortingEntity.java:109)
 *	at universe.sort.SortingEntity.swap(SortingEntity.java:82)
 *	at jlm.runtime14.MyBubbleSort2.run(MyBubbleSort2.java from JavaFileObjectImpl:6)
 *	at jlm.universe.World$1.run(World.java:109)
 *	at java.lang.Thread.run(Thread.java:637)
 *
 * I guess it means we have to protect a list from concurrent modifications
 * in the notifyWorldUpdatesListeners() ?!?
 * 
 */

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
		addExercise(new ExExchangeSort(this));
		addExercise(new ExSelectionSort(this));
		addExercise(new ExInsertionSort(this));
	}
}
