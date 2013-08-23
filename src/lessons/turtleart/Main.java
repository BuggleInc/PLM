package lessons.turtleart;

import java.io.IOException;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import jlm.universe.World;
import jlm.universe.turtles.Turtle;
import jlm.universe.turtles.TurtleWorld;

public class Main extends Lesson {
	
	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		System.out.println("Load lesson turtleart");
		addExercise(new TurtleGraphicalExercise(this,"Square", 400,400,200,200));
	}

	
}

class TurtleGraphicalExercise extends ExerciseTemplated{
	
	public TurtleGraphicalExercise(Lesson lesson,String name,
			int worldWidth,int worldHeight,int tx, int ty) {
		
		super(lesson, lesson.getClass().getName()+"."+name);
		
		setName(name);
		tabName = "Source";
		nameOfCorrectionEntity = lesson.getClass().getName().replace(".Main","")+"."+name+"Entity";
		World myWorld = new TurtleWorld("WhiteBoard", worldWidth, worldHeight);
		new Turtle(myWorld, "Hawksbill", tx, ty);
		System.err.println("correctionEntitiy: "+nameOfCorrectionEntity);
		setup(myWorld);
	}
	@Override
	public void loadHTMLMission() {
		setMission("");
	}
}


abstract class GraphicalExercise extends ExerciseTemplated{
	public GraphicalExercise(Lesson lesson,String name) {
		super(lesson, lesson.getClass().getName()+"."+name);
		init();
	}
	public abstract void init();
	public void setupTurtle(int worldWidth,int worldHeight,int tx, int ty) {
		World myWorld = new TurtleWorld("WhiteBoard", worldWidth, worldHeight);
		new Turtle(myWorld, "Hawksbill", tx, ty);
		setup(myWorld);
	}
}