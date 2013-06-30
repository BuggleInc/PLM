package jlm.universe.sort;

import java.util.ArrayList;

public abstract class Operation {
	
	protected int type;	// the type of operation
	protected int source;	// the source of the operation
	protected int destination;	// the destination of the operation
	
	/**
	 * Constructor of the class Operation
	 * @param source the source of the operation
	 * @param destination the destination of the operation
	 */
	public Operation(int source, int destination){
		this.source = source;
		this.destination = destination;
	}
	
	/**
	 * Indicate whether some other object is "equal to" this one
	 * @param o the reference object with which to compare
	 */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Operation))
			return false;
		
		Operation other = (Operation) o;
		if (destination != other.destination)
			return false;
		if (source != other.source)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * Returns the source of the operation
	 */
	public int getSource() {
		return this.source;
	}

	/**
	 * Returns the destination of the operation
	 */
	public int getDestination() {
		return this.destination;
	}
	
	/**
	 * Compute the rang first operations of the list of operations ops on the array init
	 * @param init the values where apply the operations
	 * @param ops the list of operations to compute
	 * @param rang the number of operations to compute
	 * @return A new array of integer equals to init where the rang first operations of ops have been applied
	 */
	public static int[] compute(int[] init, ArrayList<Operation> ops, int rang){
		int[] current = new int[init.length];
		current = init;
		for(int i=0;i<init.length ; i++){
			current[i] = init[i];
		}
		
		int cmpt=1;
		for(Operation op : ops){
			
			current = op.run(current);
			if(cmpt==rang){
				break;
			}
			cmpt++;
		}
		
		return current;
	}
	
	/**
	 * Compute an operation on init
	 * @param init the values on which compute the operation
	 * @return a new array of values equals to init where the operation had been computed
	 */
	public abstract int[] run(int[] init);

	/**
	 * Return the type of the operation
	 * @return the type of the operation
	 */
	public int getType() {
		return type;
	}
	
}
