package plm.universe.pancake;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import plm.core.log.Logger;
import plm.core.utils.FileUtils;
import plm.universe.dutchflag.DutchFlagWorldView;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class drawPancakeSVG {
    public static void main(String[] args) throws UnsupportedEncodingException {
        // Get a DOMImplementation.
        DOMImplementation domImpl =
                GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Ask the test to render into the SVG Graphics2D implementation.
        PancakeWorld world = new PancakeWorld(new FileUtils(ClassLoader.getSystemClassLoader()),"5 pancakes",5,false);
        int[] center={50,50};
        world.draw();

        String test = PancakeWorldView.draw(world,400,400);

        Logger.info(test);
        //Logger.info(myworld.draw().get(0).getOperation());

    }

}
