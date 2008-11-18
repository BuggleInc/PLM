package universe.turtles.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;

import jlm.ui.WorldView;
import universe.Entity;
import universe.World;
import universe.turtles.AbstractTurtle;
import universe.turtles.ShapeAbstract;
import universe.turtles.TurtleWorld;

public class TurtleWorldView extends WorldView {
	private static final long serialVersionUID = 1674820378395646693L;
	
	public TurtleWorldView(World w) {
		super(w);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Iterator<Entity> it = world.entities();
		while (it.hasNext())
			drawTurtle(g2, (AbstractTurtle)it.next());
		Iterator<ShapeAbstract> it2 = ((TurtleWorld) world).shapes();
			it2.next().draw(g2);
	}

	private void drawTurtle(Graphics2D g, AbstractTurtle b) {
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
}
