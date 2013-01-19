package jlm.universe.pancake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.World;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 */
public class PancakeWorldView extends WorldView {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of the class PancakeWorldView
	 * @version 1.2
	 * @param w : a world
	 */
	public PancakeWorldView(World w) {
		super(w);
	}

	/**
	 * Draw the component of the world
	 * @version 1.2
	 * @param g : some Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		double renderedX = 310.;
		double rendered236 = 250.;		
		double ratio = Math.min(((double) getWidth()) / renderedX, ((double) getHeight()) / rendered236);
		g2.translate(Math.abs(getWidth() - ratio * renderedX)/2., Math.abs(getHeight() - ratio * rendered236)/2.);
		g2.scale(ratio, ratio);
		
		/* clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., renderedX, rendered236));
		
		drawStack( g2,  155.);
	}
	
	/**
	 * Draw the plate and the stack of pancakes
	 * @version 1.2
	 * @param g2 : an entity of the Graphics2D class
	 * @param xoffset : the horizontal offset 
	 */
	private void drawStack(Graphics2D g2, double xoffset) {
		/* draw bar */
		PancakesStack stack = ((PancakeWorld) this.world).getStack();
		if (stack!=null)
		{
			/* draw pancakes */
			int amountOfPancakes = stack.getSize();
			g2.setColor(Color.black);
			drawPlate(g2,xoffset,amountOfPancakes);
			for (int i = 0; i<amountOfPancakes ;i++)
			{ 
				Pancake p = stack.getPancake(amountOfPancakes-i-1);
				drawPancake(g2,xoffset,p,i);
			}
			
		}
	}
	
	/**
	 * Draw some plate
	 * @version 1.2
	 * @param g2 : an entity of the Graphics2D class
	 * @param xoffset : the horizontal offset 
	 * @param amountOfPancakes: the total amount of pancakes
	 */
	private void drawPlate(Graphics2D g2, double xoffset, int amountOfPancakes) {
		g2.draw(new Rectangle2D.Double( 0, 244., xoffset*2, 5));
	}
	
	/**
	 * Draw some pancakes
	 * @version 1.2
	 * @param g2 : an entity of the Graphics2D class
	 * @param xoffset : the horizontal offset 
	 * @param p: a pancake
	 * @param pancakeNumber: the number of the pancake draw
	 */
	private void drawPancake(Graphics2D g2, double xoffset,Pancake p,int pancakeNumber){
		int psize = p.getSize();
		if ( p.isUpsideDown())
		{
			g2.setColor(Color.YELLOW);
			g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, 239-(8.*(pancakeNumber)),  psize*10+3, 5));
			g2.setColor(new Color(91, 59, 17));
			g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, 236-(8.*(pancakeNumber)),  psize*10+3, 3));
			g2.setColor(Color.black);
			g2.draw(new Rectangle2D.Double( xoffset-psize*5-3, 236-(8.*(pancakeNumber)),  psize*10+3, 8));
		}
		else
		{
			g2.setColor(Color.YELLOW);
			g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, 236-(8.*(pancakeNumber)),  psize*10+3, 5));
			g2.setColor(new Color(91, 59, 17));
			g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, 241-(8.*(pancakeNumber)),  psize*10+3, 3));
			g2.setColor(Color.black);
			g2.draw(new Rectangle2D.Double( xoffset-psize*5-3, 236-(8.*(pancakeNumber)),  psize*10+3, 8));
		}
	}

	/**
	 * Return the icon of the world
	 * @return the icon for the exercice selection
	 * @version 1.2
	 */
	// http://omgwtflols.deviantart.com/
	// http://fc06.deviantart.net/fs71/f/2012/118/5/7/pixel_art__pancakes_with_s236rup_b236_omgwtflols-d4xu72c.gif
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/pancake.png");
	}
	
	
	
}
