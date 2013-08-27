package lessons.turmites.langton;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.turmites.universe.TurmiteWorld;

public class Langton extends ExerciseTemplated {

	public Langton(Lesson lesson) {
		super(lesson);
		tabName = "LangtonsAnt";

		setup(new TurmiteWorld("12000 steps",12000,null,100, 70, 66,23));

	}
}
