package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class AltPairs extends BatExercise {
	public AltPairs(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("altPairs");
		myWorld.addTest(VISIBLE, "kitten") ;
		myWorld.addTest(VISIBLE, "Chocolate") ;
		myWorld.addTest(VISIBLE, "CodingHorror") ;
		myWorld.addTest(INVISIBLE, "yak") ;
		myWorld.addTest(INVISIBLE, "ya") ;
		myWorld.addTest(INVISIBLE, "y") ;
		myWorld.addTest(INVISIBLE, "") ;
		myWorld.addTest(INVISIBLE, "ThisThatTheOther") ;

                setup(myWorld);
        }
}
