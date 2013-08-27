package plm.universe.sort;

public class CopyVal extends Operation {
	
	/**
	 * Constructor of the class CopyVal
	 * @param source : the source of the operation
	 * @param destination : the destination of the operation
	 */
	public CopyVal(int source, int destination){
		super(source,destination);
	}

	/**
	 * Compute an operation on init
	 * @param init the values on which compute the operation 
	 * @return the array of values after the operation
	 */
	@Override
	public int[] run(int[] init) {
		init[destination] = init[source];
		return init;
	}
	
}
