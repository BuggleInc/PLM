package plm.universe.turtles;

import java.awt.Color;

import org.xnap.commons.i18n.I18n;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import plm.core.model.json.CustomColorSerializer;

public class Circle implements Shape {
	public double x,y,radius;
	@JsonSerialize(using = CustomColorSerializer.class)
	public Color color;

	@JsonCreator
	public Circle(@JsonProperty("x")double x, @JsonProperty("y")double y, 
			@JsonProperty("radius")double radius, @JsonProperty("color")Color color) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
	}

	@Override
	public Shape copy() {
		return new Circle(x, y, radius, color);
	}
	private boolean doubleEqual(double a, double b) {
		return (Math.abs(a-b)<0.000001);
	}
	@Override
	public String diffTo(Shape o, I18n i18n) {
		if (o instanceof Circle) {
			Circle other = (Circle) o;
			if (!doubleEqual(x,other.x))
				return i18n.tr("x differs");
			if (!doubleEqual(y,other.y))
				return i18n.tr("y differs");
			if (!doubleEqual(radius,other.radius))
				return i18n.tr("radius differs");
			return i18n.tr("I dont see the difference");
		} else 
			return i18n.tr("That's not a line");
	}
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Circle))
			return false;
		
		Circle other = (Circle) obj;
		if (!doubleEqual(x,other.x))
			return false;
		if (!doubleEqual(y,other.y))
			return false;
		if (!doubleEqual(radius,other.radius))
			return false;
				
		return true;
	}

}
