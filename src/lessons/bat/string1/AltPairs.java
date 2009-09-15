package lessons.bat.string1;
import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class AltPairs extends BatExercise {
  public AltPairs(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[8];
    myWorlds[0] = new BatWorld(VISIBLE, "kitten") ;
    myWorlds[1] = new BatWorld(VISIBLE, "Chocolate") ;
    myWorlds[2] = new BatWorld(VISIBLE, "CodingHorror") ;
    myWorlds[3] = new BatWorld(INVISIBLE, "yak") ;
    myWorlds[4] = new BatWorld(INVISIBLE, "ya") ;
    myWorlds[5] = new BatWorld(INVISIBLE, "y") ;
    myWorlds[6] = new BatWorld(INVISIBLE, "") ;
    myWorlds[7] = new BatWorld(INVISIBLE, "ThisThatTheOther") ;

    setup(myWorlds,"altPairs");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = altPairs((String)w.getParameter(0));
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
