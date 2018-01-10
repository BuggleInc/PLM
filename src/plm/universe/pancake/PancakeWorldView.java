package plm.universe.pancake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import plm.utils.SVGGraphics2D;

public class PancakeWorldView {

	private static double height;

	/**
	 * Draw the component of the world
	 */
	public  static void paintComponent(SVGGraphics2D g,PancakeWorld pancakeWorld,int width, int height1) {
		PancakeWorld stack = pancakeWorld;
		int spatulaSize = 30;

		g.setFont(new Font("Monaco", Font.PLAIN, 12));

		/* clear board */
		g.setColor(Color.white);
		g.fill(new Rectangle2D.Double(0., 0., width,height1));

		/* Draw the pancakes */
		int stackSize = stack.getStackSize();
		double usefullWidth = width-2*spatulaSize;
		height = ((double) height1) / stackSize;

		for (int rank=0;rank< stackSize;rank++) {
			int radius = stack.getPancakeRadius(rank);
			double halfWidth =  usefullWidth/2. * ((double)radius) / ((double)(stackSize));

			Shape rect = new Rectangle2D.Double(spatulaSize+usefullWidth/2-halfWidth, height*rank, 2*halfWidth, height);

			g.setColor(Color.yellow) ;
			g.fill(rect);

			g.setColor(Color.black);
			g.draw(rect);

		}
		// Draw the burnt side, if any
		if (stack.isBurnedPancake()) {
			g.setColor(new Color(200, 113, 55)); // it's brown
			double burntSize = Math.max(height/5, 4);

			for (int rank=0;rank<stackSize;rank++) {
				int radius = stack.getPancakeRadius(rank);
				double halfWidth =  usefullWidth/2. * ((double)radius) / ((double)(stackSize));

				if (stack.isPancakeUpsideDown(rank))
					g.fill(new Rectangle2D.Double(spatulaSize+usefullWidth/2-halfWidth, height*rank, 2*halfWidth, burntSize));
				else
					g.fill(new Rectangle2D.Double(spatulaSize+usefullWidth/2-halfWidth, height*(rank+1)-burntSize, 2*halfWidth, burntSize));
			}
		}

		// draw the marker of the lastly flipped pancakes
		if (stack.getSelectedPancake() > 0) {
			int move = stack.getSelectedPancake();
			Stroke old = g.getStroke();
			Stroke fat = new BasicStroke(3);
			g.setStroke(fat);
			g.setColor(Color.black);
			g.drawLine(spatulaSize, (int)(height*move), (int) (usefullWidth+spatulaSize), (int)(height*move) );
			g.drawLine(0,(int)(height*move-30), 30, (int)(height*move) );
			g.setStroke(old);
		} else if (stack.getLastMove() > 0) {
			int move = stack.getLastMove();
			Stroke old = g.getStroke();
			Stroke fat = new BasicStroke(3);
			g.setStroke(fat);
			g.setColor(Color.black);
			g.drawLine(spatulaSize, (int)(height*move), (int) (usefullWidth+spatulaSize), (int)(height*move) );
			g.drawLine(0,(int)(height*move+30), 30, (int)(height*move));
			g.setStroke(old);
		}

		// Draw the markers indicating that pancakes are in row
		double markerRadius = Math.max(height/8, 3);
		g.setColor(Color.magenta);
		for (int rank = 1; rank<stackSize ;rank++)
			if (Math.abs(stack.getPancakeRadius(rank-1)-stack.getPancakeRadius(rank)) == 1) {
				if (stack.isBurnedPancake()) {
					if (stack.isPancakeUpsideDown(rank-1) != stack.isPancakeUpsideDown(rank))
						continue; // Burnt sides don't match -> don't draw it
					if (stack.isPancakeUpsideDown(rank) && stack.getPancakeRadius(rank-1) == stack.getPancakeRadius(rank)-1)
						continue; // Burnt sides on bad side of pyramid 
					if (!stack.isPancakeUpsideDown(rank) && stack.getPancakeRadius(rank-1) == stack.getPancakeRadius(rank)+1)
						continue; // Burnt sides on bad side of pyramid 
				}
				g.fill(new Ellipse2D.Double(spatulaSize+usefullWidth/2-markerRadius, height*rank-markerRadius,
						2*markerRadius,2*markerRadius) );
			}

		g.setColor(Color.black);
		g.drawString(""+stack.moveCount+" moves", 0, 15);
	}

	/**
	 *
	 * @param pancakeWorld
	 * @param width
	 * @param height
	 * @return the PancakeWorld's SVG under String format
	 */
	public static String draw(PancakeWorld pancakeWorld){

		SVGGraphics2D svgGenerator  = new SVGGraphics2D(400,400);

		paintComponent(svgGenerator, pancakeWorld, 400, 400);

		String str = svgGenerator.getSVGElement();
		return str;

	}

}

