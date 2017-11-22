package plm.universe.dutchflag;




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


//import plm.core.model.Game;
//import plm.core.ui.WorldView;
//import plm.universe.EntityControlPanel;
import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.WorldView;


public class DutchFlagWorldView extends DutchFlagWorld{



    private static final long serialVersionUID = 1L;
    private double height;
    private int rankFrom=-1; // DnD marker

    private int Height=400;
    private int width=400;



    public DutchFlagWorldView(FileUtils fileUtils, String name, int size) {
        super(fileUtils, name, size);
    }

    public DutchFlagWorldView(FileUtils fileUtils, String name, int size, int colorRemoved) {
        super(fileUtils, name, size, colorRemoved);
    }

    public DutchFlagWorldView(DutchFlagWorld dfw)
    {
        super(dfw);
    }
    private static final Color dutchRed = new Color(174,28,40);
    private static final Color dutchBlue = new Color(33,70,139);
    /**
     * Draw the component of the world
     * @param g : some Graphics
     */
    public void paintComponent(Graphics g) {


        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Monaco", Font.PLAIN, 12));

		/* clear board */
        g2.setColor(Color.white);
        g2.fill(new Rectangle2D.Double(0., 0., getWidth(), getHeight()));

		/* Draw the lines */
        int stackSize = this.getSize();
        height = ((double)getHeight()) / stackSize;

        for (int rank=0;rank< stackSize;rank++) {
            Shape rect = new Rectangle2D.Double(0, height*(stackSize-rank-1), getWidth(), height);

            switch (this.getColor(rank)) {
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

            if (this.getSize()<100) {
                g2.setColor(Color.black);
                g2.draw(rect);
            }
        }

        // Display the amount of moves
        if (this.getColor(stackSize-1) == DutchFlagEntity.WHITE)
            g2.setColor(Color.black);
        else
            g2.setColor(Color.yellow);
        g2.drawString(""+this.moveCount+" moves", 0, 15);

        // Display the DnD markers
        if (this.getSize()<100 && rankFrom != -1) {
            g2.setColor(Color.red);
            g2.draw(new Rectangle2D.Double(0, height*(stackSize-rankFrom-1), getWidth(), height));
            g2.draw(new Rectangle2D.Double(1, height*(stackSize-rankFrom-1)+1, getWidth()-2, height-2));
            g2.setColor(Color.white);
            g2.draw(new Rectangle2D.Double(2, height*(stackSize-rankFrom-1)+2, getWidth()-4, height-4));
            g2.draw(new Rectangle2D.Double(3, height*(stackSize-rankFrom-1)+3, getWidth()-6, height-6));
        }
    }





    public int getWidth()
    {
        return this.width;
    }

    public  int getHeight()
    {
        return Height;
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


