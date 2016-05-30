package plm.universe.sort.operations;

import plm.universe.sort.SortingEntity;


public class SwapOperation extends SortOperation{

	private int destination;
	private int source;
	
	public SwapOperation(SortingEntity entity, int destination, int source) {
		super("swapOperation", entity);
		this.source = source;
		this.destination = destination;
	}

	public int getDestination() {
		return destination;
	}

	public int getSource() {
		return source;
	}
	


}
