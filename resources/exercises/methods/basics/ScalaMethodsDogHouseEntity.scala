package lessons.welcome.methods.basics;

import plm.universe.bugglequest.SimpleBuggle

import scala.collection.JavaConversions
import plm.core.model.lesson.Exercise
import plm.core.log.Logger
import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaMethodsDogHouseEntity extends SimpleBuggle {
	override def right()  {
	  val locale: Locale = getWorld.getLocale
	  val i18n: I18n = I18nManager.getI18n(locale)
		throw new RuntimeException(i18n.tr("Sorry Dave, I cannot let you use right() in this exercise. Use left() instead."));
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
	    // FIXME: Re-implement me
	    /*
	    var offset = getGame().getCurrentLesson().getCurrentExercise().asInstanceOf[Exercise].getSourceFile(Game.SCALA, 0).getOffset()
	    var msg = getGame().i18n.tr("Sorry Dave, I cannot let you use left() both in lines {0} and {1} in this exercise. You can write left() only once in this exercise.",
	        (savedLine-offset),(usedLine-offset));
		  
		  Logger.debug(msg);
		  throw new RuntimeException(msg);
	  	*/
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
