package plm.test.simple;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.Entity;
import plm.universe.World;

public class SimpleExercise extends ExerciseTemplated {

	public SimpleExercise(Game game, Lesson lesson) {
		super(game, lesson);
		
		SimpleWorld w = new SimpleWorld(game, "test");
		Entity newEntity = new SimpleExerciseEntity();
		w.addEntity(newEntity);
		setup( new World[] {
				w
		});
	}
	
	public SimpleExercise(Game game, Lesson lesson, String name) {
		super(game, lesson);
		this.setName(name);
	}
	
	protected void setup(World[] ws) {
		super.setup(ws);
	}
	
	
}
