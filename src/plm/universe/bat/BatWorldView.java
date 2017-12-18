package plm.universe.bat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.StringWriter;
import java.util.List;

import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;
import plm.universe.World;
import plm.universe.WorldView;

public class BatWorldView extends WorldView {

    private static final long serialVersionUID = 1L;

    public BatWorldView(World w) {
        super(w);
    }

//    @Override
//    public boolean isWorldCompatible(World world) {
//        return world instanceof BatWorld;
//    }


    public static void paintComponent(Graphics g, BatWorld batWorld, int width, int height) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);
        g2.fill(new Rectangle2D.Double(0., 0., (double) width, (double) height));

        List<BatTest> tests = batWorld.getTests();
        boolean foundError = false;
        for (int i = 0; i < tests.size(); i++) {
            BatTest currTest = tests.get(i);
            if (!currTest.isVisible() && foundError)
                break;

            if (currTest.isObjective()) {
                if (currTest.isVisible())
                    g2.setColor(Color.black);
                else
                    g2.setColor(Color.white);
            } else {
                if (currTest.isAnswered()) {

                    if (currTest.isCorrect())
                        g2.setColor(Color.blue);
                    else {
                        g2.setColor(Color.red);
                        foundError = true;
                    }
                } else {
                    if (currTest.isVisible())
                        g2.setColor(Color.black);
                    else
                        g2.setColor(Color.white);
                }
            }
            g2.drawString(currTest.getName(batWorld.getProgLang()) + "=" + currTest.getResult()
                    //+ (currTest.isAnswered() && !currTest.isCorrect() ? " (expected: " + currTest.stringParameter(batWorld.getProgLang(),currTest.expected) + ")" : "")
                    , 0, (i + 1) * 20);
        }

    }

    /**
     *
     * @param batWorld
     * @param width
     * @param height
     * @return the BatWorld's SVG under String Format
     */
    public static String draw(BatWorld batWorld, int width, int height) {

        // Ask the test to render into the SVG Graphics2D implementation.

        org.jfree.graphics2d.svg.SVGGraphics2D svgGenerator  = new org.jfree.graphics2d.svg.SVGGraphics2D(400,400);

        paintComponent(svgGenerator, batWorld,width,height);

        String str = svgGenerator.getSVGElement();
        return str;

    }
}