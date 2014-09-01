package plm.universe.bugglequest;

import java.awt.Color;

import plm.core.model.Game;
import plm.universe.Direction;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import plm.universe.bugglequest.exception.BuggleInOuterSpaceException;
import plm.universe.bugglequest.exception.BuggleWallException;
import plm.universe.bugglequest.exception.DontHaveBaggleException;
import plm.universe.bugglequest.exception.NoBaggleUnderBuggleException;



public class SimpleBuggle extends AbstractBuggle  {
	/* Make it possible to instantiate SimpleBuggles from exercises so that Python also gets graphical window showing */
	public SimpleBuggle() {
		super();
	}
	public SimpleBuggle(BuggleWorld world, String name, int i, int j, Direction north, Color color, Color brush) {
		super(world, name, i, j, north, color, brush);
	}
	@Override
	public void run() {
		// Overridden by children
	}

	/* Raise a window error when an exception is encountered */
	@Override
	public void forward()  {
		try { 
			super.forward(); 
		} catch (BuggleWallException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void forward(int count)  {
		try { 
			super.forward(count); 
		} catch (BuggleWallException e) { 
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void backward()  {
		try { 
			super.backward(); 
		} catch (BuggleWallException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void backward(int count)  {
		try { 
			super.backward(count); 
		} catch (BuggleWallException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Deprecated
	@Override
	public void pickUpBaggle () { 
		pickupBaggle();
	}
	@Override
	public void pickupBaggle () { 
		try { 
			super.pickupBaggle(); 
		} catch (NoBaggleUnderBuggleException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		} catch (AlreadyHaveBaggleException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void dropBaggle () { 
		try { 
			super.dropBaggle(); 
		} catch (AlreadyHaveBaggleException e) { 
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		} catch (DontHaveBaggleException e) {
			javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}	
	@Override 
	public void setX(int x) {
		try {
			super.setX(x);
		} catch (BuggleInOuterSpaceException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}
	@Override 
	public void setY(int y) {
		try {
			super.setY(y);
		} catch (BuggleInOuterSpaceException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}
	@Override 
	public void setPos(int x,int y) {
		try {
			super.setPos(x,y);
		} catch (BuggleInOuterSpaceException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}
	
	/* BINDINGS TRANSLATION: French (get/set X/Y/Pos are not translated as they happen to be the same in French) */
	public void avance()          { forward(); }
	public void avance(int steps) { forward(steps); }
	public void recule()          { backward(); }
	public void recule(int steps) { backward(steps); }
	public void prendBiscuit()    { pickupBaggle(); }
	public void poseBiscuit()     { dropBaggle(); }

}