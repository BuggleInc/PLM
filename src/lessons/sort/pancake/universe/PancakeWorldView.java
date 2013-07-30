package lessons.sort.pancake.universe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import jlm.core.ui.WorldView;
import jlm.universe.World;

public class PancakeWorldView extends WorldView {

	private static final long serialVersionUID = 1L;
	private double height;
		
	public PancakeWorldView(World w) {
		super(w);
		
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int rank = (int) (e.getY()/height);
						
				((PancakeWorld) world).setSelectedPancake(rank+1);
				if (e.getClickCount()==2)
					((PancakeWorld) world).doMove();
			}
		});

	}

	/**
	 * Draw the component of the world
	 * @param g : some Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		PancakeWorld stack = (PancakeWorld) world;
		int spatulaSize = 30;
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		/* clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., getWidth(), getHeight()));
		
		/* Draw the pancakes */
		int stackSize = stack.getStackSize();
		double usefullWidth = getWidth()-2*spatulaSize;
		height = ((double)getHeight()) / stackSize;
		
		for (int rank=0;rank< stackSize;rank++) { 
			int radius = stack.getPancakeRadius(rank);
			double halfWidth =  usefullWidth/2. * ((double)radius) / ((double)(stackSize));
			
			Shape rect = new Rectangle2D.Double(spatulaSize+usefullWidth/2-halfWidth, height*rank, 2*halfWidth, height);

			g2.setColor(Color.yellow) ;
			g2.fill(rect);

			g2.setColor(Color.black);
			g2.draw(rect);
			
		}
		// Draw the burnt side, if any
		if (stack.isBurnedPancake()) {
			g2.setColor(new Color(200, 113, 55)); // it's brown
			double burntSize = Math.max(height/5, 4);

			for (int rank=0;rank<stackSize;rank++) {
				int radius = stack.getPancakeRadius(rank);
				double halfWidth =  usefullWidth/2. * ((double)radius) / ((double)(stackSize));
				
				if (stack.isPancakeUpsideDown(rank)) 
					g2.fill(new Rectangle2D.Double(spatulaSize+usefullWidth/2-halfWidth, height*rank, 2*halfWidth, burntSize));
				else 
					g2.fill(new Rectangle2D.Double(spatulaSize+usefullWidth/2-halfWidth, height*(rank+1)-burntSize, 2*halfWidth, burntSize));
			}
		}
				
		// draw the marker of the lastly flipped pancakes
		if (stack.getSelectedPancake() > 0) {
			int move = stack.getSelectedPancake();
			Stroke old = g2.getStroke();
			Stroke fat = new BasicStroke(3);
			g2.setStroke(fat);
			g2.setColor(Color.black);
			g2.drawLine(spatulaSize, (int)(height*move), (int) (usefullWidth+spatulaSize), (int)(height*move) );
			g2.drawLine(0,(int)(height*move-30), 30, (int)(height*move) );
			g2.setStroke(old);			
		} else if (stack.getLastMove() > 0) {
			int move = stack.getLastMove();
			Stroke old = g2.getStroke();
			Stroke fat = new BasicStroke(3);
			g2.setStroke(fat);
			g2.setColor(Color.black);
			g2.drawLine(spatulaSize, (int)(height*move), (int) (usefullWidth+spatulaSize), (int)(height*move) );
			g2.drawLine(0,(int)(height*move+30), 30, (int)(height*move));
			g2.setStroke(old);
		}
		
		// Draw the markers indicating that pancakes are in row
		double markerRadius = Math.max(height/8, 3);
		g2.setColor(Color.magenta);
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
				g2.fill(new Ellipse2D.Double(spatulaSize+usefullWidth/2-markerRadius, height*rank-markerRadius, 
						2*markerRadius,2*markerRadius) );
			}

// TODO: display move count
//		g2.setColor(Color.black);
//		g2.drawString(world.getName()+" ("+world.getWriteCount()+" write, "+world.getReadCount()+" read)", 0, 15);
	}
	
	
	
}
