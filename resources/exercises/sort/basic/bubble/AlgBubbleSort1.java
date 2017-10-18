package sort.basic.bubble;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.sort.SortingWorld;

public class AlgBubbleSort1 extends ExerciseTemplated {

	public AlgBubbleSort1(FileUtils fileUtils) {
		super("AlgBubbleSort1");
	   
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld(fileUtils, "Functional test",10);
		myWorlds[1] = new SortingWorld(fileUtils, "Performance test (150 elms)",150);
		
		setup(myWorlds);
	}
}
