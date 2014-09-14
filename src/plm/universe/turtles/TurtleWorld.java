package plm.universe.turtles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.Entity;
import plm.universe.EntityControlPanel;
import plm.universe.World;

public class TurtleWorld extends World {

	ArrayList<Shape> shapes = new ArrayList<Shape>();
	ArrayList<SizeHint> sizeHints = new ArrayList<SizeHint>();

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
	
	public TurtleWorld(TurtleWorld original) {
		super(original);
		sizeHints = new ArrayList<SizeHint>();
		for (SizeHint sh :original.sizeHints)
			sizeHints.add(sh);
		for (Entity e: getEntities()){
			Turtle t = (Turtle) e;
			t.startX = t.getX();
			t.startY = t.getY();
		}
	}

	@Override
	public void reset(World w) {
		TurtleWorld initialWorld = (TurtleWorld)w;
		shapes = new ArrayList<Shape>();
		this.height = initialWorld.height;
		this.width = initialWorld.width;

		Iterator<Shape> it = initialWorld.shapes();
		while (it.hasNext()) 
			shapes.add(it.next().copy());
		
		sizeHints = new ArrayList<SizeHint>();
		for (SizeHint sh : initialWorld.sizeHints)
			sizeHints.add(sh);
		
		super.reset(w);		
	}
	
	public void addSizeHint(int x1, int y1, int x2, int y2) {
		addSizeHint(x1, y1, x2, y2, null);
	}
	public void addSizeHint(int x1, int y1, int x2, int y2, String text) {
		synchronized (sizeHints) {
			sizeHints.add(new SizeHint(x1, y1, x2, y2, text));
			notifyWorldUpdatesListeners();
		}
	}
	public void addLine(double x, double y, double newX, double newY, Color color) {
		synchronized (shapes) {
			shapes.add(new Line(x,y,newX,newY,color));
			notifyWorldUpdatesListeners();
		}
	}
	public void addCircle(double x, double y, double radius, Color color) {
		synchronized (shapes) {
			shapes.add(new Circle(x,y,radius,color));
			notifyWorldUpdatesListeners();
		}
	}
	public void clear() {
		synchronized (shapes) {
			shapes.clear();
			notifyWorldUpdatesListeners();
		}
	}

	public Iterator<Shape> shapes() {
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
		
	// TODO implement world IO	
	
	@Override
	public String toString(){
		String res = "TurtleWorld: name="+getName()+
			", size="+width+"x"+height+
			", parameters: " +parameters+
			", shapes=[";
		synchronized (shapes) {
			Iterator<Shape> it = shapes();
			while (it.hasNext()) 
				res += it.next().toString();
		}
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
					"def addSizeHint(x,y,z,t):\n"+
					"  entity.addSizeHint(x,y,z,t)\n"+
					"def circle(radius):\n"+
					"  entity.circle(radius)\n"+
					"def clear():\n"+
					"  entity.clear()\n"+
					"def hide():\n"+
					"  entity.hide()\n"+
					"def show():\n"+
					"  entity.show()\n"+
					"def isVisible():\n"+
					"  return entity.isVisible()\n"+
					/* BINDINGS TRANSLATION: French */
					"def recule(i):\n"+
					"  backward(i)\n"+
					"def avance(i):\n"+
					"  forward(i)\n"+
					"def leveCrayon():\n"+
					"  penUp()\n"+
					"def baisseCrayon():\n"+
					"  penDown()\n"+
					"def estCrayonBaisse():\n"+
					"  return isPenDown()\n"+
					"def gauche(i):\n"+
					"  left(i)\n"+
					"def droite(i):\n"+
					"  right(i)\n"+
					"def setCouleur(c):\n"+
					"  setColor(c)\n" +
					"def allerVers(x,y):\n" +
					"  moveTo(x,y)\n"+
					"def cercle(radius):\n"+
					"  circle(radius)\n"+
					"def efface():\n"+
					"  clear()\n"+
					"def cache():\n"+
					"  hide()\n"+
					"def montre():\n"+
					"  show()\n"+
					"def estVisible():\n"+
					"  return isVisible()\n"
					);
		} else {
			throw new RuntimeException("No binding of TurtleWorld for "+lang);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof TurtleWorld))
			return false;
		
		TurtleWorld other = (TurtleWorld) obj;
		if (other.entities.size() != entities.size())
			return false;
		for (int i=0; i<other.entities.size();i++)
			if (! other.entities.get(i).equals(entities.get(i)))
				return false;
		synchronized (shapes) { synchronized (other.shapes) {
			if (shapes.size() != other.shapes.size())
				return false;
			ShapeComparator cmp = new ShapeComparator();
			Collections.sort(shapes, cmp);
			Collections.sort(other.shapes, cmp);
			for (int i=0;i<other.shapes.size();i++)
				if (! other.shapes.get(i).equals(shapes.get(i)))
					return false;
		}}		
		return true;
	}
	@Override
	public String diffTo(World world) {
		StringBuffer sb = new StringBuffer();
		TurtleWorld other = (TurtleWorld) world;
		if (other.entities.size() != entities.size())
			return Game.i18n.tr("  There is {0} entities, but {1} entities were expected\n",other.entities.size(),entities.size());;
		for (int i=0; i<other.entities.size();i++)
			if (! other.entities.get(i).equals(entities.get(i)))
				sb.append(((Turtle) other.entities.get(i)).diffTo(entities.get(i)));
		synchronized (shapes) { synchronized (other.shapes) {
			if (shapes.size() != other.shapes.size())
				return Game.i18n.tr("  There is {0} shapes, but {1} shapes were expected\n",other.shapes.size(),shapes.size());
			ShapeComparator cmp = new ShapeComparator();
			Collections.sort(shapes, cmp);
			Collections.sort(other.shapes, cmp);
			for (int i=0;i<other.shapes.size();i++)
				if (! other.shapes.get(i).equals(shapes.get(i)))
					sb.append(Game.i18n.tr("  {0} (got {1} instead of {2})\n",
							((Shape) other.shapes.get(i)).diffTo(shapes.get(i)),
							other.shapes.get(i),shapes.get(i)));
		} }
		return sb.toString();
	}
}

class ShapeComparator implements Comparator<Shape> {

	public ShapeComparator() {
		super();
	}

	int cmp(double a, double b) {
		if (Math.abs(a-b)<0.001)
			return 0;
		if (a<b)
			return 1;
		return -1;
	}
	
	@Override
	public int compare(Shape s1, Shape s2) {
		if (s1 instanceof Line && s2 instanceof Circle)
			return -1;
		if (s1 instanceof Circle && s2 instanceof Line)
			return 1;
		
		if (s1 instanceof Line) {
			Line l1 = (Line) s1;
			Line l2 = (Line) s2;
			
			int res = cmp(l1.x1, l2.x1);
			if (res != 0)
				return res;
			
			res = cmp(l1.y1, l2.y1);
			if (res != 0)
				return res;
			
			res = cmp(l1.x2, l2.x2);
			if (res != 0)
				return res;

			return cmp(l1.y2, l2.y2);
		}
		
		if (s1 instanceof Circle) {
			Circle c1 = (Circle) s1;
			Circle c2 = (Circle) s2;
		
			int res = cmp(c1.x, c2.x);
			if (res != 0)
				return res;
			
			res = cmp(c1.y, c2.y);
			if (res != 0)
				return res;
			
			res = cmp(c1.radius, c2.radius);
			if (res != 0)
				return res;
		}
		return 0;
	}
	
}
