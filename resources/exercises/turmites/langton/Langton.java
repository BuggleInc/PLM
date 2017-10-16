package turmites.langton;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.turmites.TurmiteWorld;

public class Langton extends ExerciseTemplated {

	public Langton(FileUtils fileUtils) {
		super("Langton", "Langton");
		tabName = "LangtonsAnt";

		setup(new TurmiteWorld(fileUtils, "12000 steps",12000,null,100, 70, 66,23));

	}
}
