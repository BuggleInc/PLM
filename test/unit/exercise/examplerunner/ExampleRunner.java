package unit.exercise.examplerunner;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.Entity;
import plm.universe.World;
import unit.exercise.universe.ExampleWorld;

public class ExampleRunner extends ExerciseTemplated {

	public ExampleRunner(FileUtils fileUtils) {
		super("ExampleRunner");
		tabName = "Example";
		
		ExampleWorld w1 = new ExampleWorld(fileUtils, "Example World 1");
		Entity entity1 = new ExampleRunnerEntity();
		w1.addEntity(entity1);
		
		ExampleWorld w2 = new ExampleWorld(fileUtils, "Example World 2");
		Entity entity2 = new ExampleRunnerEntity();
		w2.addEntity(entity2);
		
		World[] worlds = { w1, w2 };
		
		setup(worlds);
	}

}
