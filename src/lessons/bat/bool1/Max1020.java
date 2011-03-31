package lessons.bat.bool1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatWorld;

public class Max1020 extends BatExercise {
  public Max1020(Lesson lesson) {
    super(lesson);
    
    World[] myWorlds = new BatWorld[11];
    myWorlds[0] = new BatWorld(VISIBLE, 11, 19) ;
    myWorlds[1] = new BatWorld(VISIBLE, 19, 11) ;
    myWorlds[2] = new BatWorld(VISIBLE, 11, 9) ;
    myWorlds[3] = new BatWorld(INVISIBLE, 9, 21) ;
    myWorlds[4] = new BatWorld(INVISIBLE, 10, 21) ;
    myWorlds[5] = new BatWorld(INVISIBLE, 21, 10) ;
    myWorlds[6] = new BatWorld(INVISIBLE, 9, 11) ;
    myWorlds[7] = new BatWorld(INVISIBLE, 23, 10) ;
    myWorlds[8] = new BatWorld(INVISIBLE, 20, 10) ;
    myWorlds[9] = new BatWorld(INVISIBLE, 7, 20) ;
    myWorlds[10] = new BatWorld(INVISIBLE, 17, 16) ;

    setup(myWorlds,"max1020");
  }

  /* BEGIN SKEL */
  public void run(World w) {
    BatWorld bw = (BatWorld) w;
    bw.result = max1020((Integer)w.getParameter(0), (Integer)w.getParameter(1));
  }
  /* END SKEL */

  /* BEGIN TEMPLATE */
int max1020(int a, int b) {
  /* BEGIN SOLUTION */
	int A = a>b?a:b;
	int B = a>b?b:a;
	if (A<21 && A>9)
		return A;
	if (B<21 && B>9)
		return B;
	return 0;
  /* END SOLUTION */
}
  /* END TEMPLATE */
}
