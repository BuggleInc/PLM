package plm.universe.lander;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import plm.core.utils.FileUtils;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiWorldView;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import scala.collection.JavaConverters;
public class drawLanderSVG {

    public static void main(String[] args) throws UnsupportedEncodingException {
        // Get a DOMImplementation.
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Ask the test to render into the SVG Graphics2D implementation.

        Point ppos = new Point(1200, 700);
        Point pspeed = new Point(0, 0);
        Point p2 = new Point(125,414);
        Point p3 = new Point(205,271);
        Point p4 = new Point(348,597);
        Point p5 = new Point(460,257);
        Point p6 = new Point(534,438);
        Point p7 = new Point(637,160);
        Point p8 = new Point(760,371);
        Point p9 = new Point(854,200);
        Point p10 = new Point(1468,200);
        Point p11 = new Point(1585,440);
        Point p12 = new Point(1682,280);
        Point p13 = new Point(1845,668);
        Point p14 = new Point(2000,294);
        List<Point> points = new ArrayList<Point>();
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        points.add(p8);
        points.add(p9);
        points.add(p10);
        points.add(p11);
        points.add(p12);
        points.add(p13);
        points.add(p14);

        scala.collection.immutable.List<Point> ls = JavaConverters.asScalaBufferConverter(points).asScala().toList();

    DelegatingLanderWorld deleg = new DelegatingLanderWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "lander",2000,1000,ls,ppos,pspeed,0,0,3000);
        deleg.draw();


    }

}
