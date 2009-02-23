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
	public int getY() {
		return y;
	}
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
		stepUI();
	}
	public void forward() {
		if (getCellNeighbor(getDirection().toPoint()).getHeight() == getCell().getHeight())
			move();
		else 
			System.out.println("facing wall");
	}
	public void jump(){
		if (Math.abs(getCellNeighbor(getDirection().toPoint()).getHeight() - getCell().getHeight()) == 1)
			move();
		else 
			System.out.println("not facing steps");
	}

	private void move() {
		int newx = (x + getDirection().toPoint().x) % getWorldWidth();
		if (newx < 0)
			newx += getWorldWidth();
		int newy = (y + getDirection().toPoint().y) % getWorldHeight();
		if (newy < 0)
			newy += getWorldHeight();
		x=newx;
		y=newy;
		stepUI();
	}

	public void left() {
		direction=getDirection().left();
		stepUI();
	}
	public void right() {
		direction=getDirection().right();
		stepUI();
	}
	public void light() {
		((LightBotWorld) world).switchLight(x,y);
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

	private class SyntaxErrorException extends Exception {
		private static final long serialVersionUID = 1L;

		public SyntaxErrorException(String s, String location) {
			super(location+": Syntax error: "+s);
		}
	}

	private enum InstructionKind {
		FORWARD,JUMP,LIGHT,LEFT,RIGHT,F1,F2;		
	}
	private class Instruction {

		InstructionKind kind;
		String loc;
		public Instruction(String s,String location) throws SyntaxErrorException {
			loc=location;
			String read = s.replaceAll("^[ \t]*", "").replaceAll("[ \t]*$", "");
			
			if (read.equalsIgnoreCase("forward")) {
				kind=InstructionKind.FORWARD;
			} else if (read.equalsIgnoreCase("jump")) {
				kind=InstructionKind.JUMP;
			} else if (read.equalsIgnoreCase("light")) {
				kind=InstructionKind.LIGHT;
			} else if (read.equalsIgnoreCase("left")) {
				kind=InstructionKind.LEFT;
			} else if (read.equalsIgnoreCase("right")) {
				kind=InstructionKind.RIGHT;
			} else if (read.equalsIgnoreCase("f1")) {
				kind=InstructionKind.F1;
			} else if (read.equalsIgnoreCase("f2")) {
				kind=InstructionKind.F2;
			} else {
				throw new SyntaxErrorException(s,location); 
			}
		}
		public void run() {
			System.err.println("Execute "+kind+" at "+LightBot.this.getX()+","+LightBot.this.getY());
			switch (kind) {
			case FORWARD: forward(); break;
			case JUMP: jump(); break;
			case LEFT: left(); break;
			case RIGHT: right(); break;
			case LIGHT: light(); break;
			case F1: LightBot.this.run(func1); break;
			case F2: LightBot.this.run(func2); break;
			}
		}
	}
	private Instruction[] parseFile(SourceFile sf,int maxSize) throws SyntaxErrorException {
		if (sf == null)
			return null;
		Instruction[] res = new Instruction[maxSize];

		String[] lines = sf.getCompilableContent().split("\n| |\t");
		int lineNumber=1;
		for (String l: lines) {
			res[lineNumber] = new Instruction(l,sf.getName()+":"+lineNumber);
			lineNumber++;
		}
		return res;
	}

	Instruction[] main;
	Instruction[] func1;
	Instruction[] func2;
	@Override
	public void run() {
		SourceFile sf;
		
		/* Parse every functions */
		try {
			sf = Game.getInstance().getCurrentLesson().getCurrentExercise().getPublicSourceFile("main");
			main=parseFile(sf, 12);

			sf = Game.getInstance().getCurrentLesson().getCurrentExercise().getPublicSourceFile("function 1");
			func1=parseFile(sf, 8);

			sf = Game.getInstance().getCurrentLesson().getCurrentExercise().getPublicSourceFile("function 2");
			func2=parseFile(sf, 8);
		} catch (SyntaxErrorException e) {
			System.err.println(e.getMessage());
		}
		
		/* Run main */
		run(main);
	}

	private void run(Instruction[] file) {
		for (Instruction i: file)
			if (i!=null)
				i.run();
	}
}
