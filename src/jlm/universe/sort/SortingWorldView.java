package jlm.universe.sort;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jlm.core.model.Game;
import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.World;

public class SortingWorldView extends WorldView {
	private static final long serialVersionUID = 1L;
	private JPopupMenu popup = new JPopupMenu();
	boolean useStateView = true; // chronoView if false


	public SortingWorldView(World w) {
		super(w);
		JMenuItem menuItem;
		
		//Create the popup menu allowing to switch between views
	    menuItem = new JMenuItem("Switch to time view");
	    menuItem.addActionListener(new SortingViewActionListener(menuItem,this));
	    popup.add(menuItem);

	    //Connect it as contextual menu to this pane
	    MouseListener popupListener = new MouseAdapter() {
	        public void mousePressed(MouseEvent e) {
	            maybeShowPopup(e);
	        }

	        public void mouseReleased(MouseEvent e) {
	            maybeShowPopup(e);
	        }

	        private void maybeShowPopup(MouseEvent e) {
	            if (e.isPopupTrigger()) {
	                popup.show(e.getComponent(),
	                           e.getX(), e.getY());
	            }
	        }
		};
	    this.addMouseListener(popupListener);
	}
	
	
/*	@Override
	public String getTabName(){
		return " (state)";
	}KillMe*/
	
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/sorting.png");
	}

	public void paintComponent(Graphics g) {
		if (useStateView) { // myExo is null during initialization
			paintComponentState(g);
		} else {
			paintComponentChrono(g);			
		}
	}
	
	public void paintComponentState(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0.,0.,(double)getWidth(),(double)getHeight()));

		g2.setColor(Color.black);
		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		if (world.getEntityCount() > 1) {
			System.err.println("Sorting World");
		}
		int maxSize = getHeight() / world.getEntityCount();
		int offset=0;
		Iterator<Entity> it = world.entities();
		while (it.hasNext()) {
			drawAlgoState(g2, (SortingEntity)it.next(), offset, maxSize);
			offset+=maxSize;
			g2.drawLine(0, offset, getWidth(), offset);
		}
	}

	private void drawAlgoState(Graphics2D g2, SortingEntity ent, int offset, int maxSize) {
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
	
	public void paintComponentChrono(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., (double) getWidth(),
				(double) getHeight()));

		g2.setColor(Color.black);
		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		/* FIXME: this is CRUDE! We circumvent the fact that there is no getSelectedSolutionEntity
		 * On the other side, this is the only point where we need this...
		 * 
		 * Search the offset of the selected entity
		 */
		int offset = 0;
		
		for (Iterator<Entity> it = Game.getInstance().getSelectedWorld().entities();
			it.hasNext();
			) {
			if (it.next().equals(Game.getInstance().getSelectedEntity()))
				break;
			offset++;
		}		
		
		drawAlgoChrono(g2, (SortingEntity) world.getEntity(offset));
	}

	private void drawAlgoChrono(Graphics2D g2, SortingEntity ent) {
		synchronized (ent.values) {
			//If the array is small enough, we print the values
			boolean drawStr = ent.operations.size() <= 50 && ent.values.length <= 50;
			/* getWidth()-12 to keep the room to display the very left value */
			float stepX = ((float)getWidth()-(drawStr?12:0)) / ((float)(Math.max(ent.operations.size(), 1)));
			float stepY = ((float)getHeight()) / ((float)(ent.getValueCount()));
			int x1, y1, x2, y2, tone;

			int[] vals = new int[ent.initValues.size()];
			int[] prevVals = new int[ent.initValues.size()];
			int[] init = new int[ent.initValues.size()];
			for (int i = 0; i < ent.initValues.size(); i++) {
				prevVals[i] = ent.initValues.get(i);
				init[i] = ent.initValues.get(i);
			}

			
			
			// Case without any operation to draw: initial view
			if (ent.operations.size() == 0) {

				for (int valueIdx = 0; valueIdx < ent.getValueCount(); valueIdx++) { 
					y1 = (int) (valueIdx * stepY + stepY/2.);

					tone = (int) ((((float) ent.values[valueIdx]) / ((float) ent.getValueCount())) * 255.);
					g2.setColor(new Color(tone, tone, 128));

					g2.drawLine(0, y1, (int)stepX, y1);

					//If the array is small enough, we print the values
					if (drawStr) 
						g2.drawString("" + ent.values[valueIdx], 0, y1);
				}
				return;
			}

			// Draw the values at the very left of the figure
			if (drawStr) 
				for (int valueIdx = 0; valueIdx < ent.getValueCount(); valueIdx++) { 
					y1 = (int) (valueIdx * stepY + stepY/2);
					tone = ent.getValueColor(ent.initValues.get(valueIdx));
					g2.setColor(new Color(tone, tone, 128));
					g2.drawString("" + ent.initValues.get(valueIdx), 0, y1);
				}

			// Case with several operations
			for (int opIdx = 0; opIdx < ent.operations.size(); opIdx++) {
				Operation op = ent.operations.get(opIdx);
				
				vals = Operation.compute(init, ent.operations, opIdx + 1);
				
				if (op instanceof Swap) {
					x1 = (int) (opIdx * stepX);
					x2 = (int) (x1 + stepX);
					for (int valueIterator=0; valueIterator<ent.getValueCount();valueIterator++) {
						y1 = (int) (valueIterator * stepY + stepY/2);
						
						if (op.source != valueIterator && op.destination != valueIterator) {							
							tone = ent.getValueColor(vals[valueIterator]);
							g2.setColor(new Color(tone, tone, 128));

							g2.drawLine(x1, y1, x2, y1);
						}
						
						if (drawStr) {
							tone = ent.getValueColor(vals[valueIterator]);
							g2.setColor(new Color(tone, tone, 128));
							g2.drawString("" + vals[valueIterator], x2, y1);							
						}
					}
					// draw source->dest
					y1 = (int) (op.source * stepY + stepY/2);
					y2 = (int) (op.destination * stepY + stepY/2);
					tone = ent.getValueColor(vals[op.destination]);
					g2.setColor(new Color(tone, tone, 128));
					g2.drawLine(x1, y1, x2, y2);
					
					// draw dest->source
					y1 = (int) (op.destination * stepY + stepY/2);
					y2 = (int) (op.source * stepY + stepY/2);
					tone = ent.getValueColor(vals[op.source]);
					g2.setColor(new Color(tone, tone, 128));
					g2.drawLine(x1, y1, x2, y2);
					
				} else {
					if (!( op instanceof CopyVal) && !(op instanceof SetVal) )
					{
						System.out.println("Ouch: that's not a swap but a "+op.toString()+" this code was never debugged.");
					}
				for (int valueIterator = 0; valueIterator < ent.getValueCount(); valueIterator++) {
					x1 = (int) (opIdx * stepX);
					y1 = 0;
					for (int prevPos = 0; prevPos < prevVals.length; prevPos++) 
						if (prevVals[prevPos] == valueIterator) 
							y1 = (int) (prevPos * stepY + stepY/2.);

					x2 = (int) (x1 + stepX);
					y2 = y1;
					for (int newPos = 0; newPos < vals.length; newPos++) 
						if (vals[newPos] == valueIterator) 
							y2 = (int) (newPos * stepY + stepY/2.);

					tone = (int) ((float) ((float) (valueIterator) / ((float) ent.getValueCount())) * 255);
					g2.setColor(new Color(tone, tone, 128));

					g2.drawLine(x1, y1, x2, y2);

					if (drawStr) {
						g2.drawString("" + valueIterator, x1, y1);

						//Last array (sorted)
						if(opIdx==ent.operations.size()-1)
							g2.drawString("" + valueIterator, x2, y2);
					}
				}

				for (int k = 0; k < vals.length; k++) {
					prevVals[k] = vals[k];
				}
				}
			}
		}
	}
}


class SortingViewActionListener implements ActionListener {
	private JMenuItem item;
	private SortingWorldView view;
	public SortingViewActionListener(JMenuItem i, SortingWorldView v) {
		item=i;
		view=v;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		view.useStateView = !view.useStateView;
		if (view.useStateView) {
			item.setText("Switch to time view");
		} else {
			item.setText("Switch to state view");
		}
		view.repaint();
	}
}
