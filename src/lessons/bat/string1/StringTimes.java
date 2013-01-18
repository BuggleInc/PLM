package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class StringTimes extends BatExercise {
  public StringTimes(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("stringTimes");
    myWorld.addTest(VISIBLE, "Hi", 2) ;
    myWorld.addTest(VISIBLE, "Hi", 3) ;
    myWorld.addTest(VISIBLE, "Hi", 1) ;
    myWorld.addTest(INVISIBLE, "Hi", 0) ;
    myWorld.addTest(INVISIBLE, "Oh Boy!", 2) ;
    myWorld.addTest(INVISIBLE, "x", 4) ;
    myWorld.addTest(INVISIBLE, "", 4) ;
    myWorld.addTest(INVISIBLE, "code", 2) ;
    myWorld.addTest(INVISIBLE, "code", 3) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( stringTimes((String)t.getParameter(0), (Integer)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
String stringTimes(String str, int n) {
  /* BEGIN SOLUTION */
  String result = "";
  for (int i=0; i<n; i++) {
    result = result + str;  // could use += here
  }
  return result;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
