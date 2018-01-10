package plm.universe.dutchflag;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import plm.utils.SVGGraphics2D;

public class DutchFlagWorldView {


    private static double heightworld;
    private static int rankFrom=-1; // DnD marker

    private static final Color dutchRed = new Color(174,28,40);
    private static final Color dutchBlue = new Color(33,70,139);
    /**
     * Draw the component of the world
     * @param h : some Graphics
     */
    public static void paintComponent(SVGGraphics2D g, DutchFlagWorld dutchFlagWorld,int width, int height) {

        g.setFont(new Font("Monaco", Font.PLAIN, 12));

        /* clear board */
        g.setColor(Color.white);
        g.fill(new Rectangle2D.Double(0., 0., width, height));

        /* Draw the lines */
        int stackSize = dutchFlagWorld.getSize();
        heightworld = ((double)height) / stackSize;

        for (int rank=0;rank< stackSize;rank++) {
            Shape rect = new Rectangle2D.Double(0, heightworld*(stackSize-rank-1), width, heightworld);

            switch (dutchFlagWorld.getColor(rank)) {
                case DutchFlagEntity.BLUE:
                    g.setColor(dutchBlue);
                    break;
                case DutchFlagEntity.WHITE:
                    g.setColor(Color.white);
                    break;
                case DutchFlagEntity.RED:
                    g.setColor(dutchRed);
                    break;
            }
            g.fill(rect);

            if (dutchFlagWorld.getSize()<100) {
                g.setColor(Color.black);
                g.draw(rect);
            }
        }

        // Display the amount of moves
        if (dutchFlagWorld.getColor(stackSize-1) == DutchFlagEntity.WHITE)
            g.setColor(Color.black);
        else
            g.setColor(Color.yellow);
        g.drawString(""+dutchFlagWorld.moveCount+" moves", 0, 15);

        // Display the DnD markers
        if (dutchFlagWorld.getSize()<100 && rankFrom != -1) {
            g.setColor(Color.red);
            g.draw(new Rectangle2D.Double(0, height*(stackSize-rankFrom-1), width, height));
            g.draw(new Rectangle2D.Double(1, height*(stackSize-rankFrom-1)+1, width-2, height-2));
            g.setColor(Color.white);
            g.draw(new Rectangle2D.Double(2, height*(stackSize-rankFrom-1)+2, width-4, height-4));
            g.draw(new Rectangle2D.Double(3, height*(stackSize-rankFrom-1)+3, width-6, height-6));
        }
    }

    /**
     *
     * @param dutchFlagWorld
     * @param width
     * @param height
     * @return the DutchFlagWorld's world under SVG Format
     */
    public static String draw(DutchFlagWorld dutchFlagWorld) {
        // Ask the test to render into the SVG Graphics2D implementation.
        SVGGraphics2D svgGenerator  = new SVGGraphics2D(400,400);

        paintComponent(svgGenerator, dutchFlagWorld, 400, 400);

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


