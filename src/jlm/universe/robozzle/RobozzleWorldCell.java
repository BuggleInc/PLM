package jlm.universe.robozzle;

import jlm.universe.GridWorld;
import jlm.universe.GridWorldCell;

public class RobozzleWorldCell extends GridWorldCell {
	private boolean hasStar=false;

	private char color = ' ';

	private boolean leftWall;

	private boolean topWall;

	public RobozzleWorldCell(GridWorld w, int x, int y) {
		this(w, x, y, false);
	}
	
	public RobozzleWorldCell(RobozzleWorld w, int x, int y) {
		this(w, x, y, ' ', false, false, false);
	}

	public RobozzleWorldCell(RobozzleWorldCell c, GridWorld w) {
		this((RobozzleWorld) w, c.x, c.y, c.color, c.leftWall, c.topWall, c.hasStar);
	}

	public RobozzleWorldCell(RobozzleWorld w, int x, int y, char color, boolean leftWall, boolean topWall) {
		this(w, x, y, color, leftWall, topWall, false);
	}

	public RobozzleWorldCell(RobozzleWorld w, int x, int y, char c, boolean leftWall, boolean topWall, boolean hasStar) {
		super(w,x,y);
		this.color = c;
		this.leftWall = leftWall;
		this.topWall = topWall;
		this.hasStar = hasStar;
	}

	@Override
	public GridWorldCell copy(GridWorld world) {
		return new RobozzleWorldCell(this,world);
	}

	public RobozzleWorldCell(GridWorld w, int x, int y, boolean star) {
		super(w,x,y);
		this.hasStar = star;
	}

	public void addStar() {
		this.hasStar = true;
		world.notifyWorldUpdatesListeners();
	}
	public void removeStar() {
		this.hasStar = false;
		world.notifyWorldUpdatesListeners();
	}

	public boolean hasStar() {
		return hasStar;
	}

	public void setColor(char color) {
		this.color = color;
		world.notifyWorldUpdatesListeners();
	}	
	
	public char getColor() {
		return this.color;
	}

	public boolean hasTopWall() {
		return this.topWall;
	}

	public boolean hasLeftWall() {
		return this.leftWall;
	}

	public void putTopWall() {
		this.topWall = true;
		world.notifyWorldUpdatesListeners();
	}

	public void removeTopWall() {
		this.topWall = false;
		world.notifyWorldUpdatesListeners();
	}

	public void putLeftWall() {
		this.leftWall = true;
		world.notifyWorldUpdatesListeners();
	}

	public void removeLeftWall() {
		this.leftWall = false;
		world.notifyWorldUpdatesListeners();
	}
	

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RobozzleWorldCell other = (RobozzleWorldCell) obj;
		if (hasStar != other.hasStar)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (color != other.color)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RBCell[x:"+this.x+";y:"+this.y+";color:"+this.color+";star:"+(hasStar?"yes":"none")+"]";
	}	
}
