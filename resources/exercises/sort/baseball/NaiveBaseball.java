package sort.baseball;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.baseball.BaseballWorld;

public class NaiveBaseball extends ExerciseTemplated {

	public NaiveBaseball(FileUtils fileUtils) {
		super("NaiveBaseball");

		setup(new BaseballWorld[] {
				new BaseballWorld(fileUtils, "Field 1",4,2, BaseballWorld.MIX_NOBODY_HOME),
				new BaseballWorld(fileUtils, "Field 2",4,2, BaseballWorld.MIX_NOBODY_HOME)
		});
	}

}
