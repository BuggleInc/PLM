package plm.core.model.lesson;

import java.util.Locale;

import plm.core.lang.ProgrammingLanguage;

public class UserSettings {
	private Locale humanLang;
	private ProgrammingLanguage progLang;
	
	public UserSettings(Locale humanLang, ProgrammingLanguage progLang) {
		this.setHumanLang(humanLang);
		this.setProgLang(progLang);
	}

	public Locale getHumanLang() {
		return humanLang;
	}

	public void setHumanLang(Locale humanLang) {
		this.humanLang = humanLang;
	}

	public ProgrammingLanguage getProgLang() {
		return progLang;
	}

	public void setProgLang(ProgrammingLanguage progLang) {
		this.progLang = progLang;
	}
}
