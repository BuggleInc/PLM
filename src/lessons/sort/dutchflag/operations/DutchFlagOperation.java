package lessons.sort.dutchflag.operations;

import lessons.sort.dutchflag.universe.DutchFlagEntity;
import plm.universe.Operation;

public abstract class DutchFlagOperation extends Operation {

	private DutchFlagEntity entity;
	
	public DutchFlagOperation(String name, DutchFlagEntity entity) {
		super(name);
		this.entity = entity;
	}
	
	public DutchFlagEntity getEntity()
	{
		return entity;
	}

	
}
