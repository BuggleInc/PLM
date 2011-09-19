package jlm.core.model;

public class ProgrammingLanguage {
	String lang;
	public ProgrammingLanguage(String l) {
		lang = l;
	}
	public boolean equals(Object o) {
		if (!super.equals(o))
			return false;
		if (getClass() != o.getClass())
			return false;
		return lang.equals(((ProgrammingLanguage)o).lang);
	}
	public String getLang() {
		return lang;
	}
	public String toString() {
		return lang;
	}
}
