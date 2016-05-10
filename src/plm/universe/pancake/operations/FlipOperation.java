package plm.universe.pancake.operations;

public class FlipOperation extends PancakeOperation {

	private int number;
	private int oldNumber;

	public FlipOperation(int nb, int old)
	{
		super("flipOperation");
		this.number = nb;
		this.oldNumber = old;
	}

	public int getNumber()
	{
		return number;
	}

	public int getOldNumber()
	{
		return oldNumber;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setOldNumber(int oldNumber) {
		this.oldNumber = oldNumber;
	}
}
