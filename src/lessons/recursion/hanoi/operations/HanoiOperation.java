package lessons.recursion.hanoi.operations;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.universe.Operation;

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