//package plm.universe.sort;
//
//import org.apache.batik.dom.GenericDOMImplementation;
//import org.apache.batik.svggen.SVGGraphics2D;
//import org.apache.batik.svggen.SVGGraphics2DIOException;
//import org.w3c.dom.DOMImplementation;
//import org.w3c.dom.Document;
//import plm.core.utils.FileUtils;
//
//import java.io.OutputStreamWriter;
//import java.io.Writer;
//
//public class drawSortWorlSVG {
//
//    public static void main(String[] args) throws Exception {
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
//        SortingWorldView sort = new SortingWorldView(new FileUtils(ClassLoader.getSystemClassLoader()),"SORT",10);
//        //baseBallView base2= new baseBallView(new FileUtils(ClassLoader.getSystemClassLoader()), "10 bases",10,2,new int[]{-1,7 , 2,7 , 1,4 , 3,4 , 5,6 , 8,9 , 5,1 , 3,6 , 0,2 , 0,8});
//
//
//        int[] center={50,50};
//        sort.paintComponent(svgGenerator);
//        // drawPlayer(Graphics2D g, int[] center, int radius, Color color, double theta, boolean isHome)
//        //paintComponent(svgGenerator);
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
