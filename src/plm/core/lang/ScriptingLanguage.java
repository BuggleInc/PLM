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
		String script = null;
		try {
			ScriptEngineManager manager = new ScriptEngineManager();       
			engine = manager.getEngineByName(getLang().toLowerCase());
			if (engine==null)
				throw new RuntimeException(Game.i18n.tr("No ScriptEngine for {0}. Please check your classpath and similar settings.",getLang()));

			/* Inject the entity into the scripting world so that it can forward script commands to the world */
			engine.put("entity", ent);


			/* Inject commands' wrappers that forward the calls to the entity */
			ent.getWorld().setupBindings(this,engine);

			/* getParam is in every Entity, so put it here to not request the universe to call super.setupBinding() */
			if (this.equals(Game.PYTHON)) {
				engine.eval(
						"def getParam(i):\n"+
						"  return entity.getParam(i)\n" +
						"def isSelected():\n" +
						"  return entity.isSelected()\n");		
			}
			script = ent.getScript(this);

			if (script == null) { 
				System.err.println(Game.i18n.tr("No {0} script source for entity {1}. Please report that bug against PLM.",this,ent));
				return;
			}
			if (this.equals(Game.PYTHON)) {
				/* that's not really clean to get the output working when we redirect to the graphical console, 
				 * but it works (as long as it's evaluated at the exact same time than the script). */
				ent.setScriptOffset(this, ent.getScriptOffset(this)+7);
				script= "import sys;\n" +
						"import java.lang;\n" +
						"class PLMOut:\n" +
						"  def write(obj,msg):\n" +
						"    java.lang.System.out.print(str(msg))\n" +
						"sys.stdout = PLMOut()\n" +
						"sys.stderr = PLMOut()\n" +
						script;
			}
			engine.eval(script);

		} catch (ScriptException e) {
			if (Game.getInstance().isDebugEnabled()) 
				System.err.println("Here is the script in "+getLang()+" >>>>"+script+"<<<<");
			if (Game.getInstance().canPython && PythonExceptionDecipher.isPythonException(e))
				PythonExceptionDecipher.handlePythonException(e,ent,progress);
			else {
				System.err.println(Game.i18n.tr("Received a ScriptException that does not come from Python.\n")+e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			String msg = Game.i18n.tr("Script evaluation raised an exception that is not a ScriptException but a {0}.\n"+
					" Please report this as a bug against PLM, with all details allowing to reproduce it.\n" +
					"Exception message: {1}\n",e.getClass(),e.getLocalizedMessage());
			System.err.println(msg);
			for (StackTraceElement elm : e.getStackTrace()) 
				msg += elm.toString()+"\n";

			progress.setCompilationError(msg);
			e.printStackTrace();
		}
	}


}
