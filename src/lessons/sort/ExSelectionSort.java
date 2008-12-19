package lessons.sort;

import jlm.lesson.Lesson;
import universe.sort.SortingExercise;
import universe.sort.SortingWorld;

public class ExSelectionSort extends SortingExercise {

	public ExSelectionSort(Lesson lesson) {
		super(lesson);
		name = "Tris itératifs";
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (200 elms)",200);

		addEntityKind(myWorlds, new AlgSelectionSort(), "SelectionSort");  
		
		setup(myWorlds);
	}
}
