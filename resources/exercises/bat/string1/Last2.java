package bat.string1;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Last2 extends ExerciseTemplated {
    public Last2(FileUtils fileUtils) {
        BatWorld myWorld = new BatWorld(fileUtils, "last2");
        myWorld.addTest(BatTest.VISIBLE, "hixxhi");
        myWorld.addTest(BatTest.VISIBLE, "xaxxaxaxx");
        myWorld.addTest(BatTest.VISIBLE, "axxxaaxx");
        myWorld.addTest(BatTest.INVISIBLE, "xxaxxaxxaxx");
        myWorld.addTest(BatTest.INVISIBLE, "xaxaxaxx");
        myWorld.addTest(BatTest.INVISIBLE, "13121312");
        myWorld.addTest(BatTest.INVISIBLE, "11212");
        myWorld.addTest(BatTest.INVISIBLE, "13121311");
        myWorld.addTest(BatTest.INVISIBLE, "1717171");
        myWorld.addTest(BatTest.INVISIBLE, "hi");
        myWorld.addTest(BatTest.INVISIBLE, "h");
        myWorld.addTest(BatTest.INVISIBLE, "");

        setup(myWorld);
    }
}
