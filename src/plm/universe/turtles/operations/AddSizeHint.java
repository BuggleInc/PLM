package plm.universe.turtles.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddSizeHint extends TurtleOperation {

	private int x1, y1, x2, y2;
	private String text;
	
	@JsonCreator
	public AddSizeHint(@JsonProperty("turtleID")String turtleID,
			@JsonProperty("x1")int x1, @JsonProperty("y1")int y1,
			@JsonProperty("x2")int x2, @JsonProperty("y2")int y2,
			@JsonProperty("text")String text) {
		super("addSizeHint", turtleID);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.text = text;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public String getText() {
		return text;
	}

}
