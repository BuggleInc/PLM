package lessons.bat.string1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class StringTimes extends BatExercise {
  public StringTimes(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[9];
    myWorlds[0] = new BatWorld(VISIBLE, "Hi", 2) ;
    myWorlds[1] = new BatWorld(VISIBLE, "Hi", 3) ;
    myWorlds[2] = new BatWorld(VISIBLE, "Hi", 1) ;
    myWorlds[3] = new BatWorld(INVISIBLE, "Hi", 0) ;
    myWorlds[4] = new BatWorld(INVISIBLE, "Oh Boy!", 2) ;
    myWorlds[5] = new BatWorld(INVISIBLE, "x", 4) ;
    myWorlds[6] = new BatWorld(INVISIBLE, "", 4) ;
    myWorlds[7] = new BatWorld(INVISIBLE, "code", 2) ;
    myWorlds[8] = new BatWorld(INVISIBLE, "code", 3) ;

    setup(myWorlds,"stringTimes");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = stringTimes((String)w.getParameter(0), (Integer)w.getParameter(1));
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
