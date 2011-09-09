package jlm.universe.robozzle;

import java.awt.Point;

import jlm.universe.Direction;
import jlm.universe.Entity;
import jlm.universe.GridWorld;
import jlm.universe.bugglequest.exception.BuggleWallException;


public abstract class AbstractRobozzle extends Entity {

	private int x;
	private int y;

	protected Direction direction;


	/* This is for the simple buggle to indicate that it did hit a wall, and is thus not a valid
	 * candidate for exercise completion.
	 */
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
	 * being not null. After using it, {@link Entity#setWorld(BuggleWorld)} must be used
	 * ASAP.
	 */
	public AbstractRobozzle() {
		this(null, "John Doe", 0, 0, Direction.NORTH);
	}

	public AbstractRobozzle(RobozzleWorld w) {
		this(w, "John Doe", 0, 0, Direction.NORTH);
	}

	public AbstractRobozzle(RobozzleWorld world, String name, int x, int y, Direction direction) {
		super(name,world);
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	@Override
	public void copy(Entity e) {
		super.copy(e);
		AbstractRobozzle other = (AbstractRobozzle)e;
		this.x = other.x;
		this.y = other.y;
		this.direction = other.direction;
	}
	@Override
	public Entity copy() {
		return new Robozzle(this);
	}

	public char getGroundColor() {
		return getCell().getColor();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		if (direction != null) {
			this.direction = direction;
			stepUI();
		}
	}

	public void turnLeft() {
		setDirection(direction.left());
	}

	public void turnRight() {
		setDirection(direction.right());
	}

	public void turnBack() {
		setDirection(direction.opposite());
	}
	
	public int getWorldHeight() {
		return ((GridWorld) world).getHeight();
	}
	
	public int getWorldWidth() {
		return ((GridWorld) world).getWidth();
	}
	protected RobozzleWorldCell getCell(){
		return (RobozzleWorldCell) ((GridWorld)world).getCell(x, y);
	}
	protected RobozzleWorldCell getCell(int u, int v){
		return (RobozzleWorldCell) ((GridWorld)world).getCell(u, v);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		assert (world != null);
		this.x = x;
		stepUI();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		assert (world != null);
		this.y = y;
		stepUI();
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		stepUI();
	}

	public void forward() throws BuggleWallException {
		move(direction.toPoint());
	}

	public void forward(int count) throws BuggleWallException {
		for (int i = 0; i < count; i++)
			forward();
	}

	public void backward() throws BuggleWallException {
		move(direction.opposite().toPoint());
	}

	public void backward(int count) throws BuggleWallException {
		for (int i = 0; i < count; i++)
			backward();
	}

	private boolean lookAtWall(boolean forward) {
		Direction delta;
		if (forward)
			delta = getDirection();
		else
			delta = getDirection().opposite();

		RobozzleWorldCell cell;
		switch (delta.intValue()) {
		case Direction.NORTH_VALUE: /* looking up is easy */
			cell = getCell();
			return cell.hasTopWall();

		case Direction.WEST_VALUE: /* looking to the left also */
			cell = getCell();
			return cell.hasLeftWall();

		case Direction.SOUTH_VALUE: /* if looking down, look to the top of one cell lower */
			cell = getCell(getX(),                        (getY()+1) % getWorldHeight());
			return cell.hasTopWall();

		case Direction.EAST_VALUE: /* if looking right, look to the left of one next cell */
			cell = getCell((getX()+1) % getWorldWidth(), getY());
			return cell.hasLeftWall();

		default: throw new RuntimeException("Invalid direction: "+delta);
		}
	}
	public boolean isFacingWall() {
		return lookAtWall(true);
	}
	public boolean isBackingWall() {
		return lookAtWall(false);
	}

	private void move(Point delta) throws BuggleWallException {
		if (delta == null)
			return;
		
		int newx = (x + delta.x) % getWorldWidth();
		if (newx < 0)
			newx += getWorldWidth();
		int newy = (y + delta.y) % getWorldHeight();
		if (newy < 0)
			newy += getWorldHeight();

		if (delta.equals(direction.toPoint())            && isFacingWall() ||
				delta.equals(direction.opposite().toPoint()) && isBackingWall())	

			throw new BuggleWallException("Cannot traverse wall");

		x = newx;
		y = newy;

		stepUI();
	}

	@Override
	public String toString() {
		return "Robozzle (" + this.getClass().getName() + "): x=" + x + " y=" + y + " Direction:" + direction;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((direction == null) ? 0 : direction.hashCode());
		result = PRIME * result + x;
		result = PRIME * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractRobozzle))
			return false;
		
		final AbstractRobozzle other = (AbstractRobozzle) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (seenError != other.seenError)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
