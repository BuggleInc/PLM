package jlm.core.model;

public class ProgrammingLanguage implements Comparable<ProgrammingLanguage> {
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
	@Override
	public int compareTo(ProgrammingLanguage o) {
		if (o == null)
			return 1;
		int res = lang.compareTo(o.lang);
		if (res != 0)
			return res;
		return ext.compareTo(o.ext);
	}
}
