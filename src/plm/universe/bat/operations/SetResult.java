package plm.universe.bat.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class SetResult extends BatOperation {
	
	private int index;
	@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
	private Object result;
	
	@JsonCreator
	public SetResult(@JsonProperty("index")int index, @JsonProperty("result")Object result) {
		super("setResult");
		this.index = index;
		this.result = result;
	}

	public int getIndex() {
		return index;
	}

	public Object getResult() {
		return result;
	}
}
