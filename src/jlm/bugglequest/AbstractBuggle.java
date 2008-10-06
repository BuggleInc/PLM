package jlm.bugglequest;

import java.awt.Color;
import java.awt.Point;

import jlm.exception.AlreadyHaveBaggleException;
import jlm.exception.BuggleWallException;
import jlm.exception.NoBaggleUnderBuggleException;


public abstract class AbstractBuggle {
	Color color;
	Color brushColor;

	private int x;
	private int y;

	Direction direction;

	World world;

	boolean brushDown;

	private Baggle baggle;

	protected String name;

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

	/* This is to allow exercise buggles to forbid the use by students of some functions 
	 * which are mandatory for core mechanism. See welcome.ArrayBuggle to see how it forbids setPos(int,int)  
	 */
	private boolean inited = false;
	public boolean isInited() {
		return inited;
	}
	public void initDone() {
		inited = true;		
	}
	
	
	/**
	 * Constructor with no argument so that child classes can avoid declaring a
	 * constructor. But it should not be used as most methods assert on world
	 * being not null. After using it, {@link #setWorld(World)} must be used
	 * ASAP.
	 */
	public AbstractBuggle() {
		this(null, "John Doe", 0, 0, Direction.NORTH, Color.red, Color.red);
	}

	public AbstractBuggle(World w) {
		this(w, "John Doe", 0, 0, Direction.NORTH, Color.red, Color.red);
	}

	public AbstractBuggle(World world, String name, int x, int y, Direction direction, Color c) {
		this(world, name, 0, 0, Direction.NORTH, c, c);
	}

	public AbstractBuggle(World world, String name, int x, int y, Direction direction, Color color, Color brushColor) {
		super();
		this.name = name;
		this.color = color;
		this.brushColor = brushColor;
		this.x = x;
		this.y = y;
		this.direction = direction;
		if (world != null) {
			this.world = world;
			world.addBuggle(this);
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBrushDown() {
		return brushDown;
	}

	public void brushDown() {
		assert (world != null);
		this.brushDown = true;
		WorldCell cell = world.getCell(x, y);
		cell.setColor(brushColor);
		world.notifyWorldUpdatesListeners();
	}

	public void brushUp() {
		assert (world != null);
		this.brushDown = false;
	}

	public Color getGroundColor() {
		return this.world.getCell(this.x, this.y).getColor();
	}

	public Color getBrushColor() {
		return brushColor;
	}

	public void setBrushColor(Color c) {
		brushColor = c;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		assert (world != null);
		this.color = c;
		world.notifyWorldUpdatesListeners();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		assert (world != null);
		this.direction = direction;
		world.notifyWorldUpdatesListeners();
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

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public int getWorldHeight() {
		return this.getWorld().getHeight();
	}
	
	public int getWorldWidth() {
		return this.getWorld().getWidth();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		assert (world != null);
		this.x = x;
		world.notifyWorldUpdatesListeners();
		stepUI();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		assert (world != null);
		this.y = y;
		world.notifyWorldUpdatesListeners();
		stepUI();
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		world.notifyWorldUpdatesListeners();
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
		Direction back = direction.opposite();
		move(back.toPoint());
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

		WorldCell cell;
		switch (delta.intValue()) {
		case Direction.NORTH_VALUE: /* looking up is easy */
			cell = world.getCell(x,y);
			return cell.hasTopWall();

		case Direction.WEST_VALUE: /* looking to the left also */
			cell = world.getCell(x,y);
			return cell.hasLeftWall();

		case Direction.SOUTH_VALUE: /* if looking down, look to the top of one cell lower */
			cell = world.getCell(getX(),                        (getY()+1) % world.getHeight());
			return cell.hasTopWall();

		case Direction.EAST_VALUE: /* if looking right, look to the left of one next cell */
			cell = world.getCell((getX()+1) % world.getWidth(), getY());
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
		assert (world != null);
		int newx = (x + delta.x) % world.getWidth();
		if (newx < 0)
			newx += world.getWidth();
		int newy = (y + delta.y) % world.getHeight();
		if (newy < 0)
			newy += world.getHeight();

		if (delta.equals(direction.toPoint())            && isFacingWall() ||
				delta.equals(direction.opposite().toPoint()) && isBackingWall())	

			throw new BuggleWallException("Cannot traverse wall");

		x = newx;
		y = newy;

		if (brushDown) {
			WorldCell cell = world.getCell(x, y);
			cell.setColor(brushColor);
		}

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

	public boolean isOverBaggle() {
		return this.world.getCell(this.x, this.y).hasBaggle();
	}

	public boolean isCarryingBaggle() {
		return this.baggle != null;
	}

	public void pickUpBaggle() throws NoBaggleUnderBuggleException, AlreadyHaveBaggleException {
		if (!isOverBaggle())
			throw new NoBaggleUnderBuggleException("There is no baggle to pick up");
		if (isCarryingBaggle())
			throw new AlreadyHaveBaggleException("Your buggle is already carrying a baggle");
		this.baggle = this.world.getCell(this.x, this.y).pickUpBaggle();
	}

	public void dropBaggle() throws AlreadyHaveBaggleException {
		this.world.getCell(this.x, this.y).setBaggle(this.baggle);
		this.baggle = null;
	}

	public boolean isOverMessage() {
		return this.world.getCell(this.x, this.y).hasContent();
	}

	public void writeMessage(String msg) {
		this.world.getCell(this.x, this.y).addContent(msg);
	}
	public void writeMessage(int nb) {
		writeMessage(""+nb);
	}

	public String readMessage() {
		return this.world.getCell(this.x, this.y).getContent();
	}

	public void clearMessage() {
		this.world.getCell(this.x, this.y).emptyContent();
	}



	@Override
	public String toString() {
		return "Buggle (" + this.getClass().getName() + "): x=" + x + " y=" + y + " Direction:" + direction + " Color:"
		+ color;
	}

	public abstract void run();

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((brushColor == null) ? 0 : brushColor.hashCode());
		result = PRIME * result + (brushDown ? 1231 : 1237);
		result = PRIME * result + ((color == null) ? 0 : color.hashCode());
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
		if (!(obj instanceof AbstractBuggle))
			return false;
		
		final AbstractBuggle other = (AbstractBuggle) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
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
