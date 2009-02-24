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

	private static final double CELL_WIDTH = 50.;
	
	public LightBotWorldView(World w) {
		super(w);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		LightBotWorld tw = (LightBotWorld) this.world;

		double ratio = Math.min(((double) getWidth()) / (tw.getWidth()*LightBotWorldView.CELL_WIDTH), ((double) getHeight()) / (tw.getHeight()*LightBotWorldView.CELL_WIDTH));
		g2.translate(Math.abs((getWidth() - ratio * tw.getWidth()*LightBotWorldView.CELL_WIDTH) / 2.), Math.abs((getHeight() - ratio * tw.getHeight()*LightBotWorldView.CELL_WIDTH) / 2.));
		g2.scale(ratio, ratio);
		

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., (double) tw.getWidth()*LightBotWorldView.CELL_WIDTH, (double) tw.getHeight()*LightBotWorldView.CELL_WIDTH));

		
		// draw background
		drawWorld2D(g2);
		
		// draw lights (and elevation)
		for (int x = 0; x < tw.getWidth(); x++) {
			for (int y = 0; y < tw.getHeight(); y++) {
				LightBotWorldCell cell = tw.getCell(x, y);
				if (cell.isLight()) {
					drawLight2D(g2, cell, cell.isLightOn());
				}
				
				
				g2.setColor(Color.RED);
				if (cell.getHeight() != 0)
					g2.drawString(Integer.toString(cell.getHeight()), (int) (x*LightBotWorldView.CELL_WIDTH), (int) ((y+1)*LightBotWorldView.CELL_WIDTH));
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

	//private final BasicStroke gridStroke = new BasicStroke((float) 0.1 / ((LightBotWorld) world).getWidth(),
	//		BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	private void drawWorld2D(Graphics2D g) {
		LightBotWorld w = (LightBotWorld) world;

		for (int x = 0; x < w.getWidth(); x++) {
			for (int y = 0; y < w.getHeight(); y++) {
				Color cellColor = Color.white;
				if ((x + y) % 2 == 0)
					cellColor = LightBotWorldView.DARK_CELL_COLOR;
				else
					cellColor = LightBotWorldView.LIGHT_CELL_COLOR;
				g.setColor(cellColor);
				g.fill(new Rectangle2D.Double(x*LightBotWorldView.CELL_WIDTH, y*LightBotWorldView.CELL_WIDTH, LightBotWorldView.CELL_WIDTH, LightBotWorldView.CELL_WIDTH));
			}
		}

		g.setColor(GRID_COLOR);
		for (int x = 0; x <= w.getWidth(); x++)
			g.draw(new Line2D.Double(x*LightBotWorldView.CELL_WIDTH, 0., x*LightBotWorldView.CELL_WIDTH, w.getHeight()*LightBotWorldView.CELL_WIDTH));
		for (int y = 0; y <= w.getHeight(); y++)
			g.draw(new Line2D.Double(0., y*LightBotWorldView.CELL_WIDTH, w.getWidth()*LightBotWorldView.CELL_WIDTH, y*LightBotWorldView.CELL_WIDTH));
	}

	private void drawLight2D(Graphics2D g, LightBotWorldCell cell, boolean lightOn) {
		if (lightOn)
			g.setColor(LightBotWorldView.LIGHT_ON_COLOR);
		else
			g.setColor(LightBotWorldView.LIGHT_OFF_COLOR);
		g.fill(new Arc2D.Double(cell.getX()*LightBotWorldView.CELL_WIDTH + 0.1*LightBotWorldView.CELL_WIDTH, cell.getY()*LightBotWorldView.CELL_WIDTH + 0.1*LightBotWorldView.CELL_WIDTH, 0.8*LightBotWorldView.CELL_WIDTH, 0.8*LightBotWorldView.CELL_WIDTH, 0, 360, Arc2D.OPEN));
	}

	private void drawBot2D(Graphics2D g, LightBot bot) {
		LightBotWorldCell cell = bot.getCell();

		double width = LightBotWorldView.CELL_WIDTH;
		double height = LightBotWorldView.CELL_WIDTH;
		double cx = cell.getX();
		double cy = cell.getY();
		
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
		g.rotate(angle, cx*LightBotWorldView.CELL_WIDTH+width/2., cy*LightBotWorldView.CELL_WIDTH+height/2.);
		
		g.setColor(LightBotWorldView.BOT_COLOR);
		g.fill(new Arc2D.Double((cx-0.25)*LightBotWorldView.CELL_WIDTH,(cy+0.1)*LightBotWorldView.CELL_WIDTH,1.5*width,1.5*height,60,60, Arc2D.PIE));		
	}

	@Override
	public boolean isWorldCompatible(World world) {
		return world instanceof LightBotWorld;
	}
}
