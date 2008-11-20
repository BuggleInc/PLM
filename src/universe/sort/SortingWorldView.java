package universe.sort;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import jlm.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.World;

public class SortingWorldView extends WorldView {
	private static final long serialVersionUID = 1L;

	public SortingWorldView(World w) {
		super(w);
	}
	@Override
	public boolean isWorldCompatible(World world) {
		return world instanceof SortingWorld;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0.,0.,(double)getWidth(),(double)getHeight()));

		g2.setColor(Color.black);
		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		int maxSize = getHeight() / world.getEntityCount();
		int offset=0;
		Iterator<Entity> it = world.entities();
		while (it.hasNext()) {
			drawAlgo(g2, (SortingEntity)it.next(), offset, maxSize);
			offset+=maxSize;
			g2.drawLine(0, maxSize, getWidth(), maxSize);
		}
	}

	private void drawAlgo(Graphics2D g2, SortingEntity ent, int offset, int maxSize) {
		synchronized (ent.values) {
			double scale = ((double)getWidth())/ent.values.length;

			for (int i=0;i<ent.values.length;i++) {
				if (ent.sorted[i]) 
					g2.setColor(Color.blue);
				else
					g2.setColor(Color.black);
				g2.drawLine((int)(scale*i),offset+maxSize,  
						(int)(scale*i), offset+(maxSize- (int)(((double)ent.values[i])*maxSize/ ent.maxValue)));
			}
		}
		g2.setColor(Color.black);
		g2.drawString(ent.getName()+" ("+ent.getWriteCount()+" write, "+ent.getReadCount()+" read)", 0, offset+15);
	}
}
