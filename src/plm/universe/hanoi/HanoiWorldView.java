package plm.universe.hanoi;


import plm.universe.WorldView;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class HanoiWorldView extends WorldView {





    private static final long serialVersionUID = 1L;
    static int  pegFrom = -1;
    static  boolean buggyMove = false;


    public HanoiWorldView(HanoiWorld hanoiWorld) {
        super(hanoiWorld);

    }

    public static  void paintComponent(Graphics g,HanoiWorld hanoiWorld,int width, int height) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



        /* clear board */
        g2.setColor(Color.white);
        g2.fill(new Rectangle2D.Double(0., 0., width, height));

        double renderedX = 300.;
        double renderedY = 250.;
        double ratio = Math.min(((double) width) / renderedX, ((double) height) / renderedY);
        g2.translate(Math.abs(width - ratio * renderedX)/2., Math.abs(height - ratio * renderedY)/2.);
        g2.scale(ratio, ratio);

        for (int s=0;s<hanoiWorld.getSlotsAmount();s++)
            drawSlot(g2, s, (1+2*s)*300./(2*hanoiWorld.getSlotsAmount()),hanoiWorld);
        g2.setColor(Color.black);
        g2.drawString(""+hanoiWorld.moveCount+" moves", 0, 15);
    }

    public static void drawSlot(Graphics2D g2, int rank, double xoffset,HanoiWorld hanoiWorld) {


        /* draw bars, with color indicating whether it's a valid move during interactive drag&drop */
        if (pegFrom == -1)
            g2.setColor(Color.black);
        else if (hanoiWorld.getRadius(pegFrom) > hanoiWorld.getRadius(rank))
            g2.setColor(Color.red);
        else
            g2.setColor(Color.green);
        g2.fill(new Rectangle2D.Double(xoffset-2, 35.,  2., 145.));

//        if (this.values(rank)==null)
//            return;

        if(hanoiWorld.getSlots()[rank].toArray(new HanoiDisk[rank])==null)
            return;

        if(hanoiWorld.getSlots()[rank]==null)
            return;

        /* draw discs */
        int height = 1;
        for (int i=0; i<hanoiWorld.getSlots()[rank].size(); i++) {
            int size = hanoiWorld.getSlots()[rank].get(i).getSize();
            g2.setColor(hanoiWorld.getColor(rank, i));
            g2.fill(new Rectangle2D.Double( xoffset-size*5-3, 180-(11.*height),  size*10+3, 10));
            if (rank == pegFrom && i==hanoiWorld.getSlots()[rank].size()-1) {
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

    /**
     *
     * @param hanoiWorld
     * @param width
     * @param height
     * @return the HanoiWorld's view under SVG String
     */
    public static String draw(HanoiWorld hanoiWorld, int width, int height){
        // Ask the test to render into the SVG Graphics2D implementation.
        org.jfree.graphics2d.svg.SVGGraphics2D svgGenerator  = new org.jfree.graphics2d.svg.SVGGraphics2D(400,400);

        paintComponent(svgGenerator, hanoiWorld,width,height);

        String str = svgGenerator.getSVGElement();
        return str;
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


