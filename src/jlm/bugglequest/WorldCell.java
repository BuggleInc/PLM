package jlm.bugglequest;

import java.awt.Color;

import jlm.exception.AlreadyHaveBaggleException;


public class WorldCell {
	private Color color;

	private Color msgColor = DEFAULT_MSG_COLOR; 
	
	public static final Color DEFAULT_COLOR = Color.white;
	public static final Color DEFAULT_MSG_COLOR = new Color(0.5f,0.5f,0.9f);

	private Baggle baggle;
	
	private String content = "";
	
	private boolean leftWall;

	private boolean topWall;

	private World world;

	private int x;

	private int y;

	public WorldCell(World w, int x, int y) {
		this(w, x, y, DEFAULT_COLOR, false, false, null, "");
	}

	public WorldCell(WorldCell c) {
		this(c.world, c.x, c.y, c.color, c.leftWall, c.topWall, null, null);
		if (c.hasBaggle()) {
			Baggle b = new Baggle(c.getBaggle());
			b.setCell(this);
			this.baggle = b;
		}
		this.content = c.content;
	}

	public WorldCell(World w, int x, int y, Color color, boolean leftWall, boolean topWall) {
		this(w, x, y, color, leftWall, topWall, null, "");
	}

	public WorldCell(World w, int x, int y, Color c, boolean leftWall, boolean topWall, Baggle baggle, String content) {
		this.world = w;
		this.x = x;
		this.y = y;
		this.color = c;
		this.leftWall = leftWall;
		this.topWall = topWall;
		this.baggle = baggle;
		this.content = content;
	}

	public void setColor(Color c) {
		this.color = c;
		world.notifyWorldUpdatesListeners();
	}

	public Color getColor() {
		return this.color;
	}
	
	public void setMsgColor(Color c) {
		this.msgColor = c;
		world.notifyWorldUpdatesListeners();
	}
	
	public Color getMsgColor() {
		return this.msgColor;
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
	public String toString() {
		String cell;
		if (hasContent()) 
			cell = this.content;
		if (hasBaggle())
			cell = "o";
		else if (color.equals(DEFAULT_COLOR))
			cell = " ";
		else
			cell = "?";
		return cell;
	}

	public boolean hasTopWall() {
		return this.topWall;
	}

	public boolean hasLeftWall() {
		return this.leftWall;
	}

	public boolean hasBaggle() {
		return this.baggle != null;
	}

	public Baggle getBaggle() {
		return this.baggle;
	}

	public void newBaggle() throws AlreadyHaveBaggleException {
		//Logger.log("WorldCell:newBaggle", "");
		if (this.baggle != null) 
			throw new AlreadyHaveBaggleException("This cell already contains a baggle");
		this.baggle = new Baggle(this);
		world.notifyWorldUpdatesListeners();		
	}
	
	public void newBaggle(Color c) throws AlreadyHaveBaggleException {
		if (this.baggle != null) 
			throw new AlreadyHaveBaggleException("This cell already contains a baggle");
		this.baggle = new Baggle(this,c);
		world.notifyWorldUpdatesListeners();
	}
	
	public void setBaggle(Baggle b) throws AlreadyHaveBaggleException {
		if (this.baggle != null) 
			throw new AlreadyHaveBaggleException("This cell already contains a baggle");
		this.baggle = b;
		world.notifyWorldUpdatesListeners();
	}

	public Baggle pickUpBaggle() {
		Baggle b = this.baggle;
		this.baggle.setCell(null);
		baggle = null;		
		world.notifyWorldUpdatesListeners();
		return b;
	}
	
	public boolean hasContent() {
		return (!this.content.equals(""));
	}

	public String getContent() {
		return this.content;
	}
	
	public void addContent(String c) {
		this.content += c;
		world.notifyWorldUpdatesListeners();
	}
	public void emptyContent() {
		this.content = "";
		world.notifyWorldUpdatesListeners();		
	}
		
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setWorld(World w) {
		this.world = w;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((baggle == null) ? 0 : baggle.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + (leftWall ? 1231 : 1237);
		result = prime * result + (topWall ? 1231 : 1237);
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorldCell other = (WorldCell) obj;
		//if (hasBaggle() != other.hasBaggle())
		//	return false;
		if (baggle == null) {
			if (other.baggle != null)
				return false;
		} else if (!baggle.equals(other.baggle))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (!content.equals(other.content))
			return false;
		if (leftWall != other.leftWall)
			return false;
		if (topWall != other.topWall)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
