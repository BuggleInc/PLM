package plm.universe.bat;

import java.awt.Color;
import java.util.List;

import plm.universe.World;
import plm.universe.WorldView;
import plm.utils.SVGGraphics2D;

public class BatWorldView extends WorldView {

    public BatWorldView(World w) {
        super(w);
    }


    public static void paintComponent(SVGGraphics2D g, BatWorld batWorld, int width, int height) {

        List<BatTest> tests = batWorld.getTests();
        boolean foundError = false;
        for (int i = 0; i < tests.size(); i++) {
            BatTest currTest = tests.get(i);
            if (!currTest.isVisible() && foundError)
                break;

            if (currTest.isObjective()) {
                if (currTest.isVisible())
                    g.setColor(Color.black);
                else
                    g.setColor(Color.white);
            } else {
                if (currTest.isAnswered()) {

                    if (currTest.isCorrect())
                        g.setColor(Color.blue);
                    else {
                        g.setColor(Color.red);
                        foundError = true;
                    }
                } else {
                    if (currTest.isVisible())
                        g.setColor(Color.black);
                    else
                        g.setColor(Color.white);
                }
            }
            g.drawString(currTest.getName(batWorld.getProgLang()) + "=" + currTest.getResult()
                    //+ (currTest.isAnswered() && !currTest.isCorrect() ? " (expected: " + currTest.stringParameter(batWorld.getProgLang(),currTest.expected) + ")" : "")
                    , 0, (i + 1) * 20);
        }

    }

    /**
     *
     * @param batWorld
     * @return the BatWorld's SVG under String Format
     */
    public static String draw(BatWorld batWorld) {

        // Ask the test to render into the SVG Graphics2D implementation.

        SVGGraphics2D svgGenerator  = new SVGGraphics2D(400,400);

        paintComponent(svgGenerator, batWorld, 400, 400);

        String str = svgGenerator.getSVGElement();
        return str;

    }
}