package plm.universe.sort;

public class GetVal extends Operation {
	
	public int position;

	/**
	 * Constructor of the class SetVal
	 * @param position the source of the operation
	 * @param destination the destination of the operation
	 */
	public GetVal(int position){
		super(-1,-1);
		this.position = position; 
	}

	/**
	 * Compute an operation on init
	 * @param init the values on which compute the operation
	 * @return the array passed as parameter, once the operation is applied to it
	 */
	@Override
	public int[] run(int[] init) {
		return init;
	}
	
	
}
