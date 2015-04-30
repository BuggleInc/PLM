package plm.universe.sort.operations;

import plm.universe.Operation;
import plm.universe.sort.SortingEntity;

public abstract class SortOperation extends Operation
{
	private SortingEntity entity;
	
	public SortOperation(String name, SortingEntity entity) {
		super(name);
		this.entity = entity;
	}
	
	public SortingEntity getEntity()
	{
		return entity;
	}

}
