package jlm.core.model;

public class ProgrammingLanguage {
	String lang;
	String ext;
	public ProgrammingLanguage(String l, String ext) {
		lang = l;
		this.ext = ext;
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
	public String getExt() {
		return ext;
	}
	@Override
	public String toString() {
		return lang;
	}
	@Override
	public int hashCode() {
		return lang.hashCode();
	}
}
