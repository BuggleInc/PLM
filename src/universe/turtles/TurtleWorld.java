package universe.turtles;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jlm.ui.WorldView;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import universe.EntityControlPanel;
import universe.World;
import universe.turtles.ui.TurtleButtonPanel;
import universe.turtles.ui.TurtleWorldView;

public class TurtleWorld extends World {

	@ElementList
	private ArrayList<ShapeAbstract> shapes = new ArrayList<ShapeAbstract>(); 

	@Attribute
	private double width;
	@Attribute
	private double height;
	
	public TurtleWorld(String name) {
		super(name);
	}

	public TurtleWorld(String name, int width, int height) {
		super(name);
		this.width = width;
		this.height = height;
	}

	@Override
	public World copy() {
		TurtleWorld res = new TurtleWorld(this.getName());
		for (ShapeAbstract s:shapes) {
			if (s instanceof ShapeLine) {
				ShapeLine l = (ShapeLine)s;
				res.addLine(l.x1, l.y1, l.x2, l.y2, l.color);
			}
			throw new RuntimeException("Unknown shape:"+s);
		}
		return res;
	}

	
	public void addLine(double x, double y, double newX, double newY, Color color) {
		shapes.add(new ShapeLine(x,y,newX,newY,color));
	}
	public Iterator<ShapeAbstract> shapes() {
		return shapes.iterator();
	}
	

	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
	}
	
	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new TurtleButtonPanel();
	}

	@Override
	public WorldView getView() {
		return new TurtleWorldView(this);
	}

	@Override
	public void readFromFile(BufferedReader br) throws IOException {
		// TODO Raccord de méthode auto-généré
	}

	@Override
	public void writeToFile(BufferedWriter f) throws IOException {
		// TODO Raccord de méthode auto-généré

	}
}
