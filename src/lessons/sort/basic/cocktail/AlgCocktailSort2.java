package lessons.sort.basic.cocktail;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.sort.SortingEntity;
import plm.universe.sort.SortingWorld;

public class AlgCocktailSort2 extends ExerciseTemplated {
	
	public AlgCocktailSort2(Lesson lesson) {
		super(lesson);
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);
		
		for ( int i = 0 ; i < myWorlds.length ; i++)
			new SortingEntity("Cocktail maker 1",myWorlds[i]);

		setup(myWorlds);
	}
}
