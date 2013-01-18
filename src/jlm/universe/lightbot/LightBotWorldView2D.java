/* This file is not used anymore: it displays a stupid 2D view of the world.
 * Its main interest were to be able to test the world before the 3D isometric were functional.
 * I don't kill it so that we have a backup in case the 3D view breaks at some point.
 */

package jlm.universe.lightbot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.Direction;
import jlm.universe.Entity;
import jlm.universe.GridWorld;
import jlm.universe.GridWorldCell;
import jlm.universe.World;

public class LightBotWorldView2D extends WorldView {
	private static final long serialVersionUID = 1674820378395646693L;

	private static Color GRID_COLOR = new Color(0.8f, 0.8f, 0.8f);
	private static Color DARK_CELL_COLOR = new Color(0.93f, 0.93f, 0.93f);
	private static Color LIGHT_CELL_COLOR = new Color(0.95f, 0.95f, 0.95f);
	private static Color LIGHT_OFF_COLOR = Color.BLACK;
	private static Color LIGHT_ON_COLOR = Color.YELLOW;
	private static Color BOT_COLOR = Color.BLUE;

	private static final double CELL_WIDTH = 50.;
	
	public LightBotWorldView2D(World w) {
		super(w);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		GridWorld tw = (GridWorld) this.world;

		double ratio = Math.min(((double) getWidth()) / (tw.getWidth()*LightBotWorldView2D.CELL_WIDTH), ((double) getHeight()) / (tw.getHeight()*LightBotWorldView2D.CELL_WIDTH));
		g2.translate(Math.abs((getWidth() - ratio * tw.getWidth()*LightBotWorldView2D.CELL_WIDTH) / 2.), Math.abs((getHeight() - ratio * tw.getHeight()*LightBotWorldView2D.CELL_WIDTH) / 2.));
		g2.scale(ratio, ratio);
		

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., (double) tw.getWidth()*LightBotWorldView2D.CELL_WIDTH, (double) tw.getHeight()*LightBotWorldView2D.CELL_WIDTH));

		
		// draw background
		drawWorld2D(g2);
		
		// draw lights (and elevation)
		for (int x = 0; x < tw.getWidth(); x++) {
			for (int y = 0; y < tw.getHeight(); y++) {
				LightBotWorldCell cell = (LightBotWorldCell) tw.getCell(x, y);
				if (cell.isLight()) {
					drawLight2D(g2, cell, cell.isLightOn());
				}
				
				
				g2.setColor(Color.RED);
				if (cell.getHeight() != 0)
					g2.drawString(Integer.toString(cell.getHeight()), (int) (x*LightBotWorldView2D.CELL_WIDTH), (int) ((y+1)*LightBotWorldView2D.CELL_WIDTH));
			}
		}
		
		// draw lightBots 
		Iterator<Entity> it = world.entities();
		while (it.hasNext())
			drawBot2D(g2, (LightBotEntity) it.next());
	}

	private void drawWorld2D(Graphics2D g) {
		GridWorld w = (GridWorld) world;

		for (int x = 0; x < w.getWidth(); x++) {
			for (int y = 0; y < w.getHeight(); y++) {
				Color cellColor = Color.white;
				if ((x + y) % 2 == 0)
					cellColor = LightBotWorldView2D.DARK_CELL_COLOR;
				else
					cellColor = LightBotWorldView2D.LIGHT_CELL_COLOR;
				g.setColor(cellColor);
				g.fill(new Rectangle2D.Double(x*LightBotWorldView2D.CELL_WIDTH, y*LightBotWorldView2D.CELL_WIDTH, LightBotWorldView2D.CELL_WIDTH, LightBotWorldView2D.CELL_WIDTH));
			}
		}

		g.setColor(GRID_COLOR);
		for (int x = 0; x <= w.getWidth(); x++)
			g.draw(new Line2D.Double(x*LightBotWorldView2D.CELL_WIDTH, 0., x*LightBotWorldView2D.CELL_WIDTH, w.getHeight()*LightBotWorldView2D.CELL_WIDTH));
		for (int y = 0; y <= w.getHeight(); y++)
			g.draw(new Line2D.Double(0., y*LightBotWorldView2D.CELL_WIDTH, w.getWidth()*LightBotWorldView2D.CELL_WIDTH, y*LightBotWorldView2D.CELL_WIDTH));
	}

	private void drawLight2D(Graphics2D g, GridWorldCell cell, boolean lightOn) {
		if (lightOn)
			g.setColor(LightBotWorldView2D.LIGHT_ON_COLOR);
		else
			g.setColor(LightBotWorldView2D.LIGHT_OFF_COLOR);
		g.fill(new Arc2D.Double(cell.getX()*LightBotWorldView2D.CELL_WIDTH + 0.1*LightBotWorldView2D.CELL_WIDTH, cell.getY()*LightBotWorldView2D.CELL_WIDTH + 0.1*LightBotWorldView2D.CELL_WIDTH, 0.8*LightBotWorldView2D.CELL_WIDTH, 0.8*LightBotWorldView2D.CELL_WIDTH, 0, 360, Arc2D.OPEN));
	}

	private void drawBot2D(Graphics2D g, LightBotEntity bot) {
		GridWorldCell cell = bot.getCell();

		double width = LightBotWorldView2D.CELL_WIDTH;
		double height = LightBotWorldView2D.CELL_WIDTH;
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
		g.rotate(angle, cx*LightBotWorldView2D.CELL_WIDTH+width/2., cy*LightBotWorldView2D.CELL_WIDTH+height/2.);
		
		g.setColor(LightBotWorldView2D.BOT_COLOR);
		g.fill(new Arc2D.Double((cx-0.25)*LightBotWorldView2D.CELL_WIDTH,(cy+0.1)*LightBotWorldView2D.CELL_WIDTH,1.5*width,1.5*height,60,60, Arc2D.PIE));		
	}
	
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/lightbot.png");
	}
}
