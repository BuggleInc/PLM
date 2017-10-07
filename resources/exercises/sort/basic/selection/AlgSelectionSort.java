package sort.basic.selection;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.sort.SortingWorld;

public class AlgSelectionSort extends ExerciseTemplated {

	public AlgSelectionSort() {
		super("AlgSelectionSort", "AlgSelectionSort");
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10,false);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);

		setup(myWorlds);
		
	}
}
