package plm.universe.hanoi;


import plm.core.utils.FileUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Vector;

public class HanoiWorldView extends HanoiWorld {



    private int height=400;
    private int width=400;

    private static final long serialVersionUID = 1L;
    int pegFrom = -1;
    boolean buggyMove = false;

    public HanoiWorldView(FileUtils fileUtils, String name, Vector<HanoiDisk> A, Vector<HanoiDisk> B, Vector<HanoiDisk> C, Vector<HanoiDisk> D) {
        super(fileUtils, name, A, B, C, D);
    }
    public HanoiWorldView(FileUtils fileUtils, String name, Vector<HanoiDisk> A, Vector<HanoiDisk> B, Vector<HanoiDisk> C) {
        super(fileUtils, name,A,B,C);

    }

    public void paintComponent(Graphics g) {

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

        for (int s=0;s<this.getSlotsAmount();s++)
            drawSlot(g2, s, (1+2*s)*300./(2*this.getSlotsAmount()));
        g2.setColor(Color.black);
        g2.drawString(""+this.moveCount+" moves", 0, 15);
    }

    private void drawSlot(Graphics2D g2, int rank, double xoffset) {


		/* draw bars, with color indicating whether it's a valid move during interactive drag&drop */
        if (pegFrom == -1)
            g2.setColor(Color.black);
        else if (this.getRadius(pegFrom) > this.getRadius(rank))
            g2.setColor(Color.red);
        else
            g2.setColor(Color.green);
        g2.fill(new Rectangle2D.Double(xoffset-2, 35.,  2., 145.));

//        if (this.values(rank)==null)
//            return;

        if(this.getSlots()[rank].toArray(new HanoiDisk[rank])==null)
            return;

      if(this.getSlots()[rank]==null)
          return;

		/* draw discs */
        int height = 1;
        for (int i=0; i<this.getSlots()[rank].size(); i++) {
            int size = this.getSlots()[rank].get(i).getSize();
            g2.setColor(this.getColor(rank, i));
            g2.fill(new Rectangle2D.Double( xoffset-size*5-3, 180-(11.*height),  size*10+3, 10));
            if (rank == pegFrom && i==this.getSlots()[rank].size()-1) {
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


    //*****************
    public int getWidth()
    {
        return this.width;
    }

    public  int getHeight()
    {
        return height;
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


