package lessons.backtracking;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.NoSuchEntityException;
import plm.universe.Entity;
import plm.universe.World;

public abstract class BacktrackingExercise extends ExerciseTemplated {
	public BacktrackingExercise(Game game, Lesson lesson) {
		super(game, lesson);
	}
	protected void setup(World[] ws,BacktrackingEntity solver) {
		for (World w:ws) {
			w.setName(((BacktrackingPartialSolution) w.getParameter(0)).getTitle());

			/* Copy the solver into the new world */
			try {
				Entity newEntity = solver.getClass().newInstance();
				newEntity.copy(solver);
				w.addEntity(newEntity);
			} catch (Exception e) {
				throw new RuntimeException("Cannot copy entity of class "+ solver.getClass().getName(), e);
			}

		}
		setupWorlds(ws, 0);
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
		ProgrammingLanguage pl = getGame().getProgrammingLanguage();
		ExecutionProgress progress = new ExecutionProgress(pl);
		
		for (World aw : answerWorld) {
			getGame().getLogger().log("Compute answer for world "+aw.getName());
			try {
				pl.runEntity(aw.getEntity(0),progress, getGame().i18n);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void newBestSolution(BacktrackingPartialSolution sol) {
		setNbError(-1);
		((BacktrackingWorld) getWorlds(WorldKind.CURRENT).get(0)).newBestSolution(sol);
	}

}
