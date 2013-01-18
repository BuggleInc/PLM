package lessons.welcome.bool1;
import jlm.core.model.lesson.Lesson;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

public class Max1020 extends BatExercise {
  public Max1020(Lesson lesson) {
    super(lesson);
    
    BatWorld myWorld = new BatWorld("Max1023");
    myWorld.addTest(VISIBLE, 11, 19) ;
    myWorld.addTest(VISIBLE, 19, 11) ;
    myWorld.addTest(VISIBLE, 11, 9) ;
    myWorld.addTest(INVISIBLE, 9, 21) ;
    myWorld.addTest(INVISIBLE, 10, 21) ;
    myWorld.addTest(INVISIBLE, 21, 10) ;
    myWorld.addTest(INVISIBLE, 9, 11) ;
    myWorld.addTest(INVISIBLE, 23, 10) ;
    myWorld.addTest(INVISIBLE, 20, 10) ;
    myWorld.addTest(INVISIBLE, 7, 20) ;
    myWorld.addTest(INVISIBLE, 17, 16) ;

    setup(myWorld);
  }

  /* BEGIN SKEL */
  public void run(BatTest t) {
    t.setResult( max1020((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
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
