package plm.core.lang;

import java.util.List;

import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.ui.ResourcesCache;
import plm.universe.Entity;

public class LangLightbot extends ProgrammingLanguage {

	public LangLightbot() {
		super("lightbot","ignored",ResourcesCache.getIcon("img/lightbot_light.png"));
	}

	@Override
	public void compileExo(Exercise exercise, LogWriter out, StudentOrCorrection whatToCompile) 
			throws PLMCompilerException {
		
		/* Nothing to do */
		
	}

	@Override
	public List<Entity> mutateEntities(Exercise exercise, List<Entity> old, StudentOrCorrection whatToMutate, int nbError) {
		
		return null; /* This is never called, no need to do anything here */
	}

	@Override
	public void runEntity(Entity ent, ExecutionProgress progress) {
		try {
			ent.run();
		} catch (Exception e) {
			String msg = Game.i18n.tr("The execution of your program raised a {0} exception: {1}\n" + 
					" Please fix your code.\n",e.getClass().getName(),e.getLocalizedMessage());

			for (StackTraceElement elm : e.getStackTrace())
				msg+= "   at "+elm.getClassName()+"."+elm.getMethodName()+" ("+elm.getFileName()+":"+elm.getLineNumber()+")"+"\n";

			System.err.println(msg);
			progress.setExecutionError(msg);
			e.printStackTrace();
		}
	}

}
