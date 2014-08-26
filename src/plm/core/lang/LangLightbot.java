package plm.core.lang;

import java.util.List;

import plm.core.PLMCompilerException;
import plm.core.model.LogWriter;
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
	public List<Entity> mutateEntities(Exercise exercise, List<Entity> old, StudentOrCorrection whatToMutate) {
		
		return null; /* This is never called, no need to do anything here */
	}

}
