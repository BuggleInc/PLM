package plm.universe.bugglequest;

import java.awt.Color;
import java.awt.Point;

import plm.core.model.Game;
import plm.universe.Direction;
import plm.universe.Entity;
import plm.universe.GridWorld;
import plm.universe.World;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import plm.universe.bugglequest.exception.BuggleInOuterSpaceException;
import plm.universe.bugglequest.exception.BuggleWallException;
import plm.universe.bugglequest.exception.NoBaggleUnderBuggleException;

public abstract class AbstractBuggle extends Entity {
	int k_val = 0;
	int[] k_seq = {0,0, 1,1, 2,3, 2,3, 4,5};
	
	Color color;
	
	Color brushColor;

	private int x;
	private int y;

	Direction direction;

	boolean brushDown;

	private Baggle baggle;

	/* This is for the simple buggle to indicate that it did hit a wall, and is thus not a valid
	 * candidate for exercise completion.
	 */
	private boolean seenError = false;
	public void seenError() {
		this.seenError = true;
	}
	public void seenError(String msg) {
		System.err.println(msg);
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
		if (k_seq[k_val]==4) k_val++; else k_val = 0;
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
		if (brushDown) // mark the ground
			brushDown();
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

	public void left() {
		if (k_seq[k_val]==2) k_val++; else k_val = 0;
		setDirection(direction.left());
	}

	public void right() {
		if (k_seq[k_val]==3) k_val++; else k_val = 0;
		setDirection(direction.right());
	}

	public void back() {
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
	protected BuggleWorldCell getCell(int u, int v) throws BuggleInOuterSpaceException{
		BuggleWorld bw = (BuggleWorld) world;
		if (y>=bw.getHeight())
			throw new BuggleInOuterSpaceException(Game.i18n.tr("You tried to access a cell with Y={0}, but the maximal Y in this world is {1}.",y,(bw.getHeight()-1)));
		if (x>=bw.getWidth())
			throw new BuggleInOuterSpaceException(Game.i18n.tr("You tried to access a cell with X={0}, but the maximal X in this world is {1}.",x,(bw.getWidth()-1)));

		return (BuggleWorldCell) ((GridWorld)world).getCell(u, v);
	}
	protected BuggleWorldCell getCellFromLesson(int u, int v) {
		try {
			return getCell(u,v);
		} catch (BuggleInOuterSpaceException e) {
			throw new RuntimeException("Broken lesson: you accessed a cell in outer space",e);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) throws BuggleInOuterSpaceException {
		BuggleWorld bw = (BuggleWorld) world;
		if (x>=bw.getWidth())
			throw new BuggleInOuterSpaceException(Game.i18n.tr("You tried to set X to {0}, but the maximal X in this world is {1}.",x,(bw.getWidth()-1)));
		this.x = x;
		stepUI();
	}
	public void setXFromLesson(int x)  {
		try {
			setX(x);
		} catch (BuggleInOuterSpaceException e) {
			throw new RuntimeException("Broken lesson: you moved to outer space",e);
		}
	}

	public int getY() {
		return y;
	}

	public void setY(int y) throws BuggleInOuterSpaceException  {
		BuggleWorld bw = (BuggleWorld) world;
		if (y>=bw.getHeight())
			throw new BuggleInOuterSpaceException(Game.i18n.tr("You tried to set Y to {0}, but the maximal Y in this world is {1}.",y,(bw.getHeight()-1)));
		this.y = y;
		stepUI();
	}
	public void setYFromLesson(int y)  {
		try {
			setY(y);
		} catch (BuggleInOuterSpaceException e) {
			throw new RuntimeException("Broken lesson: you moved to outer space",e);
		}
	}

	public void setPos(int x, int y) throws BuggleInOuterSpaceException {
		BuggleWorld bw = (BuggleWorld) world;
		if (y>=bw.getHeight())
			throw new BuggleInOuterSpaceException(Game.i18n.tr("You tried to set Y to {0}, but the maximal Y in this world is {1}.",y,(bw.getHeight()-1)));
		if (x>=bw.getWidth())
			throw new BuggleInOuterSpaceException(Game.i18n.tr("You tried to set X to {0}, but the maximal X in this world is {1}.",x,(bw.getWidth()-1)));
		this.x = x;
		this.y = y;
		stepUI();
	}
	public void setPosFromLesson(int x, int y)  {
		try {
			setPos(x,y);
		} catch (BuggleInOuterSpaceException e) {
			throw new RuntimeException("Broken lesson: you moved to outer space",e);
		}
	}

	public void forward() throws BuggleWallException {
		if (k_seq[k_val]==0) k_val++; else k_val = 0;
		move(direction.toPoint());
	}

	public void forward(int count) throws BuggleWallException {
		for (int i = 0; i < count; i++)
			forward();
	}

	public void backward() throws BuggleWallException {
		if (k_seq[k_val]==1) k_val++; else k_val = 0;
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

			throw new BuggleWallException();

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

	@Deprecated
	public void pickUpBaggle() throws NoBaggleUnderBuggleException, AlreadyHaveBaggleException {
		pickupBaggle();
	}
	public void pickupBaggle() throws NoBaggleUnderBuggleException, AlreadyHaveBaggleException {
		if (k_seq[k_val]==5) k_val++; else k_val = 0;
		if (k_val>k_seq.length-1) {
			setName("Easter "+name);
			System.out.println("EASTEEEER");
			((BuggleWorld)world).easter= true;
			k_val=0;
			return;
		}

		if (!isOverBaggle())
			throw new NoBaggleUnderBuggleException(Game.i18n.tr("There is no baggle to pick up here."));
		if (isCarryingBaggle())
			throw new AlreadyHaveBaggleException(Game.i18n.tr("Your are already carrying a baggle."));
		baggle = getCellFromLesson(this.x, this.y).pickupBaggle();
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
		if (other == null) 
			return Game.i18n.tr("Its value is 'null', which is never good.");
		/* We cannot use a i18n defined in our class, as we have to pass the classname to the initialization of i18n, 
		 *    but gettext don't seem to like the fact that we generate at runtime some package names that it does not know at compile time.
		 * So, use Game.i18n instead.
		 */
		StringBuffer sb = new StringBuffer();
		if (getX() != other.getX() || getY() != other.getY()) 
			sb.append(Game.i18n.tr("    Its position is ({0},{1}); expected: ({2},{3}).\n",other.getX(),other.getY(),getX(),getY()));
		if (getDirection() != other.getDirection()) 
			sb.append(Game.i18n.tr("    Its direction is {0}; expected: {1}.\n",other.getDirection(),getDirection()));
		if (getColor() != other.getColor()) 
			sb.append(Game.i18n.tr("    Its color is {0}; expected: {1}.\n",other.getColor(),getColor()));
		if (getBrushColor() != other.getBrushColor())
			sb.append(Game.i18n.tr("    The color of its brush is {0}; expected: {1}.\n",other.getBrushColor(),getBrushColor()));
		if (isCarryingBaggle() && !other.isCarryingBaggle())
			sb.append(Game.i18n.tr("    It should not carry that baggle.\n"));
		if (!isCarryingBaggle() && other.isCarryingBaggle())
			sb.append(Game.i18n.tr("    It is not carrying any baggle.\n"));
		if (haveSeenError() && other.haveSeenError())
			sb.append(Game.i18n.tr("    It encountered an issue, such as bumping into a wall.\n"));
		if (haveSeenError() && !other.haveSeenError())
			sb.append(Game.i18n.tr("    It didn't encounter any issue, such as bumping into a wall.\n"));
		return sb.toString();
	}
	
	/* BINDINGS TRANSLATION: French */
	public void gauche()   { left(); }
	public void droite()   { right(); }
	public void retourne() { back(); }
	public void avance()          throws BuggleWallException { forward(); }
	public void avance(int steps) throws BuggleWallException { forward(steps); }
	public void recule()          throws BuggleWallException { backward(); }
	public void recule(int steps) throws BuggleWallException { backward(steps); }
	public Color getCouleur()             { return getColor(); }
	public void setCouleur(Color c)       { setColor(c); }
	public boolean estFaceMur()           { return isFacingWall(); }
	public boolean estDosMur()            { return isBackingWall(); }
	public void leveBrosse()              { brushUp(); }
	public void baisseBrosse()            { brushDown(); }
	public boolean estBrosseBaissee()     { return isBrushDown(); }
	public Color getCouleurBrosse()       { return getBrushColor(); }
	public void setCouleurBrosse(Color c) { setBrushColor(c); }
	public Color getCouleurSol()          { return getGroundColor(); }
	public boolean estSurBiscuit()        { return isOverBaggle(); }
	public boolean porteBiscuit()         { return isCarryingBaggle(); }
	public void prendBiscuit() throws AlreadyHaveBaggleException, NoBaggleUnderBuggleException { pickupBaggle(); }
	public void poseBiscuit()  throws AlreadyHaveBaggleException                               { dropBaggle(); }
	public boolean estSurMessage()        { return isOverMessage(); }
	public String litMessage()            { return readMessage(); }
	public void ecritMessage(String s)    { writeMessage(s); }
	public void ecritMessage(int i)       { writeMessage(i); }
	public void effaceMessage()           { clearMessage(); }
	public int getMondeHauteur()          { return getWorldHeight(); }
	public int getMondeLargeur()          { return getWorldWidth(); }
	// get/set X/Y/Pos are not translated as they happen to be the same in French
	public boolean estChoisi()           { return isSelected(); } // we have to document the version without e, since po4a allows for one variant only
	public boolean estChoisie()          { return isSelected(); } // But we want to have the grammatically correct form also possible (Buggles are feminine in French)
	
}
