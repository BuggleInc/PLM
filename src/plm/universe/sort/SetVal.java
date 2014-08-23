package plm.universe.sort;

public class SetVal extends Operation {
	
	int value;

	/**
	 * Constructor of the class SetVal
	 * @param position the source of the operation
	 * @param destination the destination of the operation
	 */
	public SetVal(int position, int value){
		super(position,-1);
		this.value = value; 
	}

	/**
	 * Compute an operation on init
	 * @param init the values on which compute the operation
	 * @return the array passed as parameter, once the operation is applied to it
	 */
	@Override
	public int[] run(int[] init) {
		init[source] = value;
		
		return init;
	}
	
	
}
