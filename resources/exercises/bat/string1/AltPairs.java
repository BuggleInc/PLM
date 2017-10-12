package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class AltPairs extends ExerciseTemplated {
	public AltPairs(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("altPairs");
		myWorld.addTest(BatTest.VISIBLE, "kitten") ;
		myWorld.addTest(BatTest.VISIBLE, "Chocolate") ;
		myWorld.addTest(BatTest.VISIBLE, "CodingHorror") ;
		myWorld.addTest(BatTest.INVISIBLE, "yak") ;
		myWorld.addTest(BatTest.INVISIBLE, "ya") ;
		myWorld.addTest(BatTest.INVISIBLE, "y") ;
		myWorld.addTest(BatTest.INVISIBLE, "") ;
		myWorld.addTest(BatTest.INVISIBLE, "ThisThatTheOther") ;

		setup(myWorld);
	}
}
