package bat.string1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringBits extends ExerciseTemplated {
    public StringBits(Lesson lesson, FileUtils fileUtils) {
        BatWorld myWorld = new BatWorld(fileUtils, "stringBits");
        myWorld.addTest(BatTest.VISIBLE, "Hello");
        myWorld.addTest(BatTest.VISIBLE, "Hi");
        myWorld.addTest(BatTest.VISIBLE, "HiHiHi");
        myWorld.addTest(BatTest.INVISIBLE, "");
        myWorld.addTest(BatTest.INVISIBLE, "Greetings");

        setup(myWorld);
    }
}
