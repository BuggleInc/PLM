package plm.universe.baseball.operations;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MoveOperation extends BaseballOperation {

	private int base;
	private int position;

	@JsonCreator
	public MoveOperation(int base, int position) {
		super("moveOperation");
		this.base = base;
		this.position = position;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public void setPosition(int position) {
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
