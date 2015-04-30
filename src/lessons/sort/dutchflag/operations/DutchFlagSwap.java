package lessons.sort.dutchflag.operations;

import lessons.sort.dutchflag.universe.DutchFlagEntity;

public class DutchFlagSwap extends DutchFlagOperation  {

	private int destination;
	private int source;
	
	public DutchFlagSwap(DutchFlagEntity entity, int source, int destination) {
		super("dutchFlagSwap", entity);
		this.source = source;
		this.destination = destination;
	}
	
	public int getDestination()
	{
		return destination;
	}
	
	public int getSource()
	{
		return source;
	}
	

}
