package plm.core.model.json

import com.google.gson.{JsonArray, JsonElement, JsonObject}

import scala.collection.JavaConverters._

object GsonUtil {

  def getOptionalGsonMember(jsonObject: JsonObject, member: String): Option[JsonElement] = {
    val value = jsonObject.get(member)
    if (value.isJsonNull) None else Some(value)
  }

  def gsonObjectToMap(jsonObject: JsonObject): Map[String, JsonElement] = {
    val entries = jsonObject.entrySet.asScala.view map { entry => (entry.getKey, entry.getValue) }
    entries.toMap
  }

  def mapToGsonObject[A <: JsonElement](entries: Map[String, A]): JsonObject = {
    val json = new JsonObject()
    for (entry <- entries) {
      json.add(entry._1, entry._2)
    }
    json
  }

  def iterableToGsonArray[A <: JsonElement](elements: Iterable[A]): JsonArray = {
    val array = new JsonArray()
    elements.foreach(array.add(_))
    array
  }
}
