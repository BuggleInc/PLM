package plm.universe.pancake.operations;

import plm.universe.Operation;
import plm.universe.pancake.PancakeEntity;

public abstract class PancakeOperation extends Operation {

	private PancakeEntity entity ;
	
	public PancakeOperation(String name, PancakeEntity entity) {
		super(name);
		this.entity = entity;
	}
	
	public PancakeEntity getEntity()
	{
		return entity;
	}

}
