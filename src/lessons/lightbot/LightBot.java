package lessons.lightbot;

import java.awt.Point;

import jlm.core.Game;
import jlm.lesson.SourceFile;
import jlm.universe.Entity;
import jlm.universe.World;

import org.simpleframework.xml.Attribute;

import universe.bugglequest.Direction;


public class LightBot extends Entity {
	@Attribute
	private int x;
	@Attribute
	private int y;

	@Attribute
	Direction direction;
	
	/**
	 * Constructor with no argument so that child classes can avoid declaring a
	 * constructor. But it should not be used as most methods assert on world
	 * being not null. After using it, {@link #setWorld(LightBotWorld)} must be used
	 * ASAP.
	 */
	public LightBot() {
		this(null, "John Doe", 0, 0, Direction.NORTH);
	}

	public LightBot(LightBotWorld w) {
		this(w, "John Doe", 0, 0, Direction.NORTH);
	}

	public LightBot(World world, String name, int x, int y, Direction direction2) {
		super(name,world);
		this.x = x;
		this.y = y;
		this.direction = direction2;
	}
	@Override
	public void copy(Entity e) {
		super.copy(e);
		LightBot other = (LightBot)e;
		this.x = other.x;
		this.y = other.y;
		this.direction = other.direction;
	}
	@Override
	public Entity copy() {
		LightBot lb = new LightBot();
		lb.setWorld(getWorld());
		lb.setName(getName());
		lb.setPos(getX(), getY());
		lb.setDirection(direction);
		return lb;
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
	
	public int getWorldHeight() {
		return ((LightBotWorld)world).getHeight();
	}
	
	public int getWorldWidth() {
		return ((LightBotWorld)world).getWidth();
	}
	protected LightBotWorldCell getCell(){
		return ((LightBotWorld)world).getCell(x, y);
	}
	protected LightBotWorldCell getCell(int u, int v){
		return ((LightBotWorld)world).getCell(u, v);
	}
	private LightBotWorldCell getCellNeighbor(Point delta) {
		return getCell( (x+delta.x)%getWorldWidth() , (y+delta.y)%getWorldHeight());
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

	public void forward() {
		move(direction.toPoint());
	}

	public void forward(int count) {
		for (int i = 0; i < count; i++)
			forward();
	}

	public void backward() {
		move(direction.opposite().toPoint());
	}

	public void backward(int count) {
		for (int i = 0; i < count; i++)
			backward();
	}

	private boolean lookAtWall(boolean forward) {
		return false;
		/*
		Direction delta;
		if (forward)
			delta = getDirection();
		else
			delta = getDirection().opposite();

		LightBotWorldCell cell;
		switch (delta.intValue()) {
		case Direction.NORTH_VALUE: /* looking up is easy * /
			cell = getCell();
			return cell.hasTopWall();

		case Direction.WEST_VALUE: /* looking to the left also * /
			cell = getCell();
			return cell.hasLeftWall();

		case Direction.SOUTH_VALUE: /* if looking down, look to the top of one cell lower * /
			cell = getCell(getX(),                        (getY()+1) % getWorldHeight());
			return cell.hasTopWall();

		case Direction.EAST_VALUE: /* if looking right, look to the left of one next cell * /
			cell = getCell((getX()+1) % getWorldWidth(), getY());
			return cell.hasLeftWall();

		default: throw new RuntimeException("Invalid direction: "+delta);
		}*/
	}
	public boolean isFacingWall() {
		return lookAtWall(true);
	}
	public boolean isBackingWall() {
		return lookAtWall(false);
	}
	public boolean canWalk(){
		return getCellNeighbor(getDirection().toPoint()).getHeight() == getCell().getHeight();
	}

	private void move(Point delta) {
		int newx = (x + delta.x) % getWorldWidth();
		if (newx < 0)
			newx += getWorldWidth();
		int newy = (y + delta.y) % getWorldHeight();
		if (newy < 0)
			newy += getWorldHeight();

		if (delta.equals(direction.toPoint())            && isFacingWall() ||
				delta.equals(direction.opposite().toPoint()) && isBackingWall()) {	

			/* do nothing: we cannot traverse walls. TODO: We should do a specific animation */
		} else {
			x = newx;
			y = newy;
		}

		stepUI();
	}




	@Override
	public String toString() {
		return "LightBot (" + this.getClass().getName() + "): x=" + x + " y=" + y + " Direction:" + direction;
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
		if (!(obj instanceof LightBot))
			return false;
		
		final LightBot other = (LightBot) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public void run() {
		SourceFile sf = Game.getInstance().getCurrentLesson().getCurrentExercise().getPublicSourceFile(0);
		if (sf == null) {
			System.err.println("No source file available. Broken exercise.");
			return;
		}
		
		System.out.println("Interpretation of file "+sf.getName()+". Body:\n"+sf.getCompilableContent());		
	}

}
