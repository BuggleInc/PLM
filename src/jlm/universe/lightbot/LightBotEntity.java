package jlm.universe.lightbot;

import java.awt.Point;

import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.universe.Direction;
import jlm.universe.Entity;
import jlm.universe.GridWorld;
import jlm.universe.World;

import org.simpleframework.xml.Attribute;


public class LightBotEntity extends Entity  {
	@Attribute
	private int x;
	@Attribute
	private int y;

	@Attribute
	Direction direction;
	
	StackTraceElement[] tracedStack = new StackTraceElement[1];
	
	/**
	 * Constructor with no argument so that child classes can avoid declaring a
	 * constructor. But it should not be used as most methods assert on world
	 * being not null. After using it, {@link jlm.universe.Entity#setWorld(LightBotWorld)} must be used
	 * ASAP.
	 */
	public LightBotEntity() {
		this(null, "John Doe", 0, 0, Direction.NORTH);
	}

	public LightBotEntity(GridWorld w) {
		this(w, "John Doe", 0, 0, Direction.NORTH);
	}

	public LightBotEntity(World world, String name, int x, int y, Direction direction2) {
		super(name,world);
		this.setX(x);
		this.setY(y);
		this.direction = direction2;
	}
	@Override
	public void copy(Entity e) {
		super.copy(e);
		LightBotEntity other = (LightBotEntity)e;
		this.setX(other.getX());
		this.setY(other.getY());
		this.direction = other.direction;
	}
	@Override
	public Entity copy() {
		LightBotEntity lb = new LightBotEntity();
		lb.setWorld(getWorld());
		lb.setName(getName());
		lb.setPos(getX(), getY());
		lb.setDirection(direction);
		return lb;
	}


	public void setDirection(Direction d) {
		direction=d;
	}
	public Direction getDirection() {
		return direction;
	}

	public int getWorldHeight() {
		return ((GridWorld)world).getHeight();
	}

	public int getWorldWidth() {
		return ((GridWorld)world).getWidth();
	}
	public LightBotWorldCell getCell(){
		return (LightBotWorldCell) ((GridWorld) world).getCell(getX(), getY());
	}
	protected LightBotWorldCell getCell(int u, int v){
		return (LightBotWorldCell) ((GridWorld) world).getCell(u, v);
	}
	private int bounded(int x,int max) {
		if (x<0)
			return 0;
		if (x>=max)
			return max-1;
		return x;
	}
	
	private LightBotWorldCell getCellNeighbor(Point delta) {
		return getCell( bounded((getX()+delta.x),getWorldWidth()) , bounded((getY()+delta.y),getWorldHeight()));
	}


	public void setPos(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	public void forward() {
		if (getCellNeighbor(getDirection().toPoint()).getHeight() == getCell().getHeight())
			move();
		else 
			System.out.println("facing wall");
	}
	public void jump(){
		int heightHere = getCell().getHeight();
		int heightThere = getCellNeighbor(getDirection().toPoint()).getHeight();
		
		if ( (heightThere - heightHere  == 1 /*jump one up*/) || 
		     (heightHere > heightThere /*jump down*/))
			move();
		else 
			System.out.println("cannot jump here");
	}

	private void move() {
		int newx = bounded(getX() + getDirection().toPoint().x , getWorldWidth());
		int newy = bounded(getY() + getDirection().toPoint().y , getWorldHeight());
		setX(newx);
		setY(newy);
	}

	public void left() {
		direction=getDirection().left();
	}
	public void right() {
		direction=getDirection().right();
	}
	public void light() {
		((LightBotWorld) world).switchLight(getX(),getY());
	}
	

	@Override
	public String toString() {
		return "LightBot (" + this.getClass().getName() + "): x=" + getX() + " y=" + getY() + " Direction:" + direction;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((direction == null) ? 0 : direction.hashCode());
		result = PRIME * result + getX();
		result = PRIME * result + getY();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LightBotEntity))
			return false;

		final LightBotEntity other = (LightBotEntity) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (getX() != other.getX())
			return false;
		if (getY() != other.getY())
			return false;
		return true;
	}



	LightBotSourceFile sf;
	@Override
	public void run() {
		sf = (LightBotSourceFile) ((Exercise) Game.getInstance().getCurrentLesson().getCurrentExercise()).getPublicSourceFile(Game.LIGHTBOT,"Code");
				
		/* Run main */
		run("main",sf.getMain());
		tracedStack[0] = new StackTraceElement("LightbotEntity","main","main",1);
		fireStackListener();
	}
	public void runF1(){
		run("func1",sf.getFunc1());
	}
	public void runF2(){
		run("func2",sf.getFunc2());
	}

	private void run(String fileName,LightBotInstruction[] file) {
		if (file == null)
			return;
		int line=0;
		for (LightBotInstruction i: file) {
			tracedStack[0] = new StackTraceElement("LightbotEntity",fileName,fileName,line++);
			fireStackListener();
			if (i!=null && !i.isNoop())
			stepUI();
			if (i!=null)
				i.run(this);
		}
	}

	public void setX(int x) {
		this.x = x;
	}
	public int getX() {
		return x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}

	@Override
	public StackTraceElement[] getCurrentStack() {
		return tracedStack;
	}
}
