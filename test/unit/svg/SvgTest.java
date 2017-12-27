package unit.svg;

import org.junit.Test;
import plm.core.utils.FileUtils;
import plm.universe.Direction;

import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.BuggleWorldView;
import plm.universe.bugglequest.SimpleBuggle;
import plm.universe.dutchflag.DutchFlagWorld;
import plm.universe.dutchflag.DutchFlagWorldView;
import plm.universe.hanoi.HanoiDisk;
import plm.universe.hanoi.HanoiWorld;
import plm.universe.hanoi.HanoiWorldView;
import plm.universe.pancake.PancakeWorld;
import plm.universe.pancake.PancakeWorldView;

import java.awt.*;
import java.io.*;
import java.util.Vector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
    Tests for the worlds :
        - Buggle
        - Pancake
        - Hanoi
        - Dutchflag
 */
public class SvgTest {

    public SvgTest(){
    }

    //Path to store the SVG
    private String root = "./../../img/";

    public String getRoot() {
        return root;
    }

    //Test to know if the generated SVG for BuggleWorld is correct with a correct File SVG
    @Test
    public void testBuggleCorrectSVG() {

        for(int j = 0; j<2; j++) {
            //Generation of an SVG
            BuggleWorld myWorld = new BuggleWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "BuggleWorld", 7, 7);
            for (int i = 0; i < 7; i++) {
                new SimpleBuggle(myWorld, "Joker " + (i + 1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
                myWorld.putTopWall(i, 6 - i);
                myWorld.putLeftWall(i, 6 - i);
                myWorld.putLeftWall(0, i);
                myWorld.putTopWall(i, 0);
            }

            //Create a file for the SVG
            String svg = BuggleWorldView.draw(myWorld, 400, 400);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(getRoot() + myWorld.getName() + ".svg", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.write(svg);
            writer.close();


            BufferedReader br1 = null;
            BufferedReader br2 = null;

            //Example File SVG
            try {
                br1 = new BufferedReader(new FileReader(getRoot() + "BuggleWorldExample.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'BuggleWorldExample' isn't found in " + getRoot() + " !");
            }

            //Generated File SVG
            try {
                br2 = new BufferedReader(new FileReader(getRoot() + "BuggleWorld.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'BuggleWorld' isn't found in " + getRoot() + " !");
            }

            String line1 = null;
            try {
                line1 = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line2 = null;
            try {
                line2 = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean areEqual = true;

            int lineNum = 1;

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    areEqual = false;

                    break;
                } else if (!line1.equalsIgnoreCase(line2)) {
                    areEqual = false;

                    break;
                }

                try {
                    line1 = br1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    line2 = br2.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lineNum++;
            }

            File f = new File(getRoot() + myWorld.getName() + ".svg");
            f.delete();

            //If all the lines of the files are equals, the Test assert true
            assertTrue("SVG BuggleWorld incorrect", (areEqual));
        }

    }


    //Test to know if the generated SVG for BuggleWorld is incorrect with a incorrect File SVG
    @Test
    public void testBuggleIncorrectSVG() {

        //Generation of an SVG
        for(int j = 0; j<2; j++) {
            BuggleWorld myWorld = new BuggleWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "BuggleWorld", 7, 7);
            for (int i = 0; i < 7; i++) {
                new SimpleBuggle(myWorld, "Joker " + (i + 1), i, 6, Direction.NORTH, Color.black, Color.lightGray);
                myWorld.putTopWall(i, 6 - i);
                myWorld.putLeftWall(i, 6 - i);
                myWorld.putLeftWall(0, i);
                myWorld.putTopWall(i, 0);
            }

            //Create a file for the SVG
            String svg = BuggleWorldView.draw(myWorld, 400, 400);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(getRoot() + myWorld.getName() + ".svg", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.write(svg);
            writer.close();


            BufferedReader br1 = null;
            BufferedReader br2 = null;

            //Example File SVG
            try {
                br1 = new BufferedReader(new FileReader(getRoot() + "BuggleWorldWrongExample.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'BuggleWorldWrongExample' isn't found in " + getRoot() + " !");
            }

            //Generated File SVG
            try {
                br2 = new BufferedReader(new FileReader(getRoot() + "BuggleWorld.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'BuggleWorld' isn't found in " + getRoot() + " !");
            }

            String line1 = null;
            try {
                line1 = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line2 = null;
            try {
                line2 = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean areEqual = true;

            int lineNum = 1;

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    areEqual = false;

                    break;
                } else if (!line1.equalsIgnoreCase(line2)) {
                    areEqual = false;

                    break;
                }

                try {
                    line1 = br1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    line2 = br2.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lineNum++;
            }

            File f = new File(getRoot() + myWorld.getName() + ".svg");
            f.delete();

            //If all the lines of the files are equals, the Test assert true
            assertFalse("SVG BuggleWorld incorrect", (areEqual));
        }
    }

    //Test to know if the generated SVG for Dutchflag is correct with a correct File SVG
    @Test
    public void testDutchflagCorrectSVG() {

        //Generation of an SVG
        for(int j = 0; j<2; j++) {
            DutchFlagWorld myWorld = new DutchFlagWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "Dutchflag", 300);

            myWorld.draw();

            //Create a file for the SVG
            String svg = DutchFlagWorldView.draw(myWorld, 400, 400);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(getRoot() + myWorld.getName() + ".svg", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.write(svg);
            writer.close();


            BufferedReader br1 = null;
            BufferedReader br2 = null;

            //Example File SVG
            try {
                br1 = new BufferedReader(new FileReader(getRoot() + "DutchflagExample.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'DutchflagExample' isn't found in " + getRoot() + " !");
            }

            //Generated File SVG
            try {
                br2 = new BufferedReader(new FileReader(getRoot() + "Dutchflag.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'Dutchflag' isn't found in " + getRoot() + " !");
            }

            String line1 = null;
            try {
                line1 = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line2 = null;
            try {
                line2 = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean areEqual = true;

            int lineNum = 1;

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    areEqual = false;

                    break;
                } else if (!line1.equalsIgnoreCase(line2)) {
                    areEqual = false;

                    break;
                }

                try {
                    line1 = br1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    line2 = br2.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lineNum++;
            }

            File f = new File(getRoot() + myWorld.getName() + ".svg");
            f.delete();

            //If all the lines of the files are equals, the Test assert true
            assertTrue("SVG Dutchflag incorrect", (areEqual));
        }
    }

    //Test to know if the generated SVG for Dutchflag is incorrect with a incorrect File SVG
    @Test
    public void testDutchflagIncorrectSVG() {

        //Generation of an SVG
        for(int j = 0; j<2; j++) {
            DutchFlagWorld myWorld = new DutchFlagWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "Dutchflag", 300);

            myWorld.draw();

            //Create a file for the SVG
            String svg = DutchFlagWorldView.draw(myWorld, 400, 400);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(getRoot() + myWorld.getName() + ".svg", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.write(svg);
            writer.close();


            BufferedReader br1 = null;
            BufferedReader br2 = null;

            //Example File SVG
            try {
                br1 = new BufferedReader(new FileReader(getRoot() + "DutchflagWrongExample.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'DutchflagWrongExample' isn't found in " + getRoot() + " !");
            }

            //Generated File SVG
            try {
                br2 = new BufferedReader(new FileReader(getRoot() + "Dutchflag.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'Dutchflag' isn't found in " + getRoot() + " !");
            }

            String line1 = null;
            try {
                line1 = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line2 = null;
            try {
                line2 = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean areEqual = true;

            int lineNum = 1;

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    areEqual = false;

                    break;
                } else if (!line1.equalsIgnoreCase(line2)) {
                    areEqual = false;

                    break;
                }

                try {
                    line1 = br1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    line2 = br2.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lineNum++;
            }

            File f = new File(getRoot() + myWorld.getName() + ".svg");
            f.delete();

            //If all the lines of the files are equals, the Test assert true
            assertFalse("SVG Dutchflag incorrect", (areEqual));
        }
    }

    //Test to know if the generated SVG for PancakeWorld is correct with a correct File SVG
    //ERROR because the SVG of Pancake is random
    @Test
    public void testPancakeCorrectSVG() {

        //Generation of an SVG
        for(int j = 0; j<2; j++) {
            int tableauEntier[] = {2, 5, 3, 4, 6};
            PancakeWorld myWorld;
            myWorld = new PancakeWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "Pancake", tableauEntier, false);
            myWorld.draw();

            //Create a file for the SVG
            String svg = PancakeWorldView.draw(myWorld, 400, 400);
            System.out.println("svg = " + svg);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(getRoot() + myWorld.getName() + ".svg", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.write(svg);
            writer.close();


            BufferedReader br1 = null;
            BufferedReader br2 = null;

            //Example File SVG
            try {
                br1 = new BufferedReader(new FileReader(getRoot() + "PancakeExample.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'PancakeExample' isn't found in " + getRoot() + " !");
            }

            //Generated File SVG
            try {
                br2 = new BufferedReader(new FileReader(getRoot() + "Pancake.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'Pancake' isn't found in " + getRoot() + " !");
            }

            String line1 = null;
            try {
                line1 = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line2 = null;
            try {
                line2 = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean areEqual = true;

            int lineNum = 1;

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    areEqual = false;

                    break;
                } else if (!line1.equalsIgnoreCase(line2)) {
                    areEqual = false;

                    break;
                }

                try {
                    line1 = br1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    line2 = br2.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lineNum++;
            }

            File f = new File(getRoot() + myWorld.getName() + ".svg");
            f.delete();

            //If all the lines of the files are equals, the Test assert true
            assertTrue("SVG Pancake incorrect", (areEqual));
        }
    }

    //Test to know if the generated SVG for Pancake is incorrect with a incorrect File SVG
    @Test
    public void testPancakeIncorrectSVG() {

        //Generation of an SVG
        for(int j = 0; j<2; j++) {
            int tableauEntier[] = {2, 5, 3, 4, 6};
            PancakeWorld myWorld = new PancakeWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "Pancake", tableauEntier, false);
            myWorld.draw();

            //Create a file for the SVG
            String svg = PancakeWorldView.draw(myWorld, 400, 400);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(getRoot() + myWorld.getName() + ".svg", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.write(svg);
            writer.close();


            BufferedReader br1 = null;
            BufferedReader br2 = null;

            //Example File SVG
            try {
                br1 = new BufferedReader(new FileReader(getRoot() + "PancakeWrongExample.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'PancakeWrongExample' isn't found in " + getRoot() + " !");
            }

            //Generated File SVG
            try {
                br2 = new BufferedReader(new FileReader(getRoot() + "Pancake.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'Pancake' isn't found in " + getRoot() + " !");
            }

            String line1 = null;
            try {
                line1 = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line2 = null;
            try {
                line2 = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean areEqual = true;

            int lineNum = 1;

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    areEqual = false;

                    break;
                } else if (!line1.equalsIgnoreCase(line2)) {
                    areEqual = false;

                    break;
                }

                try {
                    line1 = br1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    line2 = br2.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lineNum++;
            }

            File f = new File(getRoot() + myWorld.getName() + ".svg");
            f.delete();

            //If all the lines of the files are equals, the Test assert true
            assertFalse("SVG Pancake incorrect", (areEqual));
        }
    }

    //Test to know if the generated SVG for Hanoi is correct with a correct File SVG
    @Test
    public void testHanoiCorrectSVG() {

        //Generation of an SVG
        for(int j = 0; j<2; j++) {
            Vector<HanoiDisk> slot10 = HanoiDisk.generateHanoiDisks(new Integer[]{6, 5, 4, 3, 2, 1}, Color.yellow);
            Vector<HanoiDisk> slot12 = HanoiDisk.generateHanoiDisks(new Integer[]{6, 5, 4, 3, 2, 1}, Color.white);

            HanoiWorld myWorld = new HanoiWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "Hanoi", slot10, new Vector<HanoiDisk>(), slot12, new Vector<HanoiDisk>());
            myWorld.draw();



            //Create a file for the SVG
            String svg = HanoiWorldView.draw(myWorld, 400, 400);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(getRoot() + myWorld.getName() + ".svg", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.write(svg);
            writer.close();


            BufferedReader br1 = null;
            BufferedReader br2 = null;

            //Example File SVG
            try {
                br1 = new BufferedReader(new FileReader(getRoot() + "HanoiExample.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'HanoiExample' isn't found in " + getRoot() + " !");
            }

            //Generated File SVG
            try {
                br2 = new BufferedReader(new FileReader(getRoot() + "Hanoi.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'Hanoi' isn't found in " + getRoot() + " !");
            }

            String line1 = null;
            try {
                line1 = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line2 = null;
            try {
                line2 = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean areEqual = true;

            int lineNum = 1;

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    areEqual = false;

                    break;
                } else if (!line1.equalsIgnoreCase(line2)) {
                    areEqual = false;

                    break;
                }

                try {
                    line1 = br1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    line2 = br2.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lineNum++;
            }

            File f = new File(getRoot() + myWorld.getName() + ".svg");
            f.delete();

            //If all the lines of the files are equals, the Test assert true
            assertTrue("SVG Hanoi incorrect", (areEqual));
        }
    }

    //Test to know if the generated SVG for Hanoi is incorrect with a incorrect File SVG
    @Test
    public void testHanoiIncorrectSVG() {

        //Generation of an SVG
        for(int j = 0; j<2; j++) {
            Vector<HanoiDisk> slot10 = HanoiDisk.generateHanoiDisks(new Integer[]{6, 5, 4, 3, 2, 1}, Color.yellow);
            Vector<HanoiDisk> slot12 = HanoiDisk.generateHanoiDisks(new Integer[]{6, 5, 4, 3, 2, 1}, Color.white);

            HanoiWorld myWorld = new HanoiWorld(new FileUtils(ClassLoader.getSystemClassLoader()), "Hanoi", slot10, new Vector<HanoiDisk>(), slot12, new Vector<HanoiDisk>());
            myWorld.draw();

            //Create a file for the SVG
            String svg = HanoiWorldView.draw(myWorld, 400, 400);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(getRoot() + myWorld.getName() + ".svg", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.write(svg);
            writer.close();


            BufferedReader br1 = null;
            BufferedReader br2 = null;

            //Example File SVG
            try {
                br1 = new BufferedReader(new FileReader(getRoot() + "HanoiWrongExample.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'HanoiWrongExample' isn't found in " + getRoot() + " !");
            }

            //Generated File SVG
            try {
                br2 = new BufferedReader(new FileReader(getRoot() + "Hanoi.svg"));
            } catch (FileNotFoundException e) {
                System.out.println("File 'Hanoi' isn't found in " + getRoot() + " !");
            }

            String line1 = null;
            try {
                line1 = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line2 = null;
            try {
                line2 = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean areEqual = true;

            int lineNum = 1;

            while (line1 != null || line2 != null) {
                if (line1 == null || line2 == null) {
                    areEqual = false;

                    break;
                } else if (!line1.equalsIgnoreCase(line2)) {
                    areEqual = false;

                    break;
                }

                try {
                    line1 = br1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    line2 = br2.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lineNum++;
            }

            File f = new File(getRoot() + myWorld.getName() + ".svg");
            f.delete();

            //If all the lines of the files are equals, the Test assert true
            assertFalse("SVG Hanoi incorrect", (areEqual));
        }
    }

}