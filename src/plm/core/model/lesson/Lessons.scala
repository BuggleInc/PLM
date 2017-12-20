package plm.core.model.lesson

import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.nio.charset.StandardCharsets

import com.google.gson.JsonParser
import plm.core.log.Logger

import scala.io.{Codec, Source}

/**
 * @author matthieu
 */

class Lessons(
    classLoader: ClassLoader,
    availableLanguageCodes: Iterable[String]) {
  // WARNING, keep ChooseLessonDialog.lessons synchronized
  private val lessonIds: Seq[String] =
    Seq(
//    "welcome", //OK without many exercices
    "sort.basic", //OK without performance test with 150 elements
    "sort.dutchflag", //OK
//    "maze", //OK
//    "turmites", //exercices timeOUT when drawing
//    "turtleart", //turtle Draw are not correct
    "sort.baseball" // Draw are not correct
//    "sort.pancake", //OK
//    "recursion.cons", //Correct draw but not handle in JS
//    "recursion.logo", //turtle Draw are not correct
//    "recursion.hanoi"
//       "bat.string1"
    )

  private val lessons: Map[String, Lesson] = {
    val entries = for {
      lessonId <- lessonIds.view
      lesson <- loadLesson(lessonId)
    } yield lessonId -> lesson
    entries.toMap
  }

  val lessonsList: Seq[Lesson] = lessonIds.map(lessons(_))

  def lessonExists(lessonId: String): Boolean =
    lessonIds.contains(lessonId)

  def exerciseExists(lessonId: String, exerciseId: String): Boolean =
    lessonExists(lessonId) && lessons(lessonId).containsExercise(exerciseId)

  def exercisesList(lessonId: String): Seq[Lecture] =
    lessons(lessonId).lectures

  def firstExerciseId(lessonId: String): String =
    lessons(lessonId).lectures.head.id

  private def loadLesson(lessonName: String): Option[Lesson] = {
    val path = lessonName.replace(".", "/") + "/main.json"
    withResource(path) {
      case Some(is: InputStream) =>
        val reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name))
        Some(Lesson.fromJson(new JsonParser().parse(reader))(loadDescriptions(lessonName)))
      case None =>
        Logger.error(s"Lesson $lessonName is missing.")
        None
    }
  }

  private def loadDescriptions(lessonName: String): Map[String, String] = {
    val entries = for {
      languageCode <- availableLanguageCodes.view
      description <- loadDescription(lessonName, languageCode)
    } yield languageCode -> description
    entries.toMap
  }

  private def loadDescription(lessonName: String, languageCode: String): Option[String] = {
    val prefix = lessonName.replace(".", "/")
    val suffix = if (languageCode != "en") "." + languageCode else ""
    val path = s"$prefix/description$suffix.html"
    withResource(path) {
      case Some(is: InputStream) =>
        Some(Source.fromInputStream(is)(Codec.UTF8).mkString)
      case None =>
        Logger.info(s"$languageCode description for lesson $lessonName is missing.")
        None
    }
  }

  private def withResource[O](fileName: String)(action: Option[InputStream] => O): O = {
    val maybeInputStream = Option(classLoader.getResourceAsStream(fileName))
    try {
      action(maybeInputStream)
    } finally {
      maybeInputStream.foreach(_.close())
    }
  }
}
