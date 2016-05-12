package plm.universe.pancake.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlipOperation extends PancakeOperation {

	private int number;
	private int oldNumber;

	@JsonCreator
	public FlipOperation(@JsonProperty("number")int nb, @JsonProperty("oldNumber")int old)
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
