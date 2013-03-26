package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

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

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( altPairs((String)t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
String altPairs(String str) {
  /* BEGIN SOLUTION */
  String result = "";
  
  // Run i by 4 to hit 0, 4, 8, ...
  for (int i=0; i<str.length(); i += 4) {
    // Append the chars between i and i+2
    int end = i + 2;
    if (end > str.length()) {
      end = str.length();
    }
    result = result + str.substring(i, end);
  }
  
  return result;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
