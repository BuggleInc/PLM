package plm.universe.hanoi.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HanoiMove extends HanoiOperation{
	
	private int source;
	private int destination;
	
	@JsonCreator
	public HanoiMove(@JsonProperty("source")int src, @JsonProperty("destination")int dest) {
		super("hanoiMove");
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

	public void setSource(int source) {
		this.source = source;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}
}