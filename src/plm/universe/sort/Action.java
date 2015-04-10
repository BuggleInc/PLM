package plm.universe.sort;


public abstract class Action {
	
	protected int source;	// the source of the Action
	protected int destination;	// the destination of the Action
	
	/**
	 * Constructor of the class Action
	 * @param source the source of the Action
	 * @param destination the destination of the Action
	 */
	public Action(int source, int destination){
		this.source = source;
		this.destination = destination;
	}
	
	/**
	 * Indicate whether some other object is "equal to" this one
	 * @param o the reference object with which to compare
	 */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Action))
			return false;
		
		Action other = (Action) o;
		if (destination != other.destination)
			return false;
		if (!getClass().equals(o.getClass()))
			return false;
		if (source != other.source)
			return false;
		return true;
	}

	/**
	 * Returns the source of the Action
	 */
	public int getSource() {
		return this.source;
	}

	/**
	 * Returns the destination of the Action
	 */
	public int getDestination() {
		return this.destination;
	}
		
	/**
	 * Compute an Action on init
	 * @param init the values on which compute the Action
	 * @return a new array of values equals to init where the Action had been computed
	 */
	public abstract int[] run(int[] init);	
}
