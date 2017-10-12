package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringYak extends ExerciseTemplated {
	public StringYak(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("stringYak");
		myWorld.addTest(BatTest.VISIBLE, "yakpak") ;
		myWorld.addTest(BatTest.VISIBLE, "pakyak") ;
		myWorld.addTest(BatTest.VISIBLE, "yak123ya") ;
		myWorld.addTest(BatTest.INVISIBLE, "yak") ;
		myWorld.addTest(BatTest.INVISIBLE, "yakxxxyak") ;
		myWorld.addTest(BatTest.INVISIBLE, "HiyakHi") ;
		myWorld.addTest(BatTest.INVISIBLE, "xxxyakyyyakzzz") ;

		setup(myWorld);
	}
}
