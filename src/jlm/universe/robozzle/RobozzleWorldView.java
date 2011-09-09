package jlm.universe.robozzle;

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
import jlm.universe.Entity;
import jlm.universe.World;
import jlm.universe.bugglequest.AbstractBuggle;

public class RobozzleWorldView extends WorldView {

	private static final long serialVersionUID = -7164642270965762239L;


	private static Color BLUE_CELL_COLOR = new Color(0.f,0.f,0.93f);
	private static Color RED_CELL_COLOR = new Color(0.93f,0.f,0.f);
	private static Color GREEN_CELL_COLOR = new Color(0.f,0.93f,0.f);
	private static Color GREY_CELL_COLOR = new Color(0.93f,0.93f,0.93f);
	private static Color GRID_COLOR = new Color(0.8f,0.8f,0.8f);
	private static Color WALL_COLOR = new Color(0.0f,0.0f,0.5f);
	private static Color BAGGLE_COLOR = new Color(0.82f,0.41f,0.12f);
	
	public RobozzleWorldView (World w) {
		super(w);
	}
		
	protected double getCellWidth() {
		return (double) Math.min(getHeight(), getWidth()) / Math.max(((RobozzleWorld)world).getWidth(), ((RobozzleWorld)world).getHeight());
	}
	
	protected double getPadX() {
		return (getWidth() - getCellWidth() * ((RobozzleWorld)world).getWidth()) / 2;
	}
	protected double getPadY() {
		return (getHeight() - getCellWidth() * ((RobozzleWorld)world).getHeight()) / 2;
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		drawBackground(g2);
		
		Iterator<Entity> it = world.entities();
		while (it.hasNext()) {
			AbstractRobozzle b = (AbstractRobozzle)it.next();  	
			drawRobozzle(g2, b);
		}
		
		drawWalls(g2);
	}
	
	// return the color of the cell located at position (x,y)
	private Color getCellColor(int x, int y) {
		RobozzleWorldCell cell = (RobozzleWorldCell) ((RobozzleWorld)world).getCell(x, y);

		switch (cell.getColor()) {
		case 'r':
		case 'R':
				return RED_CELL_COLOR;
		case 'b':
		case 'B':
				return BLUE_CELL_COLOR;
		case 'g':
		case 'G':
				return GREEN_CELL_COLOR;
			default:
				return GREY_CELL_COLOR ;
		}		
	}
	
	private void drawBackground(Graphics2D g) {
		double cellW = getCellWidth();
		double padx = getPadX();
		double pady = getPadY();
		RobozzleWorld w = (RobozzleWorld)world;

		for (int x=0; x<w.getWidth(); x++) {
			for (int y=0; y<w.getHeight(); y++) {
				g.setColor(getCellColor(x, y));
				
				RobozzleWorldCell cell = (RobozzleWorldCell) w.getCell(x, y);

				g.fill(new Rectangle2D.Double(padx+x*cellW, pady+y*cellW, cellW, cellW));	
				
				
				if (cell.hasStar())
					drawBaggle(g, cell);
			}
		}

		g.setColor(GRID_COLOR);
		for (int x=0; x<=w.getWidth(); x++) {
			g.draw(new Line2D.Double(padx+x*cellW, pady, padx+x*cellW, pady+w.getHeight()*cellW));
		}
		for (int y=0; y<=w.getHeight(); y++) {
			g.draw(new Line2D.Double(padx+0, pady+y*cellW, padx+w.getWidth()*cellW, pady+y*cellW));						
		}
	}
	
	private void drawWalls(Graphics2D g) {
		double cellW = getCellWidth();
		double padx = getPadX();
		double pady = getPadY();
		RobozzleWorld w = (RobozzleWorld)world;

		int width = w.getWidth();
		int height = w.getHeight();
		
		g.setColor(WALL_COLOR);
		
		/*
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				RobozzleWorldCell cell = (RobozzleWorldCell) w.getCell(x, y);

				
				if (cell.hasTopWall()) {
					g.draw(new Line2D.Double(padx+x*cellW, pady+y*cellW-1, padx+(x+1)*cellW, pady+y*cellW-1));						
					g.draw(new Line2D.Double(padx+x*cellW, pady+y*cellW, padx+(x+1)*cellW, pady+y*cellW));						
					g.draw(new Line2D.Double(padx+x*cellW, pady+y*cellW+1, padx+(x+1)*cellW, pady+y*cellW+1));						
				}
				
				if (cell.hasLeftWall()) {
					g.draw(new Line2D.Double(padx+x*cellW-1, pady+y*cellW, padx+x*cellW-1, pady+(y+1)*cellW));
					g.draw(new Line2D.Double(padx+x*cellW, pady+y*cellW, padx+x*cellW, pady+(y+1)*cellW));
					g.draw(new Line2D.Double(padx+x*cellW+1, pady+y*cellW, padx+x*cellW+1, pady+(y+1)*cellW));
				}
				
			}
		}

		// frontier walls (since the world is a torus)
		for (int y = 0; y < height; y++) {
			if (((BuggleWorldCell) w.getCell(0, y)).hasLeftWall()) {
				g.draw(new Line2D.Double(padx+width*cellW-1, pady+y*cellW, padx+width*cellW-1, pady+(y+1)*cellW));
				g.draw(new Line2D.Double(padx+width*cellW, pady+y*cellW, padx+width*cellW, pady+(y+1)*cellW));
				g.draw(new Line2D.Double(padx+width*cellW+1, pady+y*cellW, padx+width*cellW+1, pady+(y+1)*cellW));				
			}
		}

		for (int x = 0; x < width; x++) {
			if (((BuggleWorldCell) w.getCell(x, 0)).hasTopWall()) {
				g.draw(new Line2D.Double(padx+x*cellW, pady+height*cellW-1, padx+(x+1)*cellW, pady+height*cellW-1));						
				g.draw(new Line2D.Double(padx+x*cellW, pady+height*cellW, padx+(x+1)*cellW, pady+height*cellW));						
				g.draw(new Line2D.Double(padx+x*cellW, pady+height*cellW+1, padx+(x+1)*cellW, pady+height*cellW+1));						
			}
		}	
		*/
	}
	
	private void drawRobozzle(Graphics2D g, AbstractRobozzle b) {
		double scaleFactor = 0.6; // to scale the sprite
		double pixW = scaleFactor * getCellWidth() / INVADER_SPRITE_SIZE;  // fake pixel width
		double pad = 0.5*(1.0-scaleFactor)*getCellWidth(); // padding to center sprite in the cell
		double padx = getPadX();
		double pady = getPadY();
	
		double ox = b.getX()*getCellWidth(); // x-offset of the cell
		double oy = b.getY()*getCellWidth(); // y-offset of the cell
		
		g.setColor(Color.BLACK);
		for (int dy=0; dy<INVADER_SPRITE_SIZE; dy++) {
			for (int dx=0; dx<INVADER_SPRITE_SIZE; dx++) {
				int direction = b.getDirection().intValue();
				if (INVADER_SPRITE[direction][dy][dx] == 1) {
						g.fill(new Rectangle2D.Double(padx+pad+ox+dx*pixW, pady+pad+oy+dy*pixW, pixW, pixW));
				}
			}				
		}
	}
	
	private void drawBaggle(Graphics2D g,RobozzleWorldCell cell) {
		double padx = getPadX();
		double pady = getPadY();
	
		double scaleFactor = 0.8; // to scale the sprite

		double d = scaleFactor*getCellWidth();
		double pad = 0.5*(1.0-scaleFactor)*getCellWidth(); // padding to center sprite in the cell

		double scaleFactor2 = 0.5; // to scale the sprite

		double d2 = scaleFactor2*scaleFactor*getCellWidth();
		double pad2 = 0.5*(1.0-scaleFactor*scaleFactor2)*getCellWidth(); // padding to center sprite in the cell
		
		double ox = cell.getX()*getCellWidth(); // x-offset of the cell
		double oy = cell.getY()*getCellWidth(); // y-offset of the cell
		
		
		g.setColor(BAGGLE_COLOR);
		g.fill(new Arc2D.Double(padx+ox+pad, pady+oy+pad, d, d, 0, 360, Arc2D.CHORD));
		g.setColor(getCellColor(cell.getX(), cell.getY()));
		g.fill(new Arc2D.Double(padx+ox+pad2, pady+oy+pad2, d2, d2, 0, 360, Arc2D.CHORD));

		g.setColor(BAGGLE_COLOR .darker().darker());
		g.draw(new Arc2D.Double(padx+ox+pad, pady+oy+pad, d, d, 0, 360, Arc2D.CHORD));
		g.draw(new Arc2D.Double(padx+ox+pad2, pady+oy+pad2, d2, d2, 0, 360, Arc2D.CHORD));
	}
	

	// old style ;b
	private static int INVADER_SPRITE_SIZE = 11;
	private static int[][][] INVADER_SPRITE = {
	{
		{ 0,0,0,0,0,0,0,0,0,0,0 },
		{ 0,0,1,0,0,0,0,0,1,0,0 },
		{ 0,0,0,1,0,0,0,1,0,0,0 },
		{ 0,0,1,1,1,1,1,1,1,0,0 },
		{ 0,1,1,0,1,1,1,0,1,1,0 },
		{ 1,1,1,1,1,1,1,1,1,1,1 },
		{ 1,0,1,1,1,1,1,1,1,0,1 },
		{ 1,0,1,0,0,0,0,0,1,0,1 },
		{ 0,0,0,1,1,0,1,1,0,0,0 },
		{ 0,0,0,0,0,0,0,0,0,0,0 },
		{ 0,0,0,0,0,0,0,0,0,0,0 },
	},
	{
	    { 0,0,0,1,1,1,0,0,0,0,0 },
		{ 0,0,0,0,0,1,1,0,0,0,0 },
		{ 0,0,0,1,1,1,1,1,0,1,0 },
		{ 0,0,1,0,1,1,0,1,1,0,0 },
		{ 0,0,1,0,1,1,1,1,0,0,0 },
		{ 0,0,0,0,1,1,1,1,0,0,0 },
		{ 0,0,1,0,1,1,1,1,0,0,0 },
		{ 0,0,1,0,1,1,0,1,1,0,0 },
		{ 0,0,0,1,1,1,1,1,0,1,0 },
		{ 0,0,0,0,0,1,1,0,0,0,0 },
		{ 0,0,0,1,1,1,0,0,0,0,0 }
	},
	{
		{ 0,0,0,0,0,0,0,0,0,0,0 },
		{ 0,0,0,0,0,0,0,0,0,0,0 },
		{ 0,0,0,1,1,0,1,1,0,0,0 },
		{ 1,0,1,0,0,0,0,0,1,0,1 },
		{ 1,0,1,1,1,1,1,1,1,0,1 },
		{ 1,1,1,1,1,1,1,1,1,1,1 },
		{ 0,1,1,0,1,1,1,0,1,1,0 },
		{ 0,0,1,1,1,1,1,1,1,0,0 },
		{ 0,0,0,1,0,0,0,1,0,0,0 },
		{ 0,0,1,0,0,0,0,0,1,0,0 },
		{ 0,0,0,0,0,0,0,0,0,0,0 },
	},
	{
		{ 0,0,0,0,0,1,1,1,0,0,0 },
		{ 0,0,0,0,1,1,0,0,0,0,0 },
		{ 0,1,0,1,1,1,1,1,0,0,0 },
		{ 0,0,1,1,0,1,1,0,1,0,0 },
		{ 0,0,0,1,1,1,1,0,1,0,0 },
		{ 0,0,0,1,1,1,1,0,0,0,0 },
		{ 0,0,0,1,1,1,1,0,1,0,0 },
		{ 0,0,1,1,0,1,1,0,1,0,0 },
		{ 0,1,0,1,1,1,1,1,0,0,0 },
		{ 0,0,0,0,1,1,0,0,0,0,0 },
		{ 0,0,0,0,0,1,1,1,0,0,0 }
	}};
	
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/Robozzle.png");
	}
}
