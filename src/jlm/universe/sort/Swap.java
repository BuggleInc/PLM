package jlm.universe.sort;

public class Swap extends Operation {
	
	/**
	 * Constructor of the class Swap
	 * @param source : the source of the operation
	 * @param destination : the destination of the operation
	 * @return A new Swap
	 */
	public Swap(int source, int destination){
		super(source,destination);
		this.type = 2;
	}

	/**
	 * Compute an operation on init
	 * @param init the values on which compute the operation
	 * @return a new array of values equals to init where the operation had been computed
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
