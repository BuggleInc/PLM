package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class ChangeBuggleBrushDown extends BuggleOperation {
	private boolean oldBrushDown;
	private boolean newBrushDown;
	
	public ChangeBuggleBrushDown(AbstractBuggle buggle, boolean oldBrushDown, boolean newBrushDown, I18n i18n) {
		super("changeBuggleBrushDown", buggle);
		this.oldBrushDown = oldBrushDown;
		this.newBrushDown = newBrushDown;
		
		String msg = "";
		if(this.newBrushDown) {
			msg = i18n.tr("Buggle {0} is now painting the floor!", buggle.getName());	
		}
		else {
			msg = i18n.tr("Buggle {0} put away its brush.", buggle.getName());
		}
		setMsg(msg);
	}

	public boolean getOldBrushDown() {
		return oldBrushDown;
	}

	public boolean getNewBrushDown() {
		return newBrushDown;
	}
}