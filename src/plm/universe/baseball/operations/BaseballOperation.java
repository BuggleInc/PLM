package plm.universe.baseball.operations;

import plm.universe.Operation;
import plm.universe.baseball.BaseballEntity;


public class BaseballOperation extends Operation{

	public BaseballEntity entity;
	
	public BaseballOperation(String name, BaseballEntity entity) {
		super(name);
		this.entity = entity;
	}
	
	public BaseballEntity getEntity()
	{
		return entity;
	}
	
	

}
