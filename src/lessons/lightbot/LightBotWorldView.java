package lessons.lightbot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import jlm.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.World;

public class LightBotWorldView extends WorldView {
	private static final long serialVersionUID = 1674820378395646693L;

	private static Color GRID_COLOR = new Color(0.8f, 0.8f, 0.8f);
	private static Color DARK_CELL_COLOR = new Color(0.93f, 0.93f, 0.93f);
	private static Color LIGHT_CELL_COLOR = new Color(0.95f, 0.95f, 0.95f);
	private static Color LIGHT_OFF_COLOR = Color.BLACK;
	private static Color LIGHT_ON_COLOR = Color.YELLOW;
	private static Color BOT_COLOR = Color.BLUE;

	public LightBotWorldView(World w) {
		super(w);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		LightBotWorld tw = (LightBotWorld) this.world;

		double ratio = Math.min(((double) getWidth()) / tw.getWidth(), ((double) getHeight()) / tw.getHeight());
		g2.translate(Math.abs((getWidth() - ratio * tw.getWidth()) / 2.), Math.abs((getHeight() - ratio
				* tw.getHeight()) / 2.));
		g2.scale(ratio, ratio);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., (double) tw.getWidth(), (double) tw.getHeight()));

		
		// draw background
		drawWorld2D(g2);
		
		// draw lights (and elevation)
		// g2.getTransform().setToIdentity();
		for (int x = 0; x < tw.getWidth(); x++) {
			for (int y = 0; y < tw.getHeight(); y++) {
				LightBotWorldCell cell = tw.getCell(x, y);
				if (cell.isLight()) {
					drawLight2D(g2, cell, cell.isLightOn());
				}
				
				
				//g2.setColor(Color.BLACK);
				//g2.drawString(Integer.toString(cell.getHeight()), x, y);
			}
		}
		
		// draw lightBots 
		Iterator<Entity> it = world.entities();
		while (it.hasNext())
			drawBot2D(g2, (LightBot) it.next());
	}

	protected double getCellWidth() {
		return (double) Math.min(getHeight(), getWidth())
				/ Math.max(((LightBotWorld) world).getWidth(), ((LightBotWorld) world).getHeight());
	}

	private final BasicStroke gridStroke = new BasicStroke((float) 0.1 / ((LightBotWorld) world).getWidth(),
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	private void drawWorld2D(Graphics2D g) {
		LightBotWorld w = (LightBotWorld) world;

		for (int x = 0; x < w.getWidth(); x++) {
			for (int y = 0; y < w.getHeight(); y++) {
				Color cellColor = Color.white;
				if ((x + y) % 2 == 0)
					cellColor = LightBotWorldView.DARK_CELL_COLOR;
				else
					cellColor = LightBotWorldView.LIGHT_CELL_COLOR;

				//LightBotWorldCell cell = w.getCell(x, y);				
				//for (int h=0; h<cell.getHeight(); h++) {
				//	cellColor.darker();
				//}
				
				g.setColor(cellColor);
				g.fill(new Rectangle2D.Double(x, y, 1., 1.));
			}
		}

		g.setColor(GRID_COLOR);
		g.setStroke(gridStroke);
		for (int x = 0; x <= w.getWidth(); x++)
			g.draw(new Line2D.Double(x, 0., x, w.getHeight()));
		for (int y = 0; y <= w.getHeight(); y++)
			g.draw(new Line2D.Double(0., y, w.getWidth(), y));
	}

	private void drawLight2D(Graphics2D g, LightBotWorldCell cell, boolean lightOn) {
		if (lightOn)
			g.setColor(LightBotWorldView.LIGHT_ON_COLOR);
		else
			g.setColor(LightBotWorldView.LIGHT_OFF_COLOR);
		g.fill(new Arc2D.Double(cell.getX() + 0.1, cell.getY() + 0.1, 0.8, 0.8, 0, 360, Arc2D.OPEN));
	}

	private void drawBot2D(Graphics2D g, LightBot bot) {
		LightBotWorldCell cell = bot.getCell();

		double width = 1.;
		double height = 1.;
		double cx = cell.getX();
		double cy = cell.getY();
		
		// ImageIcon ic = ResourcesCache.getIcon("resources/kturtle.png");
		AffineTransform t = new AffineTransform(1.,0, 0, 1., cell.getX()-width/2., cell.getY()-height/2.);
		// t.rotate(0., ic.getIconWidth()/2., ic.getIconHeight()/2.);
		// g.drawImage(ic.getImage(), t, null);

		double angle = 0.;
		switch (bot.getDirection().intValue()) {
		case Direction.NORTH_VALUE:
			angle = Math.PI;
			break;
		case Direction.SOUTH_VALUE:
			angle = 0.;
			break;
		case Direction.EAST_VALUE:
			angle = -Math.PI/2.;
			break;
		case Direction.WEST_VALUE:
			angle = Math.PI/2;
			break;
		}
		g.rotate(angle, cx+width/2., cy+height/2.);
		
		g.setColor(LightBotWorldView.BOT_COLOR);
		g.fill(new Arc2D.Double(cx,cy+0.25,1.,1.,45,90, Arc2D.PIE));		
	}

	private void drawWorld3DIsometric(Graphics2D g) {
		LightBotWorld w = (LightBotWorld) world;

		double scaleW = 1.;
		double scaleH = 0.5;

		for (int y = 1; y <= w.getHeight(); y++) {
			for (int x = 1; x <= w.getWidth(); x++) {
				int numCell = (y - 1) * w.getWidth() + x;

				double xPos = (y + x) * scaleW;
				double yPos = (y - x) * scaleH;

				g.setColor(Color.red);
				g.fill(new Arc2D.Double(xPos, yPos, 1., 1., 0., 360., Arc2D.OPEN));
				g.setColor(Color.black);
				g.drawString(Integer.toString(numCell), (int) xPos, (int) yPos);

				// g.setColor(getCellColor(x, y));
				// BuggleWorldCell cell = w.getCell(x, y);
				// g.fill(new Rectangle2D.Double(padx+x*cellW, pady+y*cellW,
				// cellW, cellW));
				// if (cell.hasBaggle())
				// drawBaggle(g, cell, cell.getBaggle());
				// if (cell.hasContent())
				// drawMessage(g, cell, cell.getContent());
			}
		}

	}

	/*
	 * private void drawTurtle(Graphics2D g, Turtle b) { ImageIcon ic =
	 * ResourcesCache.getIcon("resources/kturtle.png"); AffineTransform t = new
	 * AffineTransform(1.0, 0, 0, 1.0, b.getX()-ic.getIconWidth()/2.,
	 * b.getY()-ic.getIconHeight()/2.); t.rotate(b.getHeadingRadian(),
	 * ic.getIconWidth()/2., ic.getIconHeight()/2.); g.drawImage(ic.getImage(),
	 * t, null); }
	 */

	@Override
	public boolean isWorldCompatible(World world) {
		return world instanceof LightBotWorld;
	}
}
