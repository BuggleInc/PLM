package lessons.backtracking;

import lessons.backtracking.BacktrackingEntity;
import lessons.backtracking.BacktrackingPartialSolution;
import lessons.backtracking.KnapsackPartialSolution;
import lessons.backtracking.KnapsackSolver;

public class KnapsackSolver extends BacktrackingEntity {
	public KnapsackSolver copy(){
		return new KnapsackSolver();
	}
	public void run(BacktrackingPartialSolution bps) {
		solve((KnapsackPartialSolution)bps);
	}
	/* BEGIN TEMPLATE */
	/* BEGIN HIDDEN */
	double bestknown=0;
	/* END HIDDEN */

	public void solve(KnapsackPartialSolution bag){
		/* BEGIN HIDDEN */
		solve(0,bag);
		/* END HIDDEN */
	}
	
	private void solve(int depth, KnapsackPartialSolution bag) {
		System.out.println("solve("+depth+","+bag+"); " +
				"bestSolution so far:"+getBestSolution());
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
