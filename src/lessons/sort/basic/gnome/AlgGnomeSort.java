package lessons.sort.basic.gnome;

import lessons.sort.basic.bubble.AlgBubbleSort1Entity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.sort.SortingWorld;

import java.util.Arrays;

public class AlgGnomeSort extends ExerciseTemplated {

	public AlgGnomeSort(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test (150 elms)",150);

		myWorlds = Arrays.stream(myWorlds).map(s->s.replaceEntities(AlgGnomeSortEntity::new)).toList().toArray(new World[0]);
		setup(myWorlds);
	}

}
