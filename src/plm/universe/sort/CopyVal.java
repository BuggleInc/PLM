package plm.universe.sort;

public class CopyVal extends Action {
	
	/**
	 * Constructor of the class CopyVal
	 * @param source : the source of the Action
	 * @param destination : the destination of the Action
	 */
	public CopyVal(int source, int destination){
		super(source,destination);
	}

	/**
	 * Compute an Action on init
	 * @param init the values on which compute the Action 
	 * @return the array of values after the Action
	 */
	@Override
	public int[] run(int[] init) {
		init[destination] = init[source];
		return init;
	}
	
}
