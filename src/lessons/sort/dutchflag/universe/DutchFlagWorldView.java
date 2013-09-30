package lessons.sort.dutchflag.universe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import plm.core.ui.WorldView;
import plm.universe.World;

public class DutchFlagWorldView extends WorldView {

	private static final long serialVersionUID = 1L;
	private double height;
		
	public DutchFlagWorldView(World w) {
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
//				int rank = (int) (e.getY()/height);
						
//				((DutchFlagWorld) world).setSelectedPancake(rank+1);
//				if (e.getClickCount()==2)
//					((DutchFlagWorld) world).doMove();
			}
		});

	}

	private static final Color dutchRed = new Color(174,28,40);
	private static final Color dutchBlue = new Color(33,70,139);
	/**
	 * Draw the component of the world
	 * @param g : some Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		DutchFlagWorld flag = (DutchFlagWorld) world;
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		/* clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., getWidth(), getHeight()));
		
		/* Draw the pancakes */
		int stackSize = flag.getSize();
		height = ((double)getHeight()) / stackSize;
		
		for (int rank=0;rank< stackSize;rank++) { 
			Shape rect = new Rectangle2D.Double(0, height*(stackSize-rank-1), getWidth(), height);

			switch (flag.getColor(rank)) {
			case DutchFlagEntity.BLUE:
				g2.setColor(dutchBlue);
				break;
			case DutchFlagEntity.WHITE:
				g2.setColor(Color.white);
				break;
			case DutchFlagEntity.RED:
				g2.setColor(dutchRed);
				break;
			}
			g2.fill(rect);

			if (flag.getSize()<100) {
				g2.setColor(Color.black);
				g2.draw(rect);
			}
			
		}
						
		if (flag.getColor(0) == 1)
			g2.setColor(Color.black);
		else
			g2.setColor(Color.yellow);
		g2.drawString(""+flag.moveCount+" moves", 0, 15);
	}
}
