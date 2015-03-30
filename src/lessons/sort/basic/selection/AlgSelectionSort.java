package lessons.sort.basic.selection;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.sort.SortingWorld;

public class AlgSelectionSort extends ExerciseTemplated {

	public AlgSelectionSort(Game game, Lesson lesson) {
		super(game, lesson);
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld(game, "Functional test",10,false);
		myWorlds[1] = new SortingWorld(game, "Performance test (150 elms)",150);

		setup(myWorlds);
		
	}
}
