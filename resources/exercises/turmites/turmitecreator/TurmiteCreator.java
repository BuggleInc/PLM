package turmites.turmitecreator;


import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.turmites.TurmiteWorld;

public class TurmiteCreator extends ExerciseTemplated {
	
	public TurmiteCreator(FileUtils fileUtils) {
		super("TurmiteCreator");
		tabName = "Turmite";

		setup(new TurmiteWorld(fileUtils, "blah",1000,null,100,100,50,50));
	}
}
