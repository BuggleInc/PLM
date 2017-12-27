package plm.universe.dutchflag;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.jfree.graphics2d.svg.SVGGraphics2D;

//import plm.core.model.Game;
//import plm.core.ui.WorldView;
//import plm.universe.EntityControlPanel;
import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.WorldView;


public class DutchFlagWorldView extends WorldView {



    private static final long serialVersionUID = 1L;
    private static double heightworld;
    private static int rankFrom=-1; // DnD marker


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
    public static void paintComponent(Graphics g, DutchFlagWorld dutchFlagWorld,int width, int height) {


        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Monaco", Font.PLAIN, 12));

        /* clear board */
        g2.setColor(Color.white);
        g2.fill(new Rectangle2D.Double(0., 0., width, height));

        /* Draw the lines */
        int stackSize = dutchFlagWorld.getSize();
        heightworld = ((double)height) / stackSize;

        for (int rank=0;rank< stackSize;rank++) {
            Shape rect = new Rectangle2D.Double(0, heightworld*(stackSize-rank-1), width, heightworld);

            switch (dutchFlagWorld.getColor(rank)) {
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

            if (dutchFlagWorld.getSize()<100) {
                g2.setColor(Color.black);
                g2.draw(rect);
            }
        }

        // Display the amount of moves
        if (dutchFlagWorld.getColor(stackSize-1) == DutchFlagEntity.WHITE)
            g2.setColor(Color.black);
        else
            g2.setColor(Color.yellow);
        g2.drawString(""+dutchFlagWorld.moveCount+" moves", 0, 15);

        // Display the DnD markers
        if (dutchFlagWorld.getSize()<100 && rankFrom != -1) {
            g2.setColor(Color.red);
            g2.draw(new Rectangle2D.Double(0, height*(stackSize-rankFrom-1), width, height));
            g2.draw(new Rectangle2D.Double(1, height*(stackSize-rankFrom-1)+1, width-2, height-2));
            g2.setColor(Color.white);
            g2.draw(new Rectangle2D.Double(2, height*(stackSize-rankFrom-1)+2, width-4, height-4));
            g2.draw(new Rectangle2D.Double(3, height*(stackSize-rankFrom-1)+3, width-6, height-6));
        }
    }

    /**
     *
     * @param dutchFlagWorld
     * @param width
     * @param height
     * @return the DutchFlagWorld's world under SVG Format
     */
    public static String draw(DutchFlagWorld dutchFlagWorld, int width, int height) {
        // Ask the test to render into the SVG Graphics2D implementation.
        SVGGraphics2D svgGenerator  = new SVGGraphics2D(400,400);

        paintComponent(svgGenerator, dutchFlagWorld,width,height);

        String str = svgGenerator.getSVGElement();
        return str;

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


