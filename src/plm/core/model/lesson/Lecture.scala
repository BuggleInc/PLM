package plm.core.model.lesson

import com.google.gson.JsonElement
import plm.core.model.json.GsonUtil.{getOptionalGsonMember, gsonObjectToMap}

import scala.collection.JavaConverters._
import scala.language.postfixOps

/**
 * @author matthieu
 */
object Lecture {

  private def namesFromJson(json: JsonElement) =
    gsonObjectToMap(json.getAsJsonObject).mapValues(_.getAsString)

  def fromJson(json: JsonElement): Lecture = {
    val root = json.getAsJsonObject
    new Lecture(
      root.get("id").getAsString,
      getOptionalGsonMember(root, "names").map(namesFromJson),
      root.get("dependingLectures").getAsJsonArray.asScala.map(fromJson).toSeq
    )
  }
}

case class Lecture(id: String, optNames: Option[Map[String, String]], dependingLectures: Seq[Lecture]) {
  val orderedIDs: Seq[String] = id +: dependingLectures.flatMap(_.orderedIDs)
}
