package plm.core.model.lesson

import java.net.URLClassLoader
import java.util.Locale

import plm.core.lang._
import plm.core.model.lesson.tip.AbstractTipFactory

class Exercises(lessons: Lessons, tipFactory: AbstractTipFactory, availableLocales: Traversable[Locale]) {

  private val DEFAULT_PROGRAMMING_LANGUAGE: ProgrammingLanguage = ProgrammingLanguages.defaultProgrammingLanguage()
  private val EN_LOCALE: Locale = new Locale("en")

  private val exerciseFactory: ExerciseFactory =
    new ExerciseFactory(
      EN_LOCALE,
      new ExerciseRunner(EN_LOCALE),
      ProgrammingLanguages.programmingLanguages(),
      availableLocales.toArray,
      tipFactory)

  private val exercises: Map[String, Exercise] =
    lessons
      .lessonsList
      .flatMap(_.lectures)
      .map(lecture => lecture.id -> initExercise(lecture.id))
      .toMap

  def getExercise(exerciseId: String): Option[Exercise] =
    exercises
      .get(exerciseId)
      .map(ExerciseFactory.cloneExercise)

  private def initExercise(exerciseId: String): Exercise = {
    // Instantiate the exercise the old fashioned way
    val userSettings: UserSettings = new UserSettings(EN_LOCALE, DEFAULT_PROGRAMMING_LANGUAGE)
    val exercise: Exercise = Class.forName(exerciseId).getDeclaredConstructor().newInstance().asInstanceOf[Exercise]
    exercise.setSettings(userSettings)
    exerciseFactory.initializeExercise(exercise, DEFAULT_PROGRAMMING_LANGUAGE)
    exercise
  }
}
