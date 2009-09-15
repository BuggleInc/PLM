package lessons.bat.string1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class StringX extends BatExercise {
  public StringX(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[8];
    myWorlds[0] = new BatWorld(VISIBLE, "xxHxix") ;
    myWorlds[1] = new BatWorld(VISIBLE, "abxxxcd") ;
    myWorlds[2] = new BatWorld(VISIBLE, "xabxxxcdx") ;
    myWorlds[3] = new BatWorld(INVISIBLE, "xKittenx") ;
    myWorlds[4] = new BatWorld(INVISIBLE, "Hello") ;
    myWorlds[5] = new BatWorld(INVISIBLE, "xx") ;
    myWorlds[6] = new BatWorld(INVISIBLE, "x") ;
    myWorlds[7] = new BatWorld(INVISIBLE, "") ;

    setup(myWorlds,"stringX");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = stringX((String)w.getParameter(0));
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
