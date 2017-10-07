package recursion.hanoi;


import plm.universe.hanoi.HanoiEntity
import plm.core.model.I18nManager

class ScalaLinearHanoiEntity extends HanoiEntity {
  override def move(from:Int, to:Int) {
    if ((from == 0 && to == 2) || (from == 2 && to == 0)) 
      throw new RuntimeException(I18nManager.getI18n(getWorld().getLocale()).tr(
          "Sorry Dave, I cannot let you use move disks between slots 0 and 2 directly. Use the intermediate slot in all moves."));
    super.move(from,to);
  }
  

	override def run() {
    val src= getParam(0).asInstanceOf[Int]
    val mid= getParam(1).asInstanceOf[Int]
    val dst= getParam(2).asInstanceOf[Int]
		linearHanoi(getSlotSize(src), src,mid, dst);
	}

	/* BEGIN TEMPLATE */
  def linearHanoi(height:Int, src:Int, mid:Int, dst:Int) {
	  /* BEGIN SOLUTION */
    if (height > 0) {
      linearHanoi(height-1, src,mid,dst);
      move(src,mid);
      linearHanoi(height-1, dst,mid,src);
      move(mid,dst);
      linearHanoi(height-1, src,mid,dst);
    }    
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
