package lessons.sort.baseball;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.sort.baseball.universe.BaseballWorld;

public class NaiveBaseball extends ExerciseTemplated {

	public NaiveBaseball(Lesson lesson) {
		super(game, lesson);

		setup(new BaseballWorld[] {
				new BaseballWorld("Field 1",4,2, BaseballWorld.MIX_NOBODY_HOME),
				new BaseballWorld("Field 2",4,2, BaseballWorld.MIX_NOBODY_HOME)
		});
	}

}
