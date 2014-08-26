package plm.universe.turtles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.ImageIcon;

import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.Entity;
import plm.universe.World;

public class TurtleWorldView extends WorldView {
	private static final long serialVersionUID = 1674820378395646693L;
	
	public TurtleWorldView(World w) {
		super(w);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		doPaint(g,getWidth(),getHeight(),true);
	}
	public void doPaint(Graphics g, int sizeX, int sizeY, boolean showTurtle) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		TurtleWorld tw = (TurtleWorld) this.world;
		
		double ratio = Math.min(((double) sizeX)/tw.getWidth(), ((double)sizeY)/tw.getHeight());
		g2.translate(Math.abs((sizeX-ratio*tw.getWidth())/2.), Math.abs((sizeY-ratio*tw.getHeight())/2.));
		g2.scale(ratio, ratio);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0.,0.,(double)tw.getWidth(),(double)tw.getHeight()));
		
		g2.setColor(Color.BLACK);
		Stroke oldStroke = g2.getStroke();
	    g2.setStroke(new BasicStroke(1.0f,
	                        BasicStroke.CAP_BUTT,
	                        BasicStroke.JOIN_MITER,
	                        10.0f, new float[]{2f,10.0f}, 0.0f));

		if (Game.getInstance().isDebugEnabled()) {
			for (int x=50;x<tw.getWidth();x+=50) 
				g2.drawLine(x, 1, x, (int) tw.getHeight()-1);
			for (int y=50;y<tw.getHeight();y+=50) 
				g2.drawLine(1, y, (int)tw.getWidth()-1,y);
		}
		g2.setStroke(oldStroke);
		
		if (world.isAnswerWorld() || Game.getInstance().isDebugEnabled()) {
			for (Entity e: world.getEntities()) {
				Turtle t = (Turtle) e;
				g2.setColor(SizeHint.color);
				g2.fillOval((int)(t.startX-5), (int)(t.startY-5), 10, 10);
			}
				
			synchronized (tw.sizeHints) {
				for (SizeHint indic : tw.sizeHints)
					indic.draw(g2);			
			}
		}
		if (showTurtle)
			for (Entity ent : world.getEntities())
				drawTurtle(g2, (Turtle)ent);
		
		
		synchronized (((TurtleWorld) world).shapes) {
			Iterator<Shape> it2 = ((TurtleWorld) world).shapes();
			while (it2.hasNext())
				it2.next().draw(g2);			
		}
		
		
	}

	private void drawTurtle(Graphics2D g, Turtle b) {
		if (b.isVisible()) {
			ImageIcon ic = ResourcesCache.getIcon("img/world_turtle.png");
			AffineTransform t = new AffineTransform(1.0, 0, 0, 1.0, b.getX()-ic.getIconWidth()/2., b.getY()-ic.getIconHeight()/2.);
			t.rotate(b.getHeadingRadian(), ic.getIconWidth()/2., ic.getIconHeight()/2.);
			g.drawImage(ic.getImage(), t, null);
		}
	}
}
