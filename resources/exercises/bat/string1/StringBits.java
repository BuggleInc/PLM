package bat.string1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringBits extends ExerciseTemplated {
    public StringBits(FileUtils fileUtils) {
        super("StringBits");

        BatWorld myWorld = new BatWorld(fileUtils, "stringBits");
        myWorld.addTest(BatTest.VISIBLE, "Hello");
        myWorld.addTest(BatTest.VISIBLE, "Hi");
        myWorld.addTest(BatTest.VISIBLE, "HiHiHi");
        myWorld.addTest(BatTest.INVISIBLE, "");
        myWorld.addTest(BatTest.INVISIBLE, "Greetings");

        setup(myWorld);
    }
}
