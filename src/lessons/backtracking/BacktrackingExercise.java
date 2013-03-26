package lessons.backtracking;

import java.util.List;

import jlm.core.model.Game;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.core.model.lesson.NoSuchEntityException;
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
		try {
			// FIXME: Java only aint good
			newSourceFromFile(Game.JAVA,this.tabName, solver.getClass().getCanonicalName(), "java");
		} catch (NoSuchEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		computeAnswer();
	}
	protected void computeAnswer() {
		for (World aw : answerWorld) {
			System.out.println("Compute answer for world "+aw.getName());
			try {
				aw.getEntity(0).run();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
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
	
	protected void newBestSolution(BacktrackingPartialSolution sol) {
		((BacktrackingWorld) getCurrentWorld().get(0)).newBestSolution(sol);
	}

}
