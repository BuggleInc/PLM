package plm.universe.bugglequest;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import plm.core.utils.FileUtils;
import plm.universe.Direction;
import plm.universe.Entity;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

public class BuggleWorldDraw {
    public static void main(String[] args) throws Exception {

        BuggleWorld myWorld = new BuggleWorld(new FileUtils(ClassLoader.getSystemClassLoader()),"Closed world",7,7);
        for (int i=0;i<7;i++) {
            new SimpleBuggle(myWorld, "Joker "+(i+1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
            myWorld.putTopWall (i, 6-i);
            myWorld.putLeftWall(i, 6-i);
            myWorld.putLeftWall(0, i  );
            myWorld.putTopWall (i, 0  );
        }

            // Get a DOMImplementation.
        DOMImplementation domImpl =
                GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Ask the test to render into the SVG Graphics2D implementation.
        BuggleWorldView test = new BuggleWorldView(myWorld);
        test.paintComponent(svgGenerator);

        StringWriter writer = new StringWriter();
        svgGenerator.stream(writer);
        String str = writer.getBuffer().toString();

        System.out.println(" =================================== " );

        System.out.println(str);


    }
}
