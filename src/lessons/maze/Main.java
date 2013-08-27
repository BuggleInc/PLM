package lessons.maze;

import java.io.IOException;

import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import lessons.maze.island.IslandMaze;
import lessons.maze.pledge.PledgeMaze;
import lessons.maze.randommouse.RandomMouseMaze;
import lessons.maze.shortestpath.ShortestPathMaze;
import lessons.maze.wallfindfollow.WallFindFollowMaze;
import lessons.maze.wallfollower.WallFollowerMaze;

public class Main extends Lesson {

	// see http://www.astrolog.org/labyrnth/algrithm.htm
	// see http://en.wikipedia.org/wiki/Maze#Solving_mazes
	
	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new RandomMouseMaze(this));
		addExercise(new WallFollowerMaze(this));
		addExercise(new WallFindFollowMaze(this));
		addExercise(new IslandMaze(this));
		addExercise(new PledgeMaze(this));
		addExercise(new ShortestPathMaze(this));
	}
	
}
