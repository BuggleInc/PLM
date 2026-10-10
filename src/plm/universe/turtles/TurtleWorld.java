package plm.universe.turtles;

import static java.lang.Double.parseDouble;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.EntityControlPanel;
import plm.universe.World;

public class TurtleWorld extends World {

  ArrayList<Shape> shapes       = new ArrayList<Shape>();
  ArrayList<SizeHint> sizeHints = new ArrayList<SizeHint>();

  private double width;
  private double height;

  public TurtleWorld(String name) { super(name); }

  public TurtleWorld(String name, int width, int height)
  {
    super(name);
    this.width  = width;
    this.height = height;
  }

  public TurtleWorld(TurtleWorld original)
  {
    super(original);
    sizeHints = new ArrayList<SizeHint>();
    for (SizeHint sh : original.sizeHints)
      sizeHints.add(sh);
    for (Entity e : getEntities()) {
      Turtle t = (Turtle)e;
      t.startX = t.getX();
      t.startY = t.getY();
    }
  }

  @Override public void reset(World w)
  {
    TurtleWorld initialWorld = (TurtleWorld)w;
    shapes                   = new ArrayList<Shape>();
    this.height              = initialWorld.height;
    this.width               = initialWorld.width;

    Iterator<Shape> it = initialWorld.shapes();
    while (it.hasNext())
      shapes.add(it.next().copy());

    sizeHints = new ArrayList<SizeHint>();
    for (SizeHint sh : initialWorld.sizeHints)
      sizeHints.add(sh);

    super.reset(w);
  }

  @Override public TurtleWorld replaceEntities(Supplier<? extends Entity> newEntitySupplier) { return (TurtleWorld)super.replaceEntities(newEntitySupplier); }

  public void addSizeHint(int x1, int y1, int x2, int y2) { addSizeHint(x1, y1, x2, y2, null); }
  public void addSizeHint(int x1, int y1, int x2, int y2, String text)
  {
    synchronized (sizeHints) {
      sizeHints.add(new SizeHint(x1, y1, x2, y2, text));
      notifyWorldUpdatesListeners();
    }
  }
  public void addLine(double x, double y, double newX, double newY, Color color)
  {
    synchronized (shapes) {
      shapes.add(new Line(x, y, newX, newY, color));
      notifyWorldUpdatesListeners();
    }
  }
  public void addCircle(double x, double y, double radius, Color color)
  {
    synchronized (shapes) {
      shapes.add(new Circle(x, y, radius, color));
      notifyWorldUpdatesListeners();
    }
  }
  public void clear()
  {
    synchronized (shapes) {
      shapes.clear();
      notifyWorldUpdatesListeners();
    }
  }

  public Iterator<Shape> shapes() { return shapes.iterator(); }

  public double getHeight() { return height; }
  public double getWidth() { return width; }

  @Override public EntityControlPanel getEntityControlPanel() { return new TurtleButtonPanel(); }

  @Override public WorldView getView() { return new TurtleWorldView(this); }
  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_turtle.png"); }

  @Override public boolean haveIO() { return true; }

  /** Reads a world written by writeToFile() */
  @Override protected World parse(BufferedReader reader, String path) throws IOException, BrokenWorldFileException
  {
    TurtleWorld res = new TurtleWorld(readField(reader, path, "TurtleWorld"));
    String[] size   = readField(reader, path, "Size").split("x");
    res.width       = parseDouble(size[0]);
    res.height      = parseDouble(size[1]);

    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
      String[] t = line.split("\t");
      switch (t[0]) {
        case "Turtle" -> {
          Turtle turtle = new Turtle(res, t[7], parseDouble(t[1]), parseDouble(t[2]), 0., new Color(Integer.parseUnsignedInt(t[4], 16), true));
          turtle.setHeadingRadian(parseDouble(t[3]));
          if (!Boolean.parseBoolean(t[5]))
            turtle.penUp();
          if (!Boolean.parseBoolean(t[6]))
            turtle.hide();
        }
        case "Line" ->
          res.shapes.add(
              new Line(parseDouble(t[1]), parseDouble(t[2]), parseDouble(t[3]), parseDouble(t[4]), new Color(Integer.parseUnsignedInt(t[5], 16), true)));
        case "Circle" ->
          res.shapes.add(new Circle(parseDouble(t[1]), parseDouble(t[2]), parseDouble(t[3]), new Color(Integer.parseUnsignedInt(t[4], 16), true)));
        case "Hint" -> res.sizeHints.add(new SizeHint(parseDouble(t[1]), parseDouble(t[2]), parseDouble(t[3]), parseDouble(t[4]), t[5]));
        default -> throw new BrokenWorldFileException(path + ".map: unknown line '" + line + "'.");
      }
    }
    return res;
  }

  /**
   * Writes the world: its size, then one tab-separated line per turtle, shape and size hint, in this order.
   * Colors are written as hexadecimal ARGB and doubles with their full precision.
   */
  @Override public void writeToFile(BufferedWriter writer) throws IOException
  {
    writer.write("TurtleWorld: " + getName() + "\n");
    writer.write("Size: " + width + "x" + height + "\n");
    for (Entity e : entities) {
      Turtle t = (Turtle)e;
      writer.write("Turtle\t" + t.getX() + "\t" + t.getY() + "\t" + t.getHeadingRadian() + "\t" + Integer.toHexString(t.getColor().getRGB()) + "\t" +
                   t.isPenDown() + "\t" + t.isVisible() + "\t" + t.getName() + "\n");
    }
    synchronized (shapes) {
      for (Shape s : shapes)
        if (s instanceof Line l)
          writer.write("Line\t" + l.x1 + "\t" + l.y1 + "\t" + l.x2 + "\t" + l.y2 + "\t" + Integer.toHexString(l.color.getRGB()) + "\n");
        else if (s instanceof Circle c)
          writer.write("Circle\t" + c.x + "\t" + c.y + "\t" + c.radius + "\t" + Integer.toHexString(c.color.getRGB()) + "\n");
    }
    for (SizeHint h : sizeHints)
      writer.write("Hint\t" + h.x1 + "\t" + h.y1 + "\t" + h.x2 + "\t" + h.y2 + "\t" + h.text + "\n");
  }

  @Override public String toString()
  {
    String res = "TurtleWorld: name=" + getName() + ", size=" + width + "x" + height + ", parameters: " + parameters + ", shapes=[";
    synchronized (shapes) {
      Iterator<Shape> it = shapes();
      while (it.hasNext())
        res += it.next().toString();
    }
    res += "]";
    return res;
  }

  @Override public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!(obj instanceof TurtleWorld))
      return false;

    TurtleWorld other = (TurtleWorld)obj;
    if (!other.getName().equals(getName()))
      return false;
    String diff = diffTo(other, null); // We don't have the ProgrammingLanguage at hand here, but this world ignores that parameter so use 'null'
    if (diff.equals(""))
      return true;
    return false;
  }

  /**
   * Returns the shapes where each line is merged with the first line that lengthens it or covers it (see mergeLines()).
   * A merged line may be mergeable again with the following lines, so the callers repeat this until the amount of shapes is stable.
   */
  private ArrayList<Shape> mergeLengthening(ArrayList<Shape> shapes)
  {
    ArrayList<Shape> res = new ArrayList<Shape>();
    for (Shape shape : shapes) {
      boolean merged = false;
      if (shape instanceof Line line)
        for (int i = 0; i < res.size() && !merged; i++)
          if (res.get(i) instanceof Line other) {
            Line union = mergeLines(other, line);
            if (union != null) {
              res.set(i, union);
              merged = true;
            }
          }
      if (!merged)
        res.add(shape);
    }
    return res;
  }

  /** Returns the line that replaces both lines if they lengthen each other or if one covers the other, null if they are separate */
  private Line mergeLines(Line a, Line b)
  {
    if (!a.sameSlope(b)) // We cannot have inverted slopes because the extremities within a line are sorted
      return null;

    boolean sameStart = Line.doubleEqual(a.x1, b.x1) && Line.doubleEqual(a.y1, b.y1);
    boolean sameEnd   = Line.doubleEqual(a.x2, b.x2) && Line.doubleEqual(a.y2, b.y2);
    if (sameStart || sameEnd) // Keep the longer
      return a.getLength() > b.getLength() ? a : b;
    if (Line.doubleEqual(a.x1, b.x2) && Line.doubleEqual(a.y1, b.y2)) // b ends where a starts
      return new Line(b.x1, b.y1, a.x2, a.y2, a.color);
    if (Line.doubleEqual(a.x2, b.x1) && Line.doubleEqual(a.y2, b.y1)) // a ends where b starts
      return new Line(a.x1, a.y1, b.x2, b.y2, a.color);

    if (a.sameRoot(b)) { // The lines are parallel and on the same ray: keep the one covering the other, if any
      if (covers(b, a))
        return b;
      if (covers(a, b))
        return a;
    }
    return null;
  }

  /** Returns whether the inner line is within the outer one, supposing that they have the same slope and root */
  private boolean covers(Line outer, Line inner)
  {
    return Line.doubleWithin(inner.x1, outer.x1, outer.x2) && Line.doubleWithin(inner.x2, outer.x1, outer.x2) &&
        Line.doubleWithin(inner.y1, outer.y1, outer.y2) && Line.doubleWithin(inner.y2, outer.y1, outer.y2);
  }

  private void killDuplicate(ArrayList<Shape> shapes)
  {
    for (int i = 0; i < shapes.size() - 1; i++)
      if (shapes.get(i).equals(shapes.get(i + 1))) {
        shapes.remove(i + 1);
        i--; // counters the effect of next i++ in the for loop
      }
  }

  @Override public String diffTo(World world, ProgrammingLanguage ignored)
  {
    if (world == this)
      return "";
    StringBuffer sb   = new StringBuffer();
    TurtleWorld other = (TurtleWorld)world;

    // First compare entities
    if (other.entities.size() != entities.size())
      return Game.i18n.tr("  There is {0} entities, but {1} entities were expected\n", other.entities.size(), entities.size());
    ;
    for (int i = 0; i < other.entities.size(); i++)
      if (!other.entities.get(i).equals(entities.get(i)))
        sb.append(((Turtle)other.entities.get(i)).diffTo(entities.get(i)));

    // Compare shapes, on normalized copies: the comparison must not change the worlds
    ArrayList<Shape> expected = normalizedShapes(shapes);
    ArrayList<Shape> got      = normalizedShapes(other.shapes);

    // Same amount of shapes?
    if (expected.size() != got.size())
      return sb.append(diffShapeCounts(expected, got)).toString();

    // Same shapes?
    for (int i = 0; i < got.size(); i++)
      if (!got.get(i).equals(expected.get(i)))
        sb.append(Game.i18n.tr("  {0} (got {1} instead of {2})\n", got.get(i).diffTo(expected.get(i)), got.get(i), expected.get(i)));
    return sb.toString();
  }

  /** Describes how the obtained shapes differ from the expected ones when their amounts differ: the amounts, then the superfluous and missing shapes */
  private String diffShapeCounts(List<Shape> expected, List<Shape> got)
  {
    StringBuffer sb = new StringBuffer();
    if (got.size() > expected.size())
      sb.append(Game.i18n.tr("  There is {0} shapes, but only {1} shapes were expected\n", got.size(), expected.size()));
    else
      sb.append(Game.i18n.tr("  There is only {0} shapes, but {1} shapes were expected\n", got.size(), expected.size()));

    if (Game.getInstance().isDebugEnabled()) {
      sb.append("Shapes available in the student's work (after mergin' madness):\n");
      for (Shape s : got)
        sb.append("  " + s + "\n");
      sb.append("Expected shapes:\n");
      for (Shape s : expected)
        sb.append("  " + s + "\n");
    }

    List<Shape> superfluous = new ArrayList<Shape>(got);
    List<Shape> missing     = new ArrayList<Shape>(expected);
    for (Shape s : got)
      if (missing.remove(s))
        superfluous.remove(s);

    if (!superfluous.isEmpty()) {
      sb.append(Game.i18n.tr("Superflous shapes in your solution:\n"));
      for (Shape s : superfluous)
        sb.append("   " + s + "\n");
    }
    if (!missing.isEmpty()) {
      sb.append(Game.i18n.tr("Missing shapes in your solution:\n"));
      for (Shape s : missing)
        sb.append("   " + s + "\n");
    }
    return sb.toString();
  }

  /**
   * Returns a sorted copy of the shapes, where duplicates and lengthening lines are merged. The shapes themselves are not modified.
   * Merging lines may change the shape order while the duplicate detection needs them sorted, hence the loop.
   */
  private ArrayList<Shape> normalizedShapes(ArrayList<Shape> original)
  {
    ArrayList<Shape> res;
    synchronized (original) {
      res = new ArrayList<Shape>(original);
    }
    int previousSize;
    do {
      previousSize = res.size();
      Collections.sort(res, new ShapeComparator());
      killDuplicate(res);
      res = mergeLengthening(res);
    } while (res.size() < previousSize);
    return res;
  }
}

class ShapeComparator implements Comparator<Shape> {

  public ShapeComparator() { super(); }

  int cmp(double a, double b)
  {
    if (Math.abs(a - b) < 0.001)
      return 0;
    if (a < b)
      return 1;
    return -1;
  }

  @Override public int compare(Shape s1, Shape s2)
  {
    if (s1 instanceof Line && s2 instanceof Circle)
      return -1;
    if (s1 instanceof Circle && s2 instanceof Line)
      return 1;

    if (s1 instanceof Line) {
      Line l1 = (Line)s1;
      Line l2 = (Line)s2;

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
      Circle c1 = (Circle)s1;
      Circle c2 = (Circle)s2;

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
