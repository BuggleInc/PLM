package plm.core.model.session;

import java.util.HashMap;
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
  private Map<String, Map<ProgrammingLanguage, Boolean>> passed           = new HashMap<String, Map<ProgrammingLanguage, Boolean>>();

  /* Per lesson summary */
  private Map<String, Map<ProgrammingLanguage, Integer>> passedExercises   = new HashMap<String, Map<ProgrammingLanguage, Integer>>();
  private Map<String, Map<ProgrammingLanguage, Integer>> possibleExercises = new HashMap<String, Map<ProgrammingLanguage, Integer>>();

  /** Stores what the student wrote in that source, and tells its editor */
  public void setBody(Lecture exo, ProgrammingLanguage lang, String sourceName, String content)
  {
    BodyKey key = new BodyKey(exo.getId(), lang, sourceName);
    bodies.put(key, lang.isPython() ? content.replace("\t", "    ") : content);
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

  public void setPassed(Lecture exo, ProgrammingLanguage lang, boolean _passed)
  {
    if (exo == null)
      exo = Game.getInstance().getCurrentLesson().getCurrentExercise();
    if (getPassed(exo, lang) == _passed)
      return;

    setPassedExercises(exo.getLesson().getId(), lang, getPassedExercises(exo.getLesson().getId(), lang) + (_passed ? 1 : -1));

    Map<ProgrammingLanguage, Boolean> passedE = passed.get(exo.getId());
    if (passedE == null) {
      passedE = new HashMap<ProgrammingLanguage, Boolean>();
      passed.put(exo.getId(), passedE);
    }
    passedE.put(lang, _passed);
  }

  /** If the exercise was never attempted (not present in DB), it returns false */
  public boolean getPassed(Lecture exo, ProgrammingLanguage lang)
  {
    if (exo == null)
      exo = Game.getInstance().getCurrentLesson().getCurrentExercise();
    Map<ProgrammingLanguage, Boolean> passedE = passed.get(exo.getId());
    if (passedE == null)
      return false;

    Boolean res = passedE.get(lang);
    if (res == null)
      return false;
    return res;
  }
  public Integer getPossibleExercises(String lesson, ProgrammingLanguage lang)
  {
    if (lesson == null)
      lesson = Game.getInstance().getCurrentLesson().getId();
    Map<ProgrammingLanguage, Integer> passedL = possibleExercises.get(lesson);
    if (passedL == null)
      return 0;

    Integer res = passedL.get(lang);
    if (res == null)
      return 0;
    return res;
  }
  public void setPassedExercises(String lesson, ProgrammingLanguage lang, int val)
  {
    if (lesson == null)
      lesson = Game.getInstance().getCurrentLesson().getId();
    Map<ProgrammingLanguage, Integer> passedL = passedExercises.get(lesson);
    if (passedL == null) {
      passedL = new HashMap<ProgrammingLanguage, Integer>();
      passedExercises.put(lesson, passedL);
    }

    passedL.put(lang, val);
  }
  public void setPossibleExercises(String lesson, ProgrammingLanguage lang, int val)
  {
    if (lesson == null)
      lesson = Game.getInstance().getCurrentLesson().getId();
    Map<ProgrammingLanguage, Integer> possibleL = possibleExercises.get(lesson);
    if (possibleL == null) {
      possibleL = new HashMap<ProgrammingLanguage, Integer>();
      possibleExercises.put(lesson, possibleL);
    }

    possibleL.put(lang, val);
  }
  public Integer getPassedExercises(String lesson, ProgrammingLanguage lang)
  {
    if (lesson == null)
      lesson = Game.getInstance().getCurrentLesson().getId();
    Map<ProgrammingLanguage, Integer> passedL = passedExercises.get(lesson);
    if (passedL == null)
      return 0;

    Integer res = passedL.get(lang);
    if (res == null)
      return 0;
    return res;
  }
  public Set<String> getLessonsNames() { return possibleExercises.keySet(); }

  public String lessonSummary(String lesson)
  {
    JSONObject result = new JSONObject();

    Map<ProgrammingLanguage, Integer> possibleL = possibleExercises.get(lesson);
    for (ProgrammingLanguage pl : possibleL.keySet())
      if (possibleL.get(pl) != 0)
        result.put("possible" + pl.getLang(), possibleL.get(pl));

    Map<ProgrammingLanguage, Integer> passedL = passedExercises.get(lesson);
    for (ProgrammingLanguage pl : passedL.keySet())
      if (passedL.get(pl) != 0)
        result.put("passed" + pl.getLang(), passedL.get(pl));

    return result.toJSONString();
  }

  public void lessonSummaryParse(String lesson, String JSONString)
  {
    JSONParser parser = new JSONParser();
    JSONObject data;
    try {
      data = (JSONObject)parser.parse(JSONString);
    } catch (ParseException e) {
      System.out.println("Ignoring invalid lesson summary (parse error: " + e.getLocalizedMessage() + ").");
      return;
    }

    Map<ProgrammingLanguage, Integer> possibleL = new HashMap<ProgrammingLanguage, Integer>();
    possibleExercises.put(lesson, possibleL);
    Map<ProgrammingLanguage, Integer> passedL = new HashMap<ProgrammingLanguage, Integer>();
    passedExercises.put(lesson, passedL);

    for (ProgrammingLanguage pl : Game.getInstance().programmingLanguageManager.langs) {
      if (data.containsKey("possible" + pl.getLang())) {
        Long v = (Long)data.get("possible" + pl.getLang());
        possibleL.put(pl, v.intValue());
      }
      if (data.containsKey("passed" + pl.getLang())) {
        Long v = (Long)data.get("passed" + pl.getLang()); // damn, damn java casting madness
        passedL.put(pl, v.intValue());
      }
    }
  }
}
