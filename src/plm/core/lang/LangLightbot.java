package plm.core.lang;

import java.util.List;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.model.LogHandler;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.universe.Entity;

public class LangLightbot extends ProgrammingLanguage {

	public LangLightbot(boolean isDebugEnabled) {
		super("lightbot", "ignored", isDebugEnabled);
	}

	@Override
	public void compileExo(Exercise exercise, LogHandler logger, StudentOrCorrection whatToCompile, I18n i18n) 
			throws PLMCompilerException {
		
		/* Nothing to do */
		
	}

	@Override
	public List<Entity> mutateEntities(Exercise exercise, List<Entity> old, StudentOrCorrection whatToMutate, I18n i18n, int nbError) {
		return null; /* This is never called, no need to do anything here */
	}

	@Override
	public void runEntity(Entity ent, ExecutionProgress progress, I18n i18n) {
		try {
			ent.run();
		} catch (Exception e) {
			String msg = i18n.tr("The execution of your program raised a {0} exception: {1}\n" + 
					" Please fix your code.\n",e.getClass().getName(),e.getLocalizedMessage());

			for (StackTraceElement elm : e.getStackTrace())
				msg+= "   at "+elm.getClassName()+"."+elm.getMethodName()+" ("+elm.getFileName()+":"+elm.getLineNumber()+")"+"\n";

			System.err.println(msg);
			progress.setExecutionError(msg);
			e.printStackTrace();
		}
	}

	@Override
	public void compileExo(SourceFile sourceFile,
			StudentOrCorrection whatToCompile, LogHandler logger, I18n i18n)
			throws PLMCompilerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Entity> mutateEntities(String newClassName, List<Entity> old)
			throws PLMCompilerException {
		// TODO Auto-generated method stub
		return null;
	}

}
