package plm.universe.sort;

public class GetVal extends Action {
	
	public int position;

	/**
	 * Constructor of the class SetVal
	 * @param position the source of the Action
	 * @param destination the destination of the Action
	 */
	public GetVal(int position){
		super(-1,-1);
		this.position = position; 
	}

	/**
	 * Compute an Action on init
	 * @param init the values on which compute the Action
	 * @return the array passed as parameter, once the Action is applied to it
	 */
	@Override
	public int[] run(int[] init) {
		return init;
	}
	
	
}
