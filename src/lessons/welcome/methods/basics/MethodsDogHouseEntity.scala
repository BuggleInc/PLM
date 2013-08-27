package lessons.welcome.methods.basics;

import plm.universe.bugglequest.SimpleBuggle
import plm.core.model.Game
import scala.collection.JavaConversions
import plm.core.model.lesson.Exercise

class ScalaMethodsDogHouseEntity extends SimpleBuggle {
	override def right()  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use right."));
	}

	var savedLine = -1
	var studentCode = false
	override def left() {
	  if (!studentCode) {
		  super.left()
		  return
	  }
	  var usedLine = 0
	  for (s <- Thread.currentThread().getStackTrace())
	    if (s.getMethodName().equals("dogHouse")) {
	      usedLine = s.getLineNumber()
	      /*println("left() used in "+s.getFileName()+":"+usedLine+" ("+
	    		  Thread.currentThread().getStackTrace().apply(2).getClassName()+":"+
	    		  Thread.currentThread().getStackTrace().apply(2).getMethodName()+")")*/
	    }
	  if (usedLine==0) {
	    //println("Warning, cannot determine the call stack of left(). No check enforced");
	    super.left()
	    return
	  }
	  if (savedLine == -1) {
	    savedLine = usedLine
	  } else if (savedLine != usedLine) {
	    var offset = Game.getInstance().getCurrentLesson().getCurrentExercise().asInstanceOf[Exercise].getSourceFile(Game.SCALA, 0).getOffset()
	    var msg = Game.i18n.tr("I'm sorry Dave, I'm affraid I cant let you use left() both in lines {0} and {1}.",
	        (savedLine-offset),(usedLine-offset));
		  System.out.println(msg);
		  throw new RuntimeException(msg);
	  }
	  super.left();
	}
	/* BEGIN SOLUTION */
	def dogHouse() {
		for (i <- 1 to 4) {
			forward()
			forward()
			left()
		}
	}
	/* END SOLUTION */

	override def run() {
		brushDown();
		studentCode = true
		dogHouse();
		brushUp();

		forward(4);

		brushDown();
		dogHouse();		
		brushUp();

		forward(2);
		studentCode = false
		left();
		studentCode = true
		forward(4);

		brushDown();
		dogHouse();		
		brushUp();

		forward(2);
		studentCode = false
		left();
		studentCode = true
		forward(4);

		brushDown();
		dogHouse();		
	} 
}
