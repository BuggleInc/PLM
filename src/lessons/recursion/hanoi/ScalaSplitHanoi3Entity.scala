package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.model.Game

class ScalaSplitHanoi3Entity extends HanoiEntity {

	override def run():Unit = {
		solve(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Int], getParam(2).asInstanceOf[Int], getParam(3).asInstanceOf[Int]);
	}

	def solve(src:Int,other:Int, dst1:Int, dst2:Int):Unit = {
		splitHanoi(getSlotSize(src)/2, src,other, dst1,dst2);
	}

	/* BEGIN TEMPLATE */
  def splitHanoi(height:Int, src:Int, other:Int, dst1:Int, dst2:Int):Unit = {
	  /* BEGIN SOLUTION */
    if (height > 0) {
      splitHanoi(height-1, src,dst1,dst2,other);
      move(src,dst1);
      hanoi_extra(height-1, dst2,src,other,dst1);
      move(src,dst2);
      hanoi_extra(height-1, other,src,dst1,dst2);
    }    
  }
  def hanoi_extra(height:Int, src:Int, other:Int, used:Int, dst:Int):Unit = {
    //for (int i=6;i>height;i--) System.out.print(" ");
    //getGame().getLogger().log("Extra "+height+", "+src+", "+other+", "+used+", "+dst);
    if (height<=0)
      return;
    if (height==1) 
      move(src,dst);
    else {
      move(src,used);
      hanoi(height-1,src,other,dst);
      move(used,dst);
    }
  }

  def hanoi(height:Int, src:Int, other:Int, dst:Int):Unit = {
    if (height>0) {
      hanoi(height-1,  src,dst,other);
      move(src,dst);
      hanoi(height-1,  other,src,dst);
    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
