package universe.turtles;

import java.awt.Color;

import org.simpleframework.xml.Attribute;

import universe.Entity;
import universe.bugglequest.BuggleWorld;



public  class Turtle extends Entity {
	@Attribute
	private Color color;

	@Attribute
	private double x=0.;
	@Attribute
	private double y=0.;

	@Attribute
	private double heading=0.;

	@Attribute
	boolean penDown=true;
	
	final static int AngularUnitDegree = 1;
	final static int AngularUnitRadian = 2;
	@Attribute
	int angularUnit = AngularUnitDegree;
	
	/* This is for the simple buggle to indicate that it did hit a wall, and is thus not a valid
	 * candidate for exercise completion.
	 */
	@Attribute
	private boolean seenError = false;
	public void seenError() {
		this.seenError = true;
	}
	public boolean haveSeenError() {
		return seenError;
	}	
	
	/**
	 * Constructor with no argument so that child classes can avoid declaring a
	 * constructor. But it should not be used as most methods assert on world
	 * being not null. After using it, {@link #setWorld(BuggleWorld)} must be used
	 * ASAP.
	 */
	public Turtle() {
		this(null, "John Doe", 0., 0., 0., Color.red);
	}

	public Turtle(TurtleWorld w) {
		this(w, "John Doe", 0., 0., 0., Color.red);
	}
	public Turtle(TurtleWorld w, String name) {
		this(w, name, 0., 0., 0., Color.red);
	}

	public Turtle(TurtleWorld world, String name, double x, double y, double heading, Color c) {
		super(name,world);
		this.color = c;
		this.x = x;
		this.y = y;
		this.heading = heading;
	}
	public Turtle(Turtle t) {
		super();
		setName(t.getName());
		setWorld(t.getWorld());
		this.color = t.color;
		this.x = t.x;
		this.y = t.y;
		this.heading = t.heading;		
	}
	@Override
	public void copy(Entity e) {
		super.copy(e);
		Turtle other = (Turtle)e;
		this.color = other.color;
		this.x = other.x;
		this.y = other.y;
		this.heading = other.heading;
	}
	@Override
	public Entity copy() {
		return new Turtle(this);
	}

	public boolean isPenDown() {
		return penDown;
	}

	public void penDown() {
		this.penDown = true;
	}

	public void brushUp() {
		this.penDown = false;
	}

	/* TODO
	public Color getGroundColor() {
		return getCell().getColor();
	}*/

	public Color getPenColor() {
		return color;
	}

	public void setPenColor(Color c) {
		color = c;
	}

	private double fromAngularUnit(double a){
		switch (angularUnit) {
		case AngularUnitDegree:
			return a/180.*Math.PI;
		case AngularUnitRadian:
			return a;
		}
		throw new RuntimeException("Unknown angular unit:"+angularUnit+" (please report this bug)");
	}
	private final double toAngularUnit(double a){
		switch (angularUnit) {
		case AngularUnitDegree:
			return a*180./Math.PI;
		case AngularUnitRadian:
			return a;
		}
		throw new RuntimeException("Unknown angular unit:"+angularUnit+" (please report this bug)");
	}

	public double getHeading() {
		return toAngularUnit(heading);
	}

	final public void setHeading(double heading) {
		setHeadingRadian(fromAngularUnit(heading));
	}
	protected void setHeadingRadian(double heading) {
		this.heading = heading % (2*Math.PI);
		world.notifyWorldUpdatesListeners();
	}

	public void turnLeft(double angle) {
		setHeadingRadian(heading-fromAngularUnit(angle));
	}

	public void turnRight(double angle) {
		setHeadingRadian(heading+fromAngularUnit(angle));
	}

	public void turnBack() {
		setHeadingRadian(heading+Math.PI);
	}
	
	public double getWorldHeight() {
		return ((TurtleWorld)world).getHeight();
	}
	
	public double getWorldWidth() {
		return ((TurtleWorld)world).getWidth();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		world.notifyWorldUpdatesListeners();
		stepUI();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		world.notifyWorldUpdatesListeners();
		stepUI();
	}

	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
		world.notifyWorldUpdatesListeners();
		stepUI();
	}

	public void forward(double dist) {
		moveTo(x + dist*Math.cos(heading), y + dist*Math.sin(heading));
	}

	public void backward(double dist) {
		moveTo(x + dist*Math.cos(heading+Math.PI), y + dist*Math.sin(heading+Math.PI));
	}

	private void moveTo(double newX, double newY) {

		if (penDown) 
			((TurtleWorld) world).addLine(x,y,newX,newY,color);
		
		x = newX;
		y = newY;

		world.notifyWorldUpdatesListeners();
		stepUI();
	}

	protected void stepUI() {
		// only a trial to see moving steps
		if (world.getDelay() > 0) {
			try {
				Thread.sleep(world.getDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
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
		result = PRIME * result + (int)heading;
		result = PRIME * result + (int)x;
		result = PRIME * result + (int)y;
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
		if (heading != other.heading)
			return false;
		if (seenError != other.seenError)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	@Override
	public void run() {
		// TODO Raccord de méthode auto-généré
		
	}

}
