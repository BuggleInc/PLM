//package plm.universe.hanoi;
//
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
//import java.util.Vector;
//
//public class drawHanoiSVG {
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        // Get a DOMImplementation.
//        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
//
//        // Create an instance of org.w3c.dom.Document.
//        String svgNS = "http://www.w3.org/2000/svg";
//        Document document = domImpl.createDocument(svgNS, "svg", null);
//
//        // Create an instance of the SVG Generator.
//        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
//
//        // Ask the test to render into the SVG Graphics2D implementation.
////        Vector<HanoiDisk> slot10 = HanoiDisk.generateHanoiDisks(new Integer[] {6,5,4,3,2,1}, Color.yellow);
////        Vector<HanoiDisk> slot12 = HanoiDisk.generateHanoiDisks(new Integer[] {6,5,4,3,2,1}, Color.white);
////        HanoiWorldView HanoiWorldView = new HanoiWorldView(new FileUtils(ClassLoader.getSystemClassLoader()), "solve(0,2,3,1)",
////                slot10, new Vector<HanoiDisk>(), slot12, new Vector<HanoiDisk>());
////        HanoiWorldView.setParameter(new Integer[]{0,2,3,1});
////
////        HanoiWorldView.paintComponent(svgGenerator);
//
//
//        Vector<HanoiDisk> slot = HanoiDisk.generateHanoiDisks(new Integer[] {10,9,8,7,6,5,4,3,2,1});
//        HanoiWorldView myWorlds = new HanoiWorldView(new FileUtils(ClassLoader.getSystemClassLoader()), "solve(0,2,1), 2 disks",
//                slot, new Vector<HanoiDisk>(),new Vector<HanoiDisk>());
//        myWorlds.setParameter(new Object[] {0,2,1});
//
//
//       // myWorlds.move(0,1);
//       // myWorlds.move(0,2);
//        myWorlds.paintComponent(svgGenerator);
//
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
