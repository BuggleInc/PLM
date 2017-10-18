package sort.basic.selection;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.sort.SortingWorld;

public class AlgSelectionSort extends ExerciseTemplated {

	public AlgSelectionSort(FileUtils fileUtils) {
		super("AlgSelectionSort");
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld(fileUtils, "Functional test",10,false);
		myWorlds[1] = new SortingWorld(fileUtils, "Performance test (150 elms)",150);

		setup(myWorlds);
		
	}
}
