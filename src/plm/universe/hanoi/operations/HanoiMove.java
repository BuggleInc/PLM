package plm.universe.hanoi.operations;

import plm.universe.hanoi.HanoiEntity;

public class HanoiMove extends HanoiOperation{
	
	private int source;
	private int destination;
	
	public HanoiMove(HanoiEntity entity, int src, int dest) {
		super("hanoiMove", entity);
		this.source = src;
		this.destination = dest;
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