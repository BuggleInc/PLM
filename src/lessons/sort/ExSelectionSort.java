package lessons.sort;

import jlm.core.model.lesson.Lesson;
import jlm.universe.sort.SortingExercise;
import jlm.universe.sort.SortingWorld;

public class ExSelectionSort extends SortingExercise {

	public ExSelectionSort(Lesson lesson) {
		super(lesson);
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (200 elms)",200);

		addEntityKind(myWorlds, new AlgSelectionSort(), "SelectionSort");  
		
		setup(myWorlds);
	}
}
