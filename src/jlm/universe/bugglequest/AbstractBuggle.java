package jlm.universe.bugglequest;

import java.awt.Color;
import java.awt.Point;

import jlm.universe.Direction;
import jlm.universe.Entity;
import jlm.universe.GridWorld;
import jlm.universe.World;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import jlm.universe.bugglequest.exception.BuggleInOuterSpace;
import jlm.universe.bugglequest.exception.BuggleWallException;
import jlm.universe.bugglequest.exception.NoBaggleUnderBuggleException;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;




public abstract class AbstractBuggle extends Entity {
	@Attribute
	Color color;
	
	@Attribute
	Color brushColor;

	@Attribute
	private int x;
	@Attribute
	private int y;

	@Attribute
	Direction direction;

	@Attribute
	boolean brushDown;

	@Element
	private Baggle baggle;

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
	 * being not null. After using it, {@link Entity#setWorld(BuggleWorld)} must be used
	 * ASAP.
	 */
	public AbstractBuggle() {
		this(null, "John Doe", 0, 0, Direction.NORTH, Color.red, Color.red);
	}

	public AbstractBuggle(BuggleWorld w) {
		this(w, "John Doe", 0, 0, Direction.NORTH, Color.red, Color.red);
	}

	public AbstractBuggle(BuggleWorld world, String name, int x, int y, Direction direction, Color c) {
		this(world, name, 0, 0, Direction.NORTH, c, c);
	}

	public AbstractBuggle(World world, String name, int x, int y, Direction direction, Color color, Color brushColor) {
		super(name,world);
		this.color = color;
		this.brushColor = brushColor;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	@Override
	public void copy(Entity e) {
		super.copy(e);
		AbstractBuggle other = (AbstractBuggle)e;
		this.color = other.color;
		this.brushColor = other.brushColor;
		this.x = other.x;
		this.y = other.y;
		this.direction = other.direction;
	}
	@Override
	public Entity copy() {
		return new Buggle(this);
	}

	public boolean isBrushDown() {
		return brushDown;
	}

	public void brushDown() {
		this.brushDown = true;
		BuggleWorldCell cell = (BuggleWorldCell) ((BuggleWorld)world).getCell(x, y);
		cell.setColor(brushColor);
		world.notifyWorldUpdatesListeners();
	}

	public void brushUp() {
		this.brushDown = false;
	}

	public Color getGroundColor() {
		return getCell().getColor();
	}

	public Color getBrushColor() {
		return brushColor;
	}

	public void setBrushColor(Color c) {
		if (c != null)
			brushColor = c;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		if (c != null) {
			this.color = c;
			world.notifyWorldUpdatesListeners();
		}
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
	protected BuggleWorldCell getCell(){
		return (BuggleWorldCell) ((GridWorld)world).getCell(x, y);
	}
	protected BuggleWorldCell getCell(int u, int v) throws BuggleInOuterSpace{
		BuggleWorld bw = (BuggleWorld) world;
		if (y>=bw.getHeight())
			throw new BuggleInOuterSpace("You tried to access a cell with Y="+y+", but the maximal Y in this world is "+(bw.getHeight()-1));
		if (x>=bw.getWidth())
			throw new BuggleInOuterSpace("You tried to access a cell with X="+x+", but the maximal X in this world is "+(bw.getWidth()-1));

		return (BuggleWorldCell) ((GridWorld)world).getCell(u, v);
	}
	protected BuggleWorldCell getCellFromLesson(int u, int v) {
		try {
			return getCell(u,v);
		} catch (BuggleInOuterSpace e) {
			throw new RuntimeException("Broken lesson: you accessed a cell in outer space",e);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) throws BuggleInOuterSpace {
		BuggleWorld bw = (BuggleWorld) world;
		if (x>=bw.getWidth())
			throw new BuggleInOuterSpace("You tried to set X to "+x+", but the maximal X in this world is "+(bw.getWidth()-1));
		this.x = x;
		stepUI();
	}
	public void setXFromLesson(int x)  {
		try {
			setX(x);
		} catch (BuggleInOuterSpace e) {
			throw new RuntimeException("Broken lesson: you moved to outer space",e);
		}
	}

	public int getY() {
		return y;
	}

	public void setY(int y) throws BuggleInOuterSpace  {
		BuggleWorld bw = (BuggleWorld) world;
		if (y>=bw.getHeight())
			throw new BuggleInOuterSpace("You tried to set Y to "+y+", but the maximal Y in this world is "+(bw.getHeight()-1));
		this.y = y;
		stepUI();
	}
	public void setYFromLesson(int y)  {
		try {
			setY(y);
		} catch (BuggleInOuterSpace e) {
			throw new RuntimeException("Broken lesson: you moved to outer space",e);
		}
	}

	public void setPos(int x, int y) throws BuggleInOuterSpace {
		BuggleWorld bw = (BuggleWorld) world;
		if (y>=bw.getHeight())
			throw new BuggleInOuterSpace("You tried to set Y to "+y+", but the maximal Y in this world is "+(bw.getHeight()-1));
		if (x>=bw.getWidth())
			throw new BuggleInOuterSpace("You tried to set X to "+x+", but the maximal X in this world is "+(bw.getWidth()-1));
		this.x = x;
		this.y = y;
		stepUI();
	}
	public void setPosFromLesson(int x, int y)  {
		try {
			setPos(x,y);
		} catch (BuggleInOuterSpace e) {
			throw new RuntimeException("Broken lesson: you moved to outer space",e);
		}
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

		BuggleWorldCell cell;
		switch (delta.intValue()) {
		case Direction.NORTH_VALUE: /* looking up is easy */
			cell = getCell();
			return cell.hasTopWall();

		case Direction.WEST_VALUE: /* looking to the left also */
			cell = getCell();
			return cell.hasLeftWall();

		case Direction.SOUTH_VALUE: /* if looking down, look to the top of one cell lower */
			cell = getCellFromLesson(getX(),                        (getY()+1) % getWorldHeight());
			return cell.hasTopWall();

		case Direction.EAST_VALUE: /* if looking right, look to the left of one next cell */
			cell = getCellFromLesson((getX()+1) % getWorldWidth(), getY());
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

		if (brushDown) {
			getCell().setColor(brushColor);
		}

		stepUI();
	}

	public boolean isOverBaggle() {
		return getCellFromLesson(this.x, this.y).hasBaggle();
	}

	public boolean isCarryingBaggle() {
		return this.baggle != null;
	}

	public void pickUpBaggle() throws NoBaggleUnderBuggleException, AlreadyHaveBaggleException {
		if (!isOverBaggle())
			throw new NoBaggleUnderBuggleException("There is no baggle to pick up");
		if (isCarryingBaggle())
			throw new AlreadyHaveBaggleException("Your buggle is already carrying a baggle");
		baggle = getCellFromLesson(this.x, this.y).pickUpBaggle();
	}

	public void dropBaggle() throws AlreadyHaveBaggleException {
		getCellFromLesson(this.x, this.y).setBaggle(this.baggle);
		baggle = null;
	}

	public boolean isOverMessage() {
		return getCell().hasContent();
	}

	public void writeMessage(String msg) {
		getCell().addContent(msg);
	}
	public void writeMessage(int nb) {
		writeMessage(""+nb);
	}

	public String readMessage() {
		return getCell().getContent();
	}

	public void clearMessage() {
		getCell().emptyContent();
	}



	@Override
	public String toString() {
		return "Buggle (" + this.getClass().getName() + "): x=" + x + " y=" + y + " Direction:" + direction + " Color:"
		+ color;
	}

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
	public String diffTo(AbstractBuggle other) {
		StringBuffer sb = new StringBuffer();
		if (getX() != other.getX() || getY() != other.getY()) 
			sb.append("    Its position is ("+other.getX()+","+other.getY()+"); expected: ("+getX()+","+getY()+").\n");
		if (getDirection() != other.getDirection()) 
			sb.append("    Its direction is "+other.getDirection()+"; expected: "+getDirection()+".\n");
		if (getColor() != other.getColor()) 
			sb.append("    Its color is "+other.getColor()+"; expected: "+getColor()+".\n");
		if (getBrushColor() != other.getBrushColor())
			sb.append("    The color of its brush is "+other.getBrushColor()+"; expected: "+getBrushColor()+".\n");
		if (isCarryingBaggle() && !other.isCarryingBaggle())
			sb.append("    It should not carry that baggle.\n");
		if (!isCarryingBaggle() && other.isCarryingBaggle())
			sb.append("    It is not carrying any baggle.\n");
		if (haveSeenError() && !other.haveSeenError())
			sb.append("    It encountered an issue, such as bumping into a wall.\n");
		if (!haveSeenError() && other.haveSeenError())
			sb.append("    It didn't encounter any issue, such as bumping into a wall.\n");
		return sb.toString();
	}
}
