package plm.universe.cons

import com.fasterxml.jackson.databind.module.SimpleModule

object RecListWrapper {
  def equalsToScalaList(recList: RecList, o: Any): Boolean = {
    val list: List[Int] = recList.toArray.toList
    val other: List[Int] = o.asInstanceOf[List[Int]]

    (list, other) match {
      case (List(), Nil) =>
        true
      case (_, _) =>
        list.equals(other)
    }
  }
}