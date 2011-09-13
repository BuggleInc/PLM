package lessons.welcome.cells;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bugglequest.BuggleWorld;

public class LangtonColors extends ExerciseTemplated {
	BuggleWorld createWorld(String rule, int nbSteps, int width, int height, int buggleX, int buggleY) {
		return new TurmiteWorld(rule+" ("+nbSteps+" steps)", nbSteps, rule.toCharArray(), width, height, buggleX, buggleY);
	}
	
	public LangtonColors(Lesson lesson) {
		super(lesson);
		tabName = "LangtonsAnt";

		BuggleWorld[] myWorlds = new BuggleWorld[] {
				 createWorld("RL",           12000, 100, 70, 66, 23),
				 createWorld("LLRR",         20000,  30, 30, 15, 15),
				 createWorld("LRRRRRLLR",     9000,  25, 24, 11, 12),
				 createWorld("LLRRRLRLRLLR", 33000, 100, 60, 80, 30),
				 createWorld("RRLLLRLLLRRR", 30000,  80, 90, 60, 28),
		};
		
		setup(myWorlds);

	}
}
