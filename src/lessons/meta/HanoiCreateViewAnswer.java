package lessons.meta;

/* BEGIN TEMPLATE */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.World;
import javax.swing.ImageIcon;


public class HanoiCreateViewAnswer extends WorldView {
	/* This field is mandatory because WorldView are swing components  */
	private static final long serialVersionUID = 1L;
	
	/* WorldView constructors naturally take the world to render in argument */
	public HanoiCreateViewAnswer(World w) {
		super(w);
	}

	/** Determines whether this worldview can render a new given world
	 * 
	 * This method is called by JLM when the user selects a new world in the interface. 
	 * If it returns yes, then the WorldView component is reused. If not, a new one is created.
	 * 
	 * This method is not often defined as the ancestor checks that the classes match 
	 * (using world.getClass()), but you may want to override in specific settings. 
	 * For example, BatWorldView redefines it to false to ensure that a new view is recreated each time.
	 */
	public boolean isWorldCompatible(World world) {
		return super.isWorldCompatible(world);
	}

	/** Main method of the renderer: the painting one
	 * 
	 * This is where you actually draw the graphical representation of your world.
	 * Any Java graphical method is just fine.
	 */
	@Override
	public void paintComponent(Graphics g) {
		/* Common setups to any such method */
		super.paintComponent(g);		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/* This is to have the view resized to adapt to the available space
		 * 
		 * Change renderedX and renderedY to the virtual size of the drawing, ie, the size you are using. 
		 *   If it's bigger than the available size for the widget, your drawing will get scaled down; 
		 *   It will be scaled up in the other case.
		 */
		double renderedX = 300.;
		double renderedY = 250.;		
		double ratio = Math.min(((double) getWidth()) / renderedX, ((double) getHeight()) / renderedY);
		g2.translate(Math.abs(getWidth() - ratio * renderedX)/2., Math.abs(getHeight() - ratio * renderedY)/2.);
		g2.scale(ratio, ratio);
		
		/* Clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., renderedX, renderedY));
		
		HanoiMetaWorld hw = (HanoiMetaWorld)world;
		/* Write here the code to render your world. Feel free to declare methods to factorize the code */
		/* BEGIN SOLUTION */		
		drawSlot(g2,hw.values(0), 50.);
		drawSlot(g2,hw.values(1), 150.);
		drawSlot(g2,hw.values(2), 250.);
	}
	
	private void drawSlot(Graphics2D g2, Integer[] values, double xoffset) {
		/* draw bar */
		g2.setColor(Color.black);
		g2.fill(new Rectangle2D.Double(xoffset-2, 55.,  2., 125.));
		
		/* draw discs */
		int height = 1;
		for (int size : values) { 
			g2.setColor(Color.yellow);
			g2.fill(new Rectangle2D.Double( xoffset-size*5-3, 180-(15.*height),  size*10+3, 10));
			g2.setColor(Color.black);
			g2.draw(new Rectangle2D.Double( xoffset-size*5-3, 180-(15.*height),  size*10+3, 10));
			height++;
		}
		/* END SOLUTION */
	}
	
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/hanoi.png");
	}

}
/* END TEMPLATE */