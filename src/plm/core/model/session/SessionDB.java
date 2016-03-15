package plm.core.model.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Lecture;

public class SessionDB {
	private Map<String, Map<ProgrammingLanguage, Map<String, String>>> body = new HashMap<String, Map<ProgrammingLanguage,Map<String,String>>>();
	private Map<String, Map<ProgrammingLanguage, Boolean>> passed = new HashMap<String, Map<ProgrammingLanguage,Boolean>>();

	/* Per lesson summary */
	private Map<String, Map<ProgrammingLanguage, Integer>> passedExercises = new HashMap<String, Map<ProgrammingLanguage,Integer>>();
	private Map<String, Map<ProgrammingLanguage, Integer>> possibleExercises = new HashMap<String, Map<ProgrammingLanguage,Integer>>();

	private Game game;

	public SessionDB(Game game) {
		this.game = game;
	}

	public void setBody(Lecture exo, ProgrammingLanguage lang, String sourceName, String _body) {
		if (exo == null)
			exo = game.getCurrentLesson().getCurrentExercise();
		if (lang == null)
			lang = game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Map<String, String>> bodyE = body.get(exo);
		if (bodyE == null) {
			bodyE = new HashMap<ProgrammingLanguage,Map<String, String>>();
			body.put(exo.getId(), bodyE);
		}
		Map<String, String> bodyLEP = bodyE.get(lang);
		if (bodyLEP == null) {
			bodyLEP = new HashMap<String, String>();
			bodyE.put(lang, bodyLEP);
		}

		bodyLEP.put(sourceName, _body);
	}
	public String getBody(Lecture exo, ProgrammingLanguage lang, String sourceName) {
		if (exo == null)
			exo = game.getCurrentLesson().getCurrentExercise();
		if (lang == null)
			lang = game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Map<String, String>> bodyE = body.get(exo.getId());
		if (bodyE == null)
			return null;
		Map<String, String> bodyEP = bodyE.get(lang);
		if (bodyEP == null)
			return null;
		return bodyEP.get(sourceName);
	}


	public void setPassed(Lecture exo, ProgrammingLanguage lang, boolean _passed) {
		if (exo == null)
			exo = game.getCurrentLesson().getCurrentExercise();
		if (lang == null)
			lang = game.getProgrammingLanguage();

		if (getPassed(exo, lang) == _passed)
			return;

		setPassedExercises(exo.getLesson().getId(), lang, getPassedExercises(exo.getLesson().getId(), lang) + (_passed?1:-1));

		Map<ProgrammingLanguage, Boolean> passedE = passed.get(exo.getId());
		if (passedE == null) {
			passedE = new HashMap<ProgrammingLanguage, Boolean>();
			passed.put(exo.getId(), passedE);
		}
		passedE.put(lang,_passed);
	}

	/** If the exercise was never attempted (not present in DB), it returns false */
	public boolean getPassed(Lecture exo, ProgrammingLanguage lang) {
		if (exo == null)
			exo = game.getCurrentLesson().getCurrentExercise();
		if (lang == null)
			lang = game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Boolean> passedE = passed.get(exo.getId());
		if (passedE == null)
			return false;

		Boolean res = passedE.get(lang);
		if (res == null)
			return false;
		return res;
	}
	public Integer getPossibleExercises(String lesson, ProgrammingLanguage lang) {
		if (lesson == null)
			lesson = game.getCurrentLesson().getId();
		if (lang == null)
			lang = game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Integer> passedL = possibleExercises.get(lesson);
		if (passedL == null)
			return 0;

		Integer res = passedL.get(lang);
		if (res == null)
			return 0;
		return res;
	}
	public void setPassedExercises(String lesson, ProgrammingLanguage lang, int val) {
		if (lesson == null)
			lesson = game.getCurrentLesson().getId();
		if (lang == null)
			lang = game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Integer> passedL = passedExercises.get(lesson);
		if (passedL == null) {
			passedL = new HashMap<ProgrammingLanguage, Integer>();
			passedExercises.put(lesson, passedL);
		}

		passedL.put(lang, val);
	}
	public void setPossibleExercises(String lesson, ProgrammingLanguage lang, int val) {
		if (lesson == null)
			lesson = game.getCurrentLesson().getId();
		if (lang == null)
			lang = game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Integer> possibleL = possibleExercises.get(lesson);
		if (possibleL == null) {
			possibleL = new HashMap<ProgrammingLanguage, Integer>();
			possibleExercises.put(lesson, possibleL);
		}

		possibleL.put(lang,val);
	}
	public Integer getPassedExercises(String lesson, ProgrammingLanguage lang) {
		if (lesson == null)
			lesson = game.getCurrentLesson().getId();
		if (lang == null)
			lang = game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Integer> passedL = passedExercises.get(lesson);
		if (passedL == null)
			return 0;

		Integer res = passedL.get(lang);
		if (res == null)
			return 0;
		return res;
	}
	public Set<String> getLessonsNames() {
		return possibleExercises.keySet();
	}


	@SuppressWarnings("unchecked")
	public String lessonSummary(String lesson) {
		JSONObject result = new JSONObject();

		Map<ProgrammingLanguage, Integer> possibleL = possibleExercises.get(lesson);
		for (ProgrammingLanguage pl: possibleL.keySet())
			if (possibleL.get(pl)!=0)
				result.put("possible"+pl.getLang(), possibleL.get(pl));

		Map<ProgrammingLanguage, Integer> passedL = passedExercises.get(lesson);
		for (ProgrammingLanguage pl: passedL.keySet())
			if (passedL.get(pl)!=0)
				result.put("passed"+pl.getLang(), passedL.get(pl));

		return result.toJSONString();
	}

	public void lessonSummaryParse(String lesson, String JSONString) {
		JSONParser parser = new JSONParser();
		JSONObject data;
		try {
			data = (JSONObject) parser.parse(JSONString);
		} catch (ParseException e) {
			game.getLogger().log(LogHandler.ERROR, "Ignoring invalid lesson summary (parse error: "+e.getLocalizedMessage()+").");
			return;
		}

		Map<ProgrammingLanguage, Integer> possibleL = new HashMap<ProgrammingLanguage, Integer>();
		possibleExercises.put(lesson, possibleL);
		Map<ProgrammingLanguage, Integer> passedL = new HashMap<ProgrammingLanguage, Integer>();
		passedExercises.put(lesson, passedL);

		for (ProgrammingLanguage pl: Game.getProgrammingLanguages()) {
			if (data.containsKey("possible"+pl.getLang())) {
				Long v = (Long) data.get("possible"+pl.getLang());
				possibleL.put(pl, v.intValue());
			}
			if (data.containsKey("passed"+pl.getLang())) {
				Long v = (Long) data.get("passed"+pl.getLang()); // damn, damn java casting madness
				passedL.put(pl, v.intValue());
			}
		}
	}
}
