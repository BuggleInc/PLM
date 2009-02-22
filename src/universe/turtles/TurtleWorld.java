package universe.turtles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import jlm.ui.WorldView;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;


public class TurtleWorld extends World {

	@ElementList 
	ArrayList<ShapeAbstract> shapes = new ArrayList<ShapeAbstract>(); 

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
		if (world2.parameters != null)
			parameters = world2.parameters;
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
		synchronized (shapes) {
			//ShapeLine line =new ShapeLine(width/2+x,height/2+y,width/2+newX,height/2+newY,color); 
			ShapeLine line =new ShapeLine(x,y,newX,newY,color); 
			shapes.add(line);
			notifyWorldUpdatesListeners();
		}
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
	

	// TODO implement world IO	
	
	@Override
	public String toString(){
		String res = "TurtleWorld: name="+getName()+
			", size="+width+"x"+height+
			", parameters: " +parameters+
			", shapes=[";
		Iterator<ShapeAbstract> it = shapes();
		while (it.hasNext()) 
			res += it.next().toString();
		res += "]";
		return res;
	}

	protected Object[] parameters=null;
	public void setParameter(Object[] parameters) {
		this.parameters = parameters;		
	}
}
