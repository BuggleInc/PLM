package sort.baseball;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.baseball.BaseballWorld;

public class SelectBaseball extends ExerciseTemplated {

	public SelectBaseball(FileUtils fileUtils) {
		super("SelectBaseball");

		setup(new BaseballWorld[] {
				new BaseballWorld(fileUtils, "Almost",4,2, BaseballWorld.MIX_ALMOST_SORTED),
				new BaseballWorld(fileUtils, "5 bases",5,2),
				new BaseballWorld(fileUtils, "6 bases",6,2),
				new BaseballWorld(fileUtils, "7 bases",7,2),
				new BaseballWorld(fileUtils, "8 bases",8,2),
				new BaseballWorld(fileUtils, "9 bases",9,2),
				new BaseballWorld(fileUtils, "10 bases",10,2),
				new BaseballWorld(fileUtils, "15 bases",15,2),
		});
	}

}
