package plm.core.utils

object ScalaHelper {
  def toList(data: Array[Int]): List[Int] = {
    data match {
      case null =>
        Nil
      case _ =>
        data.toList
    }
  }

  def compareNullToNil(o1: Any, o2: Any): Boolean = {
    (o1, o2) match {
      case (null, Nil) =>
        true
      case (Nil, null) =>
        true
      case _ =>
        false
    }
  }
}