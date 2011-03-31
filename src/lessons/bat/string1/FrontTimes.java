package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class FrontTimes extends BatExercise {
  public FrontTimes(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[7];
    myWorlds[0] = new BatWorld(VISIBLE, "Chocolate", 2) ;
    myWorlds[1] = new BatWorld(VISIBLE, "Chocolate", 3) ;
    myWorlds[2] = new BatWorld(VISIBLE, "Abc", 3) ;
    myWorlds[3] = new BatWorld(INVISIBLE, "Ab", 4) ;
    myWorlds[4] = new BatWorld(INVISIBLE, "A", 4) ;
    myWorlds[5] = new BatWorld(INVISIBLE, "", 4) ;
    myWorlds[6] = new BatWorld(INVISIBLE, "Abc", 0) ;

    setup(myWorlds,"frontTimes");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = frontTimes((String)w.getParameter(0), (Integer)w.getParameter(1));
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
