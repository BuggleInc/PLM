package lessons.backtracking;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.World;

public class ExKnapsack extends BacktrackingExercise {

	public ExKnapsack(Game game, Lesson lesson) {
		super(game, lesson);
		
		KnapsackPartialSolution instance = new KnapsackPartialSolution(10,new int[] {2,2,4,4});
		World[] myWorld = new World[] {
				new BacktrackingWorld(game, instance)
		};
		
		setup(myWorld,new KnapsackSolver());
	}
}
