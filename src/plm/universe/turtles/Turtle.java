package plm.universe.turtles;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;

import plm.core.utils.ColorMapper;
import plm.core.utils.InvalidColorNameException;
import plm.universe.Entity;
import plm.universe.World;
import plm.universe.turtles.operations.*;

public class Turtle extends Entity {

	public final static int DEGREE = 1;
	public final static int RADIAN = 2;
	public static final double EPSILON = .0000001;

	private Color color = Color.black;

	private double x = 0.;
	private double y = 0.;

	protected double startX,startY;

	private double heading = 0.;
	private boolean penDown = true;
	private int angularUnit = Turtle.DEGREE;

	private boolean visible = true;

	/** The PLM calls that constructor with no parameter, so it must exist (but you probably don't want to use it yourself). */
	public Turtle() {
		super();
	}	

	public Turtle(World w, String name, double x, double y) {
		this(w, name, x, y, 0., Color.black);
	}

	public Turtle(World world, String name, double x, double y, double heading, Color c) {
		super(name, world);
		this.color = c;
		this.x = x;
		this.y = y;
		this.startX = x;
		this.startY = y;
		this.heading = heading;
		setHeading(heading);
	}

	public Turtle(Turtle t) {
		super();
		setName(t.getName());
		setWorld(t.getWorld());
		this.color = t.color;
		this.x = t.x;
		this.y = t.y;
		this.startX = t.startX;
		this.startY = t.startY;
		this.heading = t.heading;
		this.penDown = t.penDown;
	}

	@Override
	public void copy(Entity e) {
		super.copy(e);
		Turtle other = (Turtle) e;
		this.color = other.color;
		this.x = other.x;
		this.y = other.y;
		this.startX = other.startX;
		this.startY = other.startY;
		this.heading = other.heading;
		this.penDown = other.penDown;
	}

	public void forward(double dist) {
		moveTo(x + dist * Math.cos(heading), y + dist * Math.sin(heading));
	}

	public void backward(double dist) {
		moveTo(x + dist * Math.cos(heading + Math.PI), y + dist * Math.sin(heading + Math.PI));
	}

	public Direction getHeadingDirection() {
		final double w = getWorld().getWidth();
		final double h = getWorld().getHeight();
		final double angleHeading = 2*Math.PI - this.heading;

		if (this.heading >=0 && this.heading < Math.PI) {
			// bottom half-circle
			final double tangenteAngleToBottomLeftCorner = (this.x>EPSILON)?((h-this.y) / this.x):Double.POSITIVE_INFINITY;
			final double angleToBottomLeftCorner = Math.PI+Math.atan(tangenteAngleToBottomLeftCorner);
			final double tangenteAngleToBottomRightCorner = ((w-this.x)>EPSILON)?((h-this.y) / (w-this.x)):Double.POSITIVE_INFINITY;
			final double angleToBottomRightCorner = 2*Math.PI-Math.atan(tangenteAngleToBottomRightCorner);

			if (angleHeading < angleToBottomLeftCorner) {
				return Direction.WEST;
			} else if (angleHeading < angleToBottomRightCorner) {
				return Direction.SOUTH;
			} else {
				return Direction.EAST;
			}
		} else {
			// bottom half-circle			
			final double tangenteAngleToTopRightCorner = ((w-this.x)>EPSILON)?((this.y) / (w-this.x)):Double.POSITIVE_INFINITY;
			final double angleToTopRightCorner = Math.atan(tangenteAngleToTopRightCorner);
			final double tangenteAngleToTopLeftCorner = (this.x>EPSILON)?(this.y / this.x):Double.POSITIVE_INFINITY;
			final double angleToTopLeftCorner = Math.PI-Math.atan(tangenteAngleToTopLeftCorner);

			if (angleHeading < angleToTopRightCorner) {
				return Direction.EAST;
			} else if (angleHeading < angleToTopLeftCorner) {
				return Direction.NORTH;
			} else {
				return Direction.WEST;
			}
		}
	}
	
	public void line(double x1, double y1, double x2, double y2, Color color) {
		if (penDown) {
			addOperation(new AddLine(this, x1, y1, x2, y2, color));
			getWorld().addLine(x1, y1, x2, y2, color);
		}
	}
	
	public void circle(double radius) {
		if (penDown) {
			addOperation(new AddCircle(this, x, y, radius, color));
			getWorld().addCircle(x, y, radius, color);
			stepUI();
		}
	}

	public void moveTo(double newX, double newY) {
		final double w = this.getWorld().getWidth();
		final double h = this.getWorld().getHeight();

		double nX = newX;
		double nY = newY;

		while (nX < 0 || nX > w || nY < 0 || nY > h) { // need to clip			
			// line equation y = y1+m(x-x1)
			// where m=(y2-y1)/(x2-x1)
			final double m = (nY - y) / (nX - x);

			switch (this.getHeadingDirection()) {
			case EAST:
				// intersection with the right boundary (x=r)
				// x=r and y=y1+m(r-x1)
			{
				final double xc = w;
				final double yc = y + m * (w - x);

				line(x, y, xc, yc, color);
				setPos(0., yc);
				nX = nX - w;
			}
			break;
			case NORTH:
				// intersection with the top boundary (y=t)
				// x=(1/m)(t-y1)+x1 and y=t
			{
				final double xc = (0 - this.y) / m + this.x;
				final double yc = 0;

				line(x, y, xc, yc, color);
				setPos(xc, h);
				nY = nY + h;
			}
			break;
			case WEST:
				// intersection with the left boundary (x=l)
				// x=l and y=y1+m(l-x1)
			{
				final double xc = 0;
				final double yc = y + m * (0 - x);

				line(x, y, xc, yc, color);
				setPos(w, yc);
				nX = nX + w;
			}
			break;
			case SOUTH:
				// intersection with the top boundary (y=t)
				// x=(1/m)(t-y1)+x1 and y=t
			{
				final double xc = (h - this.y) / m + this.x;
				final double yc = h;

				line(x, y, xc, yc, color);
				setPos(xc, 0.);
				nY = nY - h;
			}
			break;			
			}	
		} 

		line(x, y, nX, nY, color);
		addOperation(new MoveTurtle(this, x, y, nX, nY));
		this.x = nX;
		this.y = nY;		

		stepUI();
	}

	public void left(double angle) {
		setHeadingRadian(heading - fromAngularUnit(angle));
	}
	public void right(double angle) {
		setHeadingRadian(heading + fromAngularUnit(angle));
	}
	
	// Make sure that the case issue is detected in Scala by overriding the Left() and Right() methods (see #236)
	public void Left(double angle) { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use Left() with an uppercase. Use left() instead."));
	}
	public void Right(double angle) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use Right() with an uppercase. Use right() instead."));
	}

	public void brushDown(){
		throw new RuntimeException(getGame().i18n.tr(
				"Sorry Dave, I cannot let you use brushDown() here. Turtles have pens, not brushes. Use penDown() instead."));
	}
	public void brushUp(){
		throw new RuntimeException(getGame().i18n.tr(
				"Sorry Dave, I cannot let you use brushUp() here. Turtles have pens, not brushes. Use penUp() instead."));
	}

	public boolean isPenDown() {
		return penDown;
	}

	public void penDown() {
		this.penDown = true;
	}

	public void penUp() {
		this.penDown = false;
	}

	public void hide() {
		addOperation(new ChangeTurtleVisible(this, this.visible, false));
		this.visible = false;
		stepUI();
	}
	public void show() {
		addOperation(new ChangeTurtleVisible(this, this.visible, true));
		this.visible = true;
		stepUI();
	}
	public boolean isVisible() {
		return this.visible;
	}
	public void clear() {
		addOperation(new ClearCanvas(this));
		getWorld().clear();
		stepUI();
	}

	private double fromAngularUnit(double angle) {
		switch (angularUnit) {
		case Turtle.DEGREE:
			return Math.toRadians(angle);
		case Turtle.RADIAN:
			return angle;
		}
		throw new RuntimeException("Unknown angular unit:" + angularUnit + " (please report this bug)");
	}

	private final double toAngularUnit(double angle) {
		switch (angularUnit) {
		case Turtle.DEGREE:
			return Math.toDegrees(angle);
		case Turtle.RADIAN:
			return angle;
		}
		throw new RuntimeException("Unknown angular unit:" + angularUnit + " (please report this bug)");
	}

	public double getHeading() {
		return toAngularUnit(heading);
	}

	public void setHeading(double heading) {
		setHeadingRadian(fromAngularUnit(heading));
	}

	protected void setHeadingRadian(double heading) {
		addOperation(new RotateTurtle(this, toAngularUnit(this.heading), toAngularUnit(heading)));
		this.heading = ((2. * Math.PI) + heading) % (2. * Math.PI);
		if (world != null)
			world.notifyWorldUpdatesListeners();
	}

	protected double getHeadingRadian() {
		return this.heading;
	}

	@Override
	public TurtleWorld getWorld() {
		return (TurtleWorld) super.getWorld();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		addOperation(new MoveTurtle(this, this.x, y, x, y));
		this.x = x;
		stepUI();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		addOperation(new MoveTurtle(this, x, this.y, x, y));
		this.y = y;
		stepUI();
	}

	public void setPos(double x, double y) {
		addOperation(new MoveTurtle(this, this.x, this.y, x, y));
		this.x = x;
		this.y = y;
		stepUI();
	}

	/* let's accept integers as arguments, too */
	public void forward(int steps) {
		forward((double) steps);
	}

	public void backward(int steps) {
		backward((double) steps);
	}

	public void left(int a) {
		left((double) a);
	}

	public void right(int a) {
		right((double) a);
	}

	public void setHeading(int heading) {
		setHeading((double) heading);
	}

	public void setX(int x) {
		setX((double) x);
	}

	public void setY(int y) {
		setY((double) y);
	}

	public void setPos(int x, int y) {
		setPos((double) x, (double) y);
	}

	public void setPos(double x, int y) {
		setPos((double) x, (double) y);
	}

	public void setPos(int x, double y) {
		setPos((double) x, (double) y);
	}

	public void addSizeHint(int x1, int y1, int x2, int y2,String text){
		addOperation(new AddSizeHint(this, x1, y1, x2, y2, text));
		((TurtleWorld) world).addSizeHint(x1,y1,x2,y2,text);
	}
	public void addSizeHint(int x1, int y1, int x2, int y2){
		String text = String.format("%.0f", Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)));
		addOperation(new AddSizeHint(this, x1, y1, x2, y2, text));
		((TurtleWorld) world).addSizeHint(x1,y1,x2,y2,text);
	}

	@Override
	public String toString() {
		return "Turtle (" + this.getClass().getName() + "): x=" + x + " y=" + y + " Heading:" + heading + " Color:"
				+ color;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((color == null) ? 0 : color.hashCode());
		result = PRIME * result + (penDown ? 1231 : 1237);
		result = PRIME * result + (int) heading;
		result = PRIME * result + (int) x;
		result = PRIME * result + (int) y;
		return result;
	}

	public String diffTo(Object obj) {
		if (this == obj)
			return "";
		if (obj == null)
			return "I was not expecting a (null) turtle";
		if (!(obj instanceof Turtle))
			return "The turtle is not a turtle! It's a trap!";

		final Turtle other = (Turtle) obj;
		if (Math.abs(heading-other.heading) > Turtle.EPSILON)
			return getGame().i18n.tr("The turtle {0} is not heading to the right direction.",getName());
		if (Math.abs(x-other.x) > Turtle.EPSILON || Math.abs(y-other.y) > Turtle.EPSILON)
			return getGame().i18n.tr("The turtle {0} is at the right location.",getName());
		return "";

	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Turtle))
			return false;

		final Turtle other = (Turtle) obj;
		if (Math.abs(heading-other.heading) > Turtle.EPSILON)
			return false;
		if (Math.abs(x-other.x) > Turtle.EPSILON)
			return false;
		if (Math.abs(y-other.y) > Turtle.EPSILON)
			return false;
		return true;
	}

	@Override
	public void run() {
		// Overriden by childs
	}

	/* BINDINGS TRANSLATION: French */
	public void avance(double steps) { forward(steps); }
	public void recule(double steps) { backward(steps); }
	public void gauche(double angle) { left(angle); }
	public void droite(double angle) { right(angle); }
	public void cercle(double radius){ circle(radius); }
	// get/set X/Y/Pos are not translated as they happen to be the same in French
	public void allerVers(double x, double y) {moveTo(x,y);}
	public double getCap()           { return getHeading(); }
	public void setCap(double cap)   { setHeading(cap);     }
	public void leveBrosse()         { brushUp(); }
	public void baisseBrosse()       { brushDown(); }
	public void leveCrayon()         { penUp(); }
	public void baisseCrayon()       { penDown(); }
	public boolean estCrayonBaisse() { return isPenDown();}
	public Color getCouleur()        { return getColor(); }
	public void setCouleur(Color c)  { setColor(c); }
	public boolean estChoisi()       { return isSelected(); } // we have to document the version without e, since po4a allows for one variant only
	public boolean estChoisie()      { return isSelected(); } // But we want to have the grammatically correct form also possible (turtles are feminine)
	public void efface()             { clear(); } 
	public void cache()              { hide(); }
	public void montre()             { show(); }
	public boolean estVisible()      { return isVisible(); }

	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		double nb,nb2;
		int nbInt;
		try {
			switch(num){
			case 110:
				nb = Double.parseDouble((command.split(" ")[1]));
				left(nb);
				break;
			case 111:
				nb = Double.parseDouble((command.split(" ")[1]));
				right(nb);
				break;
			case 112:
				nb = Double.parseDouble((command.split(" ")[1]));
				forward(nb);
				break;
			case 113 : 
				nb = Double.parseDouble((command.split(" ")[1]));
				backward(nb);
				break;
			case 114:
				out.write(Double.toString(getX()));
				out.write("\n");
				break;
			case 115:
				out.write(Double.toString(getY()));
				out.write("\n");
				break;
			case 116:
				nb = Double.parseDouble((command.split(" ")[1]));
				setX(nb);
				break;
			case 117:
				nb = Double.parseDouble((command.split(" ")[1]));
				setY(nb);
				break;
			case 118:
				nb = Double.parseDouble((command.split(" ")[1]));
				nb2 = Double.parseDouble((command.split(" ")[2]));
				setPos(nb, nb2);
				break;
			case 119:
				nb = Double.parseDouble((command.split(" ")[1]));
				nb2 = Double.parseDouble((command.split(" ")[2]));
				moveTo(nb, nb2);
				break;
			case 120:
				nb = Double.parseDouble((command.split(" ")[1]));
				circle(nb);
				break;
			case 121 :
				hide();
				break;
			case 122:
				show();
				break;
			case 123:
				out.write((isVisible()?"1":"0"));
				out.write("\n");
				break;
			case 124:
				clear();
				break;
			case 125:
				out.write(Double.toString(getCap()));
				out.write("\n");
				break;
			case 126:
				nb = Double.parseDouble((command.split(" ")[1]));
				setCap(nb);
				break;
			case 127:
				penUp();
				break;
			case 128:
				penDown();
				break;
			case 129:
				out.write((isPenDown()?"1":"0"));
				out.write("\n");
				break;
			case 130:
				out.write(ColorMapper.color2int(getColor()));
				out.write("\n");
				break;
			case 131:
				nbInt = Integer.parseInt((command.split(" ")[1]));
				setColor(ColorMapper.int2color(nbInt));
				break;
			case 132:
				out.write((isSelected()?"1":"0"));
				out.write("\n");
				break;
			case 200:
				nbInt = Integer.parseInt((command.split(" ")[1]));
				out.write(Integer.toString((int)getParam(nbInt)));
				out.write("\n");
				break;
			case 201:
				nbInt = Integer.parseInt((command.split(" ")[1]));
				double param;
				if(getParam(nbInt) instanceof Integer){
					param =(double) ((Integer) getParam(nbInt)).intValue();
				}else{
					param = (double)getParam(nbInt);
				}
				out.write(Double.toString(param));
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
