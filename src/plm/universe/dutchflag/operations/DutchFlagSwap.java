package plm.universe.dutchflag.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DutchFlagSwap extends DutchFlagOperation  {

	private int destination;
	private int source;
	
	@JsonCreator
	public DutchFlagSwap(@JsonProperty("source")int source, @JsonProperty("destination")int destination) {
		super("dutchFlagSwap");
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

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public void setSource(int source) {
		this.source = source;
	}
}
