package lessons.bat.bool;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class StringSplosion extends BatExercise {
  public StringSplosion(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[7];
    myWorlds[0] = new BatWorld(VISIBLE, "Code") ;
    myWorlds[1] = new BatWorld(VISIBLE, "abc") ;
    myWorlds[2] = new BatWorld(VISIBLE, "x") ;
    myWorlds[3] = new BatWorld(INVISIBLE, "There") ;
    myWorlds[4] = new BatWorld(INVISIBLE, "Bye") ;
    myWorlds[5] = new BatWorld(INVISIBLE, "Good") ;
    myWorlds[6] = new BatWorld(INVISIBLE, "Bad") ;

    setup(myWorlds,"stringSplosion");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = stringSplosion((String)w.getParameter(0));
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
