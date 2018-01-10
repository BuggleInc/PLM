package plm.universe.sort.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class SwapOperation extends SortOperation{

	private int destination;
	private int source;

	@JsonCreator
	public SwapOperation(@JsonProperty("destination")int destination, @JsonProperty("source")int source) {
		super("swapOperation");
		this.source = source;
		this.destination = destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public int getSource() {
		return source;
	}
}
