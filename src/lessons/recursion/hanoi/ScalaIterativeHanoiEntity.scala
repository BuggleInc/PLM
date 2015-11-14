package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.model.Game

class ScalaIterativeHanoiEntity extends HanoiEntity {
	override def run():Unit = {
    val initialPos=getParam(0).asInstanceOf[Int]
    val increasing=getParam(1).asInstanceOf[Boolean]
		hanoi(initialPos, increasing);
	}

	/* BEGIN TEMPLATE */
	def hanoi(initialPos:Int, increasing:Boolean):Unit = {
		/* BEGIN SOLUTION */
		var small = initialPos
    var count = 0
    var pos1=0
    var pos2=0
    do {
      if (count%2 == 0) {
        val next = (if (increasing) {small+1} else {small-1+3}) % 3;
        //getGame().getLogger().log("move("+small+","+next+")");
        move(small, next);
        small=next;
      }
      
      small match {
      case 0 => pos1=1; pos2=2; 
      case 1 => pos1=0; pos2=2; 
      case 2 => pos1=0; pos2=1; 
      }
      if (count%2 == 1) {
        if (getSlotRadius(pos1) > getSlotRadius(pos2))
          move(pos2,pos1);
        else
          move(pos1,pos2);
      }
      
      count += 1;
    } while (getSlotSize(pos1) != 0 || getSlotSize(pos2) != 0);

		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
