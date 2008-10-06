package bugglequest.core;

import java.awt.Color;

import bugglequest.exception.AlreadyHaveBaggleException;
import bugglequest.exception.BuggleWallException;
import bugglequest.exception.NoBaggleUnderBuggleException;

public abstract class SimpleBuggle extends AbstractBuggle  {
	public SimpleBuggle(World w, String name, int i, int j, Direction dir, Color c, Color bc) {
		super(w,name,i,j,dir,c,bc);
	}

	public SimpleBuggle() {
		super();
	}

	@Override
	public void forward()  {
		try { 
			super.forward(); 
		} catch (BuggleWallException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, "You hit a wall.", "Test failed", javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void forward(int count)  {
		try { 
			super.forward(count); 
		} catch (BuggleWallException e) { 
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, "You hit a wall.", "Test failed", javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void backward()  {
		try { 
			super.backward(); 
		} catch (BuggleWallException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, "You hit a wall.", "Test failed", javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void backward(int count)  {
		try { 
			super.backward(count); 
		} catch (BuggleWallException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, "You hit a wall.", "Test failed", javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void pickUpBaggle () { 
		try { 
			super.pickUpBaggle(); 
		} catch (NoBaggleUnderBuggleException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, "No baggle here.", "Test failed", javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		} catch (AlreadyHaveBaggleException e) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, "You are already carrying a baggle.", "Test failed", javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}

	@Override
	public void dropBaggle () { 
		try { 
			super.dropBaggle(); 
		} catch (AlreadyHaveBaggleException e) { 
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, "Buggle already carry a baggle.", "Test failed", javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
	}	
}