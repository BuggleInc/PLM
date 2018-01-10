package plm.universe.sort.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class GetValueOperation extends SortOperation 
{
	private int position;

	@JsonCreator
	public GetValueOperation(@JsonProperty("position")int position) {
		super("getValueOperation");
		this.position = position;
	}	

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
