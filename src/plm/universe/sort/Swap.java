package plm.universe.sort;

public class Swap extends Operation {
	
	/**
	 * Constructor of the class Swap
	 * @param source the source of the operation
	 * @param destination the destination of the operation
	 */
	public Swap(int source, int destination){
		super(source,destination);
	}

	/**
	 * Compute an operation on init
	 * @param init the values on which compute the operation
	 * @return the array passed as parameter, once the operation has been applied to it
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
