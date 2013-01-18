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

public class PancakeWorldView extends WorldView {

	public static final int Y = 236;
	private static final long serialVersionUID = 1L;
	
	public PancakeWorldView(World w) {
		super(w);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		double renderedX = 310.;
		double renderedY = 250.;		
		double ratio = Math.min(((double) getWidth()) / renderedX, ((double) getHeight()) / renderedY);
		g2.translate(Math.abs(getWidth() - ratio * renderedX)/2., Math.abs(getHeight() - ratio * renderedY)/2.);
		g2.scale(ratio, ratio);
		
		/* clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., renderedX, renderedY));
		
		drawStack( g2,  155.);
	}
	
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
	
	private void drawPlate(Graphics2D g2, double xoffset, int amountOfPancakes) {
		g2.draw(new Rectangle2D.Double( 
				0, 
				Y+8.
				,  xoffset*2, 5));
	}
	
	private void drawPancake(Graphics2D g2, double xoffset,Pancake p,int i){
		int psize = p.getSize();
		if ( p.isUpsideDown())
		{
			g2.setColor(Color.yellow);
			g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, Y+3-(8.*(i)),  psize*10+3, 5));
			g2.setColor(new Color(91, 59, 17));
			g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, Y-(8.*(i)),  psize*10+3, 3));
			g2.setColor(Color.black);
			g2.draw(new Rectangle2D.Double( xoffset-psize*5-3, Y-(8.*(i)),  psize*10+3, 8));
		}
		else
		{
			g2.setColor(Color.yellow);
			g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, Y-(8.*(i)),  psize*10+3, 5));
			g2.setColor(new Color(91, 59, 17));
			g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, Y+5-(8.*(i)),  psize*10+3, 3));
			g2.setColor(Color.black);
			g2.draw(new Rectangle2D.Double( xoffset-psize*5-3, Y-(8.*(i)),  psize*10+3, 8));
		}
	}

	@Override
	// http://omgwtflols.deviantart.com/
	// http://fc06.deviantart.net/fs71/f/2012/118/5/7/pixel_art__pancakes_with_syrup_by_omgwtflols-d4xu72c.gif
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/pancake.png");
	}
	
	
	
}
