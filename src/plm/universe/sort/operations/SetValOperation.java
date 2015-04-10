package plm.universe.sort.operations;

import plm.universe.sort.SortingEntity;


public class SetValOperation extends SortOperation {

	private int value;
	private int oldValue;
	private int position;
	
	public SetValOperation(SortingEntity entity, int value, int p) {
		super("setValOperation", entity);
		this.value = value;
		this.position = p;
		
	}

	public int getValue() {
		return value;
	}

	public int getOldValue() {
		return oldValue;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	
	
}
