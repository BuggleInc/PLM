package jlm.universe.turtles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
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
	}

	@Override
	public void reset(World w) {
		TurtleWorld initialWorld = (TurtleWorld)w;
		shapes = new ArrayList<ShapeAbstract>();
		this.height = initialWorld.height;
		this.width = initialWorld.width;

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
	public WorldView[] getView() {
		return new WorldView[] { new TurtleWorldView(this) };
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof TurtleWorld))
			return false;
		if (! super.equals(obj))
			return false;
		
		TurtleWorld other = (TurtleWorld) obj;
		if (shapes.size() != other.shapes.size())
			return false;
		Collections.sort(shapes, new ShapeComparator());
		Collections.sort(other.shapes, new ShapeComparator());
		for (int i=0;i<shapes.size();i++)
			if (! shapes.get(i).equals(other.shapes.get(i)))
				return false;
		
		return true;
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
	@Override
	public String getBindings(ProgrammingLanguage lang) {
		throw new RuntimeException("No binding of TurtleWorld for "+lang);
	}

	@Override
	public String diffTo(World world) {
		StringBuffer sb = new StringBuffer();
		TurtleWorld other = (TurtleWorld) world;
		if (shapes.size() != other.shapes.size())
			sb.append("  There is only "+other.shapes.size()+" lines where "+shapes.size()+" were expected.\n");
		for (int i=0;i<other.shapes.size();i++)
			if (! other.shapes.get(i).equals(shapes.get(i)))
				sb.append("  Got "+other.shapes.get(i)+" where "+shapes.get(i)+" were expected ("+
						((ShapeLine) other.shapes.get(i)).diffTo(shapes.get(i))+")\n");
		
		return sb.toString();
	}
}

class ShapeComparator implements Comparator<ShapeAbstract> {

	public ShapeComparator() {
		super();
	}

	@Override
	public int compare(ShapeAbstract o1, ShapeAbstract o2) {
		ShapeLine l1 = (ShapeLine) o1;
		ShapeLine l2 = (ShapeLine) o2;
		if (l1.x1 < l2.x1)
			return -1;
		if (l1.x1 > l2.x1)
			return 1;

		if (l1.x2 < l2.x2)
			return -1;
		if (l1.x2 > l2.x2)
			return 1;

		if (l1.y1 < l2.y1)
			return -1;
		if (l1.y1 > l2.y1)
			return 1;

		if (l1.y2 < l2.y2)
			return -1;
		if (l1.y2 > l2.y2)
			return 1;
		
		return 0;
	}
	
}