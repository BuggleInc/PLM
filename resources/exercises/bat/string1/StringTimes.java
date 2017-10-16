package bat.string1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringTimes extends ExerciseTemplated {
    public StringTimes(FileUtils fileUtils) {
        BatWorld myWorld = new BatWorld(fileUtils, "stringTimes");
        myWorld.addTest(BatTest.VISIBLE, "Hi", 2);
        myWorld.addTest(BatTest.VISIBLE, "Hi", 3);
        myWorld.addTest(BatTest.VISIBLE, "Hi", 1);
        myWorld.addTest(BatTest.INVISIBLE, "Hi", 0);
        myWorld.addTest(BatTest.INVISIBLE, "Oh Boy!", 2);
        myWorld.addTest(BatTest.INVISIBLE, "x", 4);
        myWorld.addTest(BatTest.INVISIBLE, "", 4);
        myWorld.addTest(BatTest.INVISIBLE, "code", 2);
        myWorld.addTest(BatTest.INVISIBLE, "code", 3);

        setup(myWorld);
    }
}
