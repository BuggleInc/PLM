package plm.universe.bat;

import java.util.List;
import java.util.Vector;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplatingEntity;
import plm.core.model.lesson.Lesson;
import plm.universe.World;

public abstract class BatExercise extends ExerciseTemplatingEntity {
	public static final boolean INVISIBLE = false;
	public static final boolean VISIBLE = true;
	
	public BatExercise(Game game, Lesson lesson) {
		super(game, lesson);
	}

	public BatExercise(String id, String name) {
		super(id, name);
	}

	protected void setup(World[] ws) {
		setup(ws,"","");
	}
	protected void setup(World[] ws, String extraImport, String extraBody) {
		if (ws.length > 1)
			throw new RuntimeException("Bat exercises must have at most one world");
		
		String entName = ws[0].getName();
		
		/* Install the corrections: the first time setResult is called, it set 'expected' instead */
		for (BatTest t : ((BatWorld)ws[0]).tests)
			run(t);
		super.setup(ws,entName,
				"import plm.universe.bat.BatEntity; "+
		        "import plm.universe.bat.BatWorld; "+
		        "import plm.universe.bat.BatTest; "+
		        "import plm.universe.World; "+
		        extraImport+
		        "public class "+entName+" extends BatEntity { "+
		        extraBody);
	}
	
	@Override
	public void runDemo(List<Thread> runnerVect){
		/* No demo in bat exercises */
	}

	public abstract void run(BatTest t);
	
	public void templatePython(String entName, String initialCode, String correction) {
		throw new RuntimeException("The exercise "+getName()+" should add the amount of parameters to templatePython().");
	}
	
	/* The types parameter is needed because I sometimes need to convert from the java world to the python one (ConsWorld comes to mind)
	 * In most cases, the content is ignored but we still use the amount of elements in the array. 
	 * The most elegant usage here is to pass the same argument than to the scala template, unless when you want a specific conversion 
	 */
	protected void templatePython(String entName, String[] types, String initialCode, String correction) {
		StringBuffer skeleton = new StringBuffer();
		skeleton.append("for t in batTests:\n");
		skeleton.append("  t.setResult(");
		skeleton.append(entName);
		skeleton.append("(");
		for(int i=0; i<types.length; i++) {
			if (i>0)
				skeleton.append(", ");
			skeleton.append("t.getParameter(");
			skeleton.append(i);
			skeleton.append(")");
		}
		skeleton.append("))\n");
		
		pythonTemplate = skeleton.toString();
		super.templatePython(entName, initialCode, correction);
	}
	
	@Override 
	public void mutateEntities(WorldKind kind, StudentOrCorrection whatToMutate) {
		if (whatToMutate == StudentOrCorrection.STUDENT) {
			super.mutateEntities(kind, whatToMutate);
			return;
		}
		/* compute the correction */
			
		Vector<World> worlds = null;
		int nbErrors = getNbError();
		switch (kind) {
		case INITIAL: worlds = initialWorld; break;
		case CURRENT: worlds = currentWorld; break;
		case ANSWER:  worlds = answerWorld;  break;
		case ERROR:   if(nbErrors != -1) worlds = commonErrors.get(nbErrors);   break;
		default: throw new RuntimeException("kind is invalid: "+kind);
		}

		for (ProgrammingLanguage pl : getProgLanguages()) {
			if (!pl.equals(Game.JAVA) && !pl.equals(Game.SCALA)) 
				worlds.get(0).getEntity(0).setScript(pl, corrections.get(pl));
		}
		
		for (BatTest t : ((BatWorld)worlds.get(0)).tests) 
			t.objectiveTest = true;
	}
}
