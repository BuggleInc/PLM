package universe.turtles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.ImageIcon;

import jlm.ui.ResourcesCache;
import jlm.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.World;

public class TurtleWorldView extends WorldView {
	private static final long serialVersionUID = 1674820378395646693L;
	
	public TurtleWorldView(World w) {
		super(w);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		TurtleWorld tw = (TurtleWorld) this.world;
		
		//FIXME: dirty code.
		double ratio = Math.min(((double) getWidth())/tw.getWidth(), ((double)getHeight())/tw.getHeight());
		g2.scale(ratio, ratio);

		g2.translate(Math.abs((getWidth()-ratio*tw.getWidth())/2.), Math.abs((getHeight()-ratio*tw.getHeight())/2.));
		
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		//g2.fill(new Rectangle2D.Double(0.,0.,(double)getWidth(),(double)getHeight()));
		g2.fill(new Rectangle2D.Double(0.,0.,(double)tw.getWidth(),(double)tw.getHeight()));
		
		synchronized (((TurtleWorld) world).shapes) {
			Iterator<ShapeAbstract> it2 = ((TurtleWorld) world).shapes();
			while (it2.hasNext())
				it2.next().draw(g2);			
		}
		Iterator<Entity> it = world.entities();
		while (it.hasNext())
			drawTurtle(g2, (Turtle)it.next());
		
		
	}

	private void drawTurtle(Graphics2D g, Turtle b) {
		ImageIcon ic = ResourcesCache.getIcon("resources/kturtle.png");
		AffineTransform t = new AffineTransform(1.0, 0, 0, 1.0, b.getX()-ic.getIconWidth()/2, b.getY()-ic.getIconHeight()/2);
		t.rotate(Math.PI*b.getHeading()/180, ic.getIconWidth()/2, ic.getIconHeight()/2);
		g.drawImage(ic.getImage(), t, null);
		
			
		/* TODO	
  		double scaleFactor = 0.6; // to scale the sprite
		double pixW = scaleFactor * 400 / (Math.min(((TurtleWorld) world).getHeight(),((TurtleWorld) world).getWidth()));  // fake pixel width
		double padx = 1;//getPadX();
		double pady = 1;//getPadY();
		double pad = 1;
	
		double ox = 1;//b.getX()*getCellWidth(); // x-offset of the cell
		double oy = 1;//b.getY()*getCellWidth(); // y-offset of the cell
		
		for (int dy=0; dy<INVADER_SPRITE_SIZE; dy++) {
			for (int dx=0; dx<INVADER_SPRITE_SIZE; dx++) {
				if (INVADER_SPRITE[dy][dx] == 1) {
						g.fill(new Rectangle2D.Double(padx+pad+ox+dx*pixW, pady+pad+oy+dy*pixW, pixW, pixW));
				}
			}				
		}
		*/
	}
	// old style ;b
	/*
	private static int INVADER_SPRITE_SIZE = 11;
	private static int[][] INVADER_SPRITE = {
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
	};
	 */

	@Override
	public boolean isWorldCompatible(World world) {
		return world instanceof TurtleWorld;
	}
}
