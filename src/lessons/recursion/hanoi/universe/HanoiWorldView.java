package lessons.recursion.hanoi.universe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.swing.JOptionPane;

import plm.core.model.Game;
import plm.core.ui.WorldView;
import plm.universe.EntityControlPanel;
import plm.universe.World;

public class HanoiWorldView extends WorldView {
	private static final long serialVersionUID = 1L;
	int pegFrom = -1;
	boolean buggyMove = false;
	
	public HanoiWorldView(World w) {
		super(w);
		
		new DragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new HanoiDragGestureListener());
        DropTarget dropTarget=new DropTarget(this,new HanoiDropTargetListener());
        dropTarget.setActive(true); // We accept drops
        this.setDropTarget(dropTarget); 

	}

	@Override
	public void paintComponent(Graphics g) {
		HanoiWorld board = (HanoiWorld)world;
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/* clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., getWidth(), getHeight()));

		double renderedX = 300.;
		double renderedY = 250.;		
		double ratio = Math.min(((double) getWidth()) / renderedX, ((double) getHeight()) / renderedY);
		g2.translate(Math.abs(getWidth() - ratio * renderedX)/2., Math.abs(getHeight() - ratio * renderedY)/2.);
		g2.scale(ratio, ratio);
		
		for (int s=0;s<board.getSlotAmount();s++) 
			drawSlot(g2, s, (1+2*s)*300./(2*board.getSlotAmount()));
	}
	
	private void drawSlot(Graphics2D g2, int rank, double xoffset) {
		HanoiWorld board = (HanoiWorld)world;
		
		/* draw bars, with color indicating whether it's a valid move during interactive drag&drop */
		if (pegFrom == -1)
			g2.setColor(Color.black);
		else if (board.getRadius(pegFrom) > board.getRadius(rank))
			g2.setColor(Color.red);
		else 
			g2.setColor(Color.green);
		g2.fill(new Rectangle2D.Double(xoffset-2, 55.,  2., 125.));
		
		if (board.values(rank)==null)
			return;
		
		/* draw discs */
		int height = 1;
		for (int i=0; i<board.values(rank).length; i++) {
			int size = board.values(rank)[i];
			g2.setColor(board.getColor(rank, i));
			g2.fill(new Rectangle2D.Double( xoffset-size*5-3, 180-(11.*height),  size*10+3, 10));
			if (rank == pegFrom && i==board.values(rank).length-1) {
				g2.setStroke(new BasicStroke(3));
				g2.setColor(buggyMove ? Color.red : Color.green);
			} else {
				g2.setColor(Color.black);
			}
			g2.draw(new Rectangle2D.Double( xoffset-size*5-3, 180-(11.*height),  size*10+3, 10));
			height++;
		}
        g2.setStroke(new BasicStroke(1));
	}
	
	
	class HanoiDragGestureListener implements DragGestureListener {
		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {
			if (world.isAnswerWorld()) // Don't change the correction
				return;
			HanoiWorld board = (HanoiWorld) world;

			int slotAmount =  ((HanoiWorld)HanoiWorldView.this.world).getSlotAmount();
			pegFrom = dge.getDragOrigin().x / (HanoiWorldView.this.getWidth()/slotAmount);
			if (pegFrom >2 || pegFrom<0 || board.getSlotSize(pegFrom) == 0) {
				pegFrom = -1;
				return;
			}
			HanoiWorldView.this.paintImmediately(getVisibleRect());
			
			dge.startDrag(DragSource.DefaultMoveDrop,new TransferableHanoiPiece(pegFrom),new HanoiDragSourceListener());
		}
	}
	class HanoiDropTargetListener implements DropTargetListener {
		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			setCursor(DragSource.DefaultMoveDrop);
		}
		int lastPeg = -1;
		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			int slotAmount =  ((HanoiWorld)HanoiWorldView.this.world).getSlotAmount();
			int peg = dtde.getLocation().x / ( HanoiWorldView.this.getWidth() / slotAmount);
			if (peg == lastPeg)
				return;
			lastPeg = peg;
			HanoiWorld board = (HanoiWorld) world;
			if (board.getRadius(pegFrom) >= board.getRadius(lastPeg)){
				HanoiWorldView.this.setCursor(DragSource.DefaultMoveNoDrop);
				buggyMove = true;
			} else {
				HanoiWorldView.this.setCursor(DragSource.DefaultMoveDrop);
				buggyMove = false;
			}
			HanoiWorldView.this.paintImmediately(HanoiWorldView.this.getVisibleRect());
		}
		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {/* tell your mum */}
		@Override
		public void dragExit(DropTargetEvent dte) {
			setCursor(Cursor.getDefaultCursor());
		}
		@Override
		public void drop(DropTargetDropEvent dtde) {
			dtde.acceptDrop(DnDConstants.ACTION_MOVE);
			HanoiWorld board = (HanoiWorld) world; 
			int slotAmount =  board.getSlotAmount();
			int pegTo = dtde.getLocation().x/( HanoiWorldView.this.getWidth() / slotAmount);
			if (pegTo <0 || pegTo>=slotAmount) {
				System.out.println("Ignore the buggy drop onto peg "+pegTo);
				dtde.dropComplete(false);
				return;
			}
			
			// That's unheaven: we get the rankFrom directly from a global, not from the transferable...
			//Integer rankFrom = (Integer) dtde.getTransferable().getTransferData(TransferableLine.dutchLineFlavor);
			dtde.dropComplete(true);
			if (pegFrom != pegTo) {
				EntityControlPanel.echo(Game.i18n.tr("move({0},{1})",pegFrom,pegTo));
				try {
					board.move(pegFrom, pegTo);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(null, iae.getLocalizedMessage(),Game.i18n.tr("Invalid move"), JOptionPane.ERROR_MESSAGE);
				}
			}
			
			pegFrom = -1;
			buggyMove = false;
			HanoiWorldView.this.setCursor(Cursor.getDefaultCursor());
			HanoiWorldView.this.paintImmediately(HanoiWorldView.this.getVisibleRect());
			
		}		
	}
	class HanoiDragSourceListener implements DragSourceListener {
		boolean outside = false;
		@Override
		public void dragEnter(DragSourceDragEvent dsde) { 
			outside = false;
		}
		@Override
		public void dragOver(DragSourceDragEvent dsde) { /* */ }
		@Override
		public void dropActionChanged(DragSourceDragEvent dsde) { /* */ }
		@Override
		public void dragExit(DragSourceEvent dse) { 
			outside = true;
		}
		@Override
		public void dragDropEnd(DragSourceDropEvent dsde) {
			if (outside) { // Dropped outside of our area
				pegFrom = -1;
				buggyMove = false;
				HanoiWorldView.this.paintImmediately(HanoiWorldView.this.getVisibleRect());
			}
		}
	}
}


class TransferableHanoiPiece implements Transferable { 
	static DataFlavor hanoiDataFlavor = new DataFlavor(Integer.class,"Hanoi piece");
	private Integer pegFrom;
	public TransferableHanoiPiece(int _pegFrom) {
		pegFrom = _pegFrom;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {hanoiDataFlavor};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(hanoiDataFlavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		
		if (flavor.equals(hanoiDataFlavor))
			return pegFrom;
		throw new UnsupportedFlavorException(flavor);
	}
	
}

