package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class StringX extends ExerciseTemplated {
	public StringX(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("stringX");
		myWorld.addTest(BatTest.VISIBLE, "xxHxix") ;
		myWorld.addTest(BatTest.VISIBLE, "abxxxcd") ;
		myWorld.addTest(BatTest.VISIBLE, "xabxxxcdx") ;
		myWorld.addTest(BatTest.INVISIBLE, "xKittenx") ;
		myWorld.addTest(BatTest.INVISIBLE, "Hello") ;
		myWorld.addTest(BatTest.INVISIBLE, "xx") ;
		myWorld.addTest(BatTest.INVISIBLE, "x") ;
		myWorld.addTest(BatTest.INVISIBLE, "") ;

		setup(myWorld);
	}
}
