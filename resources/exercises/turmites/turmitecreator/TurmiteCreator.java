package turmites.turmitecreator;


import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.turmites.TurmiteWorld;

public class TurmiteCreator extends ExerciseTemplated {
	
	public TurmiteCreator() {
		super("TurmiteCreator", "TurmiteCreator");
		tabName = "Turmite";

		setup(new TurmiteWorld("blah",1000,null,100,100,50,50));
	}
}
