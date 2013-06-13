package jlm.universe.sort;

public class SetVal extends Operation {
	
	/**
	 * Constructor of the class SetVal
	 * @param source the source of the operation
	 * @param destination the destination of the operation
	 */
	public SetVal(int source, int destination){
		super(source,destination);
		this.type =1;
	}

	/**
	 * Compute an operation on init
	 * @param init the values on which compute the operation
	 * @return the array passed as parameter, once the operation is applied to it
	 */
	@Override
	public int[] run(int[] init) {
		init[destination] = source;
		
		return init;
	}
	
	
}
