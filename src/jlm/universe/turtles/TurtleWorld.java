package jlm.universe.turtles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

public class TurtleWorld extends World {

	ArrayList<Line> shapes = new ArrayList<Line>(); 

	private double width;
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
		shapes = new ArrayList<Line>();
		this.height = initialWorld.height;
		this.width = initialWorld.width;

		Iterator<Line> it = initialWorld.shapes();
		while (it.hasNext()) 
			shapes.add(it.next().copy());
		
		super.reset(w);		
	}
	
	public void addLine(double x, double y, double newX, double newY, Color color) {
		synchronized (shapes) {
			//ShapeLine line =new ShapeLine(width/2+x,height/2+y,width/2+newX,height/2+newY,color); 
			Line line =new Line(x,y,newX,newY,color); 
			shapes.add(line);
			notifyWorldUpdatesListeners();
		}
	}
	public Iterator<Line> shapes() {
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
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("img/world_turtle.png");
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
		Collections.sort(shapes, new LineComparator());
		Collections.sort(other.shapes, new LineComparator());
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
		Iterator<Line> it = shapes();
		while (it.hasNext()) 
			res += it.next().toString();
		res += "]";
		return res;
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			e.put("Color", Color.class);
			e.eval( "def backward(i):\n"+
					"  entity.backward(i)\n"+
					"def forward(i):\n"+
					"  entity.forward(i)\n"+
					"def penUp():\n"+
					"  entity.penUp()\n"+
					"def penDown():\n"+
					"  entity.penDown()\n"+
					"def isPenDown():\n"+
					"  return entity.isPenDown()\n"+
					"def left(i):\n"+
					"  entity.left(i)\n"+
					"def right(i):\n"+
					"  entity.right(i)\n"+
					"def setColor(c):\n"+
					"  entity.setColor(c)\n" +
					"def getColor():\n"+
					"  return entity.getColor()\n" +
					"def setPos(x,y):\n" +
					"  entity.setPos(x,y)\n" +
					"def setX(x):\n" +
					"  entity.setX(x)\n" +
					"def setY(y):\n" +
					"  entity.setY(y)\n" +
					"def getX():\n" +
					"  return entity.getX()\n" +
					"def getY():\n" +
					"  entity.getY()\n" +
					"def moveTo(x,y):\n" +
					"  entity.moveTo(x,y)\n"+
					/* BINDINGS TRANSLATION: French */
					"def recule(i):\n"+
					"  entity.backward(i)\n"+
					"def avance(i):\n"+
					"  entity.forward(i)\n"+
					"def leveCrayon():\n"+
					"  entity.penUp()\n"+
					"def baisseCrayon():\n"+
					"  entity.penDown()\n"+
					"def estCrayonBaisse():\n"+
					"  return entity.isPenDown()\n"+
					"def gauche(i):\n"+
					"  entity.left(i)\n"+
					"def droite(i):\n"+
					"  entity.right(i)\n"+
					"def setCouleur(c):\n"+
					"  entity.setColor(c)\n" +
					"def allerVers(x,y):\n" +
					"  entity.moveTo(x,y)\n"
					);
		} else {
			throw new RuntimeException("No binding of TurtleWorld for "+lang);
		}
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
						((Line) other.shapes.get(i)).diffTo(shapes.get(i))+")\n");
		
		return sb.toString();
	}
}

class LineComparator implements Comparator<Line> {

	public LineComparator() {
		super();
	}

	@Override
	public int compare(Line l1, Line l2) {
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