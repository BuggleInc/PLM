package plm.universe.turtles;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import plm.core.log.Logger;
import plm.universe.Entity;
import plm.universe.SvgGenerator;
import plm.universe.World;
import plm.universe.WorldView;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.StringWriter;
import java.util.Iterator;

public class TurtleWorldView extends WorldView {

    protected static World world;

    public TurtleWorldView(World w) {
        super(w);

    }

    public static void paintComponent(Graphics g, TurtleWorld turtleWorld, int width, int height) {
        doPaint(g,turtleWorld,width,height,true);
    }
    public static void doPaint(Graphics g, TurtleWorld turtleWorld, int sizeX, int sizeY, boolean showTurtle) {

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform initialTransform = g2.getTransform();

        double displayRatio = Math.min(((double) sizeX)/turtleWorld.getWidth(), ((double)sizeY)/turtleWorld.getHeight());
        double deltaX = Math.abs((sizeX-displayRatio*turtleWorld.getWidth())/2.);
        double deltaY = Math.abs((sizeY-displayRatio*turtleWorld.getHeight())/2.);
        g2.translate(deltaX, deltaY);
        g2.scale(displayRatio, displayRatio);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);
        g2.fill(new Rectangle2D.Double(0.,0.,(double)turtleWorld.getWidth(),(double)turtleWorld.getHeight()));

        g2.setColor(Color.BLACK);
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, new float[]{2f,10.0f}, 0.0f));

//        if (Game.getInstance().isDebugEnabled()) {
//            for (int x=50;x<turtleWorld.getWidth();x+=50)
//                g2.drawLine(x, 1, x, (int) turtleWorld.getHeight()-1);
//            for (int y=50;y<turtleWorld.getHeight();y+=50)
//                g2.drawLine(1, y, (int)turtleWorld.getWidth()-1,y);
//        }
        g2.setStroke(oldStroke);

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
        if (showTurtle)
            for (Entity ent : turtleWorld.getEntities())
                drawTurtle(g2, (Turtle)ent);


//        synchronized (((TurtleWorld) world).shapes) {
//            Iterator<Shape> it2 = ((TurtleWorld) world).shapes();
//            while (it2.hasNext())
//                it2.next().draw(g2);
//        }

    }

    private static void drawTurtle(Graphics2D g, Turtle b) {
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
    public static String draw(TurtleWorld turtleWorld, int width, int height) {

        // Ask the test to render into the SVG Graphics2D implementation.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(SvgGenerator.document);
        paintComponent(svgGenerator, turtleWorld,width,height);

        StringWriter writer = new StringWriter();
        try {
            //SvgGenerator.svgGenerator.stream(writer);
            svgGenerator.stream(writer);
        } catch (SVGGraphics2DIOException e) {
            e.printStackTrace();
        }
        String str = writer.getBuffer().toString();
        return str;

    }
}
