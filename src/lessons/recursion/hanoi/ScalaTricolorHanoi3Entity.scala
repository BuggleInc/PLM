package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.model.Game

class ScalaTricolorHanoi3Entity extends HanoiEntity {

	override def run():Unit = {
    val src = getParam(0).asInstanceOf[Int]
    val mid = getParam(1).asInstanceOf[Int]
    val dst = getParam(2).asInstanceOf[Int]
    tricolor(getSlotSize(src),src,mid,dst);
	}

	/* BEGIN TEMPLATE */
  def tricolor(height:Int, src:Int, mid:Int, dst:Int):Unit = {
	  /* BEGIN SOLUTION */
	  gather(height, src, mid, dst);
    move3(height, dst,mid,src);
    move3(height, src,dst,mid);
    scatter(height, mid,dst,src);
  }
  def scatter(height:Int, src:Int, mid:Int, dst:Int):Unit = {
    if (height >0) {
      move3(height-1, src, dst,mid);
      move(src,dst);
      move(src,dst);
      move3(height-1, mid,dst,src);
      move(dst,mid);
      scatter(height-1, src,mid,dst);
    }   
  }

  def gather(height:Int, src:Int, mid:Int, dst:Int):Unit = {
    if (height >0) {
      gather(height-1,src,mid,dst);
      move(src,mid);
      move3(height-1, dst,mid,src);
      move(mid,dst);
      move(mid,dst);
      move3(height-1, src, mid, dst);
    }
	}

  def move3(height:Int, src:Int, mid:Int, dst:Int):Unit = {
    if (height>0) {
      move3(height-1, src, dst, mid);
      move(src,dst);
      move(src,dst);
      move(src,dst);
      move3(height-1, mid, src, dst);
    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
