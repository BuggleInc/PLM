package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class StringBits extends BatExercise {
  public StringBits(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("stringBits");
    myWorld.addTest(VISIBLE, "Hello") ;
    myWorld.addTest(VISIBLE, "Hi") ;
    myWorld.addTest(VISIBLE, "HiHiHi") ;
    myWorld.addTest(INVISIBLE, "") ;
    myWorld.addTest(INVISIBLE, "Greetings") ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( stringBits((String)t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
String stringBits(String str) {
  /* BEGIN SOLUTION */
  String result = "";
  // Note: the loop increments i by 2
  for (int i=0; i<str.length(); i+=2) {
    result = result + str.substring(i, i+1);
    // Alternately could use str.charAt(i)
  }
  return result;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
