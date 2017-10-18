package sort.baseball;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.baseball.BaseballWorld;

public class BubbleBaseball extends ExerciseTemplated {

	public BubbleBaseball(FileUtils fileUtils) {
		super("BubbleBaseball");

		setup(new BaseballWorld[] {
				new BaseballWorld(fileUtils, "5 bases, 2 positions",5,2),
				new BaseballWorld(fileUtils, "5 bases, 3 positions",5,3),
				new BaseballWorld(fileUtils, "6 bases, 2 positions",6,2),
				new BaseballWorld(fileUtils, "6 bases, 3 positions",6,3),
				new BaseballWorld(fileUtils, "7 bases, 2 positions",7,2),
				new BaseballWorld(fileUtils, "7 bases, 3 positions",7,3),
				new BaseballWorld(fileUtils, "7 bases, 4 positions",7,4),
				new BaseballWorld(fileUtils, "8 bases, 2 positions",8,2),
				new BaseballWorld(fileUtils, "8 bases, 3 positions",8,3),
		});
	}

}
