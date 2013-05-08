package jlm.universe.bugglequest;

import java.awt.Color;

import jlm.universe.GridWorld;
import jlm.universe.GridWorldCell;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;



public class BuggleWorldCell extends GridWorldCell {
	private Color color;

	private Color msgColor = DEFAULT_MSG_COLOR; 
	
	public static final Color DEFAULT_COLOR = Color.white;
	public static final Color DEFAULT_MSG_COLOR = new Color(0.5f,0.5f,0.9f);

	private Baggle baggle;
	
	private String content = "";
	
	private boolean leftWall;

	private boolean topWall;

	public BuggleWorldCell(BuggleWorld w, int x, int y) {
		this(w, x, y, DEFAULT_COLOR, false, false, null, "");
	}

	public BuggleWorldCell(BuggleWorldCell c, GridWorld w) {
		this((BuggleWorld) w, c.x, c.y, c.color, c.leftWall, c.topWall, null, null);
		if (c.hasBaggle()) {
			Baggle b = new Baggle(c.getBaggle());
			b.setCell(this);
			this.baggle = b;
		}
		this.content = c.content;
	}
	public BuggleWorldCell copy(GridWorld w) {
		return new BuggleWorldCell(this,w);
	}

	public BuggleWorldCell(BuggleWorld w, int x, int y, Color color, boolean leftWall, boolean topWall) {
		this(w, x, y, color, leftWall, topWall, null, "");
	}

	public BuggleWorldCell(BuggleWorld w, int x, int y, Color c, boolean leftWall, boolean topWall, Baggle baggle, String content) {
		super(w,x,y);
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
	
	public int getContentAsInt() {
		return Integer.parseInt(this.content);
	}
	
	public void setContent(String c) {
		this.content = c;
		world.notifyWorldUpdatesListeners();
	}
	
	public void setContentFromInt(int i) {
		this.content = ""+i;
		world.notifyWorldUpdatesListeners();		
	}
	
	public void addContent(String c) {
		this.content += c;
		world.notifyWorldUpdatesListeners();
	}
	public void emptyContent() {
		this.content = "";
		world.notifyWorldUpdatesListeners();		
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
		BuggleWorldCell other = (BuggleWorldCell) obj;
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

	public String diffTo(BuggleWorldCell other) {
		StringBuffer sb = new StringBuffer();
		if (baggle == null && other.baggle != null) 
			sb.append(", there should be a baggle");
		if (baggle != null && other.baggle == null)
			sb.append(", there shouldn't be this baggle");
		if (baggle != null && other.baggle != null && !baggle.equals(other.baggle))
			sb.append(", the baggle differs");
		if (color == null) {
			if (other.color != null)
				sb.append(", the ground should be "+other.color);
		} else if (!color.equals(other.color))
			sb.append(", the ground is "+color+" (expected: "+other.color+")");
		if (!content.equals(other.content))
			sb.append(", the ground reads '"+content+" (expected: "+other.content+")");
		if (leftWall != other.leftWall)
			if (leftWall)
				sb.append(", there shouldn't be any wall at west");
			else
				sb.append(", there should be a wall at west");
		if (topWall != other.topWall)
			if (topWall)
				sb.append(", there shouldn't be any wall at north");
			else
				sb.append(", there should be a wall at north");
		return sb.toString();
	}
}
