package sort.basic.bubble;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.sort.SortingWorld;

public class AlgBubbleSort2 extends ExerciseTemplated {

	public AlgBubbleSort2(FileUtils fileUtils) {
		super("AlgBubbleSort2");
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld(fileUtils, "Functional test",10);
		myWorlds[1] = new SortingWorld(fileUtils, "Performance test (150 elms)",150);
		
		setup(myWorlds);
	}
}
