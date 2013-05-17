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

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.World;

class SortingViewActionListener implements ActionListener {
	private JMenuItem item;
	private SortingWorldView view;

	public SortingViewActionListener(JMenuItem i, SortingWorldView v) {
		item=i;
		view=v;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.setUseStateView(!view.isUseStateView());
		if (view.isUseStateView()) {
			item.setText("Switch to time view");
		} else {
			item.setText("Switch to state view");
		}
		view.repaint();
	}
}


public class SortingWorldView extends WorldView {
	private static final long serialVersionUID = 1L;
	private JPopupMenu popup = new JPopupMenu();
	private boolean useStateView = true; // chronoView if false

	public SortingWorldView(World w) {
		super(w);
		JMenuItem menuItem;

		//Create the popup menu allowing to switch between views
		menuItem = new JMenuItem("Switch to time view");
		menuItem.addActionListener(new SortingViewActionListener(menuItem,this));
		popup.add(menuItem);

		//Connect it as contextual menu to this pane
		MouseListener popupListener = new MouseAdapter() {
			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(),
							e.getX(), e.getY());
				}
			}

			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}

			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
		};
		this.addMouseListener(popupListener);
	}

	/**
	 * Draw the Chrono view
	 * @param g2 some 2D graphics
	 * @param we the sorting world considered
	 */
	private void drawAlgoChrono(Graphics2D g2, SortingWorld we) {
		int operationsAmount = we.getOperations().size();	// little optimization
		//If the array is small enough, we print the values
		boolean drawStr = operationsAmount <= 50 && we.getValues().length <= 50;
		/* getWidth()-12 to keep the room to display the very left value */
		float stepX = ((float)getWidth()-(drawStr?12:0)) / ((float)(Math.max(operationsAmount, 1)));
		float stepY = ((float)getHeight()) / ((float)(we.getValueCount()));
		int x1, y1, x2, y2, tone;

		int[] vals = new int[we.getInitValues().length];
		int[] prevVals = new int[we.getInitValues().length];
		for (int i = 0; i < we.getInitValues().length; i++) 
		{
			prevVals[i] = we.getInitValues()[i];
		}



		// Case without any operation to draw: initial view
		if (operationsAmount == 0) {

			for (int valueIdx = 0; valueIdx < we.getValueCount(); valueIdx++) { 
				y1 = (int) (valueIdx * stepY + stepY/2.);

				tone = (int) ((((float) we.getValues()[valueIdx]) / ((float) we.getValueCount())) * 255.);
				g2.setColor(new Color(tone, tone, 128));

				g2.drawLine(0, y1, (int)stepX, y1);

				//If the array is small enough, we print the values
				if (drawStr) 
					g2.drawString("" + we.getValues()[valueIdx], 0, y1);
			}
			return;
		}

		// Draw the values at the very left of the figure
		if (drawStr) 
			for (int valueIdx = 0; valueIdx < we.getValueCount(); valueIdx++) { 
				y1 = (int) (valueIdx * stepY + stepY/2);
				tone = getValueColor(we.getInitValues()[valueIdx],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawString("" + we.getInitValues()[valueIdx], 0, y1);
			}

		// Case with several operations
		for (int opIdx = 0; opIdx < operationsAmount; opIdx++) {
			Operation op = we.getOperations().get(opIdx);

			vals = Operation.compute(we.getInitValues(), we.getOperations(), opIdx + 1);

			if (op.getType() == 2 ) // op is a Swap
			{
				x1 = (int) (opIdx * stepX);
				x2 = (int) (x1 + stepX);
				for (int valueIterator=0; valueIterator<we.getValueCount();valueIterator++)
				{
					y1 = (int) (valueIterator * stepY + stepY/2);

					if ( op.getSource() != valueIterator && op.getDestination() != valueIterator) {							
						tone = getValueColor(vals[valueIterator],we.getValueCount());
						g2.setColor(new Color(tone, tone, 128));

						g2.drawLine(x1, y1, x2, y1);
					}

					if (drawStr) 
					{
						tone = getValueColor(vals[valueIterator],we.getValueCount());
						g2.setColor(new Color(tone, tone, 128));
						g2.drawString("" + vals[valueIterator], x2, y1);							
					}
				}

				// draw source->dest
				y1 = (int) (op.getSource() * stepY + stepY/2);
				y2 = (int) (op.getDestination() * stepY + stepY/2);
				tone = getValueColor(vals[op.getDestination()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y2);

				// draw dest->source
				y1 = (int) (op.getDestination() * stepY + stepY/2);
				y2 = (int) (op.getSource() * stepY + stepY/2);
				tone = getValueColor(vals[op.getSource()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y2);

			} 
			else 
			{
				if (!( op.getType() == 0 ) && !(op.getType() == 1 ) )	// op is neither a CopyVal nor a setVal 
				{
					System.out.println("Ouch: that's not a swap but a "+op.toString()+" this code was never debugged.");
				}
				for (int valueIterator = 0; valueIterator < we.getValueCount(); valueIterator++) 
				{
					x1 = (int) (opIdx * stepX);
					y1 = 0;
					for (int prevPos = 0; prevPos < prevVals.length; prevPos++) 
						if (prevVals[prevPos] == valueIterator) 
							y1 = (int) (prevPos * stepY + stepY/2.);

					x2 = (int) (x1 + stepX);
					y2 = y1;
					for (int newPos = 0; newPos < vals.length; newPos++)
					{
						if (vals[newPos] == valueIterator)
						{
							y2 = (int) (newPos * stepY + stepY/2.);
						}
					}

					tone = (int) ((float) ((float) (valueIterator) / ((float) we.getValueCount())) * 255);
					g2.setColor(new Color(tone, tone, 128));

					g2.drawLine(x1, y1, x2, y2);

					if (drawStr) 
					{
						g2.drawString("" + valueIterator, x1, y1);

						//Last array (sorted)
						if(opIdx==operationsAmount-1)
							g2.drawString("" + valueIterator, x2, y2);
					}
				}

				for (int k = 0; k < vals.length; k++) 
				{
					prevVals[k] = vals[k];
				}
			}
		}
	}

	/**
	 * Draw the state view
	 * @param g2 some graphics 2D 
	 * @param world the sorting world considered
	 * @param maxSize the max size for a rectangle ( which represents a data in the array ) 
	 */
	private void drawAlgoState(Graphics2D g2, SortingWorld world, int maxSize) {
		double scale = ((double)getWidth())/world.getValues().length;
		int[] values = world.getValues() ;
		for (int i=0;i< values.length;i++) 
		{
			double height = ((double)values[i])*((double)maxSize)/ ((double)(world.getValueCount()-1));
			Shape rect = new Rectangle2D.Double(scale*i, ((double)maxSize)- height,scale, height);

			g2.setColor( values[i]==i ? Color.GREEN : Color.RED) ;

			g2.fill(rect);

			g2.setColor(Color.black);
			g2.draw(rect);
			if (scale > 20) 
				g2.drawString(""+values[i], (int)scale*i+(int)scale/2, maxSize-2);
		}
		g2.setColor(Color.black);
		g2.drawString(world.getName()+" ("+world.getWriteCount()+" write, "+world.getReadCount()+" read)", 0, 15);
	}

	/**
	 * Return the icon of the world
	 * @return the icon for the exercise selection
	 */
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/sorting.png");
	}

	/**
	 * Decide which view we must draw ( state or chrono )
	 */
	public void paintComponent(Graphics g) {
		if (useStateView) { // myExo is null during initialization
			paintComponentState(g);
		} else {
			paintComponentChrono(g);			
		}
	}

	/**
	 * Paint the chrono view into a rectangle
	 * @param g some graphics indicating where to draw
	 */
	private void paintComponentChrono(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., (double) getWidth(),
				(double) getHeight()));

		g2.setColor(Color.black);
		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		drawAlgoChrono(g2, (SortingWorld) world);
	}

	/**
	 * Draw the state view into a rectangle
	 * @param g some graphics indicating where to draw
	 */
	private void paintComponentState(Graphics g) {
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
		int maxSize = getHeight();

		drawAlgoState(g2, (SortingWorld) this.world, maxSize);
	}

	/**
	 * Return the tone of the value. This tone is used by the chrono view, it's the color of a line.
	 * @param value the value from which get the tone
	 * @param valueCount the total amount of values in the array
	 * @return the tone of the value 
	 */
	private int getValueColor(int value,int valueCount) {
		return (int) ((((float) value) / ((float) valueCount)) * 255.);
	}

	/**
	 * @param useStateView the useStateView to set
	 */
	public void setUseStateView(boolean useStateView) {
		this.useStateView = useStateView;
	}

	/** Return if we must use the state view ( else we must use the chrono view )
	 * @return if we must use the state view
	 */
	public boolean isUseStateView() {
		return useStateView;
	}

}
