package lessons.backtracking;

import jlm.universe.Entity;
import jlm.universe.World;

public class BacktrackingEntity extends Entity {
	/** Instantiation Constructor (used by exercises to setup the world) 
	 * Must call super(name, world). If you had fields to setup, you'd be free to have more parameters
	 * @param name
	 * @param world
	 */
	public BacktrackingEntity(String name,World world) {
		super(name,world);
	}

	/** Part of the copy process 
	 * Must call super(name)
	 */
	public BacktrackingEntity(String name) {
		super(name);
	}
	public BacktrackingEntity() {
		this("Backtracking Solver");
	}

	@Override
	public Entity copy() {
		return new BacktrackingEntity(name);
	}
	@Override
	public void run() {
		run((BacktrackingPartialSolution) world.getParameter(0));
		System.out.println("Solution:"+((BacktrackingWorld) world).bestSolution);
	}
	
	protected void run(BacktrackingPartialSolution solution) {
		throw new RuntimeException(this.getClass().getCanonicalName()+": This method should be overriden! Please go fix your lesson.");
	}
	
	/* World logic */
	
	public void newBestSolution(BacktrackingPartialSolution solution) {
		((BacktrackingWorld) world).newBestSolution(solution);
	}

	protected BacktrackingPartialSolution getBestSolution() {
		return ((BacktrackingWorld) world).getBestSolution();
	}
}
