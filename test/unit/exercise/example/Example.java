package unit.exercise.example;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.Entity;
import plm.universe.World;
import unit.exercise.universe.ExampleWorld;

public class Example extends ExerciseTemplated {

	public Example() {
		super("Example", "Example");
		tabName = "Example";
		
		ExampleWorld w1 = new ExampleWorld("Example World 1");
		Entity entity1 = new ExampleEntity();
		w1.addEntity(entity1);
		
		ExampleWorld w2 = new ExampleWorld("Example World 2");
		Entity entity2 = new ExampleEntity();
		w2.addEntity(entity2);
		
		World[] worlds = { w1, w2 };
		
		setup(worlds);
	}

}
