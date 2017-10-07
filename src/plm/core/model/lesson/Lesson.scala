package plm.core.model.lesson

import com.google.gson.JsonElement

import scala.collection.JavaConverters._
import plm.core.model.json.GsonUtil.getOptionalGsonMember

/**
 * @author matthieu
 */
object Lesson {

  def fromJson(json: JsonElement)(descriptions: Map[String,String]): Lesson = {
    val root = json.getAsJsonObject
    new Lesson(
      root.get("id").getAsString,
      getOptionalGsonMember(root, "name").map(_.getAsString),
      root.get("lectures").getAsJsonArray.asScala.map(Lecture.fromJson).toSeq,
      descriptions
    )
  }
}

case class Lesson(id: String, name: Option[String], lectures: Seq[Lecture], descriptions: Map[String, String]) {

  val orderedIDs: Seq[String] = lectures.flatMap(_.orderedIDs)

  def containsExercise(exerciseID: String): Boolean =  orderedIDs.contains(exerciseID)
}
