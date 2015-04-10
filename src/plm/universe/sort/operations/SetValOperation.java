package plm.universe.sort.operations;

import plm.universe.sort.SortingEntity;


public class SetValOperation extends SortOperation {

	private int value;
	private int oldValue;
	
	public SetValOperation(SortingEntity entity, int value, int old) {
		super("setValOperation", entity);
		this.value = value;
		this.oldValue = old;
		
	}

	public int getValue() {
		return value;
	}
	
	public int getOldValue()
	{
		return this.oldValue;
	}
	
	
	
}
