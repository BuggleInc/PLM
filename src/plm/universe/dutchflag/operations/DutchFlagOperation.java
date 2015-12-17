package plm.universe.dutchflag.operations;

import plm.universe.Operation;
import plm.universe.dutchflag.DutchFlagEntity;

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
