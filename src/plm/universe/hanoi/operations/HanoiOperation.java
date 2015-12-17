package plm.universe.hanoi.operations;

import plm.universe.Operation;
import plm.universe.hanoi.HanoiEntity;

public class HanoiOperation extends Operation {
	
	private HanoiEntity entity;
	
	public HanoiOperation(String name, HanoiEntity entity) {
		super(name);
		this.entity = entity;
	}
	
	public HanoiEntity getEntity()
	{
		return entity;
	}
}