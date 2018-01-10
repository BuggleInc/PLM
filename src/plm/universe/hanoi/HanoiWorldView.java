package plm.universe.hanoi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import plm.utils.SVGGraphics2D;

public class HanoiWorldView {

    static int  pegFrom = -1;
    static  boolean buggyMove = false;

    public static  void paintComponent(SVGGraphics2D g, HanoiWorld hanoiWorld,int width, int height) {

        /* clear board */
        g.setColor(Color.white);
        g.fill(new Rectangle2D.Double(0., 0., width, height));

        double renderedX = 300.;
        double renderedY = 250.;
        double ratio = Math.min(((double) width) / renderedX, ((double) height) / renderedY);
        g.translate(Math.abs(width - ratio * renderedX)/2., Math.abs(height - ratio * renderedY)/2.);
        g.scale(ratio, ratio);

        for (int s=0;s<hanoiWorld.getSlotsAmount();s++)
            drawSlot(g, s, (1+2*s)*300./(2*hanoiWorld.getSlotsAmount()),hanoiWorld);
        g.setColor(Color.black);
        g.drawString(""+hanoiWorld.moveCount+" moves", 0, 15);
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
    public static String draw(HanoiWorld hanoiWorld){
        // Ask the test to render into the SVG Graphics2D implementation.
        SVGGraphics2D svgGenerator  = new SVGGraphics2D(400,400);

        paintComponent(svgGenerator, hanoiWorld, 400, 400);

        String str = svgGenerator.getSVGElement();
        return str;
    }
}
