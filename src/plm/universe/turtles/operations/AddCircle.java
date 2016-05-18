package plm.universe.turtles.operations;

import java.awt.Color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomColorSerializer;

public class AddCircle extends TurtleOperation {

	private double x, y, radius;
	@JsonSerialize(using = CustomColorSerializer.class)
	private Color color;
	
	@JsonCreator
	public AddCircle(@JsonProperty("turtleID")String turtleID,
			@JsonProperty("x")double x, @JsonProperty("y")double y,
			@JsonProperty("radius")double radius,
			@JsonProperty("color")Color color) {
		super("addCircle", turtleID);
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRadius() {
		return radius;
	}

	public Color getColor() {
		return color;
	}
	
}
