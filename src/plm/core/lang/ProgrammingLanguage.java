package plm.core.lang;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.script.ScriptEngine;

import lessons.lightbot.universe.LightBotEntity;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.model.LogHandler;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.universe.Entity;

/**
 * Captures the whole logic of a given programming language (compiling the user code, running it).
 * 
 * If you want to add a new programming language to the PLM, then you probably want to read that page:
 * https://github.com/oster/PLM/wiki/Adding-a-new-programming-language
 *
 */

public abstract class ProgrammingLanguage implements Comparable<ProgrammingLanguage> {
	String lang;
	String ext;
	boolean isDebugEnabled;
	String visualExt;
	int visualIndex;
	boolean visualFile;
	
	public ProgrammingLanguage(String l, String ext, boolean isDebugEnabled) {
		lang = l;
		this.ext = ext;
		this.isDebugEnabled = isDebugEnabled;
		this.visualExt = ".code";
		this.visualIndex = 0;
		this.visualFile = false;
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
	public String getVisualExt() {
		return this.visualExt;
	}
	public int getVisualIndex() {
		return  this.visualIndex;
	}
	public boolean getVisualFile() {
		return  this.visualFile;
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
	
	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}
	
	protected Map<String, String> runtimePatterns = new TreeMap<String, String>();
	public abstract void compileExo(Exercise exercise, LogHandler logger, StudentOrCorrection whatToCompile, I18n i18n) throws PLMCompilerException;
	public abstract void compileExo(SourceFile sourceFile, StudentOrCorrection whatToCompile, LogHandler logger, I18n i18n) throws PLMCompilerException;
	public abstract List<Entity> mutateEntities(Exercise exercise, List<Entity> old, StudentOrCorrection whatToMutate, I18n i18n, int nbError) throws PLMCompilerException;
	public abstract List<Entity> mutateEntities(String newClassName, List<Entity> old) throws PLMCompilerException;

	/** Make the entity run, according to the used universe and programming language.
	 * 
	 * This task is not trivial given that it depends on the universe and the programming language:
	 *  * In most universes, the active part is the entity itself. But in the Bat universe, the 
	 *    student-provided method (that is not a real entity but part of the world directly) 
	 *    is run against all testcase, that are not real worlds either.
	 *    
	 *  * Java and Scala entities are launched by just executing their {@link #run()} method that 
	 *    was redefined by the student (possibly with some templating)
	 *  * LightBot entities are launched by executing the {@link LightBotEntity#run()} method, 
	 *    that is NOT defined by the student, but interprets the code of the students.
	 *  * Python (and other scripting language) entities are launched by injecting the 
	 *    student-provided code within a {@link ScriptEngine}. 
	 *    In this later case, the java entity is injected within the scripting world so that it 
	 *    can forward the student commands to the world. 
	 *  * C starts an external program that executes the student logic, along with an handful of threads 
	 *    to deal with the pipes that are connected to the external process
	 * 
	 *  @see #run() that encodes the student logic in Java
	 */
	public abstract void runEntity(Entity ent, ExecutionProgress progress, I18n i18n);
	
	public String nameOfCorrectionEntity(Exercise exo) { // This will be redefined by Scala to prepend "Scala" to that string
		return exo.nameOfCorrectionEntity();
	}
	public String nameOfCommonError(Exercise exo, int i) {
		return exo.nameOfCommonError(i);
	}
}
