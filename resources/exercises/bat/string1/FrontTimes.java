package bat.string1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class FrontTimes extends ExerciseTemplated {
    public FrontTimes(Lesson lesson, FileUtils fileUtils) {
        BatWorld myWorld = new BatWorld(fileUtils, "frontTimes");
        myWorld.addTest(BatTest.VISIBLE, "Chocolate", 2);
        myWorld.addTest(BatTest.VISIBLE, "Chocolate", 3);
        myWorld.addTest(BatTest.VISIBLE, "Abc", 3);
        myWorld.addTest(BatTest.INVISIBLE, "Ab", 4);
        myWorld.addTest(BatTest.INVISIBLE, "A", 4);
        myWorld.addTest(BatTest.INVISIBLE, "", 4);
        myWorld.addTest(BatTest.INVISIBLE, "Abc", 0);

        setup(myWorld);
    }
}
