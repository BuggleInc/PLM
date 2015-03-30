package lessons.maze.randommouse;

import java.io.IOException;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.bugglequest.BuggleWorld;

public class RandomMouseMaze extends ExerciseTemplated {

	public RandomMouseMaze(Game game, Lesson lesson) throws IOException, BrokenWorldFileException {
		super(game, lesson);
		tabName = "RandomMouseMaze";
		
		setup( new World[] {
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/randommouse/RandomMouseMaze")).ignoreDirectionDifference(),
				((BuggleWorld) BuggleWorld.newFromFile(game, "lessons/maze/randommouse/RandomMouseMaze2")).ignoreDirectionDifference()
		});
	}
}
