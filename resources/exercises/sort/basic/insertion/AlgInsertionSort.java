package sort.basic.insertion;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.sort.SortingWorld;

public class AlgInsertionSort extends ExerciseTemplated {

	public AlgInsertionSort(FileUtils fileUtils) {
		super("AlgInsertionSort");
		
		SortingWorld[] myWorlds = new SortingWorld[1];
		myWorlds[0] = new SortingWorld(fileUtils, "Functional test",10);
//		myWorlds[1] = new SortingWorld(fileUtils, "Performance test (150 elms)",150);

		setup(myWorlds);
	}
	
}
