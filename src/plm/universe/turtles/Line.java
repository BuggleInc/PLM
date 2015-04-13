package plm.universe.turtles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import org.xnap.commons.i18n.I18n;

import plm.core.model.Game;


public class Line implements Shape {
	public double x1, y1,  x2, y2;
	public Color color;
	private double length = -1;
	private Game game;
	
	public Line(double x1, double y1, double x2, double y2, Color color, Game game) {
		this.game = game;
		this.color = color;
		/* make sure that the first point of each segment is before the second point in comparison order */ 
		if (doubleEqual(x1, x2)) { // Don't check if x1<x2 before checking their approximate equality
			if (doubleEqual(y1,y2) || y1 < y2) { // already in right order
				this.x1 = x1;
				this.y1 = y1;
				this.x2 = x2;
				this.y2 = y2;				
			} else { // invert both sides
				this.x1 = x2;
				this.y1 = y2;
				this.x2 = x1;
				this.y2 = y1;
			}
		} else if (x1<x2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;				
		} else {
			this.x1 = x2;
			this.y1 = y2;
			this.x2 = x1;
			this.y2 = y1;			
		}
	}
	
	public void draw(Graphics2D g){
		g.setColor(color);
		g.draw(new Line2D.Double(x1,y1,x2,y2));
	}

	public Line copy() {
		return new Line(x1,y1,x2,y2,color, game);
	}
	public static boolean doubleEqual(double a, double b) {
		return (Math.abs(a-b)<0.01);
	}
	public static boolean doubleApprox(double a, double b) {
		return (Math.abs(a-b)<1);
	}
	public static boolean doubleWithin(double a, double min, double max) {
		double MIN = Math.min(min,max); // We need to sort min and max because y coordinates can be out of order
		double MAX = Math.max(min,max);
		return (a > MIN && a < MAX) || doubleEqual(a, MIN) || doubleEqual(a, MAX);
	}
	public boolean sameSlope(Line other) {
		if (doubleApprox(x1, x2) && doubleApprox(other.x1, other.x2)) // Both are vertical, same infinite slope
			return true;
		
		if (doubleApprox(x1, x2) || doubleApprox(other.x1, other.x2)) // one is vertical (but not both, given above test)
			return false;
		
		// none is vertical, actually compute and compare the slopes
		return doubleEqual( (y2-y1) / (x2-x1) , (other.y2-other.y1) / (other.x2-other.x1) );
	}
	public boolean sameRoot(Line other) {
		if (doubleApprox(y1, y2) && doubleApprox(other.y1, other.y2)) // Both are horizontal, none of the roots are defined
			return true;
		if (doubleApprox(y1, y2) || doubleApprox(other.y1, other.y2)) // One of them have no root
			return false;
		// let's compute the roots that do exist, and compare them
		double root = (x2*y1 - x1*y2) / (x2-x1);
		double otherRoot = (other.x2*other.y1 - other.x1*other.y2) / (other.x2-other.x1);
		return doubleEqual(root, otherRoot);
	}
	public double getLength() {
		if (length == -1)
			length = Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) );
		return length;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Line))
			return false;
		
		Line other = (Line) obj;
		if (!doubleEqual(x1,other.x1))
			return false;
		if (!doubleEqual(x2,other.x2))
			return false;
		if (!doubleEqual(y1,other.y1))
			return false;
		if (!doubleEqual(y2,other.y2))
			return false;
		
		return color.equals(other.color);
	}
	public String diffTo(Shape o, I18n i18n) {
		if (o instanceof Line) {
			Line other = (Line) o;
			if (!doubleEqual(x1,other.x1))
				return i18n.tr("x1 differs.");
			if (!doubleEqual(x2,other.x2))
				return i18n.tr("x2 differs.");
			if (!doubleEqual(y1,other.y1))
				return i18n.tr("y1 differs.");
			if (!doubleEqual(y2,other.y2))
				return i18n.tr("y2 differs.");
			if (!color.equals(other.color))
				return i18n.tr("The color differs.");
			return i18n.tr("I dont see the difference (please report this bug).");
		} else 
			return i18n.tr("That's not a line (please report this bug).");
	}
	
	@Override
	public String toString(){
		String slope = "";
		if (game.isDebugEnabled()) {
			if (doubleApprox(x1,x2))
				slope = "slope=infty";
			else
				slope = "slope="+ ((y2-y1) / (x2-x1));
		}
		return String.format("Line (x%.3f y%.3f / x%.3f y%.3f / %s) %s", x1,y1,x2,y2,plm.core.utils.ColorMapper.color2name(color),slope);
	}
}
