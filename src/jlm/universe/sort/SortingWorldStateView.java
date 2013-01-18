package jlm.universe.sort;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.World;

public class SortingWorldStateView extends WorldView {
	private static final long serialVersionUID = 1L;

	public SortingWorldStateView(World w) {
		super(w);
	}
	@Override
	public String getTabName(){
		return " (state)";
	}
	@Override
	public String getTip(){
		return " (view only last state)";
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
			g2.drawLine(0, offset, getWidth(), offset);
		}
	}

	private void drawAlgo(Graphics2D g2, SortingEntity ent, int offset, int maxSize) {
		synchronized (ent.values) {
			double scale = ((double)getWidth())/ent.values.length;

			for (int i=0;i<ent.values.length;i++) {
				
				double height = ((double)ent.values[i])*((double)maxSize)/ ((double)ent.maxValue);
				Shape rect = new Rectangle2D.Double(scale*i, offset+((double)maxSize)- height,scale, height);
				
				g2.setColor(ent.color[i]);
				g2.fill(rect);
				
				g2.setColor(Color.black);
				g2.draw(rect);
				if (scale > 20) 
					g2.drawString(""+ent.values[i], (int)scale*i+(int)scale/2, offset+maxSize-2);
			}
		}
		g2.setColor(Color.black);
		g2.drawString(ent.getName()+" ("+ent.getWriteCount()+" write, "+ent.getReadCount()+" read)", 0, offset+15);
	}
	
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/sorting.png");
	}

}
