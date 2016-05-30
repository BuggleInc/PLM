package plm.universe.sort.operations;

import plm.universe.sort.SortingEntity; ;

public class CountOperation extends SortOperation
{
	private int read;
	private int write;
	private int oldRead;
	private int oldWrite;
	
	

	public CountOperation(SortingEntity entity, int read, int write)
	{
		super("countOperation", entity);
		this.read = read;
		this.write = write;
	}
	
	public int getRead()
	{
		return read;
	}
	
	public int getWrite()
	{
		return write;
	}
	
	public int getOldRead() {
		return oldRead;
	}

	public int getOldWrite() {
		return oldWrite;
	}
}
