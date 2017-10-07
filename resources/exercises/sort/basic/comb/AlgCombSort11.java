package sort.basic.comb;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.sort.SortingWorld;

public class AlgCombSort11 extends ExerciseTemplated {

	public AlgCombSort11() {
		super("AlgCombSort", "AlgCombSort");
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);
		 
		setup(myWorlds);
	}

}
