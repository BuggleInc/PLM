package lessons.sort.dutchflag;

import lessons.sort.dutchflag.universe.DutchFlagWorld;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class DutchFlagAlgo extends ExerciseTemplated {
	
	public DutchFlagAlgo(Lesson lesson) {
		super(game, lesson);
	
		setup( new DutchFlagWorld[] {
				
				new DutchFlagWorld("6 lines",6),
				new DutchFlagWorld("12 lines",12),
				new DutchFlagWorld("18 lines",18),
				new DutchFlagWorld("36 lines",36),
				new DutchFlagWorld("300 lines",300),
				new DutchFlagWorld("12 white/red",12,0), 
				new DutchFlagWorld("12 blue/red",12,1), 
				new DutchFlagWorld("12 blue/white",12,2),
				
		});

	}

}
