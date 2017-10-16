package turmites.langtoncolors;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.turmites.TurmiteWorld;

public class LangtonColors extends ExerciseTemplated {
	private BuggleWorld createWorld(FileUtils fileUtils, String rule, int nbSteps, int width, int height, int buggleX, int buggleY) {
		return new TurmiteWorld(fileUtils, rule+" ("+nbSteps+" steps)", nbSteps, rule.toCharArray(), width, height, buggleX, buggleY);
	}
	
	public LangtonColors(FileUtils fileUtils) {
		super("LangtonColors", "LangtonColors");
		tabName = "LangtonsAnt";

		BuggleWorld[] myWorlds = new BuggleWorld[] {
				 createWorld(fileUtils, "RL",           12001, 100, 70, 66, 23),
				 //createWorld("LLRR",         20001,  30, 30, 15, 15),
				 createWorld(fileUtils, "LRRRRRLLR",     9001,  25, 24, 11, 12),
				 //createWorld("LLRRRLRLRLLR", 36001, 120, 60, 100, 30),
				 createWorld(fileUtils, "RRLLLRLLLRRR", 30001,  80, 90, 60, 28),
		};
		
		setup(myWorlds);

	}
}
