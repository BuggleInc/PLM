package lessons.bat.warmup2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class StringMatch extends BatExercise {
  public StringMatch(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[10];
    myWorlds[0] = new BatWorld(VISIBLE, "xxcaazz", "xxbaaz") ;
    myWorlds[1] = new BatWorld(VISIBLE, "abc", "abc") ;
    myWorlds[2] = new BatWorld(VISIBLE, "abc", "axc") ;
    myWorlds[3] = new BatWorld(INVISIBLE, "hello", "he") ;
    myWorlds[4] = new BatWorld(INVISIBLE, "he", "hello") ;
    myWorlds[5] = new BatWorld(INVISIBLE, "h", "hello") ;
    myWorlds[6] = new BatWorld(INVISIBLE, "", "hello") ;
    myWorlds[7] = new BatWorld(INVISIBLE, "aabbccdd", "abbbxxd") ;
    myWorlds[8] = new BatWorld(INVISIBLE, "aaxxaaxx", "iaxxai") ;
    myWorlds[9] = new BatWorld(INVISIBLE, "iaxxai", "aaxxaaxx") ;

    setup(myWorlds,"stringMatch");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = stringMatch((String)w.getParameter(0), (String)w.getParameter(1));
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
