package lessons.sort.basic.bubble;

import lessons.sort.baseball.BubbleBaseballEntity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.sort.SortingWorld;

import java.util.Arrays;

public class AlgBubbleSort1 extends ExerciseTemplated {

	public AlgBubbleSort1(Lesson lesson) {
		super(lesson);
	   
		World[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(AlgBubbleSort1Entity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}
}
