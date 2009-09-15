package lessons.bat.warmup2;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class StringBits extends BatExercise {
  public StringBits(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[5];
    myWorlds[0] = new BatWorld(VISIBLE, "Hello") ;
    myWorlds[1] = new BatWorld(VISIBLE, "Hi") ;
    myWorlds[2] = new BatWorld(VISIBLE, "HiHiHi") ;
    myWorlds[3] = new BatWorld(INVISIBLE, "") ;
    myWorlds[4] = new BatWorld(INVISIBLE, "Greetings") ;

    setup(myWorlds,"stringBits");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = stringBits((String)w.getParameter(0));
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
