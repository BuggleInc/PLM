package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class BuggleEncounterWall extends BuggleOperation {
	
	public BuggleEncounterWall(AbstractBuggle buggle, I18n i18n) {
		super("buggleEncounterWall", buggle);
		setMsg(i18n.tr("Buggle {0} encountered a wall...", buggle.getName()));
	}

}
