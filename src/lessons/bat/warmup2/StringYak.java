package lessons.bat.warmup2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class StringYak extends BatExercise {
  public StringYak(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[7];
    myWorlds[0] = new BatWorld(VISIBLE, "yakpak") ;
    myWorlds[1] = new BatWorld(VISIBLE, "pakyak") ;
    myWorlds[2] = new BatWorld(VISIBLE, "yak123ya") ;
    myWorlds[3] = new BatWorld(INVISIBLE, "yak") ;
    myWorlds[4] = new BatWorld(INVISIBLE, "yakxxxyak") ;
    myWorlds[5] = new BatWorld(INVISIBLE, "HiyakHi") ;
    myWorlds[6] = new BatWorld(INVISIBLE, "xxxyakyyyakzzz") ;

    setup(myWorlds,"stringYak");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = stringYak((String)w.getParameter(0));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
String stringYak(String str) {
  /* BEGIN SOLUTION */
  String result = "";
  
  for (int i=0; i<str.length(); i++) {
    // Look for i starting a "yak" -- advance i in that case
    if (i+2<str.length() && str.charAt(i)=='y' && str.charAt(i+2)=='k') {
      i =  i + 2;
    } else { // Otherwise do the normal append
      result = result + str.charAt(i);
    }
  }
  
  return result;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
