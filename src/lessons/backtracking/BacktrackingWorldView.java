package lessons.backtracking;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.World;

public class BacktrackingWorldView extends WorldView {
	private static final long serialVersionUID = 1L;
	
	public BacktrackingWorldView(World w) {
		super(w);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		double renderedX = 300.;
		double renderedY = 250.;		
		double ratio = Math.min(((double) getWidth()) / renderedX, ((double) getHeight()) / renderedY);
		g2.translate(Math.abs(getWidth() - ratio * renderedX)/2., Math.abs(getHeight() - ratio * renderedY)/2.);
		g2.scale(ratio, ratio);
		
		/* clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., renderedX, renderedY));
		
		
	}
	
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/backtracking.png");
	}
}
