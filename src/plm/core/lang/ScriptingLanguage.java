package plm.core.lang;

import java.io.IOException;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.core.utils.FileUtils;
import plm.universe.Entity;


public abstract class ScriptingLanguage extends ProgrammingLanguage {

	public ScriptingLanguage(String lang, String ext, ImageIcon i) {
		super(lang, ext, i);
	}

	@Override
	public void compileExo(Exercise exercise, LogWriter out, StudentOrCorrection whatToCompile) 
			throws PLMCompilerException {
		
		/* Nothing to do */
	}

	@Override
	public List<Entity> mutateEntities(Exercise exo, List<Entity> olds, StudentOrCorrection whatToMutate) {
		String newClassName = (whatToMutate == StudentOrCorrection.STUDENT ? exo.getTabName() : nameOfCorrectionEntity(exo));

		if (whatToMutate == StudentOrCorrection.STUDENT) {
			boolean foundScript = false;

			for (SourceFile sf : exo.getSourceFilesList(this)) {
				if (newClassName.equals(sf.getName())) {
					String script = sf.getCompilableContent(whatToMutate);
					int offset = sf.getOffset();
					
					for (Entity ent : olds) {
						ent.setScript(this, script);
						ent.setScriptOffset(this, offset);
					}
					foundScript = true;
				} 
			}
			if (!foundScript) {
				StringBuffer sb = new StringBuffer();
				for (SourceFile sf: exo.getSourceFilesList(this)) 
					sb.append(sf.getName()+", ");

				System.err.println(getClass().getName()+": Cannot retrieve the script for "+newClassName+". Known scripts: "+sb+"(EOL)");
				throw new RuntimeException(getClass().getName()+": Cannot retrieve the script for "+newClassName+". Known scripts: "+sb+"(EOL)");						
			}
		} else { // whatToMutate == StudentOrCorrection.CORRECTION
			StringBuffer sb = null;
			try {
				sb = FileUtils.readContentAsText(this.nameOfCorrectionEntity(exo), getExt(), false);
			} catch (IOException ex) {
				throw new RuntimeException("Cannot compute the answer from file "+nameOfCorrectionEntity(exo)+"."+getExt()+" since I cannot read it (error was: "+
						ex.getLocalizedMessage());
			}
			String script = sb.toString();

			for (Entity ent : olds) 
				ent.setScript(this, script);
		}
		
		return olds;
	}

	@Override
	public void runEntity(Entity ent, ExecutionProgress progress) {
		ScriptEngine engine = null;		
		try {
			ScriptEngineManager manager = new ScriptEngineManager();       
			engine = manager.getEngineByName(getLang().toLowerCase());
			if (engine==null)
				throw new RuntimeException(Game.i18n.tr("No ScriptEngine for {0}. Please check your classpath and similar settings.",getLang()));

			/* Inject the entity into the scripting world so that it can forward script commands to the world */
			engine.put("entity", ent);
			engine.put("_i18n", Game.i18n);

			/* Inject commands' wrappers that forward the calls to the entity */
			ent.getWorld().setupBindings(this,engine);

			if (ent.getScript(this) == null) { 
				System.err.println(Game.i18n.tr("No {0} script source for entity {1}. Please report that bug against the PLM.",this,ent));
				return;
			}
			setupEntityBindings(ent); // Python wants to add extra definitions to intercept I/O
			if (Game.getInstance().isDebugEnabled())
				System.out.println("Compiled script:\n"+ent.getScript(this));
			engine.eval(ent.getScript(this));

		} catch (ScriptException e) {
			if (Game.getInstance().isDebugEnabled()) 
				System.err.println("Here is the script in "+getLang()+" >>>>"+ent.getScript(this)+"<<<<");
			if (!handleLangException(e,ent,progress)) {
				System.err.println(Game.i18n.tr("Received a ScriptException that does not come from the {0} language.\n",getLang())+e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			String msg = Game.i18n.tr("Script evaluation raised an exception that is not a ScriptException but a {0}.\n"+
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
	protected abstract boolean handleLangException(ScriptException e, Entity ent, ExecutionProgress progress);

}
