package bat.string1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringYak extends ExerciseTemplated {
    public StringYak(Lesson lesson, FileUtils fileUtils) {
        BatWorld myWorld = new BatWorld(fileUtils, "stringYak");
        myWorld.addTest(BatTest.VISIBLE, "yakpak");
        myWorld.addTest(BatTest.VISIBLE, "pakyak");
        myWorld.addTest(BatTest.VISIBLE, "yak123ya");
        myWorld.addTest(BatTest.INVISIBLE, "yak");
        myWorld.addTest(BatTest.INVISIBLE, "yakxxxyak");
        myWorld.addTest(BatTest.INVISIBLE, "HiyakHi");
        myWorld.addTest(BatTest.INVISIBLE, "xxxyakyyyakzzz");

        setup(myWorld);
    }
}
