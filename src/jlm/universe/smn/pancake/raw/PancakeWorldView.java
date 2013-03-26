package jlm.universe.smn.pancake.raw;

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
 * @see PancakeWorld
 * @see WorldView
 */
public class PancakeWorldView extends WorldView {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of the class PancakeWorldView
	 * @param w : a world
	 */
	public PancakeWorldView(World w) {
		super(w);
	}

	/**
	   * Draws an arrow on the given Graphics2D context
	   * ( adapted from http://www.bytemycode.com/snippets/snippet/82/ )
	   * @param g The Graphics2D context to draw on
	   * @param xTail The x location of the "tail" of the arrow
	   * @param yTail The y location of the "tail" of the arrow
	   * @param xHead The x location of the "head" of the arrow
	   * @param yHead The y location of the "head" of the arrow
	   */
	  private void drawArrow( Graphics2D g, int xTail, int yTail, int xHead, int yHead )
	  {
	    float arrowWidth = 10.0f ;
	    float theta = 0.423f ;
	    int[] xPoints = new int[ 3 ] ;
	    int[] yPoints = new int[ 3 ] ;
	    float[] vecLine = new float[ 2 ] ;
	    float[] vecLeft = new float[ 2 ] ;
	    float fLength;
	    float th;
	    float ta;
	    float baseX, baseY ;

	    xPoints[ 0 ] = xHead ;
	    yPoints[ 0 ] = yHead ;

	    // build the line vector
	    vecLine[ 0 ] = (float)xPoints[ 0 ] - xTail ;
	    vecLine[ 1 ] = (float)yPoints[ 0 ] - yTail ;

	    // build the arrow base vector - normal to the line
	    vecLeft[ 0 ] = -vecLine[ 1 ] ;
	    vecLeft[ 1 ] = vecLine[ 0 ] ;

	    // setup length parameters
	    fLength = (float)Math.sqrt( vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1] ) ;
	    th = arrowWidth / ( 2.0f * fLength ) ;
	    ta = arrowWidth / ( 2.0f * ( (float)Math.tan( theta ) / 2.0f ) * fLength ) ;

	    // find the base of the arrow
	    baseX = ( (float)xPoints[ 0 ] - ta * vecLine[0]);
	    baseY = ( (float)yPoints[ 0 ] - ta * vecLine[1]);

	    // build the points on the sides of the arrow
	    xPoints[ 1 ] = (int)( baseX + th * vecLeft[0] );
	    yPoints[ 1 ] = (int)( baseY + th * vecLeft[1] );
	    xPoints[ 2 ] = (int)( baseX - th * vecLeft[0] );
	    yPoints[ 2 ] = (int)( baseY - th * vecLeft[1] );

	    g.drawLine( xTail, yTail, (int)baseX, (int)baseY ) ;
	    g.fillPolygon( xPoints, yPoints, 3 ) ;
	  }
	
	/**
	 * Draw the marker for the flipping
	 * @param g2 : the Graphics2D context to draw on
	 * @param xMin : where the arrows should start in the horizontal plan
	 * @param xMax : where the arrows should stop in the horizontal plan
	 * @param renderedX : where the lines should stop in the horizontal plan
	 */
	private void drawMarker(Graphics2D g2, int xMin, int xMax, int renderedX) {
		PancakeWorld myWorld = (PancakeWorld) this.world;
		
		int yTop = 248 - 8*myWorld.getStackSize();
		int yBottom = 240 - 8 * ( myWorld.getStackSize() - myWorld.getLastModifiedPancake()) ;
		
		int yArrow = Math.max(yTop, yBottom);
	
		Color colors[] = new Color[2];
		if ( myWorld.isFlipped())
		{
			colors[0]=Color.blue;
			colors[1]=Color.red;
		}
		else
		{
			colors[0]=Color.red;
			colors[1]=Color.blue;
		}
		g2.setColor(colors[0]);
		g2.drawLine(xMax, yTop, renderedX, yTop);
		drawArrow(g2,xMin,yTop,xMax,yTop);
		g2.setColor(colors[1]);
		g2.drawLine(xMax, yArrow, renderedX, yArrow);
		drawArrow(g2,xMin,yArrow,xMax,yArrow);
	}
	
	/**
	 * Draw some pancakes
	 * @param g2 : the Graphics2D context to draw on
	 * @param xoffset : the horizontal offset 
	 * @param p: a pancake
	 * @param pancakeNumber: the number of the pancake draw
	 */
	private void drawPancake(Graphics2D g2, double xoffset,int psize,int pancakeNumber){
		g2.setColor(Color.YELLOW);
		g2.fill(new Rectangle2D.Double( xoffset-psize*5-3, 236-(8.*(pancakeNumber)),  psize*10+3, 8));
		g2.setColor(Color.black);
		g2.draw(new Rectangle2D.Double( xoffset-psize*5-3, 236-(8.*(pancakeNumber)),  psize*10+3, 8));
	}
	
	/**
	 * Draw some plate
	 * @param g2 : the Graphics2D context to draw on
	 * @param xoffset : the horizontal offset 
	 * @param amountOfPancakes: the total amount of pancakes
	 */
	private void drawPlate(Graphics2D g2, double xoffset, int amountOfPancakes) {
		g2.draw(new Rectangle2D.Double( 0, 244., xoffset*2, 5));
	}
	
	/**
	   * Draws an arrow on the given Graphics2D context
	   * ( From http://www.bytemycode.com/snippets/snippet/82/ )
	   * @param g The Graphics2D context to draw on
	   * @param x The x location of the "tail" of the arrow
	   * @param y The y location of the "tail" of the arrow
	   * @param xx The x location of the "head" of the arrow
	   * @param yy The y location of the "head" of the arrow
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
				int psize = stack.getPancake(amountOfPancakes-i-1).getRadius();
				drawPancake(g2,xoffset,psize,i);
			}
			
		}
	}
	
	  /**
	 * Return the icon of the world
	 * The picture comes from:
	 * http://omgwtflols.deviantart.com/
	 * http://fc06.deviantart.net/fs71/f/2012/118/5/7/pixel_art__pancakes_with_s236rup_b236_omgwtflols-d4xu72c.gif
	 * @return the icon for the exercise selection
	 */
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/pancake.png");
	}
	
	  	
	/**
	 * Draw the component of the world
	 * @param g The Graphics2D context to draw on
	 */
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
		
		drawMarker(g2, -20, 0,(int) renderedX);
		drawStack( g2,  155.);
	}
	
	
	
}
