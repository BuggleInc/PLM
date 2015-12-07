package plm.universe.turtles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.Entity;
import plm.universe.World;

public class TurtleWorld extends World {

	ArrayList<Shape> shapes = new ArrayList<Shape>();
	ArrayList<SizeHint> sizeHints = new ArrayList<SizeHint>();

	private double width;
	private double height;
	
	public TurtleWorld(Game game, String name) {
		super(game, name);
	}

	public TurtleWorld(Game game, String name, int width, int height) {
		super(game, name);
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
			shapes.add(new Line(x,y,newX,newY,color, getGame()));
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
	
	public Iterator<SizeHint> sizeHints() {
		return sizeHints.iterator();
	}

	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
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
		if (lang instanceof LangPython) {
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
		if (this == obj)
			return true;
		if (! (obj instanceof TurtleWorld))
			return false;
		
		TurtleWorld other = (TurtleWorld) obj;
		if (!other.getName().equals(getName()))
			return false;
		String diff = diffTo(other, I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages", new Locale("en"), I18nFactory.FALLBACK));
		if (diff.equals(""))
			return true;
		return false;
	}
	
	
	// Merge the lines that are lengthening each others
	private boolean mergeLengthening(ArrayList<Shape> shapes) {
		boolean changedSomething = false;

		// FIXME: this code seems rather suboptimal, but I prefer to be safe than sorry. 
		//
		// I think that we could do less tests, but I prefer to leave them all so that all duplicates are actually catched
		// In practice, it seems that case 2 (L2 after L1) never occurs, so we could kill it.
		// 
		// Since extremities are sorted for each line (so p1 < p2 in each line), 
		//  and since shapes are sorted before entering that function, 
		//  then I guess that we could have (j <- 0 to i), stopping at i instead of j, and killing the case 2 (l2 after l1).
		//
		// But I prefer not to do that before someone better in geometry than me thinks about it.
		
		for (int i=0;i<shapes.size();i++) {
			if (shapes.get(i) instanceof Line) {

				for (int j=0;j<shapes.size();j++) {
					if (i!=j && shapes.get(j) instanceof Line) {
						Line l1 = (Line) shapes.get(i);
						Line l2 = (Line) shapes.get(j);
						if (l1.sameSlope(l2)) { // We cannot have inverted slopes because the extremities within a line are sorted
							if ( (Line.doubleEqual(l1.x1, l2.x1) && Line.doubleEqual(l1.y1, l2.y1)) || // Same start. Keep the longer
									(Line.doubleEqual(l1.x2, l2.x2) && Line.doubleEqual(l1.y2, l2.y2)) ) { // Same end. Keep the longer
								int rmIdx;
								if (l1.getLength()>l2.getLength()) {
									rmIdx = j;
//									getGame().getLogger().log("1a: Kill "+shapes.get(j)+" because of "+shapes.get(i));
								} else {
									rmIdx = i;
//									getGame().getLogger().log("1b: Kill "+shapes.get(i)+" because of "+shapes.get(j));
								}
								shapes.remove(rmIdx);
								if (i>=rmIdx && rmIdx>0)
									i--;
								if (j>=rmIdx && rmIdx>0)
									j--;
								changedSomething = true;
							} else if (Line.doubleEqual(l1.x1, l2.x2) && Line.doubleEqual(l1.y1, l2.y2)) {
								// l1 and l2 are aligned, and l2 is after l1. Modify end of l1 and kill l2
//								System.out.print("2: "+l2+" is after "+l1+".");
								l1.x1 = l2.x1;
								l1.y1 = l2.y1;
//								getGame().getLogger().log(" New l1: "+l1);
								
								if (i>=j && i>0)
									i--;
								shapes.remove(j);
								if (j>0)
									j--;
								changedSomething = true;
							} else if (Line.doubleEqual(l1.x2, l2.x1) && Line.doubleEqual(l1.y2, l2.y1)) {
								// l1 and l2 are aligned, and l1 is after l2. Modify start of l1 and kill l2 
//								System.out.print("3: "+l2+" is before "+l1+".");
								l1.x2 = l2.x2;
								l1.y2 = l2.y2;
//								getGame().getLogger().log(" New l1: "+l1);
								if (i>=j  && j>0)
									i--;
								shapes.remove(j);
								if (j>0)
									j--; 
								changedSomething = true;
							} else if (l1.sameRoot(l2)) {
								// The lines are parallel and on the same ray. Check if they are intersecting
								if (Line.doubleWithin(l1.x1, l2.x1, l2.x2) && Line.doubleWithin(l1.x2, l2.x1, l2.x2) &&
									Line.doubleWithin(l1.y1, l2.y1, l2.y2) && Line.doubleWithin(l1.y2, l2.y1, l2.y2)) {
									// l1 is strictly within l2 (we don't test on y because we have the same slope & root, and extremities are sorted)
									if (j>=i  && i>0)
										j--;
									shapes.remove(i);
									if (i>0)
										i--;
									changedSomething = true;
								} else if (Line.doubleWithin(l2.x1, l1.x1, l1.x2) && Line.doubleWithin(l2.x2, l1.x1, l1.x2) &&
										   Line.doubleWithin(l2.y1, l1.y1, l1.y2) && Line.doubleWithin(l2.y2, l1.y1, l1.y2) ) {
									// l2 is strictly within l1
									if (i>=j  && j>0)
										i--;
									shapes.remove(j);
									if (j>0)
										j--;
									changedSomething = true;
								} /*else if (!Line.doubleWithin(l1.x1, l2.x1, l2.x2) && !Line.doubleWithin(l1.x2, l2.x1, l2.x2)) {
									// No point of l1 is within l2. They are disjoint, nothing to do
								} else if (!Line.doubleWithin(l2.x1, l1.x1, l1.x2) && !Line.doubleWithin(l2.x2, l1.x1, l1.x2)) {
									// No point of l2 is within l1. They are disjoint, nothing to do
								} else if (Line.doubleWithin(l1.x1, l2.x1, l2.x2) && Line.doubleWithin(l2.x1, l1.x1, l1.x2)) {
									// if l1=(ab) and l2=(cd), we have the points in that order a,c,b,d or c,a,d,b
									// FIXME: that's not sorted out yet...
								}*/
							}
						} // not same slope, certainly not lenghtening each other
					} // j is not a shape
				} // for all j
			} // i is not a shape
		} // for all i
		return changedSomething;
	}
	private boolean killDuplicate(ArrayList<Shape> shapes) {
		boolean changedSomething = false;
		
		for (int i=0;i<shapes.size()-1;i++) 
			if (shapes.get(i).equals(shapes.get(i+1))) {
				changedSomething  = true;
				shapes.remove(i+1);
				i--; // counters the effect of next i++ in the for loop
			}
		return changedSomething;
	}
	
	@Override
	public String diffTo(World world, I18n i18n) {
		if (world == this)
			return "";
		StringBuffer sb = new StringBuffer();
		TurtleWorld other = (TurtleWorld) world;
		
		// First compare entities
		if (other.entities.size() != entities.size())
			return i18n.tr("  There is {0} entities, but {1} entities were expected\n",other.entities.size(),entities.size());;
		for (int i=0; i<other.entities.size();i++)
			if (! other.entities.get(i).equals(entities.get(i)))
				sb.append(((Turtle) other.entities.get(i)).diffTo(entities.get(i), i18n));
		
		// Compare shapes
		synchronized (shapes) { synchronized (other.shapes) {
			ShapeComparator cmp = new ShapeComparator();
			
			// Sort shapes and kill duplicates as long as we manage to merge lengthening
			// that's because merging lines may change the shape order, but our duplicate detection works only when they are sorted
//			System.out.print("Shapes available in the student's work before merging:\n");
//			for (int i=0;i<other.shapes.size();i++)
//				System.out.print("  "+other.shapes.get(i)+"\n");
			do {
//				getGame().getLogger().log("Merge your solution");
				Collections.sort(other.shapes, cmp);
				killDuplicate(other.shapes);
			} while (mergeLengthening(other.shapes));

			do {
//				getGame().getLogger().log("Merge the correction");
				Collections.sort(shapes, cmp);
				killDuplicate(shapes);
			} while (mergeLengthening(shapes));
				
			// Same amount of shapes?
			if (shapes.size() != other.shapes.size()) {
				if (other.shapes.size() > shapes.size())
					sb.append( i18n.tr("  There is {0} shapes, but only {1} shapes were expected\n",other.shapes.size(),shapes.size()) );
				else 
					sb.append( i18n.tr("  There is only {0} shapes, but {1} shapes were expected\n",other.shapes.size(),shapes.size()) );
				/*
				if (getGame().isDebugEnabled()) {
					sb.append("Shapes available in the student's work (after mergin' madness):\n");
					for (int i=0;i<other.shapes.size();i++)
						sb.append("  "+other.shapes.get(i)+"\n");
					sb.append("Expected shapes:\n");
					for (int i=0;i<shapes.size();i++)
						sb.append("  "+shapes.get(i)+"\n");
				}
				*/
				Vector<Shape> studentShapes = new Vector<Shape>();
				Vector<Shape> correctionShapes = new Vector<Shape>();
				for (int i=0;i<other.shapes.size();i++)
					studentShapes.add(other.shapes.get(i));
				for (int i=0;i<shapes.size();i++)
					correctionShapes.add(shapes.get(i));

				for (int i=0;i<studentShapes.size();i++) {
					Shape s = studentShapes.get(i);
					if (correctionShapes.contains(s)) {
						studentShapes.remove(i);
						i--;
						correctionShapes.remove(s);
					}
				}
				if (!studentShapes.isEmpty()) {
					sb.append(i18n.tr("Superflous shapes in your solution:\n"));
					for (Shape s: studentShapes)
						sb.append("   "+s+"\n");
				}
				if (!correctionShapes.isEmpty()) {
					sb.append(i18n.tr("Missing shapes in your solution:\n"));
					for (Shape s: correctionShapes)
						sb.append("   "+s+"\n");
				}
				
				return sb.toString();
			}
			
			// Same shapes?
			for (int i=0;i<other.shapes.size();i++)
				if (! other.shapes.get(i).equals(shapes.get(i)))
					sb.append(i18n.tr("  {0} (got {1} instead of {2})\n",
							((Shape) other.shapes.get(i)).diffTo(shapes.get(i), getGame().i18n),
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
			
			// We don't need to sort the extremities even if [(x1,y1);(x2,y2)]   ==   [(x2,y2);(x1,y1)]
			// because the constructor of Line already deal with that issue.
			
			int res = cmp(l2.x1, l1.x1);
			if (res != 0)
				return res;
			
			res = cmp(l2.x2, l1.x2);
			if (res != 0)
				return res;
			
			res = cmp(l2.y1, l1.y1);
			if (res != 0)
				return res;

			return cmp(l2.y2, l1.y2);
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
			
			return cmp(c1.radius, c2.radius);
		}
		throw new RuntimeException("s1 is neither a Line nor a Circle. I'm puzzled.");
	}
	
}
