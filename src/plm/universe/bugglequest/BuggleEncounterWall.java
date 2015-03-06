package plm.universe.bugglequest;

public class BuggleEncounterWall extends BuggleOperation {
	
	public BuggleEncounterWall(AbstractBuggle buggle) {
		super("buggleEncounterWall", buggle);
		setMsg(getI18n().tr("Buggle {0} encountered a wall...", buggle.getName()));
	}

}
