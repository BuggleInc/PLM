package sort.basic.cocktail;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.sort.SortingWorld;

public class AlgCocktailSort1 extends ExerciseTemplated {
	
	public AlgCocktailSort1(FileUtils fileUtils) {
		super("AlgCocktailSort1");
		
		SortingWorld[] myWorlds = new SortingWorld[1];
		myWorlds[0] = new SortingWorld(fileUtils, "Functional test",10);
//		myWorlds[1] = new SortingWorld(fileUtils, "Performance test (150 elms)",150);
		
		setup(myWorlds);
	}
}
