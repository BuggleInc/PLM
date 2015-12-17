package plm.universe.lightbot;

import java.awt.Point;
import java.io.BufferedWriter;

import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.universe.Direction;
import plm.universe.Entity;
import plm.universe.GridWorld;
import plm.universe.World;



public class LightBotEntity extends Entity  {
	private int x = 0;
	private int y = 0;

	Direction direction = Direction.NORTH;
	
	StackTraceElement[] tracedStack = new StackTraceElement[1];
	
	/** The PLM calls that constructor with no parameter, so it must exist  (but you probably don't want to use it yourself). */
	public LightBotEntity() {
		super();
	}

	/** That constructor is called by the exercises */
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
			getGame().getLogger().log("facing wall");
	}
	public void jump(){
		int heightHere = getCell().getHeight();
		int heightThere = getCellNeighbor(getDirection().toPoint()).getHeight();
		
		if ( (heightThere - heightHere  == 1 /*jump one up*/) || 
		     (heightHere > heightThere /*jump down*/))
			move();
		else 
			getGame().getLogger().log("cannot jump here");
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
		sf = (LightBotSourceFile) ((Exercise) getGame().getCurrentLesson().getCurrentExercise()).getSourceFile(Game.LIGHTBOT,0);
				
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

	@Override
	public void command(String command, BufferedWriter out) {
		// not used
		
	}
}
