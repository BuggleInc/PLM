package plm.core.model.session;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Lecture;

public class SessionDB {
  /** Identifies one source of an exercise that the student edits */
  private record BodyKey(String exoId, ProgrammingLanguage lang, String sourceName) {}

  /** What the student wrote. Absent while the student did not change the initial content. Read by the run thread, written by the EDT. */
  private final Map<BodyKey, String> bodies = new ConcurrentHashMap<>();
  /** The editors to tell when the body of their source changes */
  private final Map<BodyKey, ISourceFileListener> bodyListeners           = new ConcurrentHashMap<>();
  /** One exercise in one language */
  private record ExerciseKey(String exoId, ProgrammingLanguage lang) {}

  /** One lesson in one language */
  private record LessonKey(String lessonId, ProgrammingLanguage lang) {}

  /** The exercises that the student passed. If an exercise was never attempted, it is not there. */
  private final Set<ExerciseKey> passed = ConcurrentHashMap.newKeySet();
  /* Per lesson summary */
  private final Map<LessonKey, Integer> passedExercises   = new ConcurrentHashMap<>();
  private final Map<LessonKey, Integer> possibleExercises = new ConcurrentHashMap<>();

  /** Stores what the student wrote in that source, and tells its editor */
  public void setBody(Lecture exo, ProgrammingLanguage lang, String sourceName, String content)
  {
    BodyKey key = new BodyKey(exo.getId(), lang, sourceName);
    bodies.put(key, content);
    notifyBodyListener(key);
  }

  /** The content last set for that source, or null if none was */
  public String getBody(Lecture exo, ProgrammingLanguage lang, String sourceName) { return bodies.get(new BodyKey(exo.getId(), lang, sourceName)); }

  /** Forgets what the student wrote in every source of that exercise, and tells the editors */
  public void removeBodies(Lecture exo)
  {
    for (BodyKey key : List.copyOf(bodies.keySet()))
      if (key.exoId().equals(exo.getId())) {
        bodies.remove(key);
        notifyBodyListener(key);
      }
  }

  public void setBodyListener(Lecture exo, ProgrammingLanguage lang, String sourceName, ISourceFileListener l)
  {
    bodyListeners.put(new BodyKey(exo.getId(), lang, sourceName), l);
  }

  public void removeBodyListener(Lecture exo, ProgrammingLanguage lang, String sourceName) { bodyListeners.remove(new BodyKey(exo.getId(), lang, sourceName)); }

  private void notifyBodyListener(BodyKey key)
  {
    ISourceFileListener listener = bodyListeners.get(key);
    if (listener != null)
      listener.sourceFileContentHasChanged();
  }

  public void setPassed(Lecture exo, ProgrammingLanguage lang, boolean isPassed)
  {
    ExerciseKey key = new ExerciseKey(exo.getId(), lang);
    if (isPassed ? passed.add(key) : passed.remove(key)) {
      String lessonId = exo.getLesson().getId();
      setPassedExercises(lessonId, lang, getPassedExercises(lessonId, lang) + (isPassed ? 1 : -1));
    }
  }

  public boolean getPassed(Lecture exo, ProgrammingLanguage lang) { return passed.contains(new ExerciseKey(exo.getId(), lang)); }

  public int getPossibleExercises(String lesson, ProgrammingLanguage lang) { return possibleExercises.getOrDefault(new LessonKey(lesson, lang), 0); }

  public int getPassedExercises(String lesson, ProgrammingLanguage lang) { return passedExercises.getOrDefault(new LessonKey(lesson, lang), 0); }

  public void setPossibleExercises(String lesson, ProgrammingLanguage lang, int val) { possibleExercises.put(new LessonKey(lesson, lang), val); }

  public void setPassedExercises(String lesson, ProgrammingLanguage lang, int val) { passedExercises.put(new LessonKey(lesson, lang), val); }

  /** The lessons for which the number of exercises is known */

  /** A JSON object with the counters of that lesson, omitting the null ones */
  public String lessonSummary(String lesson)
  {
    JSONObject result = new JSONObject();
    for (ProgrammingLanguage pl : Game.getInstance().programmingLanguageManager.langs) {
      if (getPossibleExercises(lesson, pl) != 0)
        result.put("possible" + pl.getLang(), getPossibleExercises(lesson, pl));
      if (getPassedExercises(lesson, pl) != 0)
        result.put("passed" + pl.getLang(), getPassedExercises(lesson, pl));
    }
    return result.toJSONString();
  }

  /** Replaces the counters of that lesson by the ones of a summary, or ignores the summary if it is invalid */
  public void lessonSummaryParse(String lesson, String JSONString)
  {
    JSONObject data;
    try {
      data = (JSONObject) new JSONParser().parse(JSONString);
    } catch (ParseException e) {
      System.out.println("Ignoring invalid lesson summary (parse error: " + e.getLocalizedMessage() + ").");
      return;
    }
    possibleExercises.keySet().removeIf(key -> key.lessonId().equals(lesson));
    passedExercises.keySet().removeIf(key -> key.lessonId().equals(lesson));
    for (ProgrammingLanguage pl : Game.getInstance().programmingLanguageManager.langs) {
      if (data.containsKey("possible" + pl.getLang()))
        setPossibleExercises(lesson, pl, ((Long)data.get("possible" + pl.getLang())).intValue());
      if (data.containsKey("passed" + pl.getLang()))
        setPassedExercises(lesson, pl, ((Long)data.get("passed" + pl.getLang())).intValue());
    }
  }
}
