package turmites.langton;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.turmites.TurmiteWorld;

public class Langton extends ExerciseTemplated {

	public Langton() {
		super("Langton", "Langton");
		tabName = "LangtonsAnt";

		setup(new TurmiteWorld("12000 steps",12000,null,100, 70, 66,23));

	}
}
