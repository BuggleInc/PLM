package plm.core.lang;

import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;

import plm.core.PLMCompilerException;
import plm.core.model.LogWriter;
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


}
