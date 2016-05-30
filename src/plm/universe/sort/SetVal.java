package plm.universe.sort;

public class SetVal extends Action {
	
	int value;

	/**
	 * Constructor of the class SetVal
	 * @param position the source of the Action
	 * @param destination the destination of the Action
	 */
	public SetVal(int position, int value){
		super(position,-1);
		this.value = value; 
	}

	/**
	 * Compute an Action on init
	 * @param init the values on which compute the Action
	 * @return the array passed as parameter, once the Action is applied to it
	 */
	@Override
	public int[] run(int[] init) {
		init[source] = value;
		
		return init;
	}
	
	
}
