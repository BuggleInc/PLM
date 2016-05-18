package plm.universe.turtles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class SizeHint {
	public double x1, y1,  x2, y2;
	public String text;

	@JsonCreator
	public SizeHint(@JsonProperty("x1")double x1, @JsonProperty("y1")double y1,
			@JsonProperty("x2")double x2, @JsonProperty("y2")double y2,
			@JsonProperty("msg")String msg) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		text = msg;
	}
}
