package lessons.backtracking;

import plm.core.model.lesson.Lesson;
import plm.universe.World;

public class ExKnapsack extends BacktrackingExercise {

	public ExKnapsack(Lesson lesson) {
		super(lesson);
		
		KnapsackPartialSolution instance = new KnapsackPartialSolution(10,new int[] {2,2,4,4});
		World[] myWorld = new World[] {
				new BacktrackingWorld(instance)
		};
		
		setup(myWorld,new KnapsackSolver());
	}
}
