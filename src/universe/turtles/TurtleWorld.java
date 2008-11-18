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
	public TurtleWorld(TurtleWorld world2) {
		super(world2);
		this.height = world2.height;
		this.width = world2.width;
		for (ShapeAbstract s:world2.shapes)
			shapes.add(s.copy());
	}

	@Override
	public World copy() {
		return new TurtleWorld(this);
	}
	@Override
	public void reset(World w) {
		TurtleWorld initialWorld = (TurtleWorld)w;
		shapes.clear();
		Iterator<ShapeAbstract> it = initialWorld.shapes();
		while (it.hasNext()) 
			shapes.add(it.next().copy());
		
		super.reset(w);
		
	}

	
	public void addLine(double x, double y, double newX, double newY, Color color) {
		ShapeLine line =new ShapeLine(width/2+x,height/2+y,width/2+newX,height/2+newY,color); 
		shapes.add(line);
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
	@Override
	public String toString(){
		String res = "TurtleWorld: name="+getName()+", size="+width+"x"+height+", shapes=[";
		Iterator<ShapeAbstract> it = shapes();
		while (it.hasNext()) 
			res += it.next().toString();
		res += "]";
		return res;
	}
}
