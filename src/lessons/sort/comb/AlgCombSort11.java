package lessons.sort.comb;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.sort.SortingEntity;
import jlm.universe.sort.SortingWorld;

public class AlgCombSort11 extends ExerciseTemplated {

	public AlgCombSort11(Lesson lesson) {
		super(lesson);
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);
		 
		for ( int i = 0 ; i < myWorlds.length ; i++)
			new SortingEntity("CombSort11",myWorlds[i]);

		setup(myWorlds);
	}

}
