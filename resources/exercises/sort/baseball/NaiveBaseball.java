package sort.baseball;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.baseball.BaseballWorld;

public class NaiveBaseball extends ExerciseTemplated {

	public NaiveBaseball() {
		super("NaiveBaseball", "NaiveBaseball");

		setup(new BaseballWorld[] {
				new BaseballWorld("Field 1",4,2, BaseballWorld.MIX_NOBODY_HOME),
				new BaseballWorld("Field 2",4,2, BaseballWorld.MIX_NOBODY_HOME)
		});
	}

}
