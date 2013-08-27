package lessons.sort.cocktail;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.sort.SortingEntity;
import plm.universe.sort.SortingWorld;

public class AlgCocktailSort3 extends ExerciseTemplated {
	
	public AlgCocktailSort3(Lesson lesson) {
		super(lesson);
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);

		for ( int i = 0 ; i < myWorlds.length ; i++)
			new SortingEntity("Cocktail maker 3",myWorlds[i]);

		setup(myWorlds);
	}
}
