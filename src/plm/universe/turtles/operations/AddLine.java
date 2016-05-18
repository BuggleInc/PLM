package plm.universe.turtles.operations;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomColorSerializer;

public class AddLine extends TurtleOperation {

	private double x1, y1, x2, y2;
	@JsonSerialize(using = CustomColorSerializer.class)
	private Color color;
	
	@JsonCreator
	public AddLine(@JsonProperty("turtleID")String turtleID,
			@JsonProperty("x1")double x1, @JsonProperty("y1")double y1,
			@JsonProperty("x2")double x2, @JsonProperty("y2")double y2,
			@JsonProperty("color")Color color) {
		super("addLine", turtleID);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
	}

	public double getX1() {
		return x1;
	}

	public double getY1() {
		return y1;
	}

	public double getX2() {
		return x2;
	}

	public double getY2() {
		return y2;
	}

	public Color getColor() {
		return color;
	}
}
