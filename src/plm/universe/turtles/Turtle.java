package plm.universe.turtles;

import java.awt.Color;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;
import plm.core.model.Game;
import plm.universe.Entity;
import plm.universe.World;

@EntityPrimitives(TurtlePrimitives.class)
public class Turtle extends Entity implements TurtlePrimitives {

  public final static int DEGREE     = 1;
  public final static int RADIAN     = 2;
  public static final double EPSILON = .0000001;

  private Color color = Color.black;

  private double x = 0.;
  private double y = 0.;

  protected double startX, startY;

  private double heading  = 0.;
  private boolean penDown = true;
  private int angularUnit = Turtle.DEGREE;

  private boolean visible = true;

  /** The PLM calls that constructor with no parameter, so it must exist (but you probably don't want to use it yourself). */
  public Turtle() { super(); }

  public Turtle(World w, String name, double x, double y) { this(w, name, x, y, 0., Color.black); }

  public Turtle(World world, String name, double x, double y, double heading, Color c)
  {
    super(name, world);
    this.color   = c;
    this.x       = x;
    this.y       = y;
    this.startX  = x;
    this.startY  = y;
    this.heading = heading;
    setHeading(heading);
  }

  public Turtle(Turtle t)
  {
    super();
    setName(t.getName());
    setWorld(t.getWorld());
    this.color   = t.color;
    this.x       = t.x;
    this.y       = t.y;
    this.startX  = t.startX;
    this.startY  = t.startY;
    this.heading = t.heading;
    this.penDown = t.penDown;
  }

  @Override public void copy(Entity e)
  {
    super.copy(e);
    Turtle other = (Turtle)e;
    this.color   = other.color;
    this.x       = other.x;
    this.y       = other.y;
    this.startX  = other.startX;
    this.startY  = other.startY;
    this.heading = other.heading;
    this.penDown = other.penDown;
  }

  @Override public void forward(double dist) { moveTo(x + dist * Math.cos(heading), y + dist * Math.sin(heading)); }

  @Override public void backward(double dist) { moveTo(x + dist * Math.cos(heading + Math.PI), y + dist * Math.sin(heading + Math.PI)); }

  public Direction getHeadingDirection()
  {
    final double w            = getWorld().getWidth();
    final double h            = getWorld().getHeight();
    final double angleHeading = 2 * Math.PI - this.heading;

    if (this.heading >= 0 && this.heading < Math.PI) {
      // bottom half-circle
      final double tangenteAngleToBottomLeftCorner  = (this.x > EPSILON) ? ((h - this.y) / this.x) : Double.POSITIVE_INFINITY;
      final double angleToBottomLeftCorner          = Math.PI + Math.atan(tangenteAngleToBottomLeftCorner);
      final double tangenteAngleToBottomRightCorner = ((w - this.x) > EPSILON) ? ((h - this.y) / (w - this.x)) : Double.POSITIVE_INFINITY;
      final double angleToBottomRightCorner         = 2 * Math.PI - Math.atan(tangenteAngleToBottomRightCorner);

      if (angleHeading < angleToBottomLeftCorner) {
        return Direction.WEST;
      } else if (angleHeading < angleToBottomRightCorner) {
        return Direction.SOUTH;
      } else {
        return Direction.EAST;
      }
    } else {
      // bottom half-circle
      final double tangenteAngleToTopRightCorner = ((w - this.x) > EPSILON) ? ((this.y) / (w - this.x)) : Double.POSITIVE_INFINITY;
      final double angleToTopRightCorner         = Math.atan(tangenteAngleToTopRightCorner);
      final double tangenteAngleToTopLeftCorner  = (this.x > EPSILON) ? (this.y / this.x) : Double.POSITIVE_INFINITY;
      final double angleToTopLeftCorner          = Math.PI - Math.atan(tangenteAngleToTopLeftCorner);

      if (angleHeading < angleToTopRightCorner) {
        return Direction.EAST;
      } else if (angleHeading < angleToTopLeftCorner) {
        return Direction.NORTH;
      } else {
        return Direction.WEST;
      }
    }
  }
  @Override public void circle(double radius)
  {
    if (penDown)
      getWorld().addCircle(x, y, radius, color);
  }

  @Override public void moveTo(double newX, double newY)
  {
    final double w = this.getWorld().getWidth();
    final double h = this.getWorld().getHeight();

    double nX = newX;
    double nY = newY;

    while (nX < 0 || nX >= w || nY < 0 || nY >= h) { // need to clip
      // line equation y = y1+m(x-x1)
      // where m=(y2-y1)/(x2-x1)
      final double m = (nY - y) / (nX - x);

      switch (this.getHeadingDirection()) {
        case EAST:
          // intersection with the right boundary (x=r)
          // x=r and y=y1+m(r-x1)
          {
            final double xc = w;
            final double yc = y + m * (w - x);

            if (this.penDown) {
              this.getWorld().addLine(x, y, xc, yc, color);
            }
            setPos(0., yc);
            nX = nX - w;
          }
          break;
        case NORTH:
          // intersection with the top boundary (y=t)
          // x=(1/m)(t-y1)+x1 and y=t
          {
            final double xc = (0 - this.y) / m + this.x;
            final double yc = 0;

            if (this.penDown) {
              this.getWorld().addLine(x, y, xc, yc, color);
            }
            setPos(xc, h);
            nY = nY + h;
          }
          break;
        case WEST:
          // intersection with the left boundary (x=l)
          // x=l and y=y1+m(l-x1)
          {
            final double xc = 0;
            final double yc = y + m * (0 - x);

            if (this.penDown) {
              this.getWorld().addLine(x, y, xc, yc, color);
            }
            setPos(w, yc);
            nX = nX + w;
          }
          break;
        case SOUTH:
          // intersection with the top boundary (y=t)
          // x=(1/m)(t-y1)+x1 and y=t
          {
            final double xc = (h - this.y) / m + this.x;
            final double yc = h;

            if (this.penDown) {
              this.getWorld().addLine(x, y, xc, yc, color);
            }
            setPos(xc, 0.);
            nY = nY - h;
          }
          break;
      }
    }

    if (this.penDown) {
      this.getWorld().addLine(x, y, nX, nY, color);
    }
    this.x = nX;
    this.y = nY;

    stepUI();
  }

  @Override public void left(double angle) { setHeadingRadian(heading - fromAngularUnit(angle)); }
  @Override public void right(double angle) { setHeadingRadian(heading + fromAngularUnit(angle)); }

  // Make sure that the case issue is detected in Scala by overriding the Left() and Right() methods (see #236)
  public void Left(double angle) { throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use Left() with an uppercase. Use left() instead.")); }
  public void Right(double angle)
  {
    throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use Right() with an uppercase. Use right() instead."));
  }

  public void brushDown()
  {
    throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use brushDown() here. Turtles have pens, not brushes. Use penDown() instead."));
  }
  public void brushUp()
  {
    throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use brushUp() here. Turtles have pens, not brushes. Use penUp() instead."));
  }

  @Override public boolean isPenDown() { return penDown; }

  @Override public void penDown() { this.penDown = true; }

  @Override public void penUp() { this.penDown = false; }

  @Override public void hide()
  {
    this.visible = false;
    stepUI();
  }
  @Override public void show()
  {
    this.visible = true;
    stepUI();
  }
  @Override public boolean isVisible() { return this.visible; }
  @Override public void clear() { getWorld().clear(); }

  private double fromAngularUnit(double angle)
  {
    switch (angularUnit) {
      case Turtle.DEGREE:
        return Math.toRadians(angle);
      case Turtle.RADIAN:
        return angle;
    }
    throw new RuntimeException("Unknown angular unit:" + angularUnit + " (please report this bug)");
  }

  private final double toAngularUnit(double angle)
  {
    switch (angularUnit) {
      case Turtle.DEGREE:
        return Math.toDegrees(angle);
      case Turtle.RADIAN:
        return angle;
    }
    throw new RuntimeException("Unknown angular unit:" + angularUnit + " (please report this bug)");
  }

  @Override public double getHeading() { return toAngularUnit(heading); }

  @Override public void setHeading(double heading) { setHeadingRadian(fromAngularUnit(heading)); }

  protected void setHeadingRadian(double heading)
  {
    this.heading = ((2. * Math.PI) + heading) % (2. * Math.PI);
    if (world != null)
      world.notifyWorldUpdatesListeners();
  }

  protected double getHeadingRadian() { return this.heading; }

  @Override public TurtleWorld getWorld() { return (TurtleWorld)super.getWorld(); }

  public Color getColor() { return color; }

  @Override public void setColor(Color c) { color = c; }

  @Override public double getX() { return x; }

  @Override public void setX(double x)
  {
    this.x = x;
    stepUI();
  }

  @Override public double getY() { return y; }

  @Override public void setY(double y)
  {
    this.y = y;
    stepUI();
  }

  @Override public void setPos(double x, double y)
  {
    this.x = x;
    this.y = y;
    stepUI();
  }

  /* let's accept integers as arguments, too */
  public void forward(int steps) { forward((double)steps); }

  public void backward(int steps) { backward((double)steps); }

  public void left(int a) { left((double)a); }

  public void right(int a) { right((double)a); }

  public void setHeading(int heading) { setHeading((double)heading); }

  public void setX(int x) { setX((double)x); }

  public void setY(int y) { setY((double)y); }

  public void setPos(int x, int y) { setPos((double)x, (double)y); }

  public void setPos(double x, int y) { setPos((double)x, (double)y); }

  public void setPos(int x, double y) { setPos((double)x, (double)y); }

  private void addSizeHint(int x1, int y1, int x2, int y2, String txt) { ((TurtleWorld)world).addSizeHint(x1, y1, x2, y2, txt); }

  @Override public void addSizeHint(int x1, int y1, int x2, int y2) { ((TurtleWorld)world).addSizeHint(x1, y1, x2, y2, null); }

  @Override public String toString() { return "Turtle (" + this.getClass().getName() + "): x=" + x + " y=" + y + " Heading:" + heading + " Color:" + color; }

  @Override public int hashCode()
  {
    final int PRIME = 31;
    int result      = 1;
    result          = PRIME * result + ((color == null) ? 0 : color.hashCode());
    result          = PRIME * result + (penDown ? 1231 : 1237);
    result          = PRIME * result + (int)heading;
    result          = PRIME * result + (int)x;
    result          = PRIME * result + (int)y;
    return result;
  }

  public String diffTo(Object obj)
  {
    if (this == obj)
      return "";
    if (obj == null)
      return "I was not expecting a (null) turtle";
    if (!(obj instanceof Turtle))
      return "The turtle is not a turtle! It's a trap!";

    final Turtle other = (Turtle)obj;
    if (Math.abs(heading - other.heading) > Turtle.EPSILON)
      return Game.i18n.tr("The turtle {0} is not heading to the right direction.", getName());
    if (Math.abs(x - other.x) > Turtle.EPSILON || Math.abs(y - other.y) > Turtle.EPSILON)
      return Game.i18n.tr("The turtle {0} is at the right location.", getName());
    return "";
  }
  @Override public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Turtle))
      return false;

    final Turtle other = (Turtle)obj;
    if (Math.abs(heading - other.heading) > Turtle.EPSILON)
      return false;
    if (Math.abs(x - other.x) > Turtle.EPSILON)
      return false;
    if (Math.abs(y - other.y) > Turtle.EPSILON)
      return false;
    return true;
  }

  @Override public void run()
  {
    // Overriden by childs
  }

  /* BINDINGS TRANSLATION: French */
  public void avance(double steps) { forward(steps); }
  public void recule(double steps) { backward(steps); }
  public void gauche(double angle) { left(angle); }
  public void droite(double angle) { right(angle); }
  public void cercle(double radius) { circle(radius); }
  // get/set X/Y/Pos are not translated as they happen to be the same in French
  public void allerVers(double x, double y) { moveTo(x, y); }
  public double getCap() { return getHeading(); }
  public void setCap(double cap) { setHeading(cap); }
  public void leveBrosse() { brushUp(); }
  public void baisseBrosse() { brushDown(); }
  public void leveCrayon() { penUp(); }
  public void baisseCrayon() { penDown(); }
  public boolean estCrayonBaisse() { return isPenDown(); }
  public Color getCouleur() { return getColor(); }
  public void setCouleur(Color c) { setColor(c); }
  public boolean estChoisi() { return isSelected(); }  // we have to document the version without e, since po4a allows for one variant only
  public boolean estChoisie() { return isSelected(); } // But we want to have the grammatically correct form also possible (turtles are feminine)
  public void efface() { clear(); }
  public void cache() { hide(); }
  public void montre() { show(); }
  public boolean estVisible() { return isVisible(); }
}
