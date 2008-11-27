package lessons.sort;

import jlm.lesson.Lesson;
import universe.sort.SortingExercise;
import universe.sort.SortingWorld;

public class ExInsertionSort extends SortingExercise {

	public ExInsertionSort(Lesson lesson) {
		super(lesson);
		name = "Tri à bulle et variantes";
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test",100);

		addEntityKind(myWorlds, new AlgGnomeSort(), "GnomeSort");  
		addEntityKind(myWorlds, new AlgSelectionSort(), "SelectionSort");  
		addEntityKind(myWorlds, new AlgInsertionSort(), "InsertionSort");  
		addEntityKind(myWorlds, new AlgShellSort(), "ShellSort");
		
		setup(myWorlds);
	}
}
