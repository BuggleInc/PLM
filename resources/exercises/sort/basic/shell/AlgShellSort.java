package sort.basic.shell;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.sort.SortingWorld;

public class AlgShellSort extends ExerciseTemplated {

	public AlgShellSort(FileUtils fileUtils) {
		super("AlgShellSort");
		
		SortingWorld[] myWorlds = new SortingWorld[1];
		myWorlds[0] = new SortingWorld(fileUtils, "Functional test",10);
//		myWorlds[1] = new SortingWorld(fileUtils, "Performance test (150 elms)",150);

		setup(myWorlds);
	}
	
}
