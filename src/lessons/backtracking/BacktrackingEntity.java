package lessons.backtracking;
import java.util.Vector;

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
	/** Must exist too. Calling HanoiEntity("dummy name") is ok */
	public BacktrackingEntity() {
		this("Backtracking Entity");
	}

	@Override
	public Entity copy() {
		return new BacktrackingEntity(name);
	}

	/** Must exist so that exercises can instantiate your entity (Entity is abstract) */
	@Override
	public void run() {
		solve((BacktrackingElement[]) world.getParameters());
	}

	public void solve(BacktrackingElement[] elements){
		
	}
	
	@Override
	public String toString(){
		return "BacktrackingEntity (" + this.getClass().getName() + ")";
	}

	/* **** world logic **** */
	public double getSolutionValue(int depth, Vector<BacktrackingElement> solution) {
		stepUI();
		return ((BacktrackingWorld) world).getSolutionValue(depth,solution);
	}
	public boolean isSolutionValid(int depth, Vector<BacktrackingElement> solution) {
		stepUI();
		return ((BacktrackingWorld) world).isSolutionValid(depth,solution);
	}
	public void newBestSolution(int depth, Vector<BacktrackingElement> solution) {
		stepUI();
		((BacktrackingWorld) world).newBestSolution(solution);
	}
}
