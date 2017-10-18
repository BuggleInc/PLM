package sort.dutchflag;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.dutchflag.DutchFlagWorld;

public class DutchFlagAlgo extends ExerciseTemplated {
	
	public DutchFlagAlgo(FileUtils fileUtils) {
		super("DutchFlagAlgo");
	
		setup( new DutchFlagWorld[] {
				
				new DutchFlagWorld(fileUtils, "6 lines",6),
				new DutchFlagWorld(fileUtils, "12 lines",12),
				new DutchFlagWorld(fileUtils, "18 lines",18),
				new DutchFlagWorld(fileUtils, "36 lines",36),
				new DutchFlagWorld(fileUtils, "300 lines",300),
				new DutchFlagWorld(fileUtils, "12 white/red",12,0),
				new DutchFlagWorld(fileUtils, "12 blue/red",12,1),
				new DutchFlagWorld(fileUtils, "12 blue/white",12,2),
				
		});

	}

}
