package plm.universe.bugglequest;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.IOException;

import plm.core.utils.ColorMapper;
import plm.core.utils.InvalidColorNameException;
import plm.universe.Direction;
import plm.universe.Entity;
import plm.universe.GridWorld;
import plm.universe.World;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import plm.universe.bugglequest.exception.BuggleInOuterSpaceException;
import plm.universe.bugglequest.exception.BuggleWallException;
import plm.universe.bugglequest.exception.DontHaveBaggleException;
import plm.universe.bugglequest.exception.NoBaggleUnderBuggleException;

public abstract class AbstractBuggle extends Entity {
	int k_val = 0;
	int[] k_seq = {0,0, 1,1, 2,3, 2,3, 4,5};

	Color bodyColor = Color.red;
	Color brushColor = Color.red;
	
	private boolean dontIgnoreDirectionDifference = true; // if the buggle direction matters for world equality


	private int x = 0;
	private int y = 0;

	Direction direction = Direction.NORTH;

	boolean brushDown;

	private boolean carryBaggle;

	/* used to tell the observers what was changed */
	public static final int BRUSH_STATE = 0, BRUSH_COLOR = 1, BUGGLE_COLOR = 2;

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

	/** The PLM calls that constructor with no parameter, so it must exist (but you probably don't want to use it yourself). */
	public AbstractBuggle() {
		super();
	}

	/** That constructor is called by the exercises */
	public AbstractBuggle(World world, String name, int x, int y, Direction direction, Color color, Color brushColor) {
		super(name,world);
		this.bodyColor = color;
		this.brushColor = brushColor;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	@Override
	public void copy(Entity e) {
		super.copy(e);
		AbstractBuggle other = (AbstractBuggle)e;
		this.bodyColor = other.bodyColor;
		this.brushColor = other.brushColor;
		this.x = other.x;
		this.y = other.y;
		this.direction = other.direction;
		this.carryBaggle = other.carryBaggle;
	}

	public void penDown(){
		throw new RuntimeException(getGame().i18n.tr(
				"Sorry Dave, I cannot let you use penDown() here. Buggles have brushes, not pens. Use brushDown() instead."));
	}
	public void penUp(){
		throw new RuntimeException(getGame().i18n.tr(
				"Sorry Dave, I cannot let you use penUp() here. Buggles have brushes, not pens. Use brushUp() instead."));
	}

	public boolean isBrushDown() {
		return brushDown;
	}

	public void brushDown() {
		this.brushDown = true;
		addOperation(new ChangeBuggleBrushDown(this, false, true, getGame().i18n));
		BuggleWorldCell cell = (BuggleWorldCell) ((BuggleWorld)world).getCell(x, y);
		Color oldColor = getCell().getColor();
		addOperation(new ChangeCellColor(cell, oldColor, brushColor));
		cell.setColor(brushColor);
		world.notifyWorldUpdatesListeners();
		setChanged();
		notifyObservers(BRUSH_STATE);
	}

	public void brushUp() {
		if (k_seq[k_val]==4) k_val++; else k_val = 0;
		addOperation(new ChangeBuggleBrushDown(this, true, false, getGame().i18n));
		this.brushDown = false;
		setChanged();
		notifyObservers(BRUSH_STATE);
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
		setChanged();
		notifyObservers(BRUSH_COLOR);
	}

	public Color getBodyColor() {
		return bodyColor;
	}

	public void setBodyColor(Color c) {
		if (c != null) {
			this.bodyColor = c;
			world.notifyWorldUpdatesListeners();
			setChanged();
			notifyObservers(BUGGLE_COLOR);
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		if (direction != null) {
			addOperation(new ChangeBuggleDirection(this, this.direction, direction, getGame().i18n));
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
	
	// Make sure that the case issue is detected in Scala by overriding the Left() and Right() methods (see #236)
	public void Left() { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use Left() with an uppercase. Use left() instead."));
	}
	public void Right() {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use Right() with an uppercase. Use right() instead."));
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
		if(y<0) {
			String message = getGame().i18n.tr("You tried to access a cell with Y={0}, but the minimal Y in this world is 0.",y);
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		if(x<0) {
			String message = getGame().i18n.tr("You tried to access a cell with X={0}, but the minimal X in this world is 0.",x);
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		if (y>=bw.getHeight()) {
			String message = getGame().i18n.tr("You tried to access a cell with Y={0}, but the maximal Y in this world is {1}.",y,(bw.getHeight()-1));
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
			
		if (x>=bw.getWidth()) {
			String message = getGame().i18n.tr("You tried to access a cell with X={0}, but the maximal X in this world is {1}.",x,(bw.getWidth()-1));
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
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
		if(x<0) {
			String message = getGame().i18n.tr("You tried to set X to {0}, but the minimal X in this world is 0.",x);
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		if (x>=bw.getWidth()) {
			String message = getGame().i18n.tr("You tried to set X to {0}, but the maximal X in this world is {1}.",x,(bw.getWidth()-1));
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		addOperation(new MoveBuggleOperation(this, this.x, y, x, y, getGame().i18n));
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
		if(y<0) {
			String message = getGame().i18n.tr("You tried to set Y {0}, but the minimal Y in this world is 0.",y);
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		if (y>=bw.getHeight()) {
			String message = getGame().i18n.tr("You tried to set Y to {0}, but the maximal Y in this world is {1}.",y,(bw.getHeight()-1));
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		addOperation(new MoveBuggleOperation(this, x, this.y, x, y, getGame().i18n));
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
		if(y<0) {
			String message = getGame().i18n.tr("You tried to set Y {0}, but the minimal Y in this world is 0.",y);
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		if(x<0) {
			String message = getGame().i18n.tr("You tried to set X to {0}, but the minimal X in this world is 0.",x);
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		if (y>=bw.getHeight()) {
			String message = getGame().i18n.tr("You tried to set Y to {0}, but the maximal Y in this world is {1}.",y,(bw.getHeight()-1));
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		if (x>=bw.getWidth()) {
			String message = getGame().i18n.tr("You tried to set X to {0}, but the maximal X in this world is {1}.",x,(bw.getWidth()-1));
			addOperation(new BuggleInOuterSpace(this, message));
			stepUI();
			throw new BuggleInOuterSpaceException(message);
		}
		addOperation(new MoveBuggleOperation(this, this.x, this.y, x, y, getGame().i18n));
		this.x = x;
		this.y = y;
		stepUI();
	}
	public void setPosFromLesson(int x, int y)  {
		try {
			setPos(x,y);
		} catch (BuggleInOuterSpaceException e) {
			throw new RuntimeException("Broken lesson: you moved to outer space (at "+x+","+y+")",e);
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
			cell = getCellFromLesson(getX(), (getY()+1) % getWorldHeight());
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
				delta.equals(direction.opposite().toPoint()) && isBackingWall()) {
			addOperation(new BuggleEncounterWall(this, getGame().i18n));
			stepUI();
			throw new BuggleWallException(getGame().i18n);
		}
			
		addOperation(new MoveBuggleOperation(this, x, y, newx, newy, getGame().i18n));

		x = newx;
		y = newy;

		if (brushDown) {
			Color oldColor = getCell().getColor();
			addOperation(new ChangeCellColor(getCell(), oldColor, brushColor));
			getCell().setColor(brushColor);
		}

		stepUI();
	}

	public boolean isOverBaggle() {
		return getCellFromLesson(this.x, this.y).hasBaggle();
	}

	public boolean isCarryingBaggle() {
		return this.carryBaggle;
	}

	public void pickupBaggle() throws NoBaggleUnderBuggleException, AlreadyHaveBaggleException {
		if (k_seq[k_val]==5) k_val++; else k_val = 0;
		if (k_val>k_seq.length-1) {
			setName("Easter "+name);
			getGame().getLogger().log("EASTEEEER");
			((BuggleWorld)world).easter= true;
			k_val=0;
			return;
		}

		if (!isOverBaggle()) {
			addOperation(new NoBaggleUnderBuggle(this, getGame().i18n));
			stepUI();
			throw new NoBaggleUnderBuggleException(getGame().i18n.tr("There is no baggle to pick up here."));
		}
		if (isCarryingBaggle()) {
			addOperation(new BuggleAlreadyHaveBaggle(this, getGame().i18n));
			stepUI();
			throw new AlreadyHaveBaggleException(getGame().i18n.tr("Your are already carrying a baggle."));
		}
		getCellFromLesson(this.x, this.y).baggleRemove();
		carryBaggle = true;
		addOperation(new ChangeCellHasBaggle(getCell(), true, false));
		addOperation(new ChangeBuggleCarryBaggle(this, false, true, getGame().i18n));
		stepUI();
	}

	public void dropBaggle() throws AlreadyHaveBaggleException, DontHaveBaggleException {
		if (! isCarryingBaggle()) {
			addOperation(new BuggleDontHaveBaggle(this, getGame().i18n));
			stepUI();
			throw new DontHaveBaggleException(getGame().i18n);
		}
		BuggleWorldCell cell = getCellFromLesson(this.x, this.y);
		try {
			cell.baggleAdd();
		}
		catch (AlreadyHaveBaggleException e) {
			addOperation(new CellAlreadyHaveBaggle(cell, getGame().i18n));
			stepUI();
			throw e;
		}
		carryBaggle = false;
		addOperation(new ChangeCellHasBaggle(getCell(), false, true));
		addOperation(new ChangeBuggleCarryBaggle(this, true, false, getGame().i18n));
		stepUI();
	}
	
	protected void doCarryBaggle() { /* This should not be used in user code, only in the world loading code */
		carryBaggle = true;
	}
	
	
	public boolean isOverMessage() {
		return getCell().hasContent();
	}

	public void writeMessage(String msg) {
		String oldContent = readMessage();
		boolean oldHasContent = getCell().hasContent();
		getCell().addContent(msg);
		String newContent = readMessage();
		generateOperationsChangeCellContent(getCell(), oldContent, newContent, oldHasContent, true);
	}
	
	public void generateOperationsChangeCellContent(BuggleWorldCell cell, String oldContent, String newContent, boolean oldHasContent, boolean newHasContent) {
		addOperation(new ChangeCellContent(cell, oldContent, newContent, getGame().i18n));
		addOperation(new ChangeCellHasContent(cell, oldHasContent, newHasContent));
		stepUI();
	}
	
	public void writeMessage(int nb) {
		writeMessage(""+nb);
	}

	public String readMessage() {
		return getCell().getContent();
	}

	public void clearMessage() {
		String oldContent = readMessage();
		boolean oldHasContent = getCell().hasContent();
		getCell().emptyContent();
		String newContent = readMessage();
		addOperation(new ChangeCellContent(getCell(), oldContent, newContent, getGame().i18n));
		addOperation(new ChangeCellHasContent(getCell(), oldHasContent, false));
		stepUI();
	}

	@Override
	public String toString() {
		return "Buggle (" + this.getClass().getName() + "): x=" + x + " y=" + y + " Direction:" + direction + " Color:"
				+ bodyColor;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((brushColor == null) ? 0 : brushColor.hashCode());
		result = PRIME * result + (brushDown ? 1231 : 1237);
		result = PRIME * result + ((bodyColor == null) ? 0 : bodyColor.hashCode());
		result = PRIME * result + ((direction == null) ? 0 : direction.hashCode());
		result = PRIME * result + x;
		result = PRIME * result + y;
		return result;
	}

	public void ignoreDirectionDifference() {
		dontIgnoreDirectionDifference = false;	
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
		if (bodyColor == null) {
			if (other.bodyColor != null)
				return false;
		} else if (!bodyColor.equals(other.bodyColor))
			return false;
		if (dontIgnoreDirectionDifference && direction == null) {
			if (other.direction != null)
				return false;
		} else if (dontIgnoreDirectionDifference && !direction.equals(other.direction))
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
			return getGame().i18n.tr("Its value is 'null', which is never good.");
		/* We cannot use a i18n defined in our class, as we have to pass the classname to the initialization of i18n, 
		 *    but gettext don't seem to like the fact that we generate at runtime some package names that it does not know at compile time.
		 * So, use getGame().i18n instead.
		 */
		StringBuffer sb = new StringBuffer();
		if (getX() != other.getX() || getY() != other.getY()) 
			sb.append(getGame().i18n.tr("    Its position is ({0},{1}); expected: ({2},{3}).\n",other.getX(),other.getY(),getX(),getY()));
		if ((!dontIgnoreDirectionDifference) && getDirection() != other.getDirection()) 
			sb.append(getGame().i18n.tr("    Its direction is {0}; expected: {1}.\n",other.getDirection(),getDirection()));
		if (getBodyColor() != other.getBodyColor()) 
			sb.append(getGame().i18n.tr("    Its color is {0}; expected: {1}.\n",other.getBodyColor(),getBodyColor()));
		if (getBrushColor() != other.getBrushColor())
			sb.append(getGame().i18n.tr("    The color of its brush is {0}; expected: {1}.\n",other.getBrushColor(),getBrushColor()));
		if (isCarryingBaggle() && !other.isCarryingBaggle())
			sb.append(getGame().i18n.tr("    It should not carry that baggle.\n"));
		if (!isCarryingBaggle() && other.isCarryingBaggle())
			sb.append(getGame().i18n.tr("    It is not carrying any baggle.\n"));
		if (haveSeenError() && other.haveSeenError())
			sb.append(getGame().i18n.tr("    It encountered an issue, such as bumping into a wall.\n"));
		if (haveSeenError() && !other.haveSeenError())
			sb.append(getGame().i18n.tr("    It didn't encounter any issue, such as bumping into a wall.\n"));
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
	public Color getCouleurCorps()        { return getBodyColor(); }
	public void setCouleurCorps(Color c)  { setBodyColor(c); }
	public boolean estFaceMur()           { return isFacingWall(); }
	public boolean estDosMur()            { return isBackingWall(); }
	public void leveCrayon()              { penUp(); }
	public void baisseCrayon()            { penDown(); }
	public void leveBrosse()              { brushUp(); }
	public void baisseBrosse()            { brushDown(); }
	public boolean estBrosseBaissee()     { return isBrushDown(); }
	public Color getCouleurBrosse()       { return getBrushColor(); }
	public void setCouleurBrosse(Color c) { setBrushColor(c); }
	public Color getCouleurSol()          { return getGroundColor(); }
	public boolean estSurBiscuit()        { return isOverBaggle(); }
	public boolean porteBiscuit()         { return isCarryingBaggle(); }
	public void prendBiscuit() throws AlreadyHaveBaggleException, NoBaggleUnderBuggleException { pickupBaggle(); }
	public void poseBiscuit()  throws AlreadyHaveBaggleException, DontHaveBaggleException      { dropBaggle(); }
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
	/* BINDINGS TRANSLATION: Brazilian Portuguese */
	public void esquerda()        { left(); }
	public void direita()         { right(); }
	public void voltar()          { back(); }
	public void avançar()          throws BuggleWallException { forward(); }
	public void avançar(int steps) throws BuggleWallException { forward(steps); }
	public void recuar()           throws BuggleWallException { backward(); }
	public void recuar(int steps)  throws BuggleWallException { backward(steps); }
	public Color getCorDoCorpo()        { return getBodyColor(); }
	public void setCorDoCorpo(Color c)  { setBodyColor(c); }
	public boolean estáDeFrenteParaParede() { return isFacingWall(); }
	public boolean estáDeCostasParaParede() { return isBackingWall(); }
	public void levantarCaneta()            { penUp(); }
	public void abaixarCaneta()             { penDown(); }
	public void levantarPincel()            { brushUp(); }
	public void abaixarPincel()             { brushDown(); }
    	public boolean pincelEstáAbaixado()     { return isBrushDown(); }
	public Color getCorDoPincel()       { return getBrushColor(); }
	public void setCorDoPincel(Color c) { setBrushColor(c); }
	public Color getCorDoChão()          { return getGroundColor(); }
	public boolean estáSobreBaggle()        { return isOverBaggle(); }
	public boolean estáCarregandoBaggle()         { return isCarryingBaggle(); }
	public void pegarBaggle() throws AlreadyHaveBaggleException, NoBaggleUnderBuggleException { pickupBaggle(); }
	public void soltarBaggle()  throws AlreadyHaveBaggleException, DontHaveBaggleException      { dropBaggle(); }
	public boolean estáSobreMensagem()        { return isOverMessage(); }
	public String lerMensagem()            { return readMessage(); }
	public void escreverMensagem(String s)    { writeMessage(s); }
	public void escrevermensagem(int i)       { writeMessage(i); }
	public void limparMensagem()           { clearMessage(); }
	public int getAlturaDoMundo()          { return getWorldHeight(); }
	public int getLarguraDoMundo()          { return getWorldWidth(); }
	// get/set X/Y/Pos are not translated as they happen to be the same in Brazilian portuguese
	public boolean estáSelecionado()           { return isSelected(); } 


	@Override
	public void command(String command, BufferedWriter out){
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		int nb,nb2;
		try {
			switch(num){
			case 110:
				left();
				break;
			case 111:
				right();
				break;
			case 112:
				back();
				break;
			case 113 : 
				nb = Integer.parseInt((command.split(" ")[1]));
				if(nb==1){
					forward();
				}else{
					forward(nb);
				}
				break;
			case 114:
				nb = Integer.parseInt((command.split(" ")[1]));
				if(nb==1){
					backward();
				}else{
					backward(nb);
				}
				break;
			case 115:
				out.write(Integer.toString(getX()));
				out.write("\n");
				break;
			case 116:
				out.write(Integer.toString(getY()));
				out.write("\n");
				break;
			case 117:
				nb = Integer.parseInt((command.split(" ")[1]));
				setX(nb);
				break;
			case 118:
				nb = Integer.parseInt((command.split(" ")[1]));
				setY(nb);
				break;
			case 119:
				nb = Integer.parseInt((command.split(" ")[1]));
				nb2 = Integer.parseInt((command.split(" ")[2]));
				setPos(nb, nb2);
				break;
			case 120:
				out.write(Integer.toString(ColorMapper.color2int(getBodyColor())));
				out.write("\n");
				break;
			case 121:
				nb = Integer.parseInt((command.split(" ")[1]));
				setBodyColor(ColorMapper.int2color(nb));
				break;
			case 122:
				out.write((isFacingWall()?"1":"0"));
				out.write("\n");
				break;
			case 123:
				out.write((isBackingWall()?"1":"0"));
				out.write("\n");
				break;	
			case 124:
				out.write(Integer.toString(getDirection().intValue()));
				out.write("\n");
				break;
			case 125:
				nb = Integer.parseInt((command.split(" ")[1]));
				Direction d=null;
				switch(nb){
				case Direction.NORTH_VALUE:
					d=Direction.NORTH;
					break;
				case Direction.EAST_VALUE:
					d=Direction.EAST;
					break;
				case Direction.SOUTH_VALUE:
					d=Direction.SOUTH;
					break;
				case Direction.WEST_VALUE:
					d=Direction.WEST;
					break;
				}
				setDirection(d);
				break;
			case 126:
				out.write((isSelected()?"1":"0"));
				out.write("\n");
				break;
			case 127:
				brushUp();
				break;
			case 128:
				brushDown();
				break;
			case 129:
				out.write((isBrushDown()?"1":"0"));
				out.write("\n");
				break;
			case 130:
				nb = Integer.parseInt((command.split(" ")[1]));
				setBrushColor(ColorMapper.int2color(nb));
				break;
			case 131:
				out.write(Integer.toString(ColorMapper.color2int(getBrushColor())));
				out.write("\n");
				getGame().getLogger().log("a toi");
				break;
			case 132:
				out.write(Integer.toString(ColorMapper.color2int(getGroundColor())));
				out.write("\n");
				break;
			case 133:
				out.write((isOverBaggle()?"1":"0"));
				out.write("\n");
				break;
			case 134:
				out.write((isCarryingBaggle()?"1":"0"));
				out.write("\n");
				break;
			case 135:
				pickupBaggle();
				break;
			case 136:
				dropBaggle();
				break;
			case 137:
				out.write((isOverMessage()?"1":"0"));
				out.write("\n");
				break;
			case 138:
				String mess = (command.split(" ")[1]);
				writeMessage(mess);
				break;
			case 139:
				out.write(readMessage());
				out.write("\n");
				break;
			case 140:
				clearMessage();
				break;
			case 141:
				out.write(Integer.toString(getWorldHeight()));
				out.write("\n");
				break;
			case 142:
				out.write(Integer.toString(getWorldWidth()));
				out.write("\n");
				break;
			default:
				getGame().getLogger().log("COMMANDE INCONNUE : "+command);
				break;

			}
			out.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}catch (InvalidColorNameException ine) {
			ine.printStackTrace();
		}

	}

}
