package lessons.turtleart;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

public class Main extends Lesson {
	
	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() throws IOException {
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Square",       300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"SmallSquare",  300,300, 50,150));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Stairs",       300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"TriangleFlat", 300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Triangle",     300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"House",        300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"HouseThree",   300,300, 50,150));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"HouseMany",    300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Polygon6",     300,300, 81,190));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Polygon7",     300,300, 65,190));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Polygon20",    300,300, 55,165));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Polygon360",   300,300, 35,149));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"CircleTwo",    300,300, 35,149));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"CircleYing",   300,300, 35,149));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"CircleSquare", 300,300, 50,200));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"CircleTen",    300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"DiskFourth",   300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"DiskFour",     300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"DiskTwo",      300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Star",         300,300, 150,200));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Flower",       300,300, 90, 175));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Kerr36",       300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Kerr40",       300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(getGame(), this,"Flower3",      300,300, 150,150));
	}
}

class TurtleGraphicalExercise extends ExerciseTemplated {
	
	public TurtleGraphicalExercise(Game game, Lesson lesson,String name,
			int worldWidth,int worldHeight,int tx, int ty) {
		
		super(game, lesson, lesson.getClass().getName()+"."+name);
		
		setName(name);
		tabName = "Source";
		World myWorld = new TurtleWorld(game, name, worldWidth, worldHeight);
		Turtle t = new Turtle(myWorld, "Hawksbill", tx, ty);
		t.setHeading(-90);
		setup(myWorld);
	}
	
	/* all exercises are packed into that file because the only difference between them is only the world size and turtle coordinates */
	@Override
	public String nameOfCorrectionEntity() { 
		return getLesson().getClass().getCanonicalName().replace(".Main","") + "." + getName() + "Entity";
	}

	@Override
	public void loadHTMLMission() {}
}
