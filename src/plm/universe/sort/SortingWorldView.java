package plm.universe.sort;

import java.awt.Color;
import java.awt.Component;
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
import java.util.Locale;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.HumanLangChangesListener;
import plm.core.model.Game;
import plm.core.ui.WorldView;
import plm.universe.World;

class SortingViewActionListener implements ActionListener, HumanLangChangesListener {
	private JMenuItem item;
	private SortingWorldView view;
	private I18n i18n;

	public SortingViewActionListener(JMenuItem i, SortingWorldView v) {
		item=i;
		view=v;
		Game.getInstance().addHumanLangListener(this);
		currentHumanLanguageHasChanged(item.getLocale());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.setUseStateView(!view.isUseStateView());
		currentHumanLanguageHasChanged(item.getLocale());
		view.repaint();
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages", newLang, I18nFactory.FALLBACK);
		if (view.isUseStateView()) {
			item.setText(i18n.tr("Switch to time view"));
		} else {
			item.setText(i18n.tr("Switch to state view"));
		}
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

	/** Make a string representation of the value to be sorted
	 * 
	 *  It's a bad idea to display integer values because the students mix the indexes 
	 *  and the values. It's better to use a string representation for that. 
	 *  
	 *  WARNING, this function is duplicated in SortingWorld. Yes, I fell ashamed.
	 */
	protected String val2str(int value,int amountOfValues) {
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (amountOfValues<26) {
			return letters.substring(value, value+1);
		} 
		if (amountOfValues < 26*26) {
			return   letters.substring(value/26, (value/26)+1 )
					+letters.substring(value%26, (value%26)+1 );
		}
		return ""+value;
	}
	
	/**
	 * Draw the Chrono view
	 * @param g2 some 2D graphics
	 * @param we the sorting world considered
	 */
	private void drawAlgoChrono(Graphics2D g2, SortingWorld we) {
		int operationsAmount = we.getOperations().size();	// little optimization
		/* getWidth()-12 to keep the room to display the very left value. Do that even if we don't depict them */
		float stepX = ((float)getWidth()-12) / ((float)(Math.max(operationsAmount, 1)));
		float stepY = ((float)getHeight()) / ((float)(we.getValueCount()));
		int x1, y1, x2, y2, tone;

		// If the array is small enough, we print the values
		boolean drawStr = (stepX > 12) && (stepY>12);
		
		// Case without any operation to draw: initial view
		if (operationsAmount == 0) {

			for (int valueIdx = 0; valueIdx < we.getValueCount(); valueIdx++) { 
				y1 = (int) (valueIdx * stepY + stepY/2.);

				tone = (int) ((((float) we.getValues()[valueIdx]) / ((float) we.getValueCount())) * 255.);
				g2.setColor(new Color(tone, tone, 128));

				g2.drawLine(0, y1, (int)stepX, y1);

				//If the array is small enough, we print the values
				if (drawStr) 
					g2.drawString(val2str(we.getValues()[valueIdx],we.getValueCount()), 0, y1);
			}
			return;
		}

		// Draw the values at the very left of the figure (in any case)
		for (int valueIdx = 0; valueIdx < we.getValueCount(); valueIdx++) { 
			y1 = (int) (valueIdx * stepY + stepY/2);
			tone =  getValueColor(we.getInitValues()[valueIdx],we.getValueCount());
			g2.setColor(new Color(tone, tone, 128));
			g2.drawString(val2str(we.getInitValues()[valueIdx],we.getValueCount()), 0, y1);
		}
		
		int[] valuesAfter = new int[we.getInitValues().length];
		int[] valuesBefore = new int[we.getInitValues().length];
		for (int i = 0; i < we.getInitValues().length; i++) { 
			valuesBefore[i] = we.getInitValues()[i];
			valuesAfter[i] = valuesBefore[i];
		}
		
		// Case with several operations
		for (int opIdx = 0; opIdx < operationsAmount; opIdx++) {
			Operation op = we.getOperations().get(opIdx);

			valuesAfter = op.run(valuesAfter);
			
			x1 = (int) (opIdx * stepX);
			x2 = (int) (x1 + stepX);

			/* Draw straight lines for unmodified values */
			for (int valIdx=0; valIdx<we.getValueCount();valIdx++) {
				y1 = (int) (valIdx * stepY + stepY/2);
				
				if ( op.getSource() != valIdx && op.getDestination() != valIdx) {							
					tone = getValueColor(valuesAfter[valIdx],we.getValueCount());
					g2.setColor(new Color(tone, tone, 128));
					
					g2.drawLine(x1, y1, x2, y1);
				}
			}
			/* Write the values in their new position (if there is not too much values) */
			if (drawStr) 
				for (int valIdx=0; valIdx<we.getValueCount();valIdx++) { 
					y1 = (int) (valIdx * stepY + stepY/2);
					
					tone = getValueColor(valuesAfter[valIdx],we.getValueCount());
					g2.setColor(new Color(tone, tone, 128));
					g2.drawString(val2str(we.getValues()[valIdx],we.getValueCount()), x2, y1);		
				}
			
			/* Draw the lines depicting the current operation */
			if (op instanceof Swap ) { // op is a Swap
//				System.out.println("Swap "+op.source+" <-> "+op.destination);
				
				// draw source->dest
				y1 = (int) (op.getSource() * stepY + stepY/2);
				y2 = (int) (op.getDestination() * stepY + stepY/2);
				tone = getValueColor(valuesAfter[op.getDestination()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y2);

				// draw dest->source
				y1 = (int) (op.getDestination() * stepY + stepY/2);
				y2 = (int) (op.getSource() * stepY + stepY/2);
				tone = getValueColor(valuesAfter[op.getSource()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y2);

			} else if (op instanceof CopyVal) {
//				System.out.println("Copy "+op.source+" -> "+op.destination);
				
				// draw the value being copied over
				y1 = (int) (op.getSource() * stepY + stepY/2);
				y2 = (int) (op.getDestination() * stepY + stepY/2);
				tone = getValueColor(valuesAfter[op.getDestination()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y2);
				
				// draw the old value remaining the same
				y1 = (int) (op.getSource() * stepY + stepY/2);
				tone = getValueColor(valuesAfter[op.getDestination()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y1);
				
			} else if (op instanceof SetVal) {
//				System.out.println("Set "+op.source+" (to "+((SetVal)op).value+")");
				if (drawStr || true) {
					y1 = (int) (op.source * stepY + stepY/2);
				
					tone = getValueColor(valuesAfter[op.source],we.getValueCount());
					g2.setColor(Color.red);
					g2.drawString(val2str(valuesAfter[op.source],we.getValueCount())+"!", x2, y1);
				}

				/* Don't draw a line for the modified value, actually */
			} else if (op instanceof GetVal) {
//				System.out.println("Get "+((GetVal)op).position);
				if (drawStr || true) {
					int pos = ((GetVal) op).position;
					y1 = (int) (pos * stepY + stepY/2);
				
					tone = getValueColor(valuesAfter[pos],we.getValueCount());
					g2.setColor(Color.MAGENTA);
					g2.drawString(val2str(valuesAfter[pos],we.getValueCount())+"?", x2, y1);
				}
			} else {
				System.out.println("This operation is not depicted because that's a "+op.toString()+"; please report this bug.");
			}
			

			for (int k = 0; k < valuesAfter.length; k++) 
				valuesBefore[k] = valuesAfter[k];
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
				g2.drawString(val2str(values[i],world.getValueCount()) , (int)scale*i+(int)scale/2, maxSize-2);
		}
		g2.setColor(Color.black);
		g2.drawString(world.getName()+" ("+world.getWriteCount()+" write, "+world.getReadCount()+" read)", 0, 15);
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

		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		if (world.getEntityCount() > 1) 
			System.err.println("Sorting World does not accept more than one entity anymore. Please fix your exercise.");
		
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


	/** Returns if we must use the state view ( else we must use the chrono view ) */
	protected boolean isUseStateView() {
		return useStateView;
	}
	protected void setUseStateView(boolean useStateView) {
		this.useStateView = useStateView;
	}

	@Override
	public void dispose() {
		super.dispose();
		for(Component child : popup.getComponents()) {
			if(child instanceof JMenuItem) {
				JMenuItem menuItem = (JMenuItem) child;
				for(ActionListener al:menuItem.getActionListeners()) {
					Game.getInstance().removeHumanLangListener((HumanLangChangesListener) al);
				}
			}
		}
	}
}
