package lessons.backtracking;

import java.util.Vector;

import jlm.ui.WorldView;
import jlm.universe.World;

public abstract class BacktrackingWorld extends World {	
	/** A copy constructor (mandatory for the internal compilation mechanism to work)
	 * 
	 * There is normally no need to change it, but it must be present. 
	 */ 
	public BacktrackingWorld(BacktrackingWorld other) {
		super(other);
	}
	
	/** The constructor that the exercises will use to setup the world.
	 */
	public BacktrackingWorld(String name, BacktrackingElement[] parameters) {
		super(name);
		setDelay(200);
		addEntity(new BacktrackingEntity());
		this.parameters = parameters; 
	}
	
	/** Reset the state of the current world to the one passed in argument
	 * 
	 * This is mandatory for the JLM good working. Even if the prototype says that the passed object can be 
	 * any kind of world, you can be sure that it's of the same type than the current world. So, there is 
	 * no need to check before casting your argument.
	 * 
	 * Do not forget to call super.reset(w) afterward, or some internal world fields may not get reset.
	 */
	@Override
	public void reset(World w) {
		BacktrackingWorld other = (BacktrackingWorld)w;
		
		bestSolution.removeAllElements();
		for (int i=0;i<other.bestSolution.size();i++)
			bestSolution.add(other.bestSolution.get(i).copy());

		/* FIXME */
		super.reset(w);		
	}

	/** Returns a component able of displaying the world -- will be used in third exercise 
	 * You should comment this for the first exercises */
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new BacktrackingWorldView(this) } ;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName()+" "+getName()+"; Parameters: (");
		boolean first=true;
		for (Object o:parameters) {
			if (first) 
				first=false;
			else
				sb.append(", ");
			sb.append(o);
		}
		
		sb.append(") Best Solution: (");
		first=true;
		for (BacktrackingElement e:bestSolution) {
			if (first) 
				first=false;
			else
				sb.append(", ");
			sb.append(e);
		}
		sb.append(")");
		return sb.toString();
	}

	/** Used to check whether the student code changed the world in the right state */
	@Override 
	public boolean equals(Object o) {
		/* same initial world */
		if (o == null || !(o instanceof BacktrackingWorld))
			return false;
		BacktrackingWorld other = (BacktrackingWorld) o;
		if (other.parameters.length != parameters.length)
			return false;
		for (int i=0;i<parameters.length;i++)
			if (!parameters[i].equals(other.parameters[i]))
				return false;
		
		/* Same best solution */
		if (! other.bestSolution.equals(bestSolution))
			return false;
		
		/* FIXME */
		return true;
	}
	
	/* Here comes the world logic */
	public abstract double getSolutionValue(int depth, Vector<BacktrackingElement> solution);
	public abstract boolean isSolutionValid(int depth, Vector<BacktrackingElement> solution);
	Vector<BacktrackingElement> bestSolution = new Vector<BacktrackingElement>();
	public void newBestSolution(Vector<BacktrackingElement> solution) {
		bestSolution.removeAllElements();
		for (int i=0;i<solution.size();i++)
			bestSolution.add(solution.get(i).copy());
	}
}
