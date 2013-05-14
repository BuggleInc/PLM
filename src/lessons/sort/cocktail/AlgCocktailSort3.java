package lessons.sort.cocktail;

import jlm.core.model.Game;
import jlm.core.model.lesson.Lesson;
import jlm.universe.sort.SortingEntity;
import jlm.universe.sort.SortingExercise;
import jlm.universe.sort.SortingWorld;

public class AlgCocktailSort3 extends SortingExercise{
	
	public AlgCocktailSort3(Lesson lesson) {
		super(lesson);
		
		addProgLanguage(Game.PYTHON);
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);

		for ( int i = 0 ; i < myWorlds.length ; i++)
			new SortingEntity("Cocktail maker 3",myWorlds[i]);

		setup(myWorlds);
	}
}
