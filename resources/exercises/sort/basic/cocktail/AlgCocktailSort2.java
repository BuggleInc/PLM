package sort.basic.cocktail;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.sort.SortingWorld;

public class AlgCocktailSort2 extends ExerciseTemplated {
	
	public AlgCocktailSort2() {
		super("AlgCocktailSort2", "AlgCocktailSort2");
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);
		
		setup(myWorlds);
	}
}
