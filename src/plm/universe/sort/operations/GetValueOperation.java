package plm.universe.sort.operations;

import plm.universe.sort.SortingEntity;;

public class GetValueOperation extends SortOperation 
{
	private int position;
	
	public GetValueOperation(SortingEntity entity, int pos) {
		super("getValueOperation", entity);
		this.position = pos;
	}	
	
	public int getPosition()
	{
		return position;
	}
	

}
