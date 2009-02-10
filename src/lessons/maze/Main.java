package lessons.maze;

import jlm.lesson.Lesson;

public class Main extends Lesson {

	// see http://www.astrolog.org/labyrnth/algrithm.htm
	// see http://en.wikipedia.org/wiki/Maze#Solving_mazes
	
	public Main() {
		super();
		setSequential(false);
	}

	@Override
	protected void loadExercises() {
		addExercise(new RandomMouseMaze(this));
		addExercise(new WallFollowerMaze(this));
		addExercise(new PledgeMaze(this));
		exercisesLoaded = true;
	}
	
}
