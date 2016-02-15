package plm.core.lang;

import java.util.List;
import java.util.Locale;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.model.I18nManager;
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
	public void runEntity(Entity ent, ExecutionProgress progress, Locale locale) {
		try {
			ent.run();
		} catch (Exception e) {
			String msg = I18nManager.getI18n(locale).tr("The execution of your program raised a {0} exception: {1}\n" + 
					" Please fix your code.\n",e.getClass().getName(),e.getLocalizedMessage());

			for (StackTraceElement elm : e.getStackTrace())
				msg+= "   at "+elm.getClassName()+"."+elm.getMethodName()+" ("+elm.getFileName()+":"+elm.getLineNumber()+")"+"\n";

			System.err.println(msg);
			progress.setExecutionError(msg);
			e.printStackTrace();
		}
	}

	@Override
	public void compileExo(SourceFile sourceFile, ExecutionProgress lastResult,
			StudentOrCorrection whatToCompile, Locale locale)
			throws PLMCompilerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Entity> mutateEntities(String newClassName, SourceFile sourceFile,
			StudentOrCorrection whatToMutate, List<Entity> old)
			throws PLMCompilerException {
		// TODO Auto-generated method stub
		return null;
	}

}
