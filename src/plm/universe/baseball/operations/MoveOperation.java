package plm.universe.baseball.operations;

public class MoveOperation extends BaseballOperation {

	private int base;
	private int position;
	private int oldBase;
	private int oldPosition;

	public MoveOperation(int base, int position) {
		super("moveOperation");
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
