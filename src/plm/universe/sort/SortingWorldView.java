package plm.universe.sort;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import plm.universe.Operation;
import plm.universe.SvgGenerator;
import plm.universe.WorldView;
import plm.universe.hanoi.HanoiWorld;
import plm.universe.sort.operations.CopyOperation;
import plm.universe.sort.operations.GetValueOperation;
import plm.universe.sort.operations.SetValOperation;
import plm.universe.sort.operations.SwapOperation;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.StringWriter;

public class SortingWorldView extends WorldView {
	private static final long serialVersionUID = 1L;


	private  static boolean useStateView = true; // chronoView if false

	public SortingWorldView(SortingWorld world) {
		super(world);
	}


	/** Make a string representation of the value to be sorted
	 *
	 *  It's a bad idea to display integer values because the students mix the indexes
	 *  and the values. It's better to use a string representation for that.
	 *
	 *  WARNING, this function is duplicated in SortingWorld. Yes, I fell ashamed.
	 */
	protected static  String val2str(int value,int amountOfValues) {
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
	private static void drawAlgoChrono(Graphics2D g2, SortingWorld we,int width, int height) throws Exception {
		int operationsAmount = we.getValueCount();	// little optimization
		/* getWidth()-12 to keep the room to display the very left value. Do that even if we don't depict them */
		float stepX = ((float)height-12) / ((float)(Math.max(operationsAmount, 1)));
		float stepY = ((float)height) / ((float)(we.getValueCount()));
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
			Operation op = we.getEntity(opIdx).getOperations().get(opIdx);

			//valuesAfter = op.run(valuesAfter);

			x1 = (int) (opIdx * stepX);
			x2 = (int) (x1 + stepX);

			/* Draw straight lines for unmodified values */
			for (int valIdx=0; valIdx<we.getValueCount();valIdx++) {
				y1 = (int) (valIdx * stepY + stepY/2);

				tone = getValueColor(valuesAfter[valIdx],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));

				g2.drawLine(x1, y1, x2, y1);
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
			if (op instanceof SwapOperation) { // op is a Swap
//				System.out.println("Swap "+op.source+" <-> "+op.destination);

				// draw source->dest
				y1 = (int) (((SwapOperation) op).getSource() * stepY + stepY/2);
				y2 = (int) (((SwapOperation) op).getDestination() * stepY + stepY/2);
				tone = getValueColor(valuesAfter[((SwapOperation) op).getDestination()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y2);

				// draw dest->source
				y1 = (int) (((SwapOperation) op).getDestination() * stepY + stepY/2);
				y2 = (int) (((SwapOperation) op).getSource() * stepY + stepY/2);
				tone = getValueColor(valuesAfter[((SwapOperation) op).getSource()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y2);

			} else if (op instanceof CopyOperation) {
//				System.out.println("Copy "+op.source+" -> "+op.destination);

				// draw the value being copied over
				y1 = (int) (((CopyOperation) op).getSource() * stepY + stepY/2);
				y2 = (int) (((CopyOperation) op).getDestination() * stepY + stepY/2);
				tone = getValueColor(valuesAfter[((CopyOperation) op).getDestination()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y2);

				// draw the old value remaining the same
				y1 = (int) (((CopyOperation) op).getSource() * stepY + stepY/2);
				tone = getValueColor(valuesAfter[((CopyOperation) op).getDestination()],we.getValueCount());
				g2.setColor(new Color(tone, tone, 128));
				g2.drawLine(x1, y1, x2, y1);

			} else if (op instanceof SetValOperation) {
//				System.out.println("Set "+op.source+" (to "+((SetVal)op).value+")");
				if (drawStr || true) {
					y1 = (int) (((SetValOperation) op).getPosition() * stepY + stepY/2);

					tone = getValueColor(valuesAfter[((SetValOperation) op).getPosition()],we.getValueCount());
					g2.setColor(Color.red);
					g2.drawString(val2str(valuesAfter[((SetValOperation) op).getPosition()],we.getValueCount())+"!", x2, y1);
				}

				/* Don't draw a line for the modified value, actually */
			} else if (op instanceof GetValueOperation) {
//				System.out.println("Get "+((GetVal)op).position);
				if (drawStr || true) {
					int pos = ((GetValueOperation) op).getPosition();
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
	 *
	 */
	private static  void drawAlgoState(Graphics2D g2, SortingWorld world, int width,int height1) {
		double scale = ((double)width)/world.getValues().length;
		int[] values = world.getValues() ;
		for (int i=0;i< values.length;i++)
		{
			double height = ((double)values[i])*((double)height1)/ ((double)(world.getValueCount()-1));
			Shape rect = new Rectangle2D.Double(scale*i, ((double)height1)- height,scale, height);

			g2.setColor( values[i]==i ? Color.GREEN : Color.RED) ;

			g2.fill(rect);

			g2.setColor(Color.black);
			g2.draw(rect);
			if (scale > 20)
				g2.drawString(val2str(values[i],world.getValueCount()) , (int)scale*i+(int)scale/2, height1-2);
		}
		g2.setColor(Color.black);
		g2.drawString(world.getName()+" ("+world.getWriteCount()+" write, "+world.getReadCount()+" read)", 0, 15);
	}

	/**
	 * Decide which view we must draw ( state or chrono )
	 */
	public static void paintComponent(Graphics g, SortingWorld sortingWorld,int width, int height){
		if (useStateView) { // myExo is null during initialization
			paintComponentState(g,sortingWorld,width,height);
		} else {
			try {
				paintComponentChrono(g,sortingWorld,width,height);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Paint the chrono view into a rectangle
	 * @param g some graphics indicating where to draw
	 */
	private static void paintComponentChrono(Graphics g,SortingWorld sortingWorld,int width, int height) throws Exception {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0.,(double)height,
				(float)width));

		g2.setColor(Color.black);
		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		drawAlgoChrono(g2,sortingWorld,width,height);
	}

	/**
	 * Draw the state view into a rectangle
	 * @param g some graphics indicating where to draw
	 */
	private static void paintComponentState(Graphics g,SortingWorld sortingWorld,int width, int height) {
		//super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0.,0.,(double)width,(float)height));

		g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		if (sortingWorld.getEntityCount() > 1)
			System.err.println("Sorting World does not accept more than one entity anymore. Please fix your exercise.");

		drawAlgoState(g2,sortingWorld ,width,height);
	}

	/**
	 * Return the tone of the value. This tone is used by the chrono view, it's the color of a line.
	 * @param value the value from which get the tone
	 * @param valueCount the total amount of values in the array
	 * @return the tone of the value
	 */
	private static int getValueColor(int value,int valueCount) {
		return (int) ((((float) value) / ((float) valueCount)) * 255.);
	}


	/** Returns if we must use the state view ( else we must use the chrono view ) */
	protected boolean isUseStateView() {
		return useStateView;
	}
	protected void setUseStateView(boolean useStateView) {
		this.useStateView = useStateView;
	}

	/**
	 *
	 * @param sortingWorld
	 * @param width
	 * @param height
	 * @return draw the sortingWorldView's SVG under String Format
	 */
	public static String draw(SortingWorld sortingWorld, int width, int height){
		// Ask the test to render into the SVG Graphics2D implementation.
		org.jfree.graphics2d.svg.SVGGraphics2D svgGenerator  = new org.jfree.graphics2d.svg.SVGGraphics2D(400,400);

		paintComponent(svgGenerator, sortingWorld,width,height);

		String str = svgGenerator.getSVGElement();
		return str;


	}
}