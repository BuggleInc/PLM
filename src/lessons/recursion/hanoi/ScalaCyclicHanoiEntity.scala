package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.model.Game

class ScalaCyclicHanoiEntity extends HanoiEntity {
  override def move(from:Int, to:Int) = cyclicMove(from, to)
  
	override def run():Unit = {
    val src=getParam(0).asInstanceOf[Int]
    val mid=getParam(1).asInstanceOf[Int]
    val dst=getParam(2).asInstanceOf[Int]
		clockwise(getSlotSize(src), src,mid,dst);
	}

	/* BEGIN TEMPLATE */
	def clockwise(height:Int, src:Int, mid:Int, dst:Int):Unit = {
		/* BEGIN SOLUTION */
		if (height>0) {
      anti(height-1,src,dst,mid);
      move(src,dst);
      anti(height-1,mid,src,dst);
		}
  }
  def anti(height:Int, src:Int, mid:Int, dst:Int):Unit = {
	  if (height > 0) {   
      //System.err.println("beg counterclockwise("+height+","+src+","+mid+","+dst+")");
      anti(height-1,src,mid,dst);
      move(src,mid);
      clockwise(height-1,dst,mid,src);      
      move(mid,dst);
      anti(height-1,src,mid,dst);
      //System.err.println("end counterclockwise("+height+","+src+","+mid+","+dst+")");
    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
