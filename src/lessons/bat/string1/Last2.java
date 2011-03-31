package lessons.bat.string1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class Last2 extends BatExercise {
  public Last2(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[12];
    myWorlds[0] = new BatWorld(VISIBLE, "hixxhi") ;
    myWorlds[1] = new BatWorld(VISIBLE, "xaxxaxaxx") ;
    myWorlds[2] = new BatWorld(VISIBLE, "axxxaaxx") ;
    myWorlds[3] = new BatWorld(INVISIBLE, "xxaxxaxxaxx") ;
    myWorlds[4] = new BatWorld(INVISIBLE, "xaxaxaxx") ;
    myWorlds[5] = new BatWorld(INVISIBLE, "13121312") ;
    myWorlds[6] = new BatWorld(INVISIBLE, "11212") ;
    myWorlds[7] = new BatWorld(INVISIBLE, "13121311") ;
    myWorlds[8] = new BatWorld(INVISIBLE, "1717171") ;
    myWorlds[9] = new BatWorld(INVISIBLE, "hi") ;
    myWorlds[10] = new BatWorld(INVISIBLE, "h") ;
    myWorlds[11] = new BatWorld(INVISIBLE, "") ;

    setup(myWorlds,"last2");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = last2((String)w.getParameter(0));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int last2(String str) {
  /* BEGIN SOLUTION */
  // Screen out too-short string case.
  if (str.length() < 2) return 0;
  
  String end = str.substring(str.length()-2);
  // Note: substring() with 1 value goes through the end of the string
  int count = 0;
  
  // Check each substring length 2 starting at i
  for (int i=0; i<str.length()-2; i++) {
    String sub = str.substring(i, i+2);
    if (sub.equals(end)) {  // Use .equals() with strings
      count++;
    }
  }

  return count;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
