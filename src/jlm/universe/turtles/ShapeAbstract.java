package jlm.universe.turtles;

import java.awt.Graphics2D;

public abstract class ShapeAbstract {
	public abstract void draw(Graphics2D g);
	public abstract ShapeAbstract copy();
}
