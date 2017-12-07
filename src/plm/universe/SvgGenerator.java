package plm.universe;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public  final class SvgGenerator {

    // Get a DOMImplementation.
    public static DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();

    // Create an instance of org.w3c.dom.Document.
    public static String svgNS = "http://www.w3.org/2000/svg";
    public static Document document = domImpl.createDocument(svgNS, "svg", null);

    // Create an instance of the SVG Generator.
    //public static  SVGGraphics2D svgGenerator = new SVGGraphics2D(document);



}

