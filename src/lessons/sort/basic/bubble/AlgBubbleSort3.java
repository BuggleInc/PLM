package lessons.sort.basic.bubble;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.sort.SortingWorld;

public class AlgBubbleSort3 extends ExerciseTemplated {

	public AlgBubbleSort3(Lesson lesson) {
		super(lesson);
		
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);
		
		setup(myWorlds);
	}
}
