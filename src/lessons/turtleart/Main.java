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
		addExercise(new TurtleGraphicalExercise(this,"Square",       300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"SmallSquare",  300,300, 50,150));
		addExercise(new TurtleGraphicalExercise(this,"Stairs",       300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"TriangleFlat", 300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"Triangle",     300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"House",        300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"HouseThree",   300,300, 50,150));
		addExercise(new TurtleGraphicalExercise(this,"HouseMany",    300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"Polygon6",     300,300, 81,190));
		addExercise(new TurtleGraphicalExercise(this,"Polygon7",     300,300, 65,190));
		addExercise(new TurtleGraphicalExercise(this,"Polygon15",    300,300, 55,165));
		addExercise(new TurtleGraphicalExercise(this,"Polygon360",   300,300, 35,149));
		addExercise(new TurtleGraphicalExercise(this,"CircleTwo",    300,300, 35,149));
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
		Turtle t = new Turtle(myWorld, "Hawksbill", tx, ty);
		t.setHeading(-90);
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