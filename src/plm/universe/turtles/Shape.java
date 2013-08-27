package plm.universe.turtles;

import java.awt.Graphics2D;

public interface Shape {
	public void draw(Graphics2D g);
	public Shape copy();
	public String diffTo(Shape o);
}
