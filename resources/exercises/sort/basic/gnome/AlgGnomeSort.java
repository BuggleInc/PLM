package sort.basic.gnome;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.sort.SortingWorld;

public class AlgGnomeSort extends ExerciseTemplated {

	public AlgGnomeSort() {
		super("AlgGnomeSort", "AlgGnomeSort");
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);
		
		setup(myWorlds);
	}

}
