package sort.basic.insertion;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.sort.SortingWorld;

public class AlgInsertionSort extends ExerciseTemplated {

	public AlgInsertionSort() {
		super("AlgInsertionSort", "AlgInsertionSort");
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);

		setup(myWorlds);
	}
	
}
