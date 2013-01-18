package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class FrontTimes extends BatExercise {
  public FrontTimes(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("frontTimes");
    myWorld.addTest(VISIBLE, "Chocolate", 2) ;
    myWorld.addTest(VISIBLE, "Chocolate", 3) ;
    myWorld.addTest(VISIBLE, "Abc", 3) ;
    myWorld.addTest(INVISIBLE, "Ab", 4) ;
    myWorld.addTest(INVISIBLE, "A", 4) ;
    myWorld.addTest(INVISIBLE, "", 4) ;
    myWorld.addTest(INVISIBLE, "Abc", 0) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( frontTimes((String)t.getParameter(0), (Integer)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
String frontTimes(String str, int n) {
  /* BEGIN SOLUTION */
  int frontLen = 3;
  if (frontLen > str.length()) {
    frontLen = str.length();
  }
  String front = str.substring(0, frontLen);
  
  String result = "";
  for (int i=0; i<n; i++) {
    result = result + front;
  }
  return result;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
