package plm.universe.turtles;

import java.awt.Graphics2D;

import org.xnap.commons.i18n.I18n;

public interface Shape {
	public void draw(Graphics2D g);
	public Shape copy();
	public String diffTo(Shape o, I18n i18n);
}
