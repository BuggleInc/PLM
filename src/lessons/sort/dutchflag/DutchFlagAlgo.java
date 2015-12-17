package lessons.sort.dutchflag;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.dutchflag.DutchFlagWorld;

public class DutchFlagAlgo extends ExerciseTemplated {
	
	public DutchFlagAlgo(Game game, Lesson lesson) {
		super(game, lesson);
	
		setup( new DutchFlagWorld[] {
				
				new DutchFlagWorld(game, "6 lines",6),
				new DutchFlagWorld(game, "12 lines",12),
				new DutchFlagWorld(game, "18 lines",18),
				new DutchFlagWorld(game, "36 lines",36),
				new DutchFlagWorld(game, "300 lines",300),
				new DutchFlagWorld(game, "12 white/red",12,0), 
				new DutchFlagWorld(game, "12 blue/red",12,1), 
				new DutchFlagWorld(game, "12 blue/white",12,2),
				
		});

	}

}
