package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class StringSplosion extends BatExercise {
  public StringSplosion(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("stringSplosion");
    myWorld.addTest(VISIBLE, "Code") ;
    myWorld.addTest(VISIBLE, "abc") ;
    myWorld.addTest(VISIBLE, "x") ;
    myWorld.addTest(INVISIBLE, "There") ;
    myWorld.addTest(INVISIBLE, "Bye") ;
    myWorld.addTest(INVISIBLE, "Good") ;
    myWorld.addTest(INVISIBLE, "Bad") ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( stringSplosion((String)t.getParameter(0)) ); 
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
String stringSplosion(String str) {
  /* BEGIN SOLUTION */
  String result = "";
  // On each iteration, add the substring of the chars 0..i
  for (int i=0; i<str.length(); i++) {
    result = result + str.substring(0, i+1);
  }
  return result;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
