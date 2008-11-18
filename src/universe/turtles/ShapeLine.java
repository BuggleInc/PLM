package universe.turtles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import org.simpleframework.xml.Attribute;

public class ShapeLine extends ShapeAbstract {
	@Attribute
	public double x1, y1,  x2, y2;
	@Attribute
	public Color color;
	
	public ShapeLine(double x1, double y1, double x2, double y2, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
	}
	
	@Override
	public void draw(Graphics2D g){
		g.setColor(color);
		g.draw(new Line2D.Double(x1,y1,x2,y2));
	}
}
