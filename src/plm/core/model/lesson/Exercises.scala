package plm.core.model.lesson

import java.net.URLClassLoader
import java.util.Locale

import plm.core.lang._
import plm.core.model.lesson.tip.AbstractTipFactory
import plm.core.utils.FileUtils

class Exercises(
    lessons: Lessons,
    fileUtils: FileUtils,
    programmingLanguages: ProgrammingLanguages,
    tipFactory: AbstractTipFactory,
    availableLocales: Array[Locale]) {

  private val DEFAULT_PROGRAMMING_LANGUAGE = programmingLanguages.defaultProgrammingLanguage()
  private val EN_LOCALE: Locale = new Locale("en")

  private val exerciseFactory: ExerciseFactory =
    new ExerciseFactory(
      fileUtils,
      EN_LOCALE,
      new ExerciseRunner(EN_LOCALE),
      programmingLanguages.programmingLanguages(),
      availableLocales,
      tipFactory)

  private val exercises: Map[String, Exercise] =
    lessons
      .lessonsList
      .flatMap(_.orderedIDs)
      .map(lectureId => lectureId -> initExercise(lectureId))
      .toMap

  def getExercise(exerciseId: String): Option[Exercise] =
    exercises
      .get(exerciseId)
      .map(ExerciseFactory.computeAnswer)
      .map(ExerciseFactory.cloneExercise)

  private def initExercise(exerciseId: String): Exercise = {
    val userSettings: UserSettings = new UserSettings(EN_LOCALE, DEFAULT_PROGRAMMING_LANGUAGE)
    val exercise: Exercise =
      Class
        .forName(exerciseId)
        .getDeclaredConstructor(classOf[FileUtils])
        .newInstance(fileUtils)
        .asInstanceOf[Exercise]
    exercise.setSettings(userSettings)
    exerciseFactory.initializeExercise(exercise, DEFAULT_PROGRAMMING_LANGUAGE)
    exercise
  }
}
