package bat.string1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringSplosion extends ExerciseTemplated {
    public StringSplosion(FileUtils fileUtils) {
        BatWorld myWorld = new BatWorld(fileUtils, "stringSplosion");
        myWorld.addTest(BatTest.VISIBLE, "Code");
        myWorld.addTest(BatTest.VISIBLE, "abc");
        myWorld.addTest(BatTest.VISIBLE, "x");
        myWorld.addTest(BatTest.INVISIBLE, "There");
        myWorld.addTest(BatTest.INVISIBLE, "Bye");
        myWorld.addTest(BatTest.INVISIBLE, "Good");
        myWorld.addTest(BatTest.INVISIBLE, "Bad");

        setup(myWorld);
    }
}
