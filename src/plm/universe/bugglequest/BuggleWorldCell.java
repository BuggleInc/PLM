package plm.universe.bugglequest;

import java.awt.Color;

import org.xnap.commons.i18n.I18n;

import plm.core.utils.ColorMapper;
import plm.universe.GridWorld;
import plm.universe.GridWorldCell;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import plm.universe.bugglequest.exception.NoBaggleUnderBuggleException;

public class BuggleWorldCell extends GridWorldCell {
	private Color color;

	private Color msgColor = DEFAULT_MSG_COLOR; 
	
	public static final Color DEFAULT_COLOR = Color.white;
	public static final Color DEFAULT_MSG_COLOR = new Color(0.5f,0.5f,0.9f);
	public static final Color DEFAULT_BAGGLE_COLOR = new Color(0.82f,0.41f,0.12f);

	private boolean hasBaggle;
	
	private String content = "";
	
	private boolean leftWall;

	private boolean topWall;

	public BuggleWorldCell(BuggleWorld w, int x, int y) {
		this(w, x, y, DEFAULT_COLOR, false, false, false, "");
	}

	public BuggleWorldCell(BuggleWorldCell c, GridWorld w) {
		this((BuggleWorld) w, c.x, c.y, c.color, c.leftWall, c.topWall, c.hasBaggle(), null);
		this.content = new String(c.content);
	}
	public BuggleWorldCell copy(GridWorld w) {
		return new BuggleWorldCell(this,w);
	}

	public BuggleWorldCell(BuggleWorld w, int x, int y, Color color, boolean leftWall, boolean topWall) {
		this(w, x, y, color, leftWall, topWall, false, "");
	}

	public BuggleWorldCell(BuggleWorld w, int x, int y, Color c, boolean leftWall, boolean topWall, boolean baggle, String content) {
		super(w,x,y);
		this.color = c;
		this.leftWall = leftWall;
		this.topWall = topWall;
		this.hasBaggle = baggle;
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
		return hasBaggle;
	}

	public void baggleAdd() throws AlreadyHaveBaggleException {
		if (hasBaggle) 
			throw new AlreadyHaveBaggleException(getWorld().getGame().i18n.tr("There is already a baggle here."));
		hasBaggle = true;
		world.notifyWorldUpdatesListeners();		
	}
	
	public void baggleRemove() {
		if (!hasBaggle)
			throw new NoBaggleUnderBuggleException(getWorld().getGame().i18n.tr("There is no baggle to pick up here."));
		hasBaggle = false;
	}
	
	public boolean hasContent() {
		return (!this.content.equals(""));
	}

	public String getContent() {
		return this.content;
	}
	
	public void setContent(String c) {
		this.content = c;
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
		if (hasBaggle() != other.hasBaggle())
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
	/* This function is called as answer.diffTo(current) */
	public String diffTo(BuggleWorldCell current, I18n i18n) {
		StringBuffer sb = new StringBuffer();
		if (! hasBaggle && current.hasBaggle) 
			sb.append(i18n.tr(", there shouldn't be this baggle"));
		if (  hasBaggle && ! current.hasBaggle)
			sb.append(i18n.tr(", there should be a baggle"));
		if (color == null) {
			if (current.color != null)
				sb.append(i18n.tr(", the ground should not be {0}",ColorMapper.color2translated(current.color, i18n)));
		} else if (!color.equals(current.color)) {
			sb.append(i18n.tr(", the ground is expected to be {0}, but it is {1}", 
					ColorMapper.color2translated(color, i18n), ColorMapper.color2translated(current.color, i18n)));
		}
		if (!content.equals(current.content))
			sb.append(i18n.tr(", the ground reads ''{0}'' (expected: ''{1}'')", current.content, content));
		if (leftWall != current.leftWall)
			if (current.leftWall)
				sb.append(i18n.tr(", there shouldn't be any wall at west"));
			else
				sb.append(i18n.tr(", there should be a wall at west"));
		if (topWall != current.topWall)
			if (current.topWall)
				sb.append(i18n.tr(", there shouldn't be any wall at north"));
			else
				sb.append(i18n.tr(", there should be a wall at north"));
		return sb.toString();
	}
}
