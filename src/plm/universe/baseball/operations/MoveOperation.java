package plm.universe.baseball.operations;

import plm.universe.baseball.BaseballEntity;

public class MoveOperation extends BaseballOperation {

	private int base;
	private int position;
	private int oldBase;
	private int oldPosition;

	
	public MoveOperation(BaseballEntity entity, int base, int position) {
		super("moveOperation", entity);
		this.base = base;
		this.position = position;
	}
	

	public int getOldBase() {
		return oldBase;
	}

	public int getOldPosition() {
		return oldPosition;
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
