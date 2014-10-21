package plm.test.simple;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Entity;
import plm.universe.World;

public class SimpleExercise extends ExerciseTemplated {

	public SimpleExercise(Lesson lesson) {
		super(lesson);
		this.setName("SimpleExercise");
		tabName = "SimpleExercise";
		
		SimpleWorld w = new SimpleWorld("test");
		Entity newEntity = null;
		try {
			newEntity = SimpleExerciseEntity.class.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		w.addEntity(newEntity);
		setup( new World[] {
				w
		});
	}
	
	public SimpleExercise(Lesson lesson, String name) {
		super(lesson);
		this.setName(name);
	}
	
	protected void setup(World[] ws) {
		super.setup(ws);
	}
	
	
}
