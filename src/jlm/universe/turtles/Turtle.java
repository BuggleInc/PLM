package jlm.universe.turtles;

import java.awt.Color;

import jlm.universe.Entity;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class Turtle extends Entity {

	public final static int DEGREE = 1;
	public final static int RADIAN = 2;
	public static final double EPSILON = .0000001;

	private Color color;

	private double x = 0.;
	private double y = 0.;

	private double heading = 0.;
	private boolean penDown = true;
	private int angularUnit = Turtle.DEGREE;

	/**
	 * Constructor with no argument so that child classes can avoid declaring a
	 * constructor. But it should not be used as most methods assert on world
	 * being not null. After using it, {@link Entity#setWorld(BuggleWorld)} must be
	 * used ASAP.
	 */
	public Turtle() {
		this(null, "John Doe", 0., 0., 0., Color.red);
	}

	public Turtle(World w) {
		this(w, "John Doe", 0., 0., 0., Color.red);
	}

	public Turtle(World w, String name) {
		this(w, name, 0., 0., 0., Color.red);
	}

	public Turtle(World w, String name, double x, double y) {
		this(w, name, x, y, 0., Color.red);
	}

	public Turtle(World world, String name, double x, double y, double heading, Color c) {
		super(name, world);
		this.color = c;
		this.x = x;
		this.y = y;
		this.heading = heading;
		setHeading(heading);
	}

	public Turtle(Turtle t) {
		super();
		setName(t.getName());
		setWorld(t.getWorld());
		this.color = t.color;
		this.x = t.x;
		this.y = t.y;
		this.heading = t.heading;
		//this.angularUnit = t.angularUnit;
		this.penDown = t.penDown;
	}

	@Override
	public void copy(Entity e) {
		super.copy(e);
		Turtle other = (Turtle) e;
		this.color = other.color;
		this.x = other.x;
		this.y = other.y;
		this.heading = other.heading;
		//this.angularUnit = other.angularUnit;
		this.penDown = other.penDown;
	}

	@Override
	public Entity copy() {
		return new Turtle(this);
	}

	public void forward(double dist) {
		moveTo(x + dist * Math.cos(heading), y + dist * Math.sin(heading));
	}

	public void backward(double dist) {
		moveTo(x + dist * Math.cos(heading + Math.PI), y + dist * Math.sin(heading + Math.PI));
	}	
	
	public Direction getHeadingDirection() {
		final double w = getWorld().getWidth();
		final double h = getWorld().getHeight();
		final double angleHeading = 2*Math.PI - this.heading;
		
		if (this.heading >=0 && this.heading < Math.PI) {
			// bottom semi-circle
			final double tangenteAngleToBottomLeftCorner = (this.x>EPSILON)?((h-this.y) / this.x):Double.POSITIVE_INFINITY;
			final double angleToBottomLeftCorner = Math.PI+Math.atan(tangenteAngleToBottomLeftCorner);
			final double tangenteAngleToBottomRightCorner = ((w-this.x)>EPSILON)?((h-this.y) / (w-this.x)):Double.POSITIVE_INFINITY;
			final double angleToBottomRightCorner = 2*Math.PI-Math.atan(tangenteAngleToBottomRightCorner);

			if (angleHeading < angleToBottomLeftCorner) {
				return Direction.WEST;
			} else if (angleHeading < angleToBottomRightCorner) {
				return Direction.SOUTH;
			} else {
				return Direction.EAST;
			}
		} else {
			// bottom semi-circle			
			final double tangenteAngleToTopRightCorner = ((w-this.x)>EPSILON)?((this.y) / (w-this.x)):Double.POSITIVE_INFINITY;
			final double angleToTopRightCorner = Math.atan(tangenteAngleToTopRightCorner);
			final double tangenteAngleToTopLeftCorner = (this.x>EPSILON)?(this.y / this.x):Double.POSITIVE_INFINITY;
			final double angleToTopLeftCorner = Math.PI-Math.atan(tangenteAngleToTopLeftCorner);
					
			if (angleHeading < angleToTopRightCorner) {
				return Direction.EAST;
			} else if (angleHeading < angleToTopLeftCorner) {
				return Direction.NORTH;
			} else {
				return Direction.WEST;
			}
		}
	}
	
	public void moveTo(double newX, double newY) {
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

	public void turnLeft(double angle) {
		setHeadingRadian(heading - fromAngularUnit(angle));
	}

	public void turnRight(double angle) {
		setHeadingRadian(heading + fromAngularUnit(angle));
	}

	public boolean isPenDown() {
		return penDown;
	}

	public void penDown() {
		this.penDown = true;
	}

	public void penUp() {
		this.penDown = false;
	}

	private double fromAngularUnit(double angle) {
		switch (angularUnit) {
		case Turtle.DEGREE:
			return Math.toRadians(angle);
		case Turtle.RADIAN:
			return angle;
		}
		throw new RuntimeException("Unknown angular unit:" + angularUnit + " (please report this bug)");
	}

	private final double toAngularUnit(double angle) {
		switch (angularUnit) {
		case Turtle.DEGREE:
			return Math.toDegrees(angle);
		case Turtle.RADIAN:
			return angle;
		}
		throw new RuntimeException("Unknown angular unit:" + angularUnit + " (please report this bug)");
	}

	public double getHeading() {
		return toAngularUnit(heading);
	}

	public void setHeading(double heading) {
		setHeadingRadian(fromAngularUnit(heading));
	}

	protected void setHeadingRadian(double heading) {
		this.heading = ((2. * Math.PI) + heading) % (2. * Math.PI);
		if (world != null)
			world.notifyWorldUpdatesListeners();
	}
	
	protected double getHeadingRadian() {
		return this.heading;
	}

	@Override
	public TurtleWorld getWorld() {
		return (TurtleWorld) super.getWorld();
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		stepUI();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		stepUI();
	}

	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
		stepUI();
	}

	/* let's accept integers as arguments, too */
	public void forward(int steps) {
		forward((double) steps);
	}

	public void backward(int steps) {
		backward((double) steps);
	}

	public void turnLeft(int a) {
		turnLeft((double) a);
	}

	public void turnRight(int a) {
		turnRight((double) a);
	}

	public void setHeading(int heading) {
		setHeading((double) heading);
	}

	public void setX(int x) {
		setX((double) x);
	}

	public void setY(int y) {
		setY((double) y);
	}

	public void setPos(int x, int y) {
		setPos((double) x, (double) y);
	}

	public void setPos(double x, int y) {
		setPos((double) x, (double) y);
	}

	public void setPos(int x, double y) {
		setPos((double) x, (double) y);
	}
	
	@Override
	public String toString() {
		return "Turtle (" + this.getClass().getName() + "): x=" + x + " y=" + y + " Heading:" + heading + " Color:"
				+ color;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((color == null) ? 0 : color.hashCode());
		result = PRIME * result + (penDown ? 1231 : 1237);
		result = PRIME * result + (int) heading;
		result = PRIME * result + (int) x;
		result = PRIME * result + (int) y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Turtle))
			return false;

		final Turtle other = (Turtle) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (Math.abs(heading-other.heading) > Turtle.EPSILON)
			return false;
		if (Math.abs(x-other.x) > Turtle.EPSILON)
			return false;
		if (Math.abs(y-other.y) > Turtle.EPSILON)
			return false;
		return true;
	}

	@Override
	public void run() {
		// Overriden by childs
	}

}
