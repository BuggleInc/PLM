package plm.universe.sort;

public class Swap extends Action {
	
	/**
	 * Constructor of the class Swap
	 * @param source the source of the Action
	 * @param destination the destination of the Action
	 */
	public Swap(int source, int destination){
		super(source,destination);
	}

	/**
	 * Compute an Action on init
	 * @param init the values on which compute the Action
	 * @return the array passed as parameter, once the Action has been applied to it
	 */
	@Override
	public int[] run(int[] init) {
		int src = init[source];
		int dest = init[destination];
		init[source] = dest;
		init[destination] = src;
		
		return init;
	}
	
	
}
