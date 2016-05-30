package lessons.maze;

import java.io.IOException;

import plm.core.model.Game;
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
	
	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new RandomMouseMaze(getGame(), this));
		addExercise(new WallFollowerMaze(getGame(), this));
		addExercise(new WallFindFollowMaze(getGame(), this));
		addExercise(new IslandMaze(getGame(), this));
		addExercise(new PledgeMaze(getGame(), this));
		addExercise(new ShortestPathMaze(getGame(), this));
	}
	
}
