package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

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

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( stringX((String)t.getParameter(0)) );
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
String stringX(String str) {
  /* BEGIN SOLUTION */
  String result = "";
  for (int i=0; i<str.length(); i++) {
    // Only append the char if it is not the "x" case
    if (!(i > 0 && i < (str.length()-1) && str.substring(i, i+1).equals("x"))) {
      result = result + str.substring(i, i+1); // Could use str.charAt(i) here
    }
  }
  return result;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
