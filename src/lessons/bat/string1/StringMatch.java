package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class StringMatch extends BatExercise {
  public StringMatch(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("stringMatch");
    myWorld.addTest(VISIBLE, "xxcaazz", "xxbaaz") ;
    myWorld.addTest(VISIBLE, "abc", "abc") ;
    myWorld.addTest(VISIBLE, "abc", "axc") ;
    myWorld.addTest(INVISIBLE, "hello", "he") ;
    myWorld.addTest(INVISIBLE, "he", "hello") ;
    myWorld.addTest(INVISIBLE, "h", "hello") ;
    myWorld.addTest(INVISIBLE, "", "hello") ;
    myWorld.addTest(INVISIBLE, "aabbccdd", "abbbxxd") ;
    myWorld.addTest(INVISIBLE, "aaxxaaxx", "iaxxai") ;
    myWorld.addTest(INVISIBLE, "iaxxai", "aaxxaaxx") ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( stringMatch((String)t.getParameter(0), (String)t.getParameter(1)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int stringMatch(String a, String b) {
  /* BEGIN SOLUTION */
  // Figure which string is shorter.
  int len = Math.min(a.length(), b.length());
  int count = 0;
  
  // Look at both substrings starting at i
  for (int i=0; i<len-1; i++) {
    String aSub = a.substring(i, i+2);
    String bSub = b.substring(i, i+2);
    if (aSub.equals(bSub)) {  // Use .equals() with strings
      count++;
    }
  }

  return count;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
