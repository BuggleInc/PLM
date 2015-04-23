package lessons.sort.baseball.operations;

import lessons.sort.baseball.universe.BaseballEntity;
import plm.universe.Operation;


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
