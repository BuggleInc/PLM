package lessons.sort.pancake.universe.operations;

import lessons.sort.pancake.universe.PancakeEntity;
import plm.universe.Operation;

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
