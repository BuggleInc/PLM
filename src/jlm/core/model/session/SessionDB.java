package jlm.core.model.session;

import java.util.HashMap;
import java.util.Map;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;

public class SessionDB {
	private Map<String, Map<ProgrammingLanguage, Map<String, String>>> body = new HashMap<String, Map<ProgrammingLanguage,Map<String,String>>>(); 
	private Map<String, Map<ProgrammingLanguage, Boolean>> passed = new HashMap<String, Map<ProgrammingLanguage,Boolean>>();
	
	public void setBody(String exo, ProgrammingLanguage lang, String sourceName, String _body) {
		if (exo == null)
			exo = Game.getInstance().getCurrentLesson().getCurrentExercise().getId();
		if (lang == null)
			lang = Game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Map<String, String>> bodyE = body.get(exo);
		if (bodyE == null) {
			bodyE = new HashMap<ProgrammingLanguage,Map<String, String>>();
			body.put(exo, bodyE);
		}
		Map<String, String> bodyLEP = bodyE.get(lang);
		if (bodyLEP == null) {
			bodyLEP = new HashMap<String, String>();
			bodyE.put(lang, bodyLEP);
		}
		
		bodyLEP.put(sourceName, _body);
	}
	public String getBody(String exo, ProgrammingLanguage lang, String sourceName) {
		if (exo == null)
			exo = Game.getInstance().getCurrentLesson().getCurrentExercise().getId();
		if (lang == null)
			lang = Game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Map<String, String>> bodyE = body.get(exo);
		if (bodyE == null) 
			return null;
		Map<String, String> bodyEP = bodyE.get(lang);
		if (bodyEP == null)
			return null;
		return bodyEP.get(sourceName);
	}


	public void setPassed(String exo, ProgrammingLanguage lang, boolean _passed) {
		if (exo == null)
			exo = Game.getInstance().getCurrentLesson().getCurrentExercise().getId();
		if (lang == null)
			lang = Game.getProgrammingLanguage();
		
		Map<ProgrammingLanguage, Boolean> passedE = passed.get(exo);
		if (passedE == null) {
			passedE = new HashMap<ProgrammingLanguage, Boolean>();
			passed.put(exo, passedE);
		}
		passedE.put(lang,_passed);
	}
	/** If the exercise was never attempted (not present in DB), it returns false */
	public boolean getPassed(String exo, ProgrammingLanguage lang) {
		if (exo == null)
			exo = Game.getInstance().getCurrentLesson().getCurrentExercise().getId();
		if (lang == null)
			lang = Game.getProgrammingLanguage();

		Map<ProgrammingLanguage, Boolean> passedE = passed.get(exo);
		if (passedE == null)
			return false;
		
		Boolean res = passedE.get(lang);
		if (res == null) 
			return false;
		return res;
	}
}
