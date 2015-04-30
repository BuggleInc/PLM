package lessons.sort.pancake.universe.operations;

import lessons.sort.pancake.universe.PancakeEntity;



public class FlipOperation extends PancakeOperation {
	
	private int number;
	
	public FlipOperation(PancakeEntity entity, int nb)
	{
		super("flipOperation", entity);
		this.number = nb;
	}
	
	public int getNumber()
	{
		return number;
	}

}
