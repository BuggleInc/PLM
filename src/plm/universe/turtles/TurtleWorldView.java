package plm.universe.turtles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import plm.universe.Entity;
import plm.utils.SVGGraphics2D;

public class TurtleWorldView {

    public static void paintComponent(SVGGraphics2D g, TurtleWorld turtleWorld, int sizeX, int sizeY) {

        double displayRatio = Math.min(((double) sizeX)/turtleWorld.getWidth(), ((double)sizeY)/turtleWorld.getHeight());
        double deltaX = Math.abs((sizeX-displayRatio*turtleWorld.getWidth())/2.);
        double deltaY = Math.abs((sizeY-displayRatio*turtleWorld.getHeight())/2.);
        g.translate(deltaX, deltaY);
        g.scale(displayRatio, displayRatio);

        g.setColor(Color.white);
        g.fill(new Rectangle2D.Double(0.,0.,(double)turtleWorld.getWidth(),(double)turtleWorld.getHeight()));

        g.setColor(Color.BLACK);
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, new float[]{2f,10.0f}, 0.0f));

//        if (Game.getInstance().isDebugEnabled()) {
//            for (int x=50;x<turtleWorld.getWidth();x+=50)
//                g2.drawLine(x, 1, x, (int) turtleWorld.getHeight()-1);
//            for (int y=50;y<turtleWorld.getHeight();y+=50)
//                g2.drawLine(1, y, (int)turtleWorld.getWidth()-1,y);
//        }
        g.setStroke(oldStroke);

//        if (world.isAnswerWorld() || Game.getInstance().isDebugEnabled()) {
//            for (Entity e: world.getEntities()) {
//                Turtle t = (Turtle) e;
//                g2.setColor(SizeHint.color);
//                g2.fillOval((int)(t.startX-5), (int)(t.startY-5), 10, 10);
//            }
//
//            synchronized (turtleWorld.sizeHints) {
//                for (SizeHint indic : turtleWorld.sizeHints)
//                    indic.draw(g2);
//            }
//        }
        
        for (Entity ent : turtleWorld.getEntities())
        	drawTurtle(g, (Turtle)ent);

//        synchronized (((TurtleWorld) world).shapes) {
//            Iterator<Shape> it2 = ((TurtleWorld) world).shapes();
//            while (it2.hasNext())
//                it2.next().draw(g2);
//        }

    }

    private static void drawTurtle(SVGGraphics2D g, Turtle b) {
        if (b.isVisible()) {
            ImageIcon ic = ResourcesCache.getIcon("img/world_turtle.png");
            AffineTransform t = new AffineTransform(1.0, 0, 0, 1.0, b.getX()-ic.getIconWidth()/2., b.getY()-ic.getIconHeight()/2.);
            t.rotate(b.getHeadingRadian(), ic.getIconWidth()/2., ic.getIconHeight()/2.);
            g.drawImage(ic.getImage(), t, null);
        }
    }

    /**
     *
     * @param turtleWorld
     * @param width
     * @param height
     * @return the TurleWorld View, but actually doesn't render the turtle
     */
    public static String draw(TurtleWorld turtleWorld) {

    	SVGGraphics2D svgGenerator  = new SVGGraphics2D(400,400);

    	paintComponent(svgGenerator, turtleWorld, 400, 400);

    	String str = svgGenerator.getSVGElement();
    	return str;
    }
}
