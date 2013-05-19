package jlm.universe.sort;

public class CopyVal extends Operation {
	
	/**
	 * Constructor of the class CopyVal
	 * @param source : the source of the operation
	 * @param destination : the destination of the operation
	 * @return A new CopyVal
	 */
	public CopyVal(int source, int destination){
		super(source,destination);
		this.type = 0;
	}

	/**
	 * Compute an operation on init
	 * @param init the values on which compute the operation
	 * @return a new array of values equals to init where the operation had been computed
	 */
	@Override
	public int[] run(int[] init) {
		init[destination] = init[source];
		return init;
	}
	
}
