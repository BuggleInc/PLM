package plm.universe.sort.operations;

import plm.universe.sort.SortingEntity;


public class CopyOperation extends SortOperation {


	private int destination;
	private int source;
	private int oldValue;
	
	public CopyOperation(SortingEntity entity, int destination, int source) {
		super("copyOperation", entity);
		this.source = source;
		this.destination = destination;
	}

	public int getDestination() {
		return destination;
	}

	public int getSource() {
		return source;
	}
	
	public int getOldValue()
	{
		return this.oldValue;
	}
	
	

}
