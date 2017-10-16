package array.basics;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

import java.awt.*;

public class Array1 extends ExerciseTemplated {

    public Array1(FileUtils fileUtils) {
        BuggleWorld[] myWorlds = new BuggleWorld[3];

        myWorlds[0] = new BuggleWorld(fileUtils, "Pattern 1", 6, 6);
        myWorlds[0].setColor(0, 0, Color.red);
        myWorlds[0].setColor(0, 1, Color.cyan);
        myWorlds[0].setColor(0, 2, Color.green);
        myWorlds[0].setColor(0, 3, Color.magenta);
        myWorlds[0].setColor(0, 4, Color.orange);
        myWorlds[0].setColor(0, 5, Color.pink);

        myWorlds[1] = new BuggleWorld(fileUtils, "Pattern 2", 7, 7);
        myWorlds[1].setColor(0, 2, Color.red);
        myWorlds[1].setColor(0, 6, Color.cyan);
        myWorlds[1].setColor(0, 5, Color.green);
        myWorlds[1].setColor(0, 4, Color.magenta);
        myWorlds[1].setColor(0, 3, Color.orange);
        myWorlds[1].setColor(0, 0, Color.pink);
        myWorlds[1].setColor(0, 1, Color.yellow);

        myWorlds[2] = new BuggleWorld(fileUtils, "Pattern 3", 8, 8);
        myWorlds[2].setColor(0, 0, Color.red);
        myWorlds[2].setColor(0, 7, Color.cyan);
        myWorlds[2].setColor(0, 1, Color.green);
        myWorlds[2].setColor(0, 6, Color.magenta);
        myWorlds[2].setColor(0, 2, Color.orange);
        myWorlds[2].setColor(0, 5, Color.pink);
        myWorlds[2].setColor(0, 3, Color.yellow);
        myWorlds[2].setColor(0, 4, Color.black);

        new SimpleBuggle(myWorlds[0], "Picasso", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
        new SimpleBuggle(myWorlds[1], "Braque", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);
        new SimpleBuggle(myWorlds[2], "Ingres", 0, 0, Direction.SOUTH, Color.black, Color.lightGray);

        setup(myWorlds);
    }
}
