package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class Last2 extends BatExercise {
	public Last2(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("last2");
		myWorld.addTest(VISIBLE, "hixxhi") ;
		myWorld.addTest(VISIBLE, "xaxxaxaxx") ;
		myWorld.addTest(VISIBLE, "axxxaaxx") ;
		myWorld.addTest(INVISIBLE, "xxaxxaxxaxx") ;
		myWorld.addTest(INVISIBLE, "xaxaxaxx") ;
		myWorld.addTest(INVISIBLE, "13121312") ;
		myWorld.addTest(INVISIBLE, "11212") ;
		myWorld.addTest(INVISIBLE, "13121311") ;
		myWorld.addTest(INVISIBLE, "1717171") ;
		myWorld.addTest(INVISIBLE, "hi") ;
		myWorld.addTest(INVISIBLE, "h") ;
		myWorld.addTest(INVISIBLE, "") ;

                setup(myWorld);
        }
}
