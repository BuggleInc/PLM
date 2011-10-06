package lessons.maze;

import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {

	// see http://www.astrolog.org/labyrnth/algrithm.htm
	// see http://en.wikipedia.org/wiki/Maze#Solving_mazes
	
	@Override
	protected void loadExercises() {
		addExercise(new RandomMouseMaze(this));
		addExercise(new WallFollowerMaze(this));
		addExercise(new WallFindFollowMaze(this));
		addExercise(new IslandMaze(this));
		addExercise(new PledgeMaze(this));
		addExercise(new ShortestPathMaze(this));
	}
	
}
