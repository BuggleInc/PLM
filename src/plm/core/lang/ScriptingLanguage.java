package plm.core.lang;

import java.util.List;
import java.util.Locale;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import plm.core.PLMCompilerException;
import plm.core.model.I18nManager;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.universe.Entity;


public abstract class ScriptingLanguage extends ProgrammingLanguage {

	public ScriptingLanguage(String lang, String ext, boolean isDebugEnabled) {
		super(lang, ext, isDebugEnabled);
	}

	@Override
	public void compileExo(SourceFile sourceFile, ExecutionProgress lastResult,
			StudentOrCorrection whatToCompile, Locale locale)
					throws PLMCompilerException {
		/* Nothing to do */

	}

	@Override
	public List<Entity> mutateEntities(String newClassName, SourceFile sourceFile, StudentOrCorrection whatToMutate, List<Entity> olds)
			throws PLMCompilerException {
		if (whatToMutate == StudentOrCorrection.STUDENT) {
			String script = sourceFile.getCompilableContent(whatToMutate);
			int offset = sourceFile.getOffset();

			for (Entity ent : olds) {
				ent.setScript(this, script);
				ent.setScriptOffset(this, offset);
			}
			// FIXME: Can we reach this case?
			/*
			if (!foundScript) {
				StringBuffer sb = new StringBuffer();
				for (SourceFile sf: exo.getSourceFilesList(this))
					sb.append(sf.getName()+", ");

				System.err.println(getClass().getName()+": Cannot retrieve the script for "+newClassName+". Known scripts: "+sb+"(EOL)");
				throw new RuntimeException(getClass().getName()+": Cannot retrieve the script for "+newClassName+". Known scripts: "+sb+"(EOL)");
			}
			 */
		} else if(whatToMutate == StudentOrCorrection.CORRECTION) {
			String script = sourceFile.getCorrection();

			for (Entity ent : olds)
				ent.setScript(this, script);
		}
		// FIXME: Handle  the common errors' cases
		/*
		else {
			StringBuffer sb = null;
			try {
				sb = FileUtils.readContentAsText(this.nameOfCommonError(exo,nbError), i18n.getLocale(), "java", false);
			} catch (IOException ex) {
				throw new RuntimeException("Cannot compute the error from file "+nameOfCommonError(exo,nbError)+"."+getExt()+" since I cannot read it (error was: "+
						ex.getLocalizedMessage());
			}
			String script = sb.toString();

			for (Entity ent : olds)
				ent.setScript(this, script);
		}
		 */
		return olds;
	}

	@Override
	public void runEntity(Entity ent, ExecutionProgress progress, Locale locale) {
		ScriptEngine engine = null;
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			if(getLang().equals("Blockly")) 
				engine = manager.getEngineByName("python");
			else
				engine = manager.getEngineByName(getLang().toLowerCase());
			if (engine==null)
				throw new RuntimeException(I18nManager.getI18n(locale).tr("No ScriptEngine for {0}. Please check your classpath and similar settings.",getLang()));

			/* Inject the entity into the scripting world so that it can forward script commands to the world */
			engine.put("entity", ent);

			/* Inject commands' wrappers that forward the calls to the entity */
			ent.getWorld().setupBindings(this,engine);

			if (ent.getScript(this) == null) {
				System.err.println(I18nManager.getI18n(locale).tr("No {0} script source for entity {1}. Please report that bug against the PLM.",this,ent));
				return;
			}
			setupEntityBindings(ent); // Python wants to add extra definitions to intercept I/O
			if (isDebugEnabled())
				System.out.println("Compiled script:\n"+ent.getScript(this));
			engine.eval(ent.getScript(this));

		} catch (ScriptException e) {
			if (isDebugEnabled())
				System.err.println("Here is the script in "+getLang()+" >>>>"+ent.getScript(this)+"<<<<");
			if (!handleLangException(e,ent,progress, locale)) {
				System.err.println(I18nManager.getI18n(locale).tr("Received a ScriptException that does not come from the {0} language.\n",getLang())+e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			String msg = I18nManager.getI18n(locale).tr("Script evaluation raised an exception that is not a ScriptException but a {0}.\n"+
					" Please report this as a bug against the PLM, with all details allowing to reproduce it.\n" +
					"Exception message: {1}\n",e.getClass(),e.getLocalizedMessage());
			System.err.println(msg);
			for (StackTraceElement elm : e.getStackTrace())
				msg += elm.toString()+"\n";

			progress.setCompilationError(msg);
			e.printStackTrace();
		}
	}

	/** Add extra definitions to the script if needed */
	protected abstract void setupEntityBindings(Entity ent);

	/** Decipher an exception and produce a meaningful feedback to the user
	 *
	 * @return true if it's an exception of that ProgrammingLanguage, and false if the exception should be handled elsewhere
	 */
	protected abstract boolean handleLangException(ScriptException e, Entity ent, ExecutionProgress progress, Locale locale);

}
