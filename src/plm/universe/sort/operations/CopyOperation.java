package plm.universe.sort.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CopyOperation extends SortOperation {

	private int destination;
	private int source;
	private int oldValue;

	@JsonCreator
	public CopyOperation(@JsonProperty("destination")int destination, @JsonProperty("source")int source) {
		super("copyOperation");
		this.source = source;
		this.destination = destination;
	}

	public int getDestination() {
		return destination;
	}

	public int getSource() {
		return source;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getOldValue() {
		return oldValue;
	}

	public void setOldValue(int oldValue) {
		this.oldValue = oldValue;
	}
}
