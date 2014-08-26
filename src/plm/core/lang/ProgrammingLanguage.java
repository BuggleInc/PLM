package plm.core.lang;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import plm.core.PLMCompilerException;
import plm.core.model.LogWriter;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.universe.Entity;

public abstract class ProgrammingLanguage implements Comparable<ProgrammingLanguage> {
	String lang;
	String ext;
	ImageIcon icon;
	public ProgrammingLanguage(String l, String ext, ImageIcon i) {
		lang = l;
		this.ext = ext;
		this.icon = i;
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
	public ImageIcon getIcon() {
		return icon;
	}
	
	protected Map<String, String> runtimePatterns = new TreeMap<String, String>();
	public abstract void compileExo(Exercise exercise, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException;
	public abstract List<Entity> mutateEntities(Exercise exercise, List<Entity> old, StudentOrCorrection whatToMutate);
	
	public String nameOfCorrectionEntity(Exercise exo) {
		return exo.getClass().getCanonicalName() + "Entity";
	}
}
