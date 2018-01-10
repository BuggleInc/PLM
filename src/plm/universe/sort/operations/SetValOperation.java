package plm.universe.sort.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class SetValOperation extends SortOperation {

	private int value;
	private int oldValue;
	private int position;

	@JsonCreator
	public SetValOperation(@JsonProperty("value")int value, @JsonProperty("position")int position) {
		super("setValOperation");
		this.value = value;
		this.position = position;
		
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setOldValue(int oldValue) {
		this.oldValue = oldValue;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getOldValue() {
		return oldValue;
	}

	public int getPosition()
	{
		return position;
	}
}
