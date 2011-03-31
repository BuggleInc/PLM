package lessons.backtracking;

import jlm.lesson.Lesson;
import jlm.universe.World;

public class ExKnapsack extends BacktrackingExercise {

	public ExKnapsack(Lesson lesson) {
		super(lesson);
		
		KnapsackPartialSolution instance = new KnapsackPartialSolution(10,new int[] {2,2,4,4});
		World[] myWorld = new World[] {
				new BacktrackingWorld(instance)
		};
		
		setup(myWorld,new KnapsackSolver());
	}

	/* BEGIN SKEL */
	public void run(BacktrackingPartialSolution ps) {
		System.out.println(this.getClass().getSimpleName()+": run(BPS "+ps+")");
		solve((KnapsackPartialSolution) ps);
	}
	/* END SKEL */

	/* BEGIN TEMPLATE */
	void solve(KnapsackPartialSolution bag) {
		/* BEGIN HIDDEN */
		solve(0,bag);
		/* END HIDDEN */
	}
	
	/* BEGIN HIDDEN */
	int bestknown=0;
	/* END HIDDEN */
	void solve(int depth, KnapsackPartialSolution bag) {
		System.out.println("solve("+depth+","+bag+")");
		/* BEGIN SOLUTION */
		if (!bag.isValid()) {
			System.out.println("solution invalid, backtrack");
			return;
		}
		if (bag.getValue()>bestknown) {
			bestknown = bag.getValue();
			newBestSolution(bag);
		}
		if (depth>=bag.size()) {
			System.out.println("depth too large: "+depth);
			return;
		}
		bag.take(depth);
		solve(depth+1,bag);
		bag.leave(depth);
		solve(depth+1,bag);
		/* END SOLUTION */
	}	
	/* END TEMPLATE */
}
