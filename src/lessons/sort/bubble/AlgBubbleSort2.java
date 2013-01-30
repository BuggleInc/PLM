package lessons.sort.bubble;

import jlm.core.model.Game;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.sort.SortingEntity;
import jlm.universe.sort.SortingWorld;

public class AlgBubbleSort2 extends ExerciseTemplated{

	public AlgBubbleSort2(Lesson lesson) {
		super(lesson);
		
		addProgLanguage(Game.PYTHON);
	   
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);
		
		for ( int i = 0 ; i < myWorlds.length ; i++)
		{
			new SortingEntity("BubbleSort2",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
