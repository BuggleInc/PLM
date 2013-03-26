package lessons.sort.selection;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.sort.SortingEntity;
import jlm.universe.sort.SortingWorld;

public class AlgSelectionSort extends ExerciseTemplated {

	public AlgSelectionSort(Lesson lesson) {
		super(lesson);
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);

		for ( int i = 0 ; i < myWorlds.length ; i++)
		{
			new SortingEntity("Selection Sort",myWorlds[i]);
		}
		
		setup(myWorlds);
		
	}
}
