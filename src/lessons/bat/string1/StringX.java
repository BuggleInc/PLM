package lessons.bat.string1;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatWorld;

public class StringX extends BatExercise {
	public StringX(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("stringX");
		myWorld.addTest(VISIBLE, "xxHxix") ;
		myWorld.addTest(VISIBLE, "abxxxcd") ;
		myWorld.addTest(VISIBLE, "xabxxxcdx") ;
		myWorld.addTest(INVISIBLE, "xKittenx") ;
		myWorld.addTest(INVISIBLE, "Hello") ;
		myWorld.addTest(INVISIBLE, "xx") ;
		myWorld.addTest(INVISIBLE, "x") ;
		myWorld.addTest(INVISIBLE, "") ;

                setup(myWorld);
        }
}
