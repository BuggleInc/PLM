package plm.universe.dutchflag;



import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import plm.core.utils.FileUtils;
import plm.universe.baseball.BaseballWorld;
import plm.universe.baseball.baseBallView;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiWorldView;
import recursion.hanoi.CyclicHanoiEntity;

import java.awt.*;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Vector;

public class drawDutchFlagSVG {

    public static void main(String[] args) throws UnsupportedEncodingException {
        // Get a DOMImplementation.
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);


        // Ask the test to render into the SVG Graphics2D implementation.
       // DutchFlagWorldView myWorlds = new DutchFlagWorldView(new FileUtils(ClassLoader.getSystemClassLoader()), "6 lines",6);

        DutchFlagWorldView myWorlds = new DutchFlagWorldView(new FileUtils(ClassLoader.getSystemClassLoader()),"12 blue/white",12,2);

     //   myWorlds.setParameter(new Object[] {0,2,1});


        myWorlds.paintComponent(svgGenerator);


        // Finally, stream out SVG to the standard output using
        // UTF-8 encoding.
        boolean useCSS = true; // we want to use CSS style attributes
        Writer out = new OutputStreamWriter(System.out, "UTF-8");
        try {
            svgGenerator.stream(out, useCSS);
        } catch (SVGGraphics2DIOException e) {
            e.printStackTrace();
        }
    }
}
