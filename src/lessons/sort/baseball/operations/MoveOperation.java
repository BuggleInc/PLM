package lessons.sort.baseball.operations;

import lessons.sort.baseball.universe.BaseballEntity;

public class MoveOperation extends BaseballOperation {

	private int base;
	private int position;
	
	public MoveOperation(BaseballEntity entity, int base, int position) {
		super("moveOperation", entity);
		this.base = base;
		this.position = position;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public int getBase()
	{
		return base;
	}

}
