//package plm.universe.baseball;
//
//import org.apache.batik.dom.GenericDOMImplementation;
//import org.apache.batik.svggen.SVGGraphics2D;
//import org.apache.batik.svggen.SVGGraphics2DIOException;
//import org.w3c.dom.DOMImplementation;
//import org.w3c.dom.Document;
//import plm.core.utils.FileUtils;
//
//import java.io.OutputStreamWriter;
//import java.io.UnsupportedEncodingException;
//import java.io.Writer;
//
//public class drawBaseballSVG {
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        // Get a DOMImplementation.
//        DOMImplementation domImpl =
//                GenericDOMImplementation.getDOMImplementation();
//
//        // Create an instance of org.w3c.dom.Document.
//        String svgNS = "http://www.w3.org/2000/svg";
//        Document document = domImpl.createDocument(svgNS, "svg", null);
//
//        // Create an instance of the SVG Generator.
//        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
//
//        // Ask the test to render into the SVG Graphics2D implementation.
//        BaseballView test = new BaseballView(new BaseballWorld(new FileUtils(ClassLoader.getSystemClassLoader()),"baseball"));
//        int[] center={50,50};
//        test.paintComponent(svgGenerator);
//
//        // Finally, stream out SVG to the standard output using
//        // UTF-8 encoding.
//        boolean useCSS = true; // we want to use CSS style attributes
//        Writer out = new OutputStreamWriter(System.out, "UTF-8");
//        try {
//            svgGenerator.stream(out, useCSS);
//        } catch (SVGGraphics2DIOException e) {
//            e.printStackTrace();
//        }
//    }
//}
