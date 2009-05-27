package universe.hanoi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import jlm.ui.WorldView;
import jlm.universe.World;

public class HanoiWorldView extends WorldView {
	private static final long serialVersionUID = 1L;
	
	public HanoiWorldView(World w) {
		super(w);
	}

	@Override
	public boolean isWorldCompatible(World world) {
		return world instanceof HanoiWorld;
	}

	
	@Override
	public void paintComponent(Graphics g) {
		HanoiWorld hw = (HanoiWorld)world;
		super.paintComponent(g);
//		System.err.println("display "+hw);

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/* clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., 300., 250.));
		
		
		drawSlot(g2,hw.values(0), 50.);
		drawSlot(g2,hw.values(1), 150.);
		drawSlot(g2,hw.values(2), 250.);
	}
	
	private void drawSlot(Graphics2D g2, Integer[] values, double xoffset) {
		/* draw bar */
		g2.setColor(Color.black);
		g2.fill(new Rectangle2D.Double(  xoffset-2, 55.,  2., 125.));
		
		/* draw discs */
		int height = 1;
		for (int size : values) { 
			g2.setColor(Color.yellow);
			g2.fill(new Rectangle2D.Double( xoffset-size*5-3, 180-(15.*height),  size*10+3, 10));
			g2.setColor(Color.black);
			g2.draw(new Rectangle2D.Double( xoffset-size*5-3, 180-(15.*height),  size*10+3, 10));
			height++;
		}
	}
}
