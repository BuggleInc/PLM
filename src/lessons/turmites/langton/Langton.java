package lessons.turmites.langton;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.turmites.universe.TurmiteWorld;

public class Langton extends ExerciseTemplated {

	public Langton(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "LangtonsAnt";

		setup(new TurmiteWorld(game, "12000 steps",12000,null,100, 70, 66,23));

	}
}
