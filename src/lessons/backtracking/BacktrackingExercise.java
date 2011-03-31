package lessons.backtracking;

import java.util.List;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.lesson.SourceFile;
import jlm.universe.World;

public abstract class BacktrackingExercise extends ExerciseTemplated {
	public BacktrackingExercise(Lesson lesson) {
		super(lesson);
		entityName = getClass().getCanonicalName()+".java";
	}
	protected void setup(World[] ws,BacktrackingEntity solver) {
		for (World w:ws) {
			w.setName(((BacktrackingPartialSolution) w.getParameter(0)).getTitle());
			w.addEntity(solver.copy());
		}
		worldDuplicate(ws);
		newSourceFromFile(this.tabName, solver.getClass().getCanonicalName(), "java"); 
		SourceFile sf = sourceFiles.get(0);

/*		sf.setTemplate(
				"$package "+
				"import lessons.backtracking.*; "+
				/*"import jlm.universe.World; "+
				"public class "+this.tabName+" extends BacktrackingEntity { "+
				sf.getTemplate()+" $body }");*/
		System.out.println("New template: "+sf.getTemplate());
		computeAnswer();
	}
	protected void computeAnswer() {
		for (World aw : answerWorld) {
			System.out.println("Compute answer for world "+aw.getName());
			run((BacktrackingPartialSolution)aw.getParameter(0));
		}
	}

	@Override
	public void run(List<Thread> runnerVect){
		mutateEntity(tabName);
		for (World cw:currentWorld) {
			System.out.println("Run world "+cw.getName()+"; entities:"+cw.getEntities());
			cw.doDelay();
			cw.runEntities(runnerVect);
		}
	}
	public abstract void run(BacktrackingPartialSolution ps);
	
	protected void newBestSolution(BacktrackingPartialSolution sol) {
		((BacktrackingWorld) getCurrentWorld().get(0)).newBestSolution(sol);
	}

}
