package lessons.sort.dutchflag.universe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import plm.core.model.Game;
import plm.core.ui.WorldView;
import plm.universe.EntityControlPanel;
import plm.universe.World;

public class DutchFlagWorldView extends WorldView  {

	private static final long serialVersionUID = 1L;
	private double height;
	private int rankFrom=-1; // DnD marker

	public DutchFlagWorldView(World w) {
		super(w);
		
		new DragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new DutchDragGestureListener());
        DropTarget dropTarget=new DropTarget(this,new DutchDropTargetListener());
        dropTarget.setActive(true); // We accept drops
        this.setDropTarget(dropTarget); 
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
		
		/* Draw the lines */
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
						
		// Display the amount of moves
		if (flag.getColor(stackSize-1) == DutchFlagEntity.WHITE)
			g2.setColor(Color.black);
		else
			g2.setColor(Color.yellow);
		g2.drawString(""+flag.moveCount+" moves", 0, 15);
		
		// Display the DnD markers
		if (flag.getSize()<100 && rankFrom != -1) {
			g2.setColor(Color.red);
			g2.draw(new Rectangle2D.Double(0, height*(stackSize-rankFrom-1), getWidth(), height));
			g2.draw(new Rectangle2D.Double(1, height*(stackSize-rankFrom-1)+1, getWidth()-2, height-2));
			g2.setColor(Color.white);
			g2.draw(new Rectangle2D.Double(2, height*(stackSize-rankFrom-1)+2, getWidth()-4, height-4));
			g2.draw(new Rectangle2D.Double(3, height*(stackSize-rankFrom-1)+3, getWidth()-6, height-6));
		}
	}


	class DutchDragGestureListener implements DragGestureListener {
		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {
			if (world.isAnswerWorld()) // Don't change the correction
				return;

			DutchFlagWorld flag = (DutchFlagWorld) world;
			
			rankFrom = flag.getSize() - dge.getDragOrigin().y/((int)height) - 1;
			DutchFlagWorldView.this.paintImmediately(getVisibleRect());
			
			dge.startDrag(DragSource.DefaultMoveDrop,new TransferableLine(rankFrom));
		}
	}
	class DutchDropTargetListener implements DropTargetListener {
		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			setCursor(DragSource.DefaultMoveDrop);
		}
		@Override
		public void dragOver(DropTargetDragEvent dtde) {/* */}
		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {/* tell your mum */}
		@Override
		public void dragExit(DropTargetEvent dte) {
			setCursor(Cursor.getDefaultCursor());
		}
		@Override
		public void drop(DropTargetDropEvent dtde) {
			dtde.acceptDrop(DnDConstants.ACTION_MOVE);
			DutchFlagWorld flag = (DutchFlagWorld) world; 
			int rankTo = flag.getSize() - dtde.getLocation().y/((int)height) - 1;
			
			// That's unheaven: we get the rankFrom directly from a global, not from the transferable...
			//Integer rankFrom = (Integer) dtde.getTransferable().getTransferData(TransferableLine.dutchLineFlavor);
			dtde.dropComplete(true);
			if (rankFrom != rankTo) {
				EntityControlPanel.echo(Game.i18n.tr("swap({0},{1})",rankFrom,rankTo));
				flag.swap(rankFrom, rankTo);
			}
			
			rankFrom = -1;
			paintImmediately(getVisibleRect());
			DutchFlagWorldView.this.setCursor(Cursor.getDefaultCursor());
			
		}		
	}
}


class TransferableLine implements Transferable { // Actually not used, but to prevent people from accepting our own drops
	static DataFlavor dutchLineFlavor = new DataFlavor(Integer.class,"DutchFlag line");
	private Integer rank;
	public TransferableLine(int _rank) {
		rank = _rank;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {dutchLineFlavor};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(dutchLineFlavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		
		if (flavor.equals(dutchLineFlavor))
			return rank;
		throw new UnsupportedFlavorException(flavor);
	}
	
}
